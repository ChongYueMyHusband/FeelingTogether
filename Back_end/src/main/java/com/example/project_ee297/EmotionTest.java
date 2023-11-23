package com.example.project_ee297;

import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStreamReader;

public class EmotionTest {

    public static void main(String[] args) {
        String pythonScriptPath = "D:\\sentimentAnalyse1\\BertModel\\BertModel.py";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Exited with error code: " + exitCode);

            if (exitCode == 0) {
                System.out.println("Python script executed successfully.");
            } else {
                System.out.println("Error: Python script execution failed.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
