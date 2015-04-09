import json
import os
import urllib


setup_file = '../src/main/java/com/ut/mpc/etch/Setup.java'

def extract_var(var_name, data_file):
    for line in data_file:
        if var_name in line:
            splits = line.split("=")
            raw_value = splits[1].strip()[1:-2]
            value = raw_value.strip()
            return value

base_dir = ''
with open(setup_file, 'r') as data_file:
    base_dir = extract_var('BASE_DIR', data_file)

with open('eval_config.json') as data_file:    
    data = json.load(data_file)

command = 'adb shell "am start -e plan ' + urllib.quote(json.dumps(data)) + ' -n ' + base_dir + '/com.ut.mpc.etch.EtchBaseActivity"'
os.system(command)
