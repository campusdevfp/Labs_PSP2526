package procesos;


public class Lanzado {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Debes indicar el programa a ejecutar como parámetro.");
            System.exit(1);
        }

        String programa = args[0];

        try {
            // 1️⃣ Crear el proceso con ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(programa);
            pb.inheritIO(); // redirige entrada/salida/errores a la consola actual

            // 2️⃣ Lanzar el proceso
            Process proceso = pb.start();

            // 3️⃣ Esperar su finalización y recoger código de salida
            int codigoSalida = proceso.waitFor();

            // 4️⃣ Devolver el mismo código de salida
            System.exit(codigoSalida);

        } catch (Exception e) {
            System.err.println("Error al ejecutar el programa: " + e.getMessage());
            System.exit(1); // código distinto de cero = error
        }
    }
}

