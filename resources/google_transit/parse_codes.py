import json
from pprint import pprint

json_data=open('GetStops.json')
data = json.load(json_data)
stop_ids = [x['stop_id'] for x in data['stops']]
codes = [x['code'] for x in data['stops']]

for i in range(len(codes)):
	print stop_ids[i] + "," + codes[i]

json_data.close()