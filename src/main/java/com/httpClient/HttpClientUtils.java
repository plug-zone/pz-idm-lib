package com.httpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.util.Map;

public class HttpClientUtils {

    /**
     * Realiza una petición POST para enviar un archivo a una URL específica.
     *
     * @param url        La URL a la que se enviará la petición POST.
     * @param filePath   La ruta del archivo que se enviará en la petición.
     * @param headersJson Un JSON que contiene los headers necesarios para la petición.
     * @param proxyInfo  Información del proxy en formato "host:puerto" (opcional, puede ser null o vacío).
     * @param proxyUser  Usuario para el proxy (opcional, puede ser null si no se requiere autenticación).
     * @param proxyPass  Contraseña para el proxy (opcional, puede ser null si no se requiere autenticación).
     * @return Una cadena que indica el resultado de la petición. "OK" si es exitosa,
     *         o un mensaje de error si ocurre una excepción.
     */
    public static String postFile(String url, String filePath, String headersJson,
                                  String proxyInfo, String proxyUser, String proxyPass) {

        String ret = "VACIO";

        if (!url.isEmpty() && !filePath.isEmpty() && !headersJson.isEmpty()) {
            try {
                // Convertir headersJson a un Map
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> headers = objectMapper.readValue(headersJson,
                        new TypeReference<Map<String, String>>() {});

                // Crear cliente HTTP con o sin proxy
                CloseableHttpClient httpClient = createHttpClient(proxyInfo, proxyUser, proxyPass);

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
            } catch (Exception e) {
                return createResponseJson("ERROR", e.getMessage());
            }
        }

        return ret;
    }

    /**
     * Crea un cliente HTTP con o sin configuración de proxy.
     *
     * @param proxyInfo Información del proxy en formato "host:puerto" (puede ser null o vacío si no hay proxy).
     * @param proxyUser Usuario del proxy (opcional, puede ser null).
     * @param proxyPass Contraseña del proxy (opcional, puede ser null).
     * @return Una instancia de CloseableHttpClient configurada según los parámetros.
     */
    private static CloseableHttpClient createHttpClient(String proxyInfo,
                                                        String proxyUser, String proxyPass) {

        if (proxyInfo != null && !proxyInfo.isEmpty()) {
            try {
                // Separar host y puerto del string proxyInfo
                String[] proxyParts = proxyInfo.split(":");
                if (proxyParts.length != 2) {
                    throw new IllegalArgumentException("El formato del proxy debe ser 'host:puerto'");
                }

                String proxyHost = proxyParts[0];
                int proxyPort = Integer.parseInt(proxyParts[1]);

                HttpHost proxy = new HttpHost(proxyHost, proxyPort);
                HttpClientBuilder clientBuilder = HttpClientBuilder.create().setProxy(proxy);

                // Si se proporcionan credenciales para el proxy
                if (proxyUser != null && !proxyUser.isEmpty() && proxyPass != null) {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    credentialsProvider.setCredentials(
                            new AuthScope(proxyHost, proxyPort),
                            new UsernamePasswordCredentials(proxyUser, proxyPass)
                    );
                    clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }

                return clientBuilder.build();
            } catch (Exception e) {
                throw new RuntimeException("Error al configurar el proxy: " + e.getMessage(), e);
            }
        } else {
            // Sin proxy
            return HttpClients.createDefault();
        }
    }

    /**
     * Crea un JSON de respuesta.
     *
     * @param status Código de estado de la respuesta.
     * @param body   Cuerpo de la respuesta.
     * @return Una cadena JSON representando el estado y el cuerpo de la respuesta.
     */
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
