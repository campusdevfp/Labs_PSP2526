package procesos;



import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Lanzador {

    public static void main(String[] args) {
        try {
            // 1️⃣ Preguntar al usuario qué programa desea ejecutar
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Introduce el programa que deseas ejecutar (por ejemplo, notepad.exe): ");
            String programa = br.readLine();

            // 2️⃣ Crear el proceso para lanzar U2A4_Lanzado
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "psp.activities.U2A4_Lanzado", programa);
            pb.inheritIO(); // muestra la salida en la misma consola

            // 3️⃣ Lanzar el proceso
            Process proceso = pb.start();

            // 4️⃣ Esperar a que termine y recoger el código de salida
            int codigoSalida = proceso.waitFor();

            // 5️⃣ Mostrar resultado según el código
            if (codigoSalida == 0) {
                System.out.println("✅ El proceso terminó correctamente. Código: " + codigoSalida);
            } else {
                System.out.println("❌ El proceso terminó con errores. Código: " + codigoSalida);
            }

        } catch (Exception e) {
            System.err.println("Error al lanzar el proceso: " + e.getMessage());
        }
    }
}
