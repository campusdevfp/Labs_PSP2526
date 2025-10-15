package procesos;

import java.io.File;
import java.io.IOException;



public class DirectorioTrabajo {

    public static void main(String[] args) throws IOException, InterruptedException {

        String sistemaOperativo = System.getProperty("os.name").toLowerCase();
        String comando;

        // Detectar sistema operativo y comando adecuado
        if (sistemaOperativo.startsWith("windows")) {
            comando = "cmd /c dir";
        } else {
            comando = "sh -c ls -l";
        }

        // 1️⃣ Crear el ProcessBuilder
        ProcessBuilder pb = new ProcessBuilder(comando.split("\\s"));

        System.out.println("== 1. Después de crear la instancia de ProcessBuilder ==");
        System.out.println("directory(): " + pb.directory());
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println();

        // 2️⃣ Cambiar la propiedad user.dir but no afecta a ProcessBuilder
        System.setProperty("user.dir", System.getProperty("user.home"));

        System.out.println("== 2. Después de cambiar la propiedad user.dir ==");
        System.out.println("directory(): " + pb.directory());
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println();

        // 3️⃣ Cambiar el directorio de trabajo
        String nuevoDirectorio;
        if (sistemaOperativo.startsWith("windows")) {
            nuevoDirectorio = "C:" +
                    "\\Temp";
        } else {
            nuevoDirectorio = "/tmp";
        }

        pb.directory(new File(nuevoDirectorio));

        System.out.println("== 3. Después de cambiar el directorio de trabajo ==");
        System.out.println("directory(): " + pb.directory());
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println();

        System.out.println("== 4. Ejecución del comando " + (sistemaOperativo.startsWith("windows") ? "dir" : "ls -l") + " ==");

        // 4️⃣ Ejecutar el proceso y redirigir E/S a la consola actual
        pb.inheritIO();

        Process proceso = pb.start();
        int codigoSalida = proceso.waitFor();

        System.out.println();
        System.out.println("El proceso terminó con código de salida: " + codigoSalida);
    }
}