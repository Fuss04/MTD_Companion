import sys
import os
import csv
import json
import httplib2 as http
try:
    from urlparse import urlparse
except ImportError:
    from urllib.parse import urlparse

# Usage: python get_stop_codes.py stops.txt <idx_start> <idx_end> > out.csv
def main(argv):
    csv_file = argv[1]
    idx_start = int(argv[2])
    idx_end = int(argv[3])

    stop_ids = []
    stop_codes = []
    with open(csv_file, 'rU') as csvfile:
        stops_reader = csv.reader(csvfile, delimiter=',', quotechar='"', dialect=csv.excel_tab)
        stops_reader.next() # Skips first line
        for row in stops_reader:
            stop_ids.append(row[0])

    headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json; charset=UTF-8'
    }

    uri = 'http://developer.cumtd.com'
    path = '/api/v2.2/json/GetStop'
    key = '5d254f4e925f48a08aab977cd1ecfc7b'
    stop_id = ''

    method = 'GET'
    body = ''

    h = http.Http()

    idx = idx_start
    for val in stop_ids[idx_start:idx_end]:
        target = urlparse(uri+path+'?key='+key+'&stop_id='+val)
        response, content = h.request(target.geturl(),method,body,headers)
        data = json.loads(content)
        print data
        # Save in stop_codes and write to stops.txt
        idx += 1

if __name__ == '__main__':
    main(sys.argv)