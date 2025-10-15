package procesos;

import java.io.File;
import java.io.IOException;

public class DirectorioTrabajo {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Prepara el comando según el sistema operativo
        String command;
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            command = "cmd /c dir";
        } else {
            command = "sh -c ls";
        }

        // 1º - Directorio de trabajo por defecto

        // Prepara el proceso
        ProcessBuilder commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Muestra propiedades del proceso y del sistema
        System.out.println("Directorio de trabajo: " + commander.directory());
        System.out.println("Variable user.dir: " + System.getProperty("user.dir"));

        // Lanza el proceso y muestra su resultado
        // El directorio de trabajo es null pero el proceso se ejecuta en el directorio actual
        commander.start().waitFor();


        // 2º - Cambia user.dir pero no el directorio de trabajo

        // Cambia la propiedad del sistema user.dir
        System.setProperty("user.dir", System.getProperty("user.home"));

        // Prepara el proceso
        commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Muestra propiedades del proceso y del sistema
        System.out.println("Directorio de trabajo: " + commander.directory());
        System.out.println("Variable user.dir: " + System.getProperty("user.dir"));

        // Lanza el proceso y muestra su resultado
        // El directorio de trabajo es null pero el proceso se ejecuta en el directorio actual
        commander.start().waitFor();
        System.out.println("El proceso se ha lanzado, y sí espera su finalización.");

        // 3º - Cambia el directorio de trabajo

        // Prepara el proceso
        commander = new ProcessBuilder(command.split("\\s"));
        commander.inheritIO();

        // Muestra propiedades del proceso y del sistema
        commander.directory(new File(System.getProperty("user.home")));
        System.out.println("Directorio de trabajo: " + commander.directory());
        System.out.println("Variable user.dir: " + System.getProperty("user.dir"));

        // Lanza el proceso y muestra su resultado
        // El directorio de trabajo es user.home y el proceso se ejecuta ahí
        commander.start().waitFor();
    }
}
