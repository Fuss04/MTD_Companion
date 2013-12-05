import time
import BaseHTTPServer
import cgi
from subprocess import call
import json
from string import digits

HOST_NAME = 'localhost'
PORT_NUMBER = 9000

stack = []
N = 0

class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    def store_path(s):
        global N
        path = 'image' + str(N)
        N += 1
        return path

    def save_image(s):
        form = cgi.FieldStorage(
            fp = s.rfile,
            headers = s.headers,
            environ = {
                'REQUEST_METHOD':'POST',
                'CONTENT_TYPE':s.headers['Content-Type']
            }
        )
        fileitem = form['filedata']
        if not fileitem.file:
            return None

        path = s.store_path()
        with open(path + '.png', 'w') as f:
            for line in fileitem.file:
                f.write(line)
        return path

    def image_filter(s, path):
        ret = call(["./clean.sh", path + '.png', path + '.png'])

    def swt(s, path):
        ret = call(["./detect.sh", path + '.png', path + '.txt'])
        locations = []
        with open(path + '.txt', 'r') as f:
            for line in f:
                if not 'total' in line:
                    l = tuple(line.split())
                    locations.append(l)
        return locations

    def crop_image(s, path, l, i):
        ''' l = (x,y,w,h) '''
        xpath = path + '_txt_' + str(i)
        ret = call(["./crop.sh", path + '.png', xpath + '.png', l[0], l[1], l[2], l[3]])
        return xpath

    def ocr(s, path):
        ret = call(["./ocr.sh", path])
        word = ''
        with open(path + '.txt', 'r') as f:
            for line in f:
                word += line
        return word

    def post_process(s, texts):
        ''' Respond with json { 'valid':<true/false>, 'stopCode':<None/code> }'''

        for text in texts:
            # Remove leading and trailing spaces and return characters
            text = text.strip()

            # Remove all spaces
            text = text.replace(' ', '')
            text = text.replace('b', '6')
            text = text.replace('B', '8')

            # All lower case
            text = text.lower()

            # Need 2 out of 3 letters in mtd
            if 'mt' in text:
                i = text.find('mt')
                text = text[i+3:i+7]
            elif 'td' in text:
                i = text.find('td')
                text = text[i+2:i+6]
            elif 'm' in text and 'd' in text:
                md = False
                for x in range(len(text)-2):
                    if text[x] == 'm' and text[x+2] == 'd':
                        text = text[x+3:x+7]
                        md = True
                        break
                if not md:
                    continue
            else:
                continue

            # Needs to be of length 4
            if len(text) != 4:
                continue

            # Replace any letters with numbers
            text = text.replace('o', '0')
            text = text.replace('l', '1')
            text = text.replace('i', '1')
            text = text.replace('z', '2')
            text = text.replace('a', '4') # Capitol
            text = text.replace('s', '5')
            text = text.replace('g', '6') # Capitol
            text = text.replace('c', '6')

            # Make sure all numbers
            fail = False
            for n in text:
                if n not in digits:
                    fail = True
                    break
            if fail:
                continue

            return json.dumps({'valid':True, 'stopCode':text})

        return json.dumps({'valid':False, 'stopCode':None})

    def process_image(s):
        path = s.save_image()
        s.image_filter(path)
        locations = s.swt(path)
        words = []
        i = 0
        for location in locations:
            xpath = s.crop_image(path, location, i)
            words.append(s.ocr(xpath))
            i += 1
        response = s.post_process(words)
        return response

    # If someone went to "http://something.somewhere.net/foo/bar/",
    # then s.path equals "/foo/bar/".
    def do_HEAD(s):
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()

    def do_POST(s):
        response = s.process_image()
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()
        s.wfile.write(response)


if __name__ == '__main__':
    server_class = BaseHTTPServer.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, PORT_NUMBER)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, PORT_NUMBER)