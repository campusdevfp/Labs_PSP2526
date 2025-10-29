package EjecutablesProcesosYServicios.LanzadorSumador;


public class Sumador {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java EjecutablesProcesosYServicios.LanzadorSumador.Sumador <n1> <n2>");
            return;
        }

        int n1 = Integer.parseInt(args[0]);
        int n2 = Integer.parseInt(args[1]);

        int resultado = 0;
        for (int i = n1; i <= n2; i++) {
            resultado += i;
        }

        System.out.printf(
                "Proceso PID=%d -> suma(%d..%d) = %d%n",
                ProcessHandle.current().pid(), n1, n2, resultado
        );
        // Get information about the current process
//        ProcessHandle processHandle = ProcessHandle.current();
//        ProcessHandle.Info processInfo = processHandle.info();
//
//        System.out.println("PID: " + processHandle.pid());
//        System.out.println("Arguments: " + processInfo.arguments());
//        System.out.println("Command: " + processInfo.command());
//        System.out.println("Instant: " + processInfo.startInstant());
//        System.out.println("Total CPU duration: " + processInfo.totalCpuDuration());
//        System.out.println("User: " + processInfo.user());
    }
}
