package com.sunbase;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Chatbot {
    private static SupabaseClient supabaseClient = new SupabaseClient();
    private static String documentContent = "";

    public static void main(String[] args) {
        String supabaseUrl = System.getenv("SUPABASE_URL");
        String supabaseKey = System.getenv("SUPABASE_KEY");

        if (supabaseUrl == null || supabaseKey == null) {
            System.err.println("Environment variables SUPABASE_URL or SUPABASE_KEY are not set.");
            return;
        }

        try {
            //adding a document from a file
            UUID docId = UUID.randomUUID();
            String filePath = "C:\\Users\\ashis\\eclipse-workspace\\sunbase-rag-ai-chatbot\\documents\\note"; // Update with your file path
            supabaseClient.addDocumentFromFile(docId, filePath);

            // fetching a document
            String document = supabaseClient.fetchDocumentById(docId);
            System.out.println("Fetched Document: ");

            // Extract document content from the fetched document
            documentContent = extractContentFromFetchedDocument(document);

            Scanner scan = new Scanner(System.in);

            // Example user query
            while (true) {
                System.out.print("Enter your query (or type 'exit' to quit): ");
                String userQuery = scan.nextLine();

                if (userQuery.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                // Process user query and generate response
                List<String> answers = extractAnswersFromDocumentContent(documentContent, userQuery);
                String response = generateResponse(answers);
                System.out.println("Chatbot Response: " + response);
            }
            scan.close();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    private static String generateResponse(List<String> answers) {
        if (answers.isEmpty()) {
            return "I don't know about that. Can you ask something else?";
        } else {
            StringJoiner joiner = new StringJoiner(". ", "Here are some answers to your query: ", ".");
            for (String answer : answers) {
                joiner.add(answer);
            }
            return joiner.toString();
        }
    }

    private static List<String> extractAnswersFromDocumentContent(String documentContent, String userQuery) {
        List<String> answers = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?i)" + Pattern.quote(userQuery) + "\\s*(.*?)\\n", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(documentContent);
        while (matcher.find()) {
            String answer = matcher.group(1).trim();
            answers.add(answer);
        }
        return answers;
    }

    private static String extractContentFromFetchedDocument(String fetchedDocument) {
        // Assuming the fetched document is a JSON array with a single JSON object
        JsonArray jsonArray = JsonParser.parseString(fetchedDocument).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        return jsonObject.get("content").getAsString();
    }
}