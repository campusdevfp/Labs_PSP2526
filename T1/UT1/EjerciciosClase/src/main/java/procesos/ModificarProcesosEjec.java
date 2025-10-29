package procesos;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ModificarProcesosEjec {

    public static ProcessBuilder createProcessBuilder() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");

        String baseCommand = isWindows ? "tasklist" : "ps -Af";
        ProcessBuilder pbuilder = new ProcessBuilder(baseCommand.split("\\s"));

        if (isWindows) {
            pbuilder.command().add(0, "cmd");
            pbuilder.command().add(1, "/c");
        } else {
            pbuilder.command().add(0, "sh");
            pbuilder.command().add(1, "-c");
        }

        return pbuilder;
    }

    public static void main(String[] args) {
        try {
            ProcessBuilder pb = createProcessBuilder();
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Código de salida: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


