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
    public static String executeCommand(String key) {
        String ret = "VACIO";
        if (!key.isEmpty()) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(
                        "bash", "-c",
                        //bash , -c linux y cmd.exe , \c windows
                        "curl --location 'https://directvla.jira.com/rest/api/2/issue/" + key + "/attachments' " +
                                "--header 'X-Atlassian-Token: no-check' " +
                                "--header 'Authorization: Basic c19yZ19xYW1faWRtX2ppcmFAZGlyZWN0dmxhLmNvbS5hcjpBVEFUVDN4RmZHRjBlVkRRUEdPZjYwaDBJa291Um92VXd4Y2c5ak9CUEhCX1k1TTR3UHBQcXZGMlFlOUpJV2EwTkljSHlJb0FpZ1BIT1BpQV9QanJoeDlIb3ZIa0pTUXpsRktvVWRqcFMtd2U2QTFTWEtfV0JiS2Fyc0JITXRRVTlDbUh2ZEJDZWpXU0ZjZkF1dEsyaXZ2djMzYXBEQ3Noa3p2R0QwTkVzSGlqdVl1YlI1Y25ENk09NTQ4OEIzQzU=' " +
                                "--header 'Cookie: atlassian.xsrf.token=43ccf0e1bb0dcc469905cccc01b46363648ab3b5_lin' " +
                                "--form 'file=@C:\\Users\\lcampassi\\Downloads\\hola.txt'"
                                // /var/opt/idm/logs/Jira/jira.txt
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