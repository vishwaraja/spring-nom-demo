package com.nominum;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vpathi on 12/19/16.
 */

public class Configuration {

    private List<Command> commands;
    private static String machineStoragePath =null;


    public static Configuration fromPostParams(String driver, String version, String userName) {

        String vmName=userName+version;
        Configuration configuration = new Configuration();
        machineStoragePath= configuration.getStoragePath();

        if (driver.equals("google")) {

            DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.VmCreateCommandBuilder()
                    .command("create")
                    .storagePath(machineStoragePath + "/" + userName)
                    .driver("google")
                    .googleProject("nominum-docker-machines")
                    .googleMachineType("n1-standard-4")
                    .googleZone("us-west1-a")
                    .googleDiskSize("150")
                    .googleMachineImage("https://www.googleapis.com/compute/v1/projects/ubuntu-os-cloud/global/images/ubuntu-1404-trusty-v20160627")
                    .vmName(vmName)
                    .userName(userName)
                    .build();

            DockerMachineCommand dockerMachineIp = new DockerMachineCommand.VmIPCommandBuilder()
                    .command("ip")
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
            DockerMachineCommand dockerMachineIp2 = new DockerMachineCommand.VmIPCommandBuilder()
                    .command("ip")
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

    public static Configuration forVmActions(String vmName,String userName,String action) {
        Map<String,String> actions= new HashMap<String,String>();
        actions.put("start","start");
        actions.put("stop","stop");
        actions.put("delete","rm -f");

        Configuration configuration = new Configuration();
        machineStoragePath= configuration.getStoragePath();
        DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.VmDeleteCommandBuilder()
                .command(actions.get(action))
                .storagePath(machineStoragePath + "/" + userName)
                .vmName(vmName)
                .build();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(dockerMachineCommand);
        return configuration;

    }

    public static Configuration forVmList(String userName) {
        Configuration configuration = new Configuration();
        machineStoragePath= configuration.getStoragePath();


        DockerMachineCommand dockerMachineCommand = new DockerMachineCommand.VmNamesCommandBuilder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .format(DockerMachineCommandLineConstants.VMNAME)
                .build();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(dockerMachineCommand);
        return configuration;
    }
    public static Configuration forVmListInfo(String userName,String vmName) {

        Configuration configuration = new Configuration();
        configuration.commands = new ArrayList<>();
        configuration.commands.add(new DockerMachineCommand.VmInfoCommandBuilder()
                .command("ls")
                .storagePath(machineStoragePath + "/" + userName)
                .filter(vmName.trim())
                .format(DockerMachineCommandLineConstants.URL,
                        DockerMachineCommandLineConstants.STATE,
                        DockerMachineCommandLineConstants.DRIVER_TYPE,
                        DockerMachineCommandLineConstants.VMNAME)
                .build());

        return configuration;
    }
    public List<Command> getCommands() {
        return commands;
    }

//    @PostConstruct
//    public  String getStoragePath()  {
//        Resource resource = new ClassPathResource("machineStorage");
//        File file = null;
//        try {
//            file = resource.getFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return file.getAbsolutePath();
//
//    }

}
