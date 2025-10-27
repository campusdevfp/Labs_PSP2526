package procesos;

import java.io.IOException;
import java.util.List;

public class ExitValue {

    public static void main(String[] args) {
        // Lista de comandos a ejecutar (modificable según tu sistema operativo)
        List<String[]> comandos = List.of(
                new String[]{"notepad"},                // Bloc de notas (Windows)
                new String[]{"calc"},                   // Calculadora (Windows)
                new String[]{"cmd", "/c", "dir"},       // Comando shell correcto
                new String[]{"cmd", "/c", "noexiste"},  // Comando inexistente
                new String[]{"cmd", "/c", "ping", "-xyz"} // Parámetros incorrectos
        );

        for (String[] comando : comandos) {
            ejecutarYMostrar(comando);
        }

        System.out.println("\n--- Pruebas con System.exit() ---");

        // Prueba con System.exit(10)
        // Descomenta para probar en IDEs :
        System.exit(10);

        // Prueba con System.exit(0)
        // Descomenta para probar en IDES:
        // System.exit(0);

        // Si no se llama explícitamente, el valor por defecto es 0.
    }

    private static void ejecutarYMostrar(String[] comando) {
        try {
            System.out.println("\nEjecutando: " + String.join(" ", comando));

            ProcessBuilder pb = new ProcessBuilder(comando);
            pb.inheritIO(); // Muestra salida y errores del proceso en la consola actual
            Process proceso = pb.start();

            int exitCode = proceso.waitFor(); // Esperar a que termine
            System.out.println("Código de salida: " + exitCode);

        } catch (IOException e) {
            System.out.println("Error al ejecutar el comando: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("La ejecución fue interrumpida");
        }
    }
}
