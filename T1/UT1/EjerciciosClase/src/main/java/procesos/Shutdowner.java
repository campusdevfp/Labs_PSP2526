package procesos;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Shutdowner {

    public static void main(String[] args) throws IOException {
        // Ask for the required information to prepare the command
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Select your option (s-shutdown / r-reboot / h-hibernate): ");
        String shutdownOption = keyboard.nextLine();

        System.out.print("How much seconds will the command wait to be run? (0 means immediately): ");
        String shutdownTime = keyboard.nextLine();

        // Prepare the command
        String command;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            command = "C:/Windows/System32/shutdown -" + shutdownOption + " -t " + shutdownTime;
        } else {
            command = "shutdown -" + shutdownOption + " -t " + shutdownTime;
        }

        // Prepare the process and launch it
        ProcessBuilder shutdowner = new ProcessBuilder(command.split("\\s"));
        Map<String, String> entorno = shutdowner.environment();
        // String path = entorno.get("Path");
        // String current_path = path + ";C:/Temp";
        // entorno.replace("Path", current_path);
        Process process = shutdowner.start();

        // Show the command to be run
        System.out.print("El comando a ejecutar es:  ");
        for (String commandPart: shutdowner.command()) {
            System.out.print(commandPart + " ");
        }
        System.out.println("");

        // Wait for the process to finish and get exit code
        try {
            int exitCode = process.waitFor();
            System.out.println("Código de salida: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
