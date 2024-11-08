package com.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    /**
     * Escribe contenido en un archivo. Si el archivo no existe, lo crea.
     *
     * @param path     La ruta del archivo donde se escribirá el contenido.
     * @param contenido El contenido que se escribirá en el archivo.
     * @throws IOException Si ocurre algún error de entrada/salida.
     */
    public static String writeFile(String path, String contenido) {

        String ret = "VACIO";

        if(!path.isEmpty() && !contenido.isEmpty()){

            try {
                Path filePath = Paths.get(path);
                Files.write(filePath, contenido.getBytes());
                ret = "OK";
            } catch (Exception e){
                return "ERROR: " + e.getMessage();
            }
        }

        return ret;
    }

    /**
     * Ejecuta un comando en la consola.
     *
     * @param comando El comando que se ejecutará.
     * @throws IOException          Si ocurre algún error de entrada/salida.
     * @throws InterruptedException Si el proceso es interrumpido.
     */
    public static String executeCommand(String comando) {

        String ret = "VACIO";

        if(!comando.isEmpty()){
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.command("bash", "-c", comando); // Para Linux/macOS
                // Para Windows, usa `processBuilder.command("cmd.exe", "/c", comando);
                ret = processBuilder.start().toString();
            } catch (Exception e){
                return "ERROR: " + e.getMessage();
            }
        }

        return ret;
    }
}