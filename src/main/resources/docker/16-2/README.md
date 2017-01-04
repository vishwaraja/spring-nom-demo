#Compose


The Compose stack is a collection of containers orchestrated with docker-compose. docker-compose is a tool for defining and running multi-container Docker applications. With Docker-Compose, you use a Compose file to configure your application's services. Then, using a single command, you create and start all the services from your configuration.

####Solutions intergrated with compose:

1. Netview
2. Threatavert
3. Reach
4. Engage - (Household only)

Due to the complex configurations involved in Engage-per-device it has its own dedicated stack to make troubeshooting easier.

###Getting started:

The stack takes two minutes to come up fully configured.

Please run the below command from the root of the directory.

	docker-compose up



###Accessing Apps Portal:

To access the Apps Portal you need to know the IP of the VM created by docker-machine.

	1. Get the ip of your vm :

		docker-machine ip <vm-name>

	2.On your favourite browser:

		http://<vm-ip>:8081 	


###Logging into containers:

	1. List the contianers running by docker-compose

		docker-compose ps

	2. Log into the container

		 docker exec -it <container-name> bash

###Recreating containers

In general it's good practise to recreate your containers (after pulling newer images) . This will reflect code changes.

	1. Download the latest images, to do so run

		./pull-all.sh

	2. Recreate containers

		docker-compose rm -f; docker-compose build --no-cache

	3. Bring the stack up

		docker-compose up


#####Docker command line reference:

	https://docs.docker.com/engine/reference/commandline/cli/

#####Compose command line reference:

	https://docs.docker.com/compose/reference/


#####Debugging:
* Logs usually follow the naming scheme of `/var/log/processname-stdout**.log` and `/var/log/processname-stderr**.log`
     
######Note:
A variable called "HOST" has been added to this stack for a use case where the user would want to use a custom linux vm as his/her docker host in which case this variable must be set to the docker host ip in the host OS before bringing the stack up . This will allow the user to perform end-end tests from the host OS. Therefore please ignore the below warning message when you bring the stack up if you are on OSX or not applying this use case.

	WARNING: The HOST variable is not set. Defaulting to a blank string.
