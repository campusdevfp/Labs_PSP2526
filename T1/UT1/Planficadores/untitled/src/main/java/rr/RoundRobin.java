package rr;

import java.util.*;

class Proceso {
    String nombre;
    int llegada;
    int duracion;
    int restante;
    int inicio = -1;
    int fin;
    int espera;
    int retorno;

    public Proceso(String nombre, int llegada, int duracion) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.duracion = duracion;
        this.restante = duracion;
    }
}

public class RoundRobin {
    public static void main(String[] args) {
        int quantum = 2;

        List<Proceso> procesos = new ArrayList<>();
        procesos.add(new Proceso("P1", 0, 10));
        procesos.add(new Proceso("P2", 1, 6));
        procesos.add(new Proceso("P3", 2, 3));

        Queue<Proceso> cola = new LinkedList<>();
        List<Proceso> completados = new ArrayList<>();

        int tiempo = 0;
        int tiempoOcioso = 0;
        int tiempoInicioSimulacion = procesos.get(0).llegada;
        int tiempoFinalSimulacion = 0;

        System.out.println("=== Algoritmo Round Robin (q = 2) ===\n");

        while (completados.size() < procesos.size()) {
            // Añadir a la cola los procesos que han llegado hasta este momento
            for (Proceso p : procesos) {
                if (p.llegada == tiempo) {
                    cola.add(p);
                }
            }

            if (cola.isEmpty()) {
                tiempo++;
                tiempoOcioso++;
                continue;
            }

            Proceso actual = cola.poll();

            if (actual.inicio == -1) {
                actual.inicio = tiempo;
            }

            int ejecucion = Math.min(quantum, actual.restante);
            actual.restante -= ejecucion;

            // Simular la ejecución segundo a segundo
            for (int i = 0; i < ejecucion; i++) {
                tiempo++;

                // Mientras se ejecuta, pueden llegar nuevos procesos
                for (Proceso p : procesos) {
                    if (p.llegada == tiempo && !cola.contains(p) && !completados.contains(p)) {
                        cola.add(p);
                    }
                }
            }

            if (actual.restante == 0) {
                actual.fin = tiempo;
                actual.retorno = actual.fin - actual.llegada;
                actual.espera = actual.retorno - actual.duracion;
                completados.add(actual);
            } else {
                cola.add(actual); // regresa al final de la cola
            }
        }

        // Calcular resultados
        double totalEspera = 0, totalRetorno = 0;
        for (Proceso p : completados) {
            totalEspera += p.espera;
            totalRetorno += p.retorno;
            tiempoFinalSimulacion = Math.max(tiempoFinalSimulacion, p.fin);
        }

        double tiempoTotal = tiempoFinalSimulacion - tiempoInicioSimulacion;
        double usoCPU = ((tiempoTotal - tiempoOcioso) / tiempoTotal) * 100;
        double throughput = (double) completados.size() / tiempoTotal;

        // Mostrar tabla
        System.out.println("Proceso | Llegada | Duración | Fin | Espera | Retorno");
        for (Proceso p : completados) {
            System.out.printf("%7s | %7d | %8d | %3d | %6d | %7d%n",
                    p.nombre, p.llegada, p.duracion, p.fin, p.espera, p.retorno);
        }

        System.out.println("\n=== Resultados ===");
        System.out.printf("Tiempo medio de espera: %.2f\n", totalEspera / procesos.size());
        System.out.printf("Tiempo medio de retorno: %.2f\n", totalRetorno / procesos.size());
        System.out.printf("Uso de CPU: %.2f%%\n", usoCPU);
        System.out.printf("Productividad (Throughput): %.2f procesos/unidad de tiempo\n", throughput);
    }
}
