docker stats `docker ps|grep -v ID|awk '{printf("%s ",$(NF));}'`
