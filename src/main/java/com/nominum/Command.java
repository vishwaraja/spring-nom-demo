package com.nominum;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
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
    private File vmLogPath=null;
    public abstract void stop();

    protected File getVmLogPath(){
        return vmLogPath;
    }


    public abstract ProcessBuilder getProcessBuilder() throws Exception;

    public String getOutput() {
        return output;
    }

    public int run() throws Exception {
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
        //processBuilder.redirectOutput();
        process = processBuilder.start();
        String line;
//I'll come back and fix this.

        String executing_command=processBuilder.command().toString();
        if ( executing_command.contains("create")) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                while ((line = input.readLine()) != null) {
                    TimeUnit.SECONDS.sleep(1);
                    Files.write(Paths.get(getVmLogPath().getAbsolutePath()), (line + '\n').getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    stringBuilder.append(line + '\n');

                }

            }catch (org.apache.catalina.connector.ClientAbortException ca) {
                System.out.println("ClientAbortException caught");
            }
            finally {
                output = stringBuilder.toString();
            }
        }

        else{
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            try {
                while ((line = input.readLine()) != null) {
                   // Files.write(Paths.get(vmLogPath.getAbsolutePath()), (line + '\n').getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                    stringBuilder.append(line + '\n');

                }

            }catch (org.apache.catalina.connector.ClientAbortException ca) {
                System.out.println("ClientAbortException caught");
            }
            finally {
                output = stringBuilder.toString();
            }
        }
//            StringBuilder stringBuilder = new StringBuilder();
//            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            try {
//                while ((line = input.readLine()) != null) {
//   //                 Files.write(Paths.get(getVmLogPath().getAbsolutePath()), (line + '\n').getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//
//                    stringBuilder.append(line + '\n');
//
//                }
//            } finally {
//                output = stringBuilder.toString();
//            }
        int exit=process.waitFor();
        if (exit!=0){
            System.out.print("COMMAND:"+processBuilder.command().toString());
        }
        return exit;
    }
}
