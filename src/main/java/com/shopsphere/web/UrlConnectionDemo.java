package com.shopsphere.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public final class UrlConnectionDemo {

    private UrlConnectionDemo() {
    }

    public static String read(String urlText) throws Exception {
        URI uri = URI.create(urlText);

        HttpURLConnection connection =
                (HttpURLConnection) uri.toURL().openConnection();

        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8))) {

            StringBuilder out = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null
                    && out.length() < 4000) {
                out.append(line)
                   .append(System.lineSeparator());
            }

            return out.toString();
        } finally {
            connection.disconnect();
        }
    }

    public static void main(String[] args) throws Exception {
        String target = args.length == 0
                ? "https://example.com"
                : args[0];

        System.out.println(read(target));
    }
}