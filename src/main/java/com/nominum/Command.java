package com.nominum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by vpathi on 12/19/16.
 */
public abstract class Command {

    protected String lastCommandOutput;
    private String output;
    private Process process;

    public abstract void stop();

    public abstract ProcessBuilder getProcessBuilder();

    public String getOutput() {
        return output;
    }

    public void run(OutputStream os) throws IOException, InterruptedException {
        URL googleCredentials = DockerMachineCommandLineConstants.class
                .getClassLoader().getResource("googleCredentials/nominum-docker-machines-cb3dc450e32f.json");


        ProcessBuilder processBuilder = getProcessBuilder();
        Map<String, String> envs = processBuilder.environment();
        envs.put("PATH", "/usr/local/bin/");
        envs.put("GOOGLE_APPLICATION_CREDENTIALS", googleCredentials.getPath());
        if (lastCommandOutput != null && !lastCommandOutput.isEmpty()) {
            envs.put("DOCKER_HOST", "tcp://" + lastCommandOutput.trim() + ":2375");
        }
        System.out.println("DEBUG: Command executing: " + processBuilder.command().toString());
        System.out.println("DEBUG:" + lastCommandOutput);
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput();
        process = processBuilder.start();

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                stringBuilder.append(line + '\n');
                //If streaming back HTML swap \n with <br/>
                os.write((line + "\n").getBytes());
                os.flush();
            }
        } finally {
            output = stringBuilder.toString();
        }
    }
}
