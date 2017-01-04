#!/usr/bin/env python

import os
import sys
import ConfigParser
import urllib
import urllib2
import time
import socket

HOME = 'HOME'
NOM_DEMO = 'nom-demo'

INFO_FILE = 'info.txt'
INFO_SECTION = 'info'
NAME_OPTION = 'name'
EMAIL_OPTION = 'email'

LOG_FILE = 'log.txt'

CALL_URL = 'http://demoinfo.nominum.com'
NAME_ARG = 'name'
EMAIL_ARG = 'emailaddress'
TIME_ARG = 'time'
ACTION_ARG = 'action'

#GOOGLE_IP = 'http://74.125.224.72'
GOOGLE_SERVER = "www.google.com"


name = None
email = None
need_to_save = False

def internet_on():
    try:
        host = socket.gethostbyname(GOOGLE_SERVER)
        s = socket.create_connection((host, 80), 2)
#        response=urllib2.urlopen(GOOGLE_IP, timeout=5)
        return True
    except: pass
    return False

def get_info(file_path = None):
    global name
    global email
    global need_to_save
    if file_path != None and os.path.exists(file_path):
        cfg = ConfigParser.ConfigParser()
        cfg.read(file_path)
        try:
            name = cfg.get(INFO_SECTION, NAME_OPTION)
        except ConfigParser.NoOptionError as exc:
            pass
        try:
            email = cfg.get(INFO_SECTION, EMAIL_OPTION)
        except ConfigParser.NoOptionError as exc:
            pass
    if name == None or name == '':
        name = raw_input('Please, provide your name: ')
        need_to_save = True
    if email == None or email == '':
        email = raw_input('Please, provide your email address: ')
        need_to_save = True

def save_info(file_path):
    if not os.path.exists(os.path.dirname(file_path)):
        try:
            os.makedirs(os.path.dirname(file_path))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise
    cfg_file = open(file_path, 'w')
    cfg = ConfigParser.ConfigParser()
    cfg.add_section(INFO_SECTION)
    cfg.set(INFO_SECTION, NAME_OPTION, name)
    cfg.set(INFO_SECTION, EMAIL_OPTION, email)
    cfg.write(cfg_file)
    cfg_file.close()

def add_log(file_path, log_entry):
    if not os.path.exists(os.path.dirname(file_path)):
        try:
            os.makedirs(os.path.dirname(file_path))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise
    log_file = open(file_path, 'a')
    log_file.write(log_entry + '\n')
    log_file.close()

def send_log(file_path):
    if internet_on() and os.path.exists(file_path):
        clean_log = True
        log_file = open(file_path, 'r+')
        for log_entry in log_file:
            try:
                urllib.urlopen(log_entry.rstrip('\n'))
            except:
                clean_log = False
        if clean_log:
            log_file.seek(0)
            log_file.truncate()
            log_file.close()

if len(sys.argv) > 1:
    action = sys.argv[1]

home_path = os.environ.get(HOME)

info_path = os.path.join(home_path, NOM_DEMO, INFO_FILE)
log_path = os.path.join(home_path, NOM_DEMO, LOG_FILE)

get_info(info_path)

if need_to_save:
    save_info(info_path)

call_args = ((NAME_ARG, name), (EMAIL_ARG, email), (TIME_ARG, time.time()), (ACTION_ARG, action))

log_entry = CALL_URL + '?' + urllib.urlencode(call_args)

add_log(log_path, log_entry)
send_log(log_path)