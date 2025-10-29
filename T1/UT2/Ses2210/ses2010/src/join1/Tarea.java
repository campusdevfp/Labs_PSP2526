package join1;

public class Tarea implements Runnable {

    private final String nombre;

    public Tarea(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {
        System.out.println("🔹 Inicia " + nombre + " (" + Thread.currentThread().getName() + ")");
        try {
            for (int i = 1; i <= 5; i++) {
                Thread.sleep(2000);
                System.out.println("   " + nombre + " ejecutando paso " + i);
            }
            System.out.println("✅ Termina " + nombre);
        } catch (InterruptedException e) {
            System.out.println("❌ Interrumpido " + nombre);
        }
    }
}
