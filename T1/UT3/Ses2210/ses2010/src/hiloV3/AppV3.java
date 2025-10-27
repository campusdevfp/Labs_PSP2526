package hiloV3;
public class AppV3 {
    
    public static void main(String[] args) {
        Thread hilo = new Thread(
            new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    System.out.print("Soy hilo 3");
                }
                
            }
        );

        hilo.start();
    }

}
