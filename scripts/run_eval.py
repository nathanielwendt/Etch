import json
import os
import urllib

with open('eval_config.json') as data_file:    
    data = json.load(data_file)

command = 'adb shell "am start -e plan ' + urllib.quote(json.dumps(data)) + ' -n com.ut.mpc/.etch.EtchBaseActivity"'
os.system(command)
