package com.nominum;


import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpathi on 1/4/17.
 */
public class ConsoleVmOutput {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    String vmName = currentUserName + "16-2";
    String fileName = "consoleOuput";
    URL path = Configuration.class
            .getClassLoader().getResource("machineStorage");
    String machineStoragePath = path.getPath();
    File dir = new File(machineStoragePath + "/" + currentUserName + "/" + "machines" + "/" + vmName + "/");
    File file = new File(dir, fileName);

    public StreamingResponseBody getLogs() {


        final StreamingResponseBody stream = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream output) throws IOException, WebApplicationException {
                InputStream targetStream = new FileInputStream(file);
                writePreHtml(output);
                IOUtils.copy(targetStream, output);
            }


        };
        return stream;
    }

    private void writeListToStream(List<String> list, OutputStream output) {
        try {
            for (String s : list) {
                output.write(s.getBytes());
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writePreHtml(OutputStream output) {
        List<String> preHtml = new ArrayList<>();
        preHtml.add("data: ");

        writeListToStream(preHtml, output);
    }




}








