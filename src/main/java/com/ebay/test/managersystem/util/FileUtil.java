package com.ebay.test.managersystem.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    public static String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());
            }
        }

        return contentBuilder.toString().trim();
    }

    public static void writeFile(String content, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, false))) {

            writer.write(content);
        }
    }
}
