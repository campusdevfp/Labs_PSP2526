package fcfs;

import java.util.*;

class Proceso {
    String id;
    int llegada;
    int duracion;
    int inicio;
    int fin;
    int espera;
    int retorno;

    public Proceso(String id, int llegada, int duracion) {
        this.id = id;
        this.llegada = llegada;
        this.duracion = duracion;
    }
}

public class FCFS_LinkedList {
    public static void main(String[] args) {
        // Cola de listos: los procesos esperan su turno
        LinkedList<Proceso> readyQueue = new LinkedList<>();
        readyQueue.add(new Proceso("P1", 0, 10));
        readyQueue.add(new Proceso("P2", 1, 6));
        readyQueue.add(new Proceso("P3", 2, 3));

        // Ordenamos por llegada (por si no estuvieran ya en orden)
        readyQueue.sort(Comparator.comparingInt(p -> p.llegada));

        int tiempo = 0;
        int totalEspera = 0;
        int totalRetorno = 0;

        System.out.println("=== Simulación FCFS con LinkedList ===");
        System.out.println("Proc | Llegada | CPU | Inicio | Fin | Espera | Retorno");

        // Mientras haya procesos en la cola
        while (!readyQueue.isEmpty()) {
            // Sacamos el primero en llegar (First Come First Served)
            Proceso p = readyQueue.poll();

            // Si el CPU está ocioso y el proceso llega más tarde, adelantamos el tiempo
            if (tiempo < p.llegada) {
                tiempo = p.llegada;
            }

            // Calcular tiempos
            p.inicio = tiempo;
            p.fin = tiempo + p.duracion;
            p.espera = p.inicio - p.llegada;
            p.retorno = p.fin - p.llegada;

            totalEspera += p.espera;
            totalRetorno += p.retorno;

            // Avanzamos el reloj del CPU
            tiempo = p.fin;

            // Mostrar resultado del proceso
            System.out.printf("%s\t   %d\t    %d\t   %d\t   %d\t   %d\t   %d%n",
                    p.id, p.llegada, p.duracion, p.inicio, p.fin, p.espera, p.retorno);
        }

        int n = 3; // número de procesos
        double mediaEspera = totalEspera / (double) n;
        double mediaRetorno = totalRetorno / (double) n;

        System.out.println("\n=== Resultados globales ===");
        System.out.printf("Tiempo medio de espera: %.2f%n", mediaEspera);
        System.out.printf("Tiempo medio de retorno: %.2f%n", mediaRetorno);
    }
}
