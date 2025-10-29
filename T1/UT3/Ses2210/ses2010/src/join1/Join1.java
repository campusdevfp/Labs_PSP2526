package join1;

public class Join1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("🚀 Lanzando hilos...");

        Tarea tarea1 = new Tarea("Tarea 1");
        Tarea tarea2 = new Tarea("Tarea 2");

        Thread hilo1 = new Thread(tarea1, "Hilo-1");
        Thread hilo2 = new Thread(tarea2, "Hilo-2");

        hilo1.start();
        hilo2.start();

        // 🧩 Prueba cambiando las líneas de join:
        // (a) ambos join → espera a los dos
        // hilo1.join();
        // hilo2.join();

        // (b) solo hilo2.join()
       //  hilo2.join();

        // (c) solo hilo1.join()
       // hilo1.join();

        System.out.println("⚠️  Main ha terminado y sigue con su vida...");
    }
}
