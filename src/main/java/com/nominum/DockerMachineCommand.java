package com.nominum;

import java.net.URL;

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

    // No-op constructor for builder
    private DockerMachineCommand() {
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getGoogleMachineImage() {
        return googleMachineImage;
    }

    public String getStoragePath() {
        return storagePath;
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



    @Override
    public String toString() {
        return DockerMachineCommandLineConstants.SHELL + " " +
                DockerMachineCommandLineConstants.SHELL_PARAM + " " +
                DockerMachineCommandLineConstants.DOCKER_MACHINE + " " +
                this.command + " " +
                DockerMachineCommandLineConstants.DRIVER + " " +
                this.driver + " " +
                DockerMachineCommandLineConstants.GOOGLE_PROJECT + " " +
                this.googleProject + " " +
                DockerMachineCommandLineConstants.GOOGLE_MACHINE_TYPE + " " +
                this.googleMachineType + " " +
                DockerMachineCommandLineConstants.GOOGLE_ZONE + " " +
                this.googleZone + " " +
                DockerMachineCommandLineConstants.GOOGLE_DISK_SIZE + " " +
                this.googleDiskSize + " " +
                DockerMachineCommandLineConstants.GOOGLE_MACHINE_IMAGE + " " +
                this.googleMachineImage + " " +
                this.vmName;
    }


    public static class Builder {
        DockerMachineCommand instance;

        public Builder() {
            instance = new DockerMachineCommand();
        }

        public static DockerMachineCommand from(Builder builder) {
            return builder.build();
        }

        public Builder command(String command) {
            instance.command = command;
            return this;

        }

        public Builder storagePath(String storagePath) {
            instance.storagePath = storagePath;
            return this;
        }

        public Builder googleProject(String googleProject) {
            instance.googleProject = googleProject;
            return this;

        }

        public Builder googleMachineType(String googleMachineType) {
            instance.googleMachineType = googleMachineType;
            return this;
        }

        public Builder googleZone(String googleZone) {
            instance.googleZone = googleZone;
            return this;
        }

        public Builder googleDiskSize(String googleDiskSize) {
            instance.googleDiskSize = googleDiskSize;
            return this;
        }

        public Builder driver(String driver) {
            instance.driver = driver;
            return this;
        }

        public Builder googleMachineImage(String googleMachineImage) {
            instance.googleMachineImage = googleMachineImage;
            return this;
        }

        public Builder vmName(String vmName) {
            instance.vmName = vmName;
            return this;
        }
        public Builder filter(String filter) {
            instance.filter = filter;
            return this;
        }

        public Builder format(String format) {
            instance.format = format;
            return this;
        }

        public Builder format(String ...formats) {
            instance.format = "";
            for(String f: formats) {
                instance.format += f + "\\\\n";
            }

            return this;
        }

        public Builder ip(String ip) {
            instance.ip = ip;
            return this;
        }

        public DockerMachineCommand build() {
            //validateUserObject(dockerMachineCommand);
            return instance;
        }
    }



    @Override
    public ProcessBuilder getProcessBuilder() {
        String WHITESPACE=" ";
        URL path = DockerMachineCommand.class
                .getClassLoader().getResource("binaries/docker-machine");
        String machine = path.getPath().replaceFirst("^(file:)?(www\\.)?", "");


        if(this.command == null) { return null; }

        if  (this.command.equals("create") ) {

            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " + "--debug  " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            DockerMachineCommandLineConstants.DRIVER + " " +
                            this.driver + " " +
                            DockerMachineCommandLineConstants.GOOGLE_PROJECT + " " +
                            this.googleProject + " " +
                            DockerMachineCommandLineConstants.GOOGLE_MACHINE_TYPE + " " +
                            this.googleMachineType + " " +
                            DockerMachineCommandLineConstants.GOOGLE_ZONE + " " +
                            this.googleZone + " " +
                            DockerMachineCommandLineConstants.GOOGLE_DISK_SIZE + " " +
                            this.googleDiskSize + " " +
                            DockerMachineCommandLineConstants.GOOGLE_MACHINE_IMAGE + " " +
                            this.googleMachineImage + " " +
                            this.vmName);
        } else if (this.command.equals("ip")) {
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            this.vmName);

        }
        else if(this.command.equals("rm -f")){
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            this.vmName);

        }
        else if(this.command.equals("ls")&&this.format.equals(DockerMachineCommandLineConstants.VMNAME) && this.filter==null ) {
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            DockerMachineCommandLineConstants.FORMAT + " " +
                            DockerMachineCommandLineConstants.VMNAME
                            );

        }
        else if(this.command.equals("ls") && this.filter != null && !this.filter.isEmpty() && this.format.equals("url")){
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            DockerMachineCommandLineConstants.FILTER+ " " +
                            DockerMachineCommandLineConstants.FILTER_ATTRIB_NAME+// no sapce required here
                            this.filter+ " " +
                            DockerMachineCommandLineConstants.FORMAT + " " +
                            DockerMachineCommandLineConstants.URL);
        }

        else {
            return new ProcessBuilder().command(
                    DockerMachineCommandLineConstants.SHELL,
                    DockerMachineCommandLineConstants.SHELL_PARAM,
                    machine + " " +
                            DockerMachineCommandLineConstants.STORAGE_PATH + " " +
                            this.storagePath + " " +
                            this.command + " " +
                            DockerMachineCommandLineConstants.FILTER+ " " +
                            DockerMachineCommandLineConstants.FILTER_ATTRIB_NAME+// no sapce required here
                            this.filter+ " " +
                            DockerMachineCommandLineConstants.FORMAT + " " +
                            this.format);
        }
    }

    @Override
    public void stop() {

    }




}