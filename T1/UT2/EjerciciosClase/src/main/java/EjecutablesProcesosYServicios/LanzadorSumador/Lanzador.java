package EjecutablesProcesosYServicios.LanzadorSumador;

import java.io.File;
import java.io.IOException;

public class Lanzador {
    public void lanzarSumador(int n1, int n2) {
        try {
            // Obtener el classpath actual del proceso de IntelliJ
            String classpath = System.getProperty("java.class.path");

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath,
                    "EjecutablesProcesosYServicios.LanzadorSumador.Sumador",
                    String.valueOf(n1), String.valueOf(n2)
            );

            pb.inheritIO(); // Muestra salida directamente en consola de IntelliJ
            Process p = pb.start();
            p.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Lanzador lanzador = new Lanzador();

        System.out.println("== Lanzando primer proceso ==");
        lanzador.lanzarSumador(1, 5);

        System.out.println("== Lanzando segundo proceso ==");
        lanzador.lanzarSumador(6, 10);

        System.out.println("== Procesos finalizados ==");
    }

}