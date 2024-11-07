package com.JPATH;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JsonProvider;

import java.util.List;

public class JSONPathUtil {

    static final String ERROR_STRING = "ERROR_READ";

    /**
     * Verifica si un atributo y/o valor del atributo existe en el JSON para el JSONPath dado.
     * Si el valor es nulo o no se encuentra o no se encuentra el atributo, devuelve false.
     * Si el JSON no es válido o está vacío, se reemplaza por un JSON vacío ("{}").
     *
     * @param jsonString El JSON en formato de cadena.
     * @param jsonPath El path que quieres verificar.
     * @return true si el valor existe y no es nulo, false en caso contrario.
     */
    public static boolean exists(String jsonString, String jsonPath) {

        JsonPath.compile(jsonPath);

        // Verificar si el JSON es válido o está vacío
        if (!isValidJson(jsonString) || jsonString.isEmpty()) {
            jsonString = "{}";
        }

        try {
            return JsonPath.read(jsonString, jsonPath) != null;
        } catch (PathNotFoundException e) {
            // Si el path no se encuentra, devuelve false
            return false;
        }
    }

    /**
     * Lee el valor del JSON en la ruta especificada y devuelve el objeto.
     * Si el resultado es una lista de un solo elemento, devuelve ese único valor.
     * En caso de cualquier error, devuelve "ERROR_READ".
     *
     * @param jsonString El JSON en formato de cadena.
     * @param jsonPath   La ruta JSONPath para consultar.
     * @return El valor encontrado o "ERROR_READ" si ocurre un error.
     */
    public static String read(String jsonString, String jsonPath) {
        try {
            JsonPath.compile(jsonPath);
            Object result = JsonPath.read(jsonString, jsonPath);
            if (result instanceof List) {
                List<?> list = (List<?>) result;
                if (list.size() == 1) {
                    // Devolver ese único elemento.
                    return list.get(0).toString();
                }
            }
            return result.toString(); // Devolver el resultado tal cual.
        } catch (InvalidJsonException | NullPointerException | IllegalArgumentException | PathNotFoundException e) {
            return ERROR_STRING;
        }
    }


    /**
     * Lee un valor de un JSON usando el JSONPath dado. Si no se encuentra el valor,
     * devuelve un valor predeterminado.
     *
     * @param jsonString El JSON en formato de cadena.
     * @param jsonPath El path que quieres verificar.
     * @param defaultValue El valor predeterminado si el path no existe o es nulo.
     * @return El valor encontrado o el predeterminado si no existe.
     */
    public static String readWithDefault(String jsonString, String jsonPath, String defaultValue) {
        try {
            Object result = read(jsonString, jsonPath);
            return result != ERROR_STRING ? result.toString() : defaultValue;
        } catch (PathNotFoundException e) {
            return defaultValue;
        }
    }

    /**
     * Verifica si un JSON es válido utilizando la librería JsonPath.
     *
     * @param jsonString El JSON en formato de cadena.
     * @return true si el JSON es válido, false en caso contrario.
     */
    public static boolean isValidJson(String jsonString) {
        try {
            JsonProvider provider = Configuration.defaultConfiguration().jsonProvider();

            // Intenta parsear el JSON, si falla, lanzará una excepción
            provider.parse(jsonString);
            return true;
        } catch (InvalidJsonException | NullPointerException | IllegalArgumentException e) {
            // Si ocurre una excepción, el JSON no es válido
            return false;
        }
    }

    /**
     * Verifica si un JSONPath dado es válido.
     * Retorna true si el JSONPath es válido, false si es inválido.
     *
     * @param jsonPath La expresión JSONPath que deseas validar.
     * @return true si el JSONPath es válido, false en caso contrario.
     */
    public static boolean isValidPath(String jsonPath) {
        try {
            // Intentamos compilar el path, si es válido no arroja excepciones
            JsonPath.compile(jsonPath);
            return true;
        } catch (InvalidPathException | IllegalArgumentException e) {
            // Si la compilación lanza una excepción, el path es inválido
            return false;
        }
    }

}
