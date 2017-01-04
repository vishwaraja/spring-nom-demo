#!/usr/bin/env python
import kafka
import snappy
import subprocess
import json
import time
import terminaltables
import signal
import sys
import os

TELEMETRY_TOPIC = 'nom-telemetry'

def signal_handler(signal, frame):
    sys.exit(0)

def get_kafka_server():

    cmd = "docker ps 2>/dev/null | grep \"_nomkafka \" | awk '{print $(NF)}' | head -1"
    kafka_container_name = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0].strip()

    cmd = "docker exec -it %s bash -c \"cat /usr/local/nom/etc/nom-kafka/server.properties | grep advertised.host.name\" | awk -F= '{print $(NF)}'" % kafka_container_name
    kafka_host = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0].strip()

    cmd = "docker ps 2>/dev/null |grep \"_nomkafka \" | awk -F 0.0.0.0: '{print $(NF)}' | head -1 | awk -F/tcp '{print $1}' | awk -F- '{print $1}'"
    kafka_port = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0].strip()

    kafka_server = kafka_host + ":" + kafka_port

    return kafka_server


def draw(data):
    table_data = [['Creator', 'Memory in use', 'CPU time']]
    for name in data:
        memory = '' if data[name]['memory'] == None else str(data[name]['memory'])
        cpu = '' if data[name]['cpu'] == None else str(data[name]['cpu'])
        table_data.append([name, memory, cpu])
    table = terminaltables.SingleTable(table_data)
    table.title = kafka_server + ' ' + TELEMETRY_TOPIC
    print(table.table)
    print('')
    print('<control-C> to close the session')

print('Getting kafka server address from docker...')
kafka_server = get_kafka_server()
print('Connecting to kafka server ' + kafka_server + '...')

consumer = None
try:
    consumer = kafka.KafkaConsumer(bootstrap_servers=kafka_server)
    part0 = kafka.common.TopicPartition(TELEMETRY_TOPIC, 0)
    consumer.assign([part0])
    consumer.seek_to_end(part0)
except:
    print('Kafka server is unreachable.')
    sys.exit(0)


data = {}

signal.signal(signal.SIGINT, signal_handler)
signal.signal(signal.SIGTERM, signal_handler)

for message in consumer:
    time.sleep(0.1)
    json_message = json.loads(message.value)
    creator = json.dumps(json_message['creator']).strip('"')
    memory = json_message['content'].get('memory-in-use')
    cpu = json_message['content'].get('cpu-time')
    
    telemetry = data.get(creator)
    if telemetry == None:
        data[creator] = {'memory': memory, 'cpu': cpu}
    else:
        if memory != None and (telemetry['memory'] == None or memory > telemetry['memory']):
            telemetry['memory'] = memory
        if cpu != None and (telemetry['cpu'] == None or cpu > telemetry['cpu']):
            telemetry['cpu'] = cpu
        data[creator] = telemetry
    os.system('clear')
    draw(data)


signal.pause()