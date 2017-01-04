#!/usr/bin/env python3

import sys


def main():

    path_to_users_file="﻿/home/user/nom-demo/compose/freeradius/src/users"
    total_args = len(sys.argv[1:])

    if total_args != 2:
        print("Required two args: username and password  ")
        sys.exit()
    else:
        username,password = sys.argv[1:]

    with open(path_to_users_file, "a") as myfile:
        x = "   "
        myfile.write("\n"+username+1*x+"Cleartext-Password := \""+password+"\"\n"+2*x+"Reply-Message = \"Hello, %{User-Name}, the auth was successful!\",\n"+2*x+"User-Ip = \"127.0.0.1\",\n"+2*x+"User-Id = user2id\n")

if __name__ == '__main__':main()
