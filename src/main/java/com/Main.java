package com;

import com.File.FileUtils;
import com.JPATH.JSONPathUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n---- Menú de Opciones ----");
            System.out.println("\n---- JsonPath ----");
            System.out.println("1. Verificar si un valor existe en el JSON exists(String jsonString, String jsonPath)");
            System.out.println("2. Leer un valor del JSON read(String jsonString, String jsonPath)");
            System.out.println("3. Leer un valor con un valor predeterminado readWithDefault(String jsonString, String jsonPath, String defaultValue)");
            System.out.println("4. Verificar si un JSONPath es válido isValidPath(String jsonPath)");
            System.out.println("5. Verificar si un JSON es válido isValidJson(String jsonString)");
            System.out.println("\n---- File ----");
            System.out.println("6. Escribir en un archivo writeFile(String path, String contenido)");
            System.out.println("7. Ejecutar un comando en la consola executeCommand(String comando)");
            System.out.println("\n---- general ----");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.next(); // Limpiar la entrada incorrecta
                continue;
            }

            int option = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer del scanner

            switch (option) {
                case 1:
                    // Verificar si un valor existe en el JSON
                    String jsonString1 = leerJson(scanner);
                    System.out.print("Ingrese el JSONPath que quiere verificar: ");
                    String jsonPath1 = scanner.nextLine();
                    boolean exists = JSONPathUtil.exists(jsonString1, jsonPath1);
                    System.out.println("¿Existe el valor? " + exists);
                    break;

                case 2:
                    // Leer un valor del JSON
                    String jsonString2 = leerJson(scanner);
                    System.out.print("Ingrese el JSONPath que quiere leer: ");
                    String jsonPath2 = scanner.nextLine();
                    String result = JSONPathUtil.read(jsonString2, jsonPath2);
                    System.out.println("Valor encontrado: " + result);
                    break;

                case 3:
                    // Leer un valor con valor predeterminado
                    String jsonString3 = leerJson(scanner);
                    System.out.print("Ingrese el JSONPath que quiere leer: ");
                    String jsonPath3 = scanner.nextLine();
                    System.out.print("Ingrese el valor predeterminado si no se encuentra el valor: ");
                    String defaultValue = scanner.nextLine();
                    String resultWithDefault = JSONPathUtil.readWithDefault(jsonString3, jsonPath3, defaultValue);
                    System.out.println("Resultado: " + resultWithDefault);
                    break;

                case 4:
                    // Verificar si un JSONPath es válido
                    System.out.print("Ingrese el JSONPath que desea validar: ");
                    String jsonPath4 = scanner.nextLine();
                    boolean isValidPath = JSONPathUtil.isValidPath(jsonPath4);
                    System.out.println("¿El JSONPath es válido? " + isValidPath);
                    break;

                case 5:
                    // Verificar si un JSON es válido
                    String jsonString6 = leerJson(scanner);
                    boolean isValidJson = JSONPathUtil.isValidJson(jsonString6);
                    System.out.println("¿El JSON es válido? " + isValidJson);
                    break;

                case 6:
                    // Escribir en un archivo
                    System.out.print("Ingrese la ruta del archivo donde desea escribir: ");
                    String path = scanner.nextLine();
                    System.out.print("Ingrese el contenido que desea escribir: ");
                    String contenido = scanner.nextLine();
                    String writeFileResult = FileUtils.writeFile(path, contenido);
                    System.out.println("Resultado de la escritura: " + writeFileResult);
                    break;

                case 7:
                    // Ejecutar un comando en la consola
                    System.out.print("Ingrese el path del archivo a adjuntar: ");
                    String filePath = scanner.nextLine();
                    System.out.print("Ingrese el url para enviar la peticion: ");
                    String url = scanner.nextLine();
                    String executeCommandResult = FileUtils.uploadFile(url,filePath);
                    System.out.print("el resultado es: " + executeCommandResult);
                    break;
                    
                case 8:
                    // Salir
                    exit = true;
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Método para leer un JSON en múltiples líneas hasta que el usuario ingrese "END".
     *
     * @param scanner Scanner para leer la entrada del usuario
     * @return El JSON completo como una cadena
     */
    private static String leerJson(Scanner scanner) {
        System.out.println("Ingrese el JSON en formato de cadena (escriba 'END' en una nueva línea para terminar):");
        System.out.println("Ejemplo de entrada:");
        System.out.println("{");
        System.out.println("  \"id\": 1");
        System.out.println("}");
        System.out.println("END");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("END")) {
            jsonBuilder.append(line).append("\n");
        }
        return jsonBuilder.toString();
    }
}
