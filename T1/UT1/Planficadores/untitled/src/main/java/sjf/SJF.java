package sjf;

import java.util.*;

class Proceso {
    String nombre;
    int llegada;
    int duracion;
    int inicio;
    int fin;
    int espera;
    int retorno;

    public Proceso(String nombre, int llegada, int duracion) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.duracion = duracion;
    }
}

public class SJF {
    public static void main(String[] args) {
        // Lista inicial (orden de llegada)
        List<Proceso> listaProcesos = new LinkedList<>();
        listaProcesos.add(new Proceso("P1", 0, 10));
        listaProcesos.add(new Proceso("P2", 1, 6));
        listaProcesos.add(new Proceso("P3", 2, 3));

        // Variables de simulación
        int tiempoActual = 0;
        int procesosCompletados = 0;
        double totalEspera = 0, totalRetorno = 0;
        int tiempoInicioSimulacion = listaProcesos.get(0).llegada;
        int tiempoFinalSimulacion = 0;
        int tiempoOcioso = 0;

        List<Proceso> completados = new ArrayList<>();

        System.out.println("=== Algoritmo SJF (No expropiativo) ===\n");
        System.out.println("Proceso | Llegada | Duración | Inicio | Fin | Espera | Retorno");

        while (procesosCompletados < listaProcesos.size()) {
            // Filtrar los procesos que ya llegaron y no están completados
            List<Proceso> disponibles = new ArrayList<>();
            for (Proceso p : listaProcesos) {
                if (p.llegada <= tiempoActual && !completados.contains(p)) {
                    disponibles.add(p);
                }
            }

            if (disponibles.isEmpty()) {
                // CPU ociosa hasta que llegue el siguiente proceso
                tiempoActual++;
                tiempoOcioso++;
                continue;
            }

            // Escoger el proceso con menor duración
            Proceso p = Collections.min(disponibles, Comparator.comparingInt(proc -> proc.duracion));

            p.inicio = tiempoActual;
            p.fin = tiempoActual + p.duracion;
            p.espera = p.inicio - p.llegada;
            p.retorno = p.fin - p.llegada;

            tiempoActual = p.fin;
            procesosCompletados++;
            completados.add(p);

            totalEspera += p.espera;
            totalRetorno += p.retorno;
            tiempoFinalSimulacion = p.fin;

            System.out.printf("%7s | %7d | %8d | %6d | %3d | %6d | %7d%n",
                    p.nombre, p.llegada, p.duracion, p.inicio, p.fin, p.espera, p.retorno);
        }

        double tiempoTotal = tiempoFinalSimulacion - tiempoInicioSimulacion;
        double usoCPU = ((tiempoTotal - tiempoOcioso) / tiempoTotal) * 100;
        double throughput = (double) listaProcesos.size() / tiempoTotal;

        System.out.println("\n=== Resultados ===");
        System.out.printf("Tiempo medio de espera: %.2f\n", totalEspera / listaProcesos.size());
        System.out.printf("Tiempo medio de retorno: %.2f\n", totalRetorno / listaProcesos.size());
        System.out.printf("Uso de CPU: %.2f%%\n", usoCPU);
        System.out.printf("Productividad (Throughput): %.2f procesos/unidad de tiempo\n", throughput);
    }
}
