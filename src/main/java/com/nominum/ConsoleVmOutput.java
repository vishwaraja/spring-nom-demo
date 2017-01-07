package com.nominum;


import org.apache.commons.io.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by vpathi on 1/4/17.
 */
public class ConsoleVmOutput {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    String vmName = currentUserName + "16-2";
    String fileName = vmName+"_"+"consoleOutput";
    URL path = Configuration.class
            .getClassLoader().getResource("machineStorage");
    String machineStoragePath = path.getPath();
    File dir = new File(machineStoragePath + "/" + currentUserName + "/" + "machines" + "/" );
    File file = new File(dir, fileName);

    public StreamingResponseBody getLogs() {


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

}








