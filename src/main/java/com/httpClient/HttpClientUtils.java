package com.httpClient;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Map;

public class HttpClientUtils {

    /**
     * Realiza una petición POST para enviar un archivo a una URL específica.
     *
     * @param url       La URL a la que se enviará la petición POST.
     * @param filePath  La ruta del archivo que se enviará en la petición.
     * @param headersJson Un JSON que contiene los headers necesarios para la petición.
     * @return Una cadena que indica el resultado de la petición. "OK" si es exitosa,
     *         o un mensaje de error si ocurre una excepción.
     */
    public static String postFile(String url, String filePath, String headersJson) {

        String ret = "VACIO";

        if (!url.isEmpty() && !filePath.isEmpty() && !headersJson.isEmpty()) {
            try {
                // Convertir headersJson a un Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> headers = objectMapper.readValue(headersJson,
                        new TypeReference<Map<String, String>>() {});

                // Crear cliente HTTP
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    // Crear objeto HttpPost con la URL
                    HttpPost post = new HttpPost(url);

                    // Agregar los headers al HttpPost
                    if (headers != null) {
                        for (Map.Entry<String, String> header : headers.entrySet()) {
                            post.setHeader(header.getKey(), header.getValue());
                        }
                    }

                    // Cargar el archivo desde el path
                    File file = new File(filePath);
                    if (!file.exists()) {
                        return createResponseJson("ERROR", "El archivo no existe: " + filePath);
                    }

                    HttpEntity entity = MultipartEntityBuilder.create()
                            .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                            .build();

                    post.setEntity(entity);

                    // Ejecutar la petición
                    try (CloseableHttpResponse response = httpClient.execute(post)) {
                        // Extraer el código de estado
                        int statusCode = response.getStatusLine().getStatusCode();

                        // Extraer el body
                        String body = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";

                        ret = createResponseJson(String.valueOf(statusCode), body);
                    }
                }
            } catch (Exception e) {
                return createResponseJson("ERROR", e.getMessage());
            }
        }

        return ret;
    }

    private static String createResponseJson(String status, String body) {
        try {
            ResponseDTO response = new ResponseDTO(status, body);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return "{\"status\":\"ERROR\",\"body\":\"Error al crear JSON de respuesta\"}";
        }
    }

}
