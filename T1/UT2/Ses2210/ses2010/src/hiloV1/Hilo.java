package hiloV1;
public class Hilo extends Thread {

    private boolean activado;

    public Hilo() {
        activado = true;
    }

    @Override
    public void run() {

        while(activado){
            System.out.println("Soy el hilo: " + getName());

        try {

            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
    }
}
