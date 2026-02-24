package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EquipmentActivity extends AppCompatActivity {
    private TextView tvTitle;
    private EditText etName;
    private Spinner spinnerCondition;
    private Button btnSave, btnDelete, btnMove;

    private AppDatabase db;
    private SessionManager sessionManager;
    private ExecutorService executor;

    private int cabinetNumber;
    private int equipmentId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);

        tvTitle = findViewById(R.id.tvTitle);
        etName = findViewById(R.id.etName);
        spinnerCondition = findViewById(R.id.spinnerCondition);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        btnMove = findViewById(R.id.btnMove);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);
        executor = Executors.newSingleThreadExecutor();

        cabinetNumber = getIntent().getIntExtra("cabinet_number", 1);
        equipmentId = getIntent().getIntExtra("equipment_id", -1);
        isEditMode = getIntent().getBooleanExtra("is_edit_mode", false);

        setupSpinner();

        if (isEditMode) {
            tvTitle.setText("Редактировать оборудование");
            loadEquipment();
            btnDelete.setVisibility(Button.VISIBLE);
            btnMove.setVisibility(Button.VISIBLE);
        } else {
            tvTitle.setText("Добавить оборудование");
            btnDelete.setVisibility(Button.GONE);
            btnMove.setVisibility(Button.GONE);
        }

        btnSave.setOnClickListener(v -> saveEquipment());
        btnDelete.setOnClickListener(v -> deleteEquipment());
        btnMove.setOnClickListener(v -> moveEquipment());
    }

    private void setupSpinner() {
        String[] conditions = {"Исправно", "Неисправно", "На ремонте", "Списано"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                conditions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCondition.setAdapter(adapter);
        spinnerCondition.setSelection(0);
    }

    private void loadEquipment() {
        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            Equipment equipment = db.equipmentDao().getEquipmentById(equipmentId, userId);
            runOnUiThread(() -> {
                if (equipment != null) {
                    etName.setText(equipment.name);
                    String[] conditions = {"Исправно", "Неисправно", "На ремонте", "Списано"};
                    int position = 0;
                    for (int i = 0; i < conditions.length; i++) {
                        if (conditions[i].equals(equipment.condition)) {
                            position = i;
                            break;
                        }
                    }
                    spinnerCondition.setSelection(position);
                }
            });
        });
    }

    private void saveEquipment() {
        String name = etName.getText().toString().trim();
        String condition = spinnerCondition.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Введите название оборудования", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() > 100) {
            Toast.makeText(this, "Название слишком длинное (максимум 100 символов)", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            if (isEditMode) {
                Equipment equipment = db.equipmentDao().getEquipmentById(equipmentId, userId);
                if (equipment != null) {
                    equipment.name = name;
                    equipment.condition = condition;
                    db.equipmentDao().update(equipment);
                    runOnUiThread(() -> {
                        Toast.makeText(EquipmentActivity.this, "Оборудование обновлено", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } else {
                Equipment equipment = new Equipment(name, condition, cabinetNumber, userId);
                db.equipmentDao().insert(equipment);
                runOnUiThread(() -> {
                    Toast.makeText(EquipmentActivity.this, "Оборудование добавлено", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void deleteEquipment() {
        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            Equipment equipment = db.equipmentDao().getEquipmentById(equipmentId, userId);
            if (equipment != null) {
                db.equipmentDao().delete(equipment);
                runOnUiThread(() -> {
                    Toast.makeText(EquipmentActivity.this, "Оборудование удалено", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    private void moveEquipment() {
        int newCabinet = (cabinetNumber == 1) ? 2 : 1;

        executor.execute(() -> {
            int userId = sessionManager.getUserId();
            Equipment equipment = db.equipmentDao().getEquipmentById(equipmentId, userId);
            if (equipment != null) {
                equipment.cabinet = newCabinet;
                db.equipmentDao().update(equipment);
                runOnUiThread(() -> {
                    Toast.makeText(EquipmentActivity.this,
                            "Оборудование перемещено в кабинет " + newCabinet,
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}