package com.nominum;

import java.net.URL;

/**
 * Created by vpathi on 12/18/16.
 */
public class DockerComposeCommand
        extends Command
        implements NeedsLastCommandOutput {

    private String yml;
    private String port;
    private String command;
    private String tlscert;
    private String tlsverify;
    private String tlscacert;
    private String tlskey;

    // No-op constructor for builder
    private DockerComposeCommand(){}
    public String getTlscert() {
        return tlscert;
    }

    public String getTlsverify() {
        return tlsverify;
    }

    public String getTlscacert() {
        return tlscacert;
    }

    public String getTlskey() {
        return tlskey;
    }

    public String getYml() {
        return yml;
    }

    public String getPort() {
        return port;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void setLastCommandOutput(String lastOutput) {
        lastCommandOutput = lastOutput;
    }

    public static class Builder{
        DockerComposeCommand instance;

        public Builder(){
            instance = new DockerComposeCommand();

        }
        public Builder tlsKey (String tlskey){
            instance.tlskey = tlskey;
            return this;
        }
        public Builder tlsCacert (String tlscacert){
            instance.tlscacert = tlscacert;
            return this;
        }

        public Builder tlsVerify (String tlsverify){
            instance.tlsverify = tlsverify;
            return this;
        }

        public Builder tlsCert (String tlscert){
            instance.tlscert = tlscert;
            return this;
        }

        public Builder port (String port){
            instance.port = port;
            return this;
        }
        public Builder yml (String yml){
            instance.yml = yml;
            return this;
        }

        public Builder command (String command){
            instance.command = command;
            return this;
        }

        public DockerComposeCommand build() {
            //validateUserObject(dockerMachineCommand);
            return instance;
        }
        public static DockerComposeCommand from(Builder builder) {
            return builder.build();
        }

    }


    @Override
    public ProcessBuilder getProcessBuilder() {
        URL path = DockerComposeCommand.class
                .getClassLoader().getResource("binaries/docker-compose");
        String compose  = path.getPath().replaceFirst("^(file:)?(www\\.)?", "");



        return new ProcessBuilder().command(

                DockerComposeCommandLineConstants.SHELL,
                DockerComposeCommandLineConstants.SHELL_PARAM,
                compose+" "+
                DockerComposeCommandLineConstants.FILE_PARAM+ " "+
                        this.yml+ " "+
                DockerComposeCommandLineConstants.HOSTNAME+" "+
                        "tcp://"+this.lastCommandOutput.trim()+":"+this.port+" "+
                DockerComposeCommandLineConstants.TLSVERIFY+" "+
                DockerComposeCommandLineConstants.TLSCACERT+" "+
                        this.tlscacert+" "+
                DockerComposeCommandLineConstants.TLSCERT+" "+
                        this.tlscert+" "+
                DockerComposeCommandLineConstants.TLSKEY+" "+
                        this.tlskey+" "+
                        this.command+" "+
                DockerComposeCommandLineConstants.DETACH);


    }

    @Override
    public void stop() {

    }


}
