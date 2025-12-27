package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.MediaType;


public class NewActivity extends AppCompatActivity {

    private Button translateButton;
    private TextView translatedTextView;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Translate Demo OkHttp");

        translateButton = findViewById(R.id.translateButton);
        translatedTextView = findViewById(R.id.translatedTextView);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateText();
            }
        });
    }

    private void translateText() {
        String apiKey = "334c880c66msh978ef7c11e2f863p1e21fcjsn27df001baac3";
        String host = "google-translate113.p.rapidapi.com";
        String url = "https://google-translate113.p.rapidapi.com/api/v1/translator/json";

        MediaType mediaType = MediaType.parse("application/json");

        // The text to translate is now hardcoded, with newlines escaped for JSON.
        String jsonPayload = "{\"from\":\"en\",\"to\":\"ro\",\"json\":{\"text\":\"El 12 de octubre es el día de la Hispanidad que celebra el descubrimiento de América en 1492. Este día coincide con la fiesta de la Virgen María del Pilar, que es el patrona de España.\\n\\nActualmente, la Hispanidad se celebra dentro y fuera de España, aunque es una de las fiestas que más polémica generan. En muchos países de Latinoamérica el descubrimiento de América se asocia al comienzo de la colonización española y a la destrucción de las culturas locales nativas. Por este motivo, en América del Sur la fiesta se percibe como una reivindicación.\\n\\nEn España la Hispanidad se festeja con un desfile militar y una recepción, encabezada por los Reyes, para el cuerpo diplomático en el Palacio Real.\"}}";

        RequestBody body = RequestBody.create(mediaType, jsonPayload);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", host)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NewActivity", "Translate request failed", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    final String jsonData = response.body().string();
                    runOnUiThread(() -> translatedTextView.setText(jsonData));
                } else {
                    Log.e("NewActivity", "Translate response not successful. Code: " + response.code());
                    if (response.body() != null) {
                        Log.e("NewActivity", "Response body: " + response.body().string());
                    }
                }
            }
        });
    }
}
