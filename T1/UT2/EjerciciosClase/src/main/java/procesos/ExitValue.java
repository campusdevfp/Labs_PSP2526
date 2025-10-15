package procesos;

import java.io.IOException;
import java.util.Scanner;

public class ExitValue {

    public static void main(String[] args) {
        do {
            // Código para pedir un programa/comando a ejecutar
            Scanner teclado = new Scanner(System.in);
            System.out.println("Introduce el programa / comando que quieres ejecutar (intro para acabar): ");
            String comando = teclado.nextLine();

            if (comando.equals("")) System.exit(0);

            try {
                // Preparamos el entrono de ejecución del proceso
                // Como no sabemos el contenido del comando, forzamos su conversión
                // a una lista para que no haya problemas con su ejecución
                ProcessBuilder pb = new ProcessBuilder(comando.split("\\s"));

                // Lanzamos el proceso hijo
                Process p = pb.start();

                // Esperamos a que acabe para recoger el valor de salida
                int exitValue = p.waitFor();

                if (exitValue == 0) {
                    System.out.println("El comando " + pb.command().toString() + " ha finalizado bien");
                } else {
                    System.out.println("El comando " + pb.command().toString() + " ha finalizado con errores. Código (" + exitValue + ")");
                }

            } catch (InterruptedException | IOException ex) {
                System.err.println(ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        } while (true);
    }
}
