package com.nominum;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.*;
import java.net.URL;

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

    public String getConsoleLogs() throws IOException {
        String str = FileUtils.readFileToString(file);
        return str;
    }

    public StreamingResponseBody getLogs() {


        final StreamingResponseBody stream = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream output) throws IOException, WebApplicationException {
                InputStream targetStream = new FileInputStream(file);
                IOUtils.copy(targetStream, output);
            }


        };
        return stream;
    }
}




