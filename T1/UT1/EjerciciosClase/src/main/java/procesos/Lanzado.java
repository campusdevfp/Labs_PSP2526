package procesos;

import java.io.IOException;
import java.util.Arrays;

public class Lanzado {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No se ha indicado ningún programa para ejecutar.");
            System.exit(1);
        }

        String programa = args[0];

        try {
            ProcessBuilder pb = new ProcessBuilder(programa);
            pb.inheritIO();
            Process proceso = pb.start();

            // Get information about the current process
//            ProcessHandle processHandle = ProcessHandle.current();
//            ProcessHandle.Info processInfo = processHandle.info();
//
//            System.out.println("PID: " + processHandle.pid());
//            System.out.println("Arguments: " + processInfo.arguments());
//            System.out.println("Command: " + processInfo.command());
//            System.out.println("Instant: " + processInfo.startInstant());
//            System.out.println("Total CPU duration: " + processInfo.totalCpuDuration());
//            System.out.println("User: " + processInfo.user());

            int codigoSalida = proceso.waitFor(); // Espera a que termine el proceso
            System.exit(codigoSalida); // Devuelve su mismo código de salida

        } catch (IOException e) {
            System.err.println("Error al ejecutar el programa: " + e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            System.err.println("El proceso fue interrumpido.");
            System.exit(2);
        }
    }
}