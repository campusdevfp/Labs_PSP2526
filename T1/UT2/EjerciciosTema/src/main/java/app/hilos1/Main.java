package app.hilos1;

public class Main {
    public static void main(String[] args) {
        Hilo hilo1 = new Hilo();
        Hilo hilo2 = new Hilo();
        Hilo hilo3 = new Hilo();
        hilo2.start();
        hilo1.start();
        hilo3.start();

    }
}
