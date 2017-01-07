package com.nominum;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by vpathi on 1/1/17.
 */
public class DockerCommand extends Command implements NeedsLastCommandOutput {

    private String command;
    private String registryUser;
    private String registryPassword;
    private String tlscert;
    private String tlsverify;
    private String tlscacert;
    private String tlskey;
    private String port;

    private DockerCommand() {
    }

    public String getPort() {
        return port;
    }

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRegistryUser() {
        return registryUser;
    }

    public void setRegistryUser(String registryUser) {
        this.registryUser = registryUser;
    }

    public String getRegistryPassword() {
        return registryPassword;
    }

    public void setRegistryPassword(String registryPassword) {
        this.registryPassword = registryPassword;
    }

    @Override
    public void setLastCommandOutput(String lastOutput) {
        lastCommandOutput = lastOutput;

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessBuilder getProcessBuilder() throws Exception {

        String docker =getExecutablePath();
        return new ProcessBuilder().command(
                DockerCommandLineConstants.SHELL,
                DockerCommandLineConstants.SHELL_PARAM,
                docker + " " +
                        DockerCommandLineConstants.HOSTNAME + " " +
                        "tcp://" + this.lastCommandOutput.trim() + ":" + this.port + " " +
                        DockerCommandLineConstants.TLSVERIFY + " " +
                        DockerCommandLineConstants.TLSCACERT + " " +
                        this.tlscacert + " " +
                        DockerCommandLineConstants.TLSCERT + " " +
                        this.tlscert + " " +
                        DockerCommandLineConstants.TLSKEY + " " +
                        this.tlskey + " " +
                        this.command + " " +
                        DockerCommandLineConstants.REGISTRY_USER_PARAM + " " +
                        this.registryUser + " " +
                        DockerCommandLineConstants.REGISTRY_PASSWORD_PARAM + " " +
                        this.registryPassword
        );

    }

    @PostConstruct
    public String getExecutablePath() throws Exception {
        Resource resource = new ClassPathResource("/binaries/docker");
        File file = resource.getFile();
        return file.getAbsolutePath();
    }


    public static class Builder {
        DockerCommand instance;

        public Builder() {
            instance = new DockerCommand();
        }

        public static DockerCommand from(Builder builder) {

            return builder.build();
        }

        public Builder tlsKey(String tlskey) {
            instance.tlskey = tlskey;
            return this;
        }

        public Builder tlsCacert(String tlscacert) {
            instance.tlscacert = tlscacert;
            return this;
        }

        public Builder tlsVerify(String tlsverify) {
            instance.tlsverify = tlsverify;
            return this;
        }

        public Builder port(String port) {
            instance.port = port;
            return this;
        }

        public Builder tlsCert(String tlscert) {
            instance.tlscert = tlscert;
            return this;
        }

        public Builder command(String command) {
            instance.command = command;
            return this;
        }

        public Builder registryUser(String registryUser) {
            instance.registryUser = registryUser;
            return this;
        }

        public Builder registryPassword(String registryPassword) {
            instance.registryPassword = registryPassword;
            return this;
        }

        public DockerCommand build() {
            return instance;
        }

    }
}
