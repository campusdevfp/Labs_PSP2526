package hiloV2;
public class AppV2 {
    public static void main(String args[]){
        HiloV2 tarea1 = new HiloV2("Tarea 1");
        HiloV2 tarea2 = new HiloV2("Tarea 2");
        Thread hilo1 = new Thread(tarea1);
        Thread hilo2 = new Thread(tarea2);

        hilo1.start();
        hilo2.start();
    }
}
