package com.nominum;


import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.PostConstruct;
import javax.ws.rs.WebApplicationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vpathi on 1/4/17.
 */
public class ConsoleVmOutput {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    String vmName = currentUserName + "16-2";
    String fileName = vmName+"_"+"consoleOutput";

    public StreamingResponseBody getLogs()  {
        String machineStoragePath = getExecutablePath();
        File dir = new File(machineStoragePath + "/" + currentUserName + "/"  );
        File file = new File(dir, fileName);


        final StreamingResponseBody stream = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream output) throws IOException, WebApplicationException {
                if(!file.exists()) {
                    return;
                }
                writeToStream("data: ", output);
                writeToStream(IOUtils.toString(new FileInputStream(file))
                        .replaceAll("\\n", "\\\\n"), output);
                writeToStream("\n\n", output);
            }


        };
        return stream;
    }

    private void writeToStream(String string, OutputStream output) {
        try {
            output.write(string.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PostConstruct
    public String getExecutablePath()  {
        Resource resource = new ClassPathResource("machineStorage");
        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }


}








