package com.nominum;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by vpathi on 12/19/16.
 */
public abstract class Command {

    protected String lastCommandOutput;
    private String output;
    private Process process;

    public abstract void stop();
    public String vmName;
    public File dir;
    public File file;


    public abstract ProcessBuilder getProcessBuilder();

    public String getOutput() {
        return output;
    }

    public void run(OutputStream os) throws IOException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        URL googleCredentials = DockerMachineCommandLineConstants.class
                .getClassLoader().getResource("googleCredentials/nominum-docker-machines-cb3dc450e32f.json");

        URL path = Configuration.class
                .getClassLoader().getResource("machineStorage");
        String machineStoragePath = path.getPath();

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
        TimeUnit.MILLISECONDS.sleep(200);
        String fileName = "consoleOutput";
        vmName =currentUserName+"16-2";
        dir = new File (machineStoragePath+"/"+currentUserName+"/"+"machines"+"/"+vmName+"/");
        dir.mkdirs();
        file = new File (dir, fileName);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        try {
            while ((line = input.readLine()) != null) {
                Files.write(Paths.get(file.getAbsolutePath()), (line + '\n').getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                stringBuilder.append(line + '\n');
                if (os != null) {
                    os.write((line + "\n").getBytes());
                    os.flush();
                }
            }
        } finally {
            output = stringBuilder.toString();
        }
    }
}
