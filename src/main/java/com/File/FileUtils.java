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
         * Realiza una solicitud POST para subir un archivo a una URL usando multipart/form-data.
         *
         * @param urlString La URL de destino.
         * @param filePath La ruta al archivo que se va a subir.
         * @return La respuesta del servidor o un mensaje de confirmación en caso de éxito.
         */
        public static String uploadFile(String urlString, String filePath) {
            String boundary = "===" + System.currentTimeMillis() + "==="; // Define el límite para separar partes en el multipart
            String LINE_FEED = "\r\n";
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();

            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setDoOutput(true); // Habilita la salida para el POST
                connection.setDoInput(true);
                connection.setRequestMethod("POST");

                // Establece encabezados para la solicitud
                connection.setRequestProperty("X-Atlassian-Token", "no-check");
                connection.setRequestProperty("Authorization", "Basic c19yZ19xYW1faWRtX2ppcmFAZGlyZWN0dmxhLmNvbS5hcjpBVEFUVDN4RmZHRjBlVkRRUEdPZjYwaDBJa291Um92VXd4Y2c5ak9CUEhCX1k1TTR3UHBQcXZGMlFlOUpJV2EwTkljSHlJb0FpZ1BIT1BpQV9QanJoeDlIb3ZIa0pTUXpsRktvVWRqcFMtd2U2QTFTWEtfV0JiS2Fyc0JITXRRVTlDbUh2ZEJDZWpXU0ZjZkF1dEsyaXZ2djMzYXBEQ3Noa3p2R0QwTkVzSGlqdVl1YlI1Y25ENk09NTQ4OEIzQzU="); // Reemplaza con tu token
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                try (OutputStream outputStream = connection.getOutputStream();
                     PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true)) {

                    // Agrega el archivo como una parte del formulario
                    File file = new File(filePath);
                    String fileName = file.getName();

                    writer.append("--").append(boundary).append(LINE_FEED);
                    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fileName).append("\"")
                            .append(LINE_FEED);
                    writer.append("Content-Type: ").append(Files.probeContentType(Paths.get(filePath))).append(LINE_FEED);
                    writer.append(LINE_FEED).flush();

                    // Escribe el contenido del archivo en el flujo de salida
                    Files.copy(Paths.get(filePath), outputStream);
                    outputStream.flush();
                    writer.append(LINE_FEED).flush();

                    // Finaliza el multipart
                    writer.append("--").append(boundary).append("--").append(LINE_FEED).flush();
                }

                // Verifica el código de respuesta del servidor
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) { // 204 No Content
                    return "204 No Content";
                } else {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                            responseCode == HttpURLConnection.HTTP_OK ? connection.getInputStream() : connection.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append(LINE_FEED);
                        }
                    }
                }
            } catch (IOException e) {
                return "ERROR: " + e.getMessage();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return response.toString().trim();
        }


}