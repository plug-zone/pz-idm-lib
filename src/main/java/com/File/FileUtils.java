package com.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtils {

    /**
     * Escribe contenido en un archivo. Si el archivo no existe, lo crea.
     *
     * @param path     La ruta del archivo donde se escribirá el contenido.
     * @param contenido El contenido que se escribirá en el archivo.
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
     * * Ejecuta un comando en la consola. *
     *  * @param comando El comando que se ejecutará.
     * * @throws IOException Si ocurre algún error de entrada/salida.
     * * @throws InterruptedException Si el proceso es interrumpido.
     *  */
    public static String executeCurl(String urlString, String filePath, String authorization, String cookie) {
        String ret = "VACIO";
        if (urlString.isEmpty() || filePath.isEmpty() || authorization.isEmpty() || cookie.isEmpty()) {
            return ret;
        } else {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "bash", "-c",
                        //bash , -c linux y cmd.exe , \c windows
                        "curl --location '" + urlString + "' " +
                                "--header 'X-Atlassian-Token: no-check' " +
                                "--header 'Authorization: " + authorization + "' " +
                                "--header 'Cookie: " + cookie + "' " +
                                "--form 'file=@" + filePath + "'"
                );

                // Ejecutar el proceso y capturar su salida
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line);
                }
                reader.close();

                // Capturar la salida final
                ret = output.toString();

            } catch (Exception e) {
                return "ERROR: " + e.getMessage();
            }
        }
        return ret;
    }
}
