package EjecutablesProcesosYServicios.LanzadorSumador;

public class LanzadorDeProcesosSimple {
    public void ejecutar(String ruta){

        ProcessBuilder pb;
        try {
            pb = new ProcessBuilder(ruta);
            pb.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        String ruta=
                "C:\\Program Files (x86)\\Adobe\\Acrobat Reader DC\\Reader\\" +
                        "AcroRd32.exe";
        LanzadorDeProcesosSimple lp=new LanzadorDeProcesosSimple();
        lp.ejecutar(ruta);
        System.out.println("Finalizado");
    }

}