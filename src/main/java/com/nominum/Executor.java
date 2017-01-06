package com.nominum;


import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpathi on 12/19/16.
 */
public class Executor {

    public StreamingResponseBody execute(final Configuration configuration) {
        final StreamingResponseBody stream = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream output) throws IOException, WebApplicationException {

                String lastCommandOutput = " ";
                try {
                    for (Command c : configuration.getCommands()) {
                        if (c instanceof NeedsLastCommandOutput && lastCommandOutput != null) {
                            ((NeedsLastCommandOutput) c).setLastCommandOutput(lastCommandOutput);
                        }
                        c.run(output);
                        lastCommandOutput = c.getOutput();
                        //output.write("-- DEBUG: End of command --".getBytes());
                        //output.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    output.close();
                }
            }
        };
        return stream;
    }

    public List<String> executeAsStringOutput(final Configuration configuration) {
        List<String> listOfOutputs = new ArrayList<>(configuration.getCommands().size());
        String lastCommandOutput = null;
        try {
            for (Command c: configuration.getCommands()) {
                if (c instanceof NeedsLastCommandOutput && lastCommandOutput != null) {
                    ((NeedsLastCommandOutput) c).setLastCommandOutput(lastCommandOutput);
                }
                c.run(null);
                String rawOutput = c.getOutput();
                List<String> items = Arrays.asList(rawOutput.split("\\n"));
                for (String item:items){
                    lastCommandOutput=item;
                    listOfOutputs.add(lastCommandOutput);
                }
                
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listOfOutputs;
    }
}
