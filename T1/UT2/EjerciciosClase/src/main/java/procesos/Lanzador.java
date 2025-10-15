package procesos;


import java.io.IOException;
import java.util.Scanner;

public class Lanzador {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce el programa que deseas ejecutar (por ejemplo, notepad.exe): ");
        String programa = sc.nextLine();
        sc.close();

        try {
            // Creamos el proceso que lanzará la clase U2A4_Lanzado, pasándole el programa como parámetro
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "procesos.Lanzado", programa);
            pb.inheritIO(); // hereda entrada/salida de la consola

            Process proceso = pb.start();
            int codigoSalida = proceso.waitFor();

            // Mostramos resultado
            if (codigoSalida == 0) {
                System.out.println("✅ El proceso terminó correctamente (código 0)");
            } else {
                System.out.println("❌ El proceso terminó con errores (código " + codigoSalida + ")");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al lanzar el proceso: " + e.getMessage());
        }
    }
}
