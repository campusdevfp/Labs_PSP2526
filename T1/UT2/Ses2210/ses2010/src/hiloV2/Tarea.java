package hiloV2;
public class Tarea implements Runnable {

    private String nombre;
    private boolean activado;

    public Tarea(String nombre){
        this.nombre = nombre;
        activado = true;
    }

    public void Desactivar(){
        activado = false;
    }

    @Override
    public void run() {
       

        while (activado) {
            System.out.println("Soy hilo versión 2 con Tarea: " + nombre + Thread.currentThread().getName());

            try {
                Thread.sleep(1000);

            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        


    
    }
    
    
}
