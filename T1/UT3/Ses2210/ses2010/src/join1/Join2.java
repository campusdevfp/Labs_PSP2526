package join1;

/**
 * Ejemplo de uso de join() de forma SECUENCIAL.
 *
 * A diferencia de Join1 (que ejecutaba los hilos en paralelo),
 * aquí cada hilo se ejecuta uno detrás del otro.
 */
public class Join2 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Iniciando programa Join2...\n");

        // Crear las tareas
        Tarea tarea1 = new Tarea("Tarea 1");
        Tarea tarea2 = new Tarea("Tarea 2");

        // Asociarlas a hilos
        Thread hilo1 = new Thread(tarea1, "Hilo-1");
        Thread hilo2 = new Thread(tarea2, "Hilo-2");

        // --- Ejecución secuencial ---
        System.out.println("▶️ Ejecutando primero Tarea 1");
        hilo1.start();
        hilo1.join(); // Espera a que termine antes de continuar

        System.out.println("\n▶️ Ejecutando después Tarea 2");
        hilo2.start();
        hilo2.join(); // Espera a que termine antes de continuar

        // --- Finalización ---
        System.out.println("\n🏁 Hilos terminados. Programa finalizado.");
    }
}
