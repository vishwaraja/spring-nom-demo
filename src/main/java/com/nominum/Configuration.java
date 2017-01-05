package com.nominum;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpathi on 12/19/16.
 */

public class Configuration {

    private List<Command> commands;
    private String driverType;
    private String version;
    private static URL path = Configuration.class
            .getClassLoader().getResource("machineStorage");
    private static String machineStoragePath = path.getPath();


    public static Configuration fromPostParams(String driver, String version, String userName) {

        String vmName=userName+version;

        Configuration configuration = new Configuration();

        if (driver.equals("google")) {

            DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.Builder()
                    .command("create")
                    .storagePath(machineStoragePath + "/" + userName)
                    .driver("google")
                    .googleProject("nominum-docker-machines")
                    .googleMachineType("n1-standard-4")
                    .googleZone("us-west1-a")
                    .googleDiskSize("150")
                    .googleMachineImage("https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/ubuntu-1404-trusty-v20160627")
                    .vmName(vmName)
                    .build();

            DockerMachineCommand dockerMachineIp = new DockerMachineCommand.Builder()
                    .command("ip")
                    .driver("google")
                    .storagePath(machineStoragePath + "/" + userName)
                    .vmName(vmName)
                    .build();


            DockerCommand dockerCommand = new DockerCommand.Builder()
                    .command("login")
                    .registryUser("nominum")
                    .port("2376")
                    .registryPassword("darthVaderForTrump2020")
                    .tlsCacert(machineStoragePath + "/" + userName + "/machines/" + vmName + "/ca.pem")
                    .tlsCert(machineStoragePath + "/" + userName + "/machines/" + vmName + "/cert.pem")
                    .tlsKey(machineStoragePath + "/" + userName + "/machines/" + vmName + "/key.pem")
                    .build();
            DockerMachineCommand dockerMachineIp2 = new DockerMachineCommand.Builder()
                    .command("ip")
                    .driver("google")
                    .storagePath(machineStoragePath + "/" + userName)
                    .vmName(vmName)
                    .build();

            URL composePath = Configuration.class
                    .getClassLoader().getResource("docker/16-2");
            String ymlPath = composePath.getPath() + "/" + "docker-compose.yml";

            DockerComposeCommand dockerComposeCommand = new DockerComposeCommand.Builder()
                    .command("up")
                    .port("2376")
                    .yml(ymlPath)
                    .tlsCacert(machineStoragePath + "/" + userName + "/machines/" + vmName + "/ca.pem")
                    .tlsCert(machineStoragePath + "/" + userName + "/machines/" + vmName + "/cert.pem")
                    .tlsKey(machineStoragePath + "/" + userName + "/machines/" + vmName + "/key.pem")
                    .build();


            configuration.commands = new ArrayList<>();
            configuration.commands.add(dockerMachineCommand);
            configuration.commands.add(dockerMachineIp);
            configuration.commands.add(dockerCommand);
            configuration.commands.add(dockerMachineIp2);
            configuration.commands.add(dockerComposeCommand);

        }

        return configuration;
    }

    public static Configuration deleteFromPostParams(String vmName,String userName) {

        Configuration configuration = new Configuration();
        DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.Builder()
                .command("rm -f")
                .storagePath(machineStoragePath + "/" + userName)
                .vmName(vmName)
                .build();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(dockerMachineCommand);
        return configuration;

    }

    public static Configuration forVmList(String userName) {
        Configuration configuration = new Configuration();


        DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.Builder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .build();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(dockerMachineCommand);
        return configuration;
    }
    public static Configuration forVmListInfo(String userName,String vmName) {

        Configuration configuration = new Configuration();
        DockerMachineCommand ip = new DockerMachineCommand.Builder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .filter(vmName)
                .format("url")
                .build();

        DockerMachineCommand state = new DockerMachineCommand.Builder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .filter(vmName)
                .format("state")
                .build();
        DockerMachineCommand driver = new DockerMachineCommand.Builder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .filter(vmName)
                .format("driver")
                .build();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(ip);
        configuration.commands.add(state);
        configuration.commands.add(driver);

        return configuration;
    }




    public List<Command> getCommands() {
        return commands;
    }






}
