package command;

import picocli.CommandLine;
@CommandLine.Command(name = "hello", description = "this is clli tools")
public class Hello implements Runnable{

    @Override
    public void run() {
        System.out.println("RUNN A COMMAND");
    }
}
