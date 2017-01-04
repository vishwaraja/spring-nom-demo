package com.nominum;


import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpathi on 12/19/16.
 */
public class Executor {

    public StreamingResponseBody execute(final Configuration configuration) {
        final StreamingResponseBody stream = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream output) throws IOException, WebApplicationException {
                //If client is unable to get streaming output then use this
                //writePreHtml(output);

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
                    //If client is unable to get streaming output then use this
                    //writePostHtml(output);
                    output.close();
                }
            }
        };
        return stream;
    }

    private void writePreHtml(OutputStream output) {
        List<String> preHtml = new ArrayList<>();
        preHtml.add("<html>");
        preHtml.add("<body>");
        preHtml.add("<b>");
        preHtml.add("<font color=\"red\"");
        writeListToStream(preHtml, output);
    }

    private void writePostHtml(OutputStream output) {
        List<String> postHtml = new ArrayList<>();
        postHtml.add("</font>");
        postHtml.add("</b>");
        postHtml.add("</body>");
        postHtml.add("</html>");
        writeListToStream(postHtml, output);
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

    public void executeNew(final Configuration configuration, OutputStream os) throws IOException {
        String lastCommandOutput = " ";
        try {
            for (Command c : configuration.getCommands()) {
                if (c instanceof NeedsLastCommandOutput && lastCommandOutput != null) {
                    ((NeedsLastCommandOutput) c).setLastCommandOutput(lastCommandOutput);
                }
                c.run(os);
                lastCommandOutput = c.getOutput();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            os.close();
        }
    }
}
