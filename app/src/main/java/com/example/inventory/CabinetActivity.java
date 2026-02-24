package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CabinetActivity extends AppCompatActivity {
    private TextView tvCabinetTitle;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;

    private AppDatabase db;
    private SessionManager sessionManager;
    private EquipmentAdapter adapter;
    private ExecutorService executor;

    private int cabinetNumber;
    private List<Equipment> equipmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        tvCabinetTitle = findViewById(R.id.tvCabinetTitle);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);
        executor = Executors.newSingleThreadExecutor();

        cabinetNumber = getIntent().getIntExtra("cabinet_number", 1);
        tvCabinetTitle.setText("Кабинет " + cabinetNumber);

        setupRecyclerView();
        loadEquipment();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEquipment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchEquipment(newText);
                return true;
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(CabinetActivity.this, EquipmentActivity.class);
            intent.putExtra("cabinet_number", cabinetNumber);
            intent.putExtra("is_edit_mode", false);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        adapter = new EquipmentAdapter(equipmentList, equipment -> {
            Intent intent = new Intent(CabinetActivity.this, EquipmentActivity.class);
            intent.putExtra("equipment_id", equipment.id);
            intent.putExtra("cabinet_number", equipment.cabinet);
            intent.putExtra("is_edit_mode", true);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadEquipment() {
        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            List<Equipment> list = db.equipmentDao().getEquipmentByCabinet(userId, cabinetNumber);
            runOnUiThread(() -> {
                equipmentList.clear();
                equipmentList.addAll(list);
                adapter.updateList(equipmentList);
            });
        });
    }

    private void searchEquipment(String query) {
        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            List<Equipment> searchResults;
            if (query.isEmpty()) {
                searchResults = db.equipmentDao().getEquipmentByCabinet(userId, cabinetNumber);
            } else {
                searchResults = db.equipmentDao().searchEquipment(userId, cabinetNumber, query);
            }

            runOnUiThread(() -> {
                equipmentList.clear();
                equipmentList.addAll(searchResults);
                adapter.updateList(equipmentList);
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEquipment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}