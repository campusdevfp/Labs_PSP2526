package procesos;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class InfoProceso {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Usar /k para que cmd no se cierre inmediatamente y podamos inspeccionar el proceso hijo
        ProcessBuilder pb = new ProcessBuilder("cmd", "/k", "dir");
        Process proceso = pb.start();

        // Opcional: esperar un poco para asegurar que hay información disponible
        TimeUnit.MILLISECONDS.sleep(200);

        ProcessHandle childProcessHandle = proceso.toHandle();
        ProcessHandle.Info childInfo = childProcessHandle.info();

        System.out.println("Child PID: " + childProcessHandle.pid());
        System.out.println("Child Command: " + childInfo.command().orElse("n/a"));
        System.out.println("Child Arguments: " + childInfo.arguments()
                .map(a -> Arrays.toString(a)).orElse("n/a"));
        System.out.println("Child Start Instant: " + childInfo.startInstant().map(Object::toString).orElse("n/a"));
        System.out.println("Child Total CPU Duration: " + childInfo.totalCpuDuration().map(Object::toString).orElse("n/a"));
        System.out.println("Child User: " + childInfo.user().orElse("n/a"));

        // Si quieres terminar el proceso hijo desde aquí:
        // proceso.destroy();
    }
}