package procesos;

import java.io.File;
import java.io.IOException;

public class DirectorioTrabajo {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Prepare the command
        String command;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            command = "cmd /c dir";
        } else {
            command = "sh -c ls";
        }

        //1st - Default working directory

        // Prepare the process
        ProcessBuilder commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Show Process and System properties
        System.out.println("Working directory: " + commander.directory());
        System.out.println("user.dir variable: " + System.getProperty("user.dir"));

        // Launch the process and show its result
        // Working directory is null but the process is run on the current dir
        commander.start().waitFor();


        //2nd - Change user.dir but not the working directory

        // Change the user.dir system property
        System.setProperty("user.dir", System.getProperty("user.home"));

        // Prepare the process
        commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Show Process and System properties
        System.out.println("Working directory: " + commander.directory());
        System.out.println("user.dir variable: " + System.getProperty("user.dir"));

        // Launch the process and show its result
        // Working directory is null but the process is run on the current dir
        commander.start().waitFor();


        // 3rd - Change the working directory

        // Prepare the process
        commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Show Process and System properties
        commander.directory(new File(System.getProperty("user.home")));
        System.out.println("Working directory: " + commander.directory());
        System.out.println("user.dir variable: " + System.getProperty("user.dir"));

        // Launch the process and show its result
        // Working directory is user.home and the process is run on it
        commander.start().waitFor();
    }
}
