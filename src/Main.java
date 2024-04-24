import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Imprimir el menú
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            System.out.println("Bienvenido al conversor de moneda =)");
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            System.out.println("1 - Dólar               =>>     Peso Argentino");
            System.out.println("2 - Peso Argentino      =>>     Dólar");
            System.out.println("3 - Dólar               =>>     Real Brasileño");
            System.out.println("4 - Real Brasileño      =>>     Dólar");
            System.out.println("5 - Dólar               =>>     Peso Colombiano");
            System.out.println("6 - Peso Colombiano     =>>     Dólar");
            System.out.println("7 - Salir");
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");

            // Lista de códigos de moneda permitidos
            List<String> allowedCurrencyCodes = Arrays.asList("ARS", "BRL", "COP", "USD");

            // URL de la API con tu clave
            String urlString = "https://v6.exchangerate-api.com/v6/1abd87223f265c3d1ef2422a/latest/USD";

            // Analizar la respuesta JSON
            JsonObject jsonResponse = parseJsonResponse(urlString);

            // Solicitar al usuario que elija una opción del menú
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean validOption = false;
            while (!validOption) {
                System.out.print("Elige una opción del menú: ");
                String option = reader.readLine().trim();

                switch (option) {
                    case "1":
                        convertCurrency("USD", "ARS", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "2":
                        convertCurrency("ARS", "USD", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "3":
                        convertCurrency("USD", "BRL", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "4":
                        convertCurrency("BRL", "USD", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "5":
                        convertCurrency("USD", "COP", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "6":
                        convertCurrency("COP", "USD", allowedCurrencyCodes, jsonResponse, reader);
                        break;
                    case "7":
                        System.out.println("Gracias por usar el conversor de moneda. ¡Hasta luego!");
                        validOption = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void convertCurrency(String fromCurrency, String toCurrency, List<String> allowedCurrencyCodes, JsonObject jsonResponse, BufferedReader reader) throws IOException {
        if (!allowedCurrencyCodes.contains(fromCurrency) || !allowedCurrencyCodes.contains(toCurrency)) {
            System.out.println("Error: uno o ambos códigos de moneda no son válidos.");
            return;
        }

        System.out.print("Ingresa la cantidad de dinero que quieres cambiar: ");
        double currencyAmount = Double.parseDouble(reader.readLine().trim());

        JsonObject conversionRates = jsonResponse.getAsJsonObject("conversion_rates");
        double currencyValue1 = conversionRates.get(fromCurrency).getAsDouble();
        double currencyValue2 = conversionRates.get(toCurrency).getAsDouble();

        double resultado = (currencyAmount / currencyValue1) * currencyValue2;

        System.out.println("El cambio de " + fromCurrency + " a " + toCurrency + " es: " + resultado + "[" + toCurrency + "]");
    }

    private static JsonObject parseJsonResponse(String urlString) throws IOException {
        // Crear URL y HttpURLConnection
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Leer la respuesta de la API
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parsear la respuesta JSON
        Gson gson = new Gson();
        return gson.fromJson(response.toString(), JsonObject.class);
    }
}
