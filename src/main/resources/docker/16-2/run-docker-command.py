#!/usr/bin/env python
# -*- coding: utf-8 -*-
# Topmenu and the submenus are based of the example found at this location http://blog.skeltonnetworks.com/2010/03/python-curses-custom-menu/
# The rest of the work was done by Matthew Bennett and he requests you keep these two mentions when you reuse the code :-)
# Basic code refactoring by Andrew Scheller

from time import sleep
import subprocess
import re
import os
import curses
import pytoml as toml

CONFIG_FILE = "menu_data.toml"
is_docker_machine = False
docker_machine_name = None
stack = ""
menu_data = None

screen = None
screen_h = None
screen_n = None

def check_docker_machine():
    global is_docker_machine
    global docker_machine_name
    docker_host = ""
    DOCKER_MACHINE_FILE_PATH = "/usr/local/bin/docker-machine"
    #if docker is configured on docker-machine
    if os.path.isfile(DOCKER_MACHINE_FILE_PATH):
        docker_machine_name = os.environ.get('DOCKER_MACHINE_NAME')
        if docker_machine_name == None:
            docker_machine_name = ""
            return
        CMD_CHECK_DOCKER_MACHINE_STATUS = "docker-machine status " + docker_machine_name
        ps = subprocess.Popen(CMD_CHECK_DOCKER_MACHINE_STATUS, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        docker_status = ps.communicate()[0]
        #if docker-machine is running
        if docker_status.rstrip() != "Running":
            print("docker machine is not running....")
            sleep(4)
            exit()	
        docker_host = os.environ.get('DOCKER_HOST')
        docker_host_ip = docker_host.rsplit("//")[0].split(":")[0]
        CMD_DOCKER_MACHINE_IP = "docker-machine ip " + docker_machine_name
        docker_machine_ip = subprocess.Popen([CMD_DOCKER_MACHINE_IP], shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT).communicate()[0]
        #if DOCKER_HOST variable contains same ip as docker-machine ip
        if docker_host_ip.rstrip() == docker_machine_ip.rstrip():
            is_docker_machine = True

def wrap_cmd(cmd):
    #add docker variables to command
    CMD_EVAL = "eval $(docker-machine env " + docker_machine_name + ")"
    if is_docker_machine:
        return CMD_EVAL + "&&" + cmd
    return cmd

def check_conf():
    global menu_data
    #check config file existence
    current_path = os.path.dirname(os.path.realpath(__file__))
    config_file_path = os.path.join(current_path, CONFIG_FILE)

    if not os.path.isfile(config_file_path):
        print("Config file %s is not exist!" % config_file_path)
        exit()

    #load menu data from config
    with open(config_file_path) as conffile:
        config = toml.load(conffile)
        menu_data = config["menu_data"]

def get_stack():
    global stack
    #get docker stack name from uui container
    CMD_FIND_RUNNING_CONTAINER = "docker ps 2>/dev/null|awk '{print $2}'|grep uui|awk -F_ '{print $1}'"
    cmd = wrap_cmd(CMD_FIND_RUNNING_CONTAINER)
    ps = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stack = ps.communicate()[0]
    if stack != "":
        print("You are running the stack " + stack)
    else:
        print("No stack is currently running")
        sleep(4)
        exit()

def log_call(action):
    INVOKE_AT_STARTUP = "./invoke_at_startup.py"
    if os.path.exists(os.path.dirname(INVOKE_AT_STARTUP)):
        CMD_INVOKE_AT_STARTUP = "python %s '%s'" % (INVOKE_AT_STARTUP, action)
        ps = subprocess.call(CMD_INVOKE_AT_STARTUP, shell=True)
        os.system('reset')
        screen.clear()


def init_screen():
    global screen
    global screen_h
    global screen_n
    screen = curses.initscr() #initializes a new window for capturing key presses
    curses.noecho() # Disables automatic echoing of key presses (prevents program from input each key twice)
    curses.cbreak() # Disables line buffering (runs each key as it is pressed rather than waiting for the return key to pressed)
    curses.echo()
    curses.start_color() # Lets you use colors when highlighting selected menu option
    screen.keypad(1) # Capture input from keypad

    # Change this to use different colors when highlighting
    curses.init_pair(1, curses.COLOR_BLACK, curses.COLOR_WHITE) # Sets up color pair #1, it does black text with white background
    screen_h = curses.color_pair(1) #h is the coloring for a highlighted menu option
    screen_n = curses.A_NORMAL #n is the coloring for a non highlighted menu option

#MENU = "menu"
#COMMAND = "command"
#EXITMENU = "exitmenu"


# This function displays the appropriate menu and returns the option selected
def runmenu(menu, parent):
    # work out what text to display as the last menu option
    if parent is None:
        lastoption = "Exit"
    else:
        lastoption = "Return to menu"
  
    optioncount = len(menu['options']) # how many options in this menu
    pos = 0 #pos is the zero-based index of the hightlighted menu option. Every time runmenu is called, position returns to 0, when runmenu ends the position is returned and tells the program what opt$
    oldpos = None # used to prevent the screen being redrawn every time
    x = None #control for while loop, let's you scroll through options until return key is pressed then returns pos to program

    # Loop until return key is pressed
    while x != ord('\n'):

        if pos != oldpos:
            oldpos = pos
            screen.border(0)
            screen.addstr(2, 2, menu['title'], curses.A_STANDOUT) # Title for this menu
            screen.addstr(4, 2, menu['subtitle'], curses.A_BOLD) #Subtitle for this menu

            # Display all the menu items, showing the 'pos' item highlighted
            for index in range(optioncount):
                textstyle = screen_n
                if pos == index:
                    textstyle = screen_h
                screen.addstr(5 + index, 4, "%d - %s" % (index + 1, menu['options'][index]['title']), textstyle)

            # Now display Exit/Return at bottom of menu
            textstyle = screen_n
            if pos == optioncount:
                textstyle = screen_h
            screen.addstr(5 + optioncount, 4, "%d - %s" % (optioncount + 1, lastoption), textstyle)
            screen.refresh()
            # finished updating screen

        x = screen.getch() # Gets user input

        # What is user input?
        if x >= ord('1') and x <= ord(str(optioncount + 1)[0]):
            pos = x - ord('0') - 1 # convert keypress back to a number, then subtract 1 to get index
        elif x == 258: # down arrow
            if pos < optioncount:
                pos += 1
            else: pos = 0
        elif x == 259: # up arrow
            if pos > 0:
                pos += -1
            else: pos = optioncount

    # return index of the selected item
    return pos

# This function calls showmenu and then acts on the selected item
def processmenu(menu, parent=None):
    
    optioncount = len(menu['options'])
    exitmenu = False
    while not exitmenu: #Loop until the user exits the menu
        getin = runmenu(menu, parent)
        if getin == optioncount:
            exitmenu = True
        elif menu['options'][getin]['type'] == 'COMMAND':
            dockerContainer = None
            container_name = menu['options'][getin]['container_name']
            if container_name != None and container_name != "":
                dockerContainer = '%s_%s_1' % (stack.rstrip(), container_name)
#            container_names =  menu['options'][getin]['container_name']
#            dockerContainers = [""] * len(container_names)
#            count = 0
#            for container_name in container_names:                     
#                 dockerContainers[count] = '%s_%s_1' % (stack.rstrip(), container_names[count])                 
#                 count = count + 1
            curses.def_prog_mode()    # save curent curses environment
            os.system('reset')
            screen.clear() #clears previous screen
            screen.keypad(0)
            help = menu['options'][getin]['help']
            container_command = menu['options'][getin]['command']
            log_call(menu['options'][getin]['type'] + ' ' + menu['options'][getin]['title'])
            if dockerContainer != None:
                subprocess.call(['echo', "**************** Container: " + dockerContainer + " below **************\n"])
#            subprocess.call(['echo', "**************** Container: " + dockerContainers[0] + " below **************\n"])
                container_command = container_command % dockerContainer.rstrip()
#            container_command = menu['options'][getin]['command']%tuple(dockerContainers)
            cmd = wrap_cmd(container_command)
            subprocess.call(['printf', help + '\n' + container_command + '\n'])
            proc = subprocess.Popen(cmd, shell=True, stderr=subprocess.STDOUT)
            proc.communicate()[0]
            raw_input('\n\nPress [Return] key to continue...')
            screen.clear() #clears previous screen on key press and updates display based on pos
            screen.keypad(1)
            curses.reset_prog_mode()   # reset to 'current' curses environment
            curses.curs_set(1)         # reset doesn't do this right
            curses.curs_set(0)
        
        elif menu['options'][getin]['type'] == 'MENU':
            screen.clear() #clears previous screen on key press and updates display based on pos
            processmenu(menu['options'][getin], menu) # display the submenu
            screen.clear() #clears previous screen on key press and updates display based on pos
        elif menu['options'][getin]['type'] == 'EXITMENU':
            exitmenu = True

#******************
#*# Main program **
#******************
check_conf()
check_docker_machine()
get_stack()
init_screen()
processmenu(menu_data['options'][0])
curses.endwin() #VITAL! This closes out the menu system and returns you to the bash prompt.
os.system('clear')