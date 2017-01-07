package com.nominum;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by vpathi on 12/19/16.
 */
public class DockerMachineCommand extends Command {

    private String command;
    private String googleProject;
    private String googleMachineType;
    private String googleZone;
    private String googleDiskSize;
    private String driver;
    private String googleMachineImage;
    private String vmName;
    private String ip;
    private String storagePath;
    private String filter;
    private String format;
    private File vmLogPath;
    private String userName;
    private String machineCommand;

    private DockerMachineCommand() {
    }

    public String getMachineCommand() {
        return machineCommand;
    }

    public void setMachineCommand(String machineCommand) {
        this.machineCommand = machineCommand;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getGoogleMachineImage() {
        return googleMachineImage;
    }

    public String getIp() {
        return ip;
    }

    public String getCommand() {
        return command;
    }

    public String getGoogleProject() {
        return googleProject;
    }

    public String getGoogleMachineType() {
        return googleMachineType;
    }

    public String getGoogleZone() {
        return googleZone;
    }

    public String getGoogleDiskSize() {
        return googleDiskSize;
    }

    public String getDriver() {
        return driver;
    }

    public String getVmName() {
        return vmName;
    }

    public String getFilter() {
        return filter;
    }

    public String getFormat() {
        return format;
    }



    public static class VmNamesCommandBuilder {
        DockerMachineCommand instance;
        public VmNamesCommandBuilder() {
            instance = new DockerMachineCommand();
        }

        public VmNamesCommandBuilder command(String command) {
            instance.command = command;
            return this;

        }
        public VmNamesCommandBuilder storagePath(String storagePath) {
            instance.storagePath = storagePath;
            return this;
        }
        public VmNamesCommandBuilder format(String format) {
            instance.format = format;
            return this;
        }

        private void setmachineCommand(){

            instance.machineCommand =
                    DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                    instance.storagePath + " " +
                    instance.command + " " +
                    DockerMachineCommandLineConstants.FORMAT+" "+
                    instance.format;
        }


        public DockerMachineCommand build() {
            setmachineCommand();
            return instance;
        }
    }
    public static class VmIPCommandBuilder {
        DockerMachineCommand instance;
        public VmIPCommandBuilder() {
            instance = new DockerMachineCommand();
        }

        public VmIPCommandBuilder command(String command) {
            instance.command = command;
            return this;

        }
        public VmIPCommandBuilder storagePath(String storagePath) {
            instance.storagePath = storagePath;
            return this;
        }
        public VmIPCommandBuilder filter(String filter) {
            instance.filter = filter;
            return this;
        }

        public VmIPCommandBuilder format(String format) {
            instance.format = format;
            return this;
        }
        public VmIPCommandBuilder vmName(String vmName) {
            instance.vmName = vmName;
            return this;
        }

        private void setmachineCommand(){

            instance.machineCommand=
                    DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                    instance.storagePath + " " +
                    instance.command + " " +
                    instance.vmName;
        }

        public DockerMachineCommand build() {
            setmachineCommand();
            return instance;
        }


    }


    public static class VmInfoCommandBuilder {
        DockerMachineCommand instance;

        public VmInfoCommandBuilder() {
            instance = new DockerMachineCommand();
        }

        public VmInfoCommandBuilder command(String command) {
            instance.command = command;
            return this;

        }

        public VmInfoCommandBuilder storagePath(String storagePath) {
            instance.storagePath = storagePath;
            return this;
        }

        public VmInfoCommandBuilder filter(String filter) {
            instance.filter = filter;
            return this;
        }


        public VmInfoCommandBuilder format(String ...formats) {
            instance.format = "";
            for(String f: formats) {
                instance.format += f + "\\\\n";
            }

            return this;
        }
        private void setmachineCommand(){

            instance.machineCommand=
                    DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                    instance.storagePath + " " +
                    instance.command + " " +
                    DockerMachineCommandLineConstants.FILTER+" "+
                    DockerMachineCommandLineConstants.FILTER_ATTRIB_NAME+
                    instance.filter+ " "+
                    DockerMachineCommandLineConstants.FORMAT+ " "+
                    instance.format;

        }


        public DockerMachineCommand build() {
            setmachineCommand();
            return instance;
        }
    }
    public static class VmDeleteCommandBuilder {
        DockerMachineCommand instance;
        public VmDeleteCommandBuilder() {
            instance = new DockerMachineCommand();
        }


        public VmDeleteCommandBuilder command(String command) {
            instance.command = command;
            return this;

        }
        public VmDeleteCommandBuilder storagePath(String storagePath) {
            instance.storagePath = storagePath;
            return this;
        }
        public VmDeleteCommandBuilder filter(String filter) {
            instance.filter = filter;
            return this;
        }

        public VmDeleteCommandBuilder vmName(String vmName) {
            instance.vmName = vmName;
            return this;
        }
        private void setmachineCommand(){

            instance.machineCommand=
                    DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            instance.storagePath + " " +
                            instance.command + " " +
                            instance.vmName;
        }


        public DockerMachineCommand build() {
            setmachineCommand();
            return instance;
        }



    }

    public static class VmCreateCommandBuilder {
        DockerMachineCommand instance;

        public VmCreateCommandBuilder() {
            instance = new DockerMachineCommand();
        }

        public VmCreateCommandBuilder command(String command) {
            instance.command = command;
            return this;

        }

        public VmCreateCommandBuilder storagePath(String storagePath) {

            instance.storagePath = storagePath;
            return this;
        }

        public VmCreateCommandBuilder googleProject(String googleProject) {
            instance.googleProject = googleProject;
            return this;

        }

        public VmCreateCommandBuilder googleMachineType(String googleMachineType) {
            instance.googleMachineType = googleMachineType;
            return this;
        }

        public VmCreateCommandBuilder googleZone(String googleZone) {
            instance.googleZone = googleZone;
            return this;
        }

        public VmCreateCommandBuilder googleDiskSize(String googleDiskSize) {
            instance.googleDiskSize = googleDiskSize;
            return this;
        }

        public VmCreateCommandBuilder driver(String driver) {
            instance.driver = driver;
            return this;
        }

        public VmCreateCommandBuilder googleMachineImage(String googleMachineImage) {
            instance.googleMachineImage = googleMachineImage;
            return this;
        }

        public VmCreateCommandBuilder vmName(String vmName) {
            instance.vmName = vmName;
            return this;
        }
        public VmCreateCommandBuilder userName(String userName) {
            instance.userName = userName;
            return this;
        }


        private void setmachineCommand(){
            String fileName =instance.vmName+"_"+"consoleOutput";

            instance.vmLogPath = new File (instance.storagePath,fileName);

            instance.machineCommand= DockerMachineCommandLineConstants.DEBUG +" "+
                    DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                    instance.storagePath + " " +
                    instance.command + " " +
                    DockerMachineCommandLineConstants.DRIVER + " " +
                    instance.driver + " " +
                    DockerMachineCommandLineConstants.GOOGLE_PROJECT + " " +
                    instance.googleProject + " " +
                    DockerMachineCommandLineConstants.GOOGLE_MACHINE_TYPE + " " +
                    instance.googleMachineType + " " +
                    DockerMachineCommandLineConstants.GOOGLE_ZONE + " " +
                    instance.googleZone + " " +
                    DockerMachineCommandLineConstants.GOOGLE_DISK_SIZE + " " +
                    instance.googleDiskSize + " " +
                    DockerMachineCommandLineConstants.GOOGLE_MACHINE_IMAGE + " " +
                    instance.googleMachineImage + " " +
                    instance.vmName;
        }


        public DockerMachineCommand build() {
            setmachineCommand();
            return instance;
        }

    }

    @Override
    public void stop() {

    }

    @Override
    protected File getVmLogPath() {
        return vmLogPath ;
    }


    @Override
    public ProcessBuilder getProcessBuilder() throws Exception {
            String machine= getExecutablePath();
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " + this.machineCommand);
    }

    @PostConstruct
    public String getExecutablePath() throws Exception {
        Resource resource = new ClassPathResource("/binaries/docker-machine");
        File file = resource.getFile();
        return file.getAbsolutePath();

    }



}

