package hiloV1;

public class App {
    public static void main(String[] args) throws Exception {
      
        Hilo hilo1 = new Hilo();
        Hilo hilo2 = new Hilo();

        hilo1.start();
        
        hilo2.start();
    }
}
