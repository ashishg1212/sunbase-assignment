package com.sunbase;

import okhttp3.*;
import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class SupabaseClient {
    private static final String SUPABASE_URL;
    private static final String SUPABASE_KEY;
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    static {
        SUPABASE_URL = System.getenv("SUPABASE_URL");
        SUPABASE_KEY = System.getenv("SUPABASE_KEY");

        if (SUPABASE_URL == null || SUPABASE_URL.isEmpty()) {
            throw new IllegalArgumentException("SUPABASE_URL environment variable is not set or is empty.");
        }

        if (SUPABASE_KEY == null || SUPABASE_KEY.isEmpty()) {
            throw new IllegalArgumentException("SUPABASE_KEY environment variable is not set or is empty.");
        }
    }

    public String fetchDocumentById(UUID id) throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/documents?id=eq." + id)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }

    public void addDocument(UUID id, String content) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id.toString());
        jsonObject.addProperty("content", content);

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/documents")
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            System.out.println(response.body().string());
        }
    }

    public void addDocumentFromFile(UUID id, String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        addDocument(id, content);
    }
}
