package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnCabinet1, btnCabinet2, btnLogout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCabinet1 = findViewById(R.id.btnCabinet1);
        btnCabinet2 = findViewById(R.id.btnCabinet2);
        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        btnCabinet1.setOnClickListener(v -> {
            Intent intent = new Intent(this, CabinetActivity.class);
            intent.putExtra("cabinet_number", 1);
            startActivity(intent);
        });

        btnCabinet2.setOnClickListener(v -> {
            Intent intent = new Intent(this, CabinetActivity.class);
            intent.putExtra("cabinet_number", 2);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}