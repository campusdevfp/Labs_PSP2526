package app.hilos1;

public class Hilo extends Thread{

    private boolean activado;
    public Hilo(){
        this.activado = true;
    }
    @Override
    public void run(){
        while (activado){
            System.out.println("Hilo en ejecución" + getName());
        }
    }
}
