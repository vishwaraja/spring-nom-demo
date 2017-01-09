package com.nominum;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vpathi on 12/19/16.
 */
public class Executor {

    public void execute(final Configuration configuration) {

        String lastCommandOutput = " ";
        int exitStatus=0;
        try {
            for (Command c : configuration.getCommands()) {
                if (c instanceof NeedsLastCommandOutput && lastCommandOutput != null) {
                    ((NeedsLastCommandOutput) c).setLastCommandOutput(lastCommandOutput);
                }

                if (exitStatus==0){
                    exitStatus=c.run();
                    System.out.print("\n********************************************\n"+exitStatus);
                }
                else{
                    throw new IOException("Invalid status code"+ exitStatus);

                }

                lastCommandOutput = c.getOutput();
            }

        }catch (org.apache.catalina.connector.ClientAbortException ca) {
            System.out.println("ClientAbortException caught");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> executeAsStringOutput(final Configuration configuration) {
        List<String> listOfOutputs = new ArrayList<>(configuration.getCommands().size());
        String output = null;
        try {
            for (Command c: configuration.getCommands()) {
                if (c instanceof NeedsLastCommandOutput && output != null) {
                    ((NeedsLastCommandOutput) c).setLastCommandOutput(output);
                }
                c.run();
                String rawOutput = c.getOutput();
                List<String> items = Arrays.asList(rawOutput.split("\\n"));
                for (String item:items){
                    output = item;
                    listOfOutputs.add(output);
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return listOfOutputs;
    }
}
