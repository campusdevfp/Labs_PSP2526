package hilos;

class Tarea extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Soy un hilo y esto es lo que hago");
        }
    }
}



public class Hilos {
    public static void main(String args[]) {
        Tarea tarea = new Tarea();
        tarea.start();
        System.out.println("Yo soy el hilo principal y sigo haciendo mi trabajo");
        System.out.println("Fin del hilo principal");
    }
}