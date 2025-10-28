package hiloV2;
public class AppV2 {
    public static void main(String args[]){
        Tarea tarea1 = new Tarea("Tarea 1");
        Tarea tarea2 = new Tarea("Tarea 2");
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread(tarea2);

        hilo1.start();
        hilo2.start();
    }
}
