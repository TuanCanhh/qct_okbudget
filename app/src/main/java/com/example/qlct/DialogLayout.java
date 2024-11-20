package com.example.qlct;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DialogLayout extends AppCompatActivity {

    EditText etGroup, etAmount, etDate;
    Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);

        // Ánh xạ các thành phần từ dialog_layout.xml
        etGroup = findViewById(R.id.etGroup);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        btnCancel = findViewById(R.id.btn_cancel);
        btnSave = findViewById(R.id.btn_save);

        // Xử lý sự kiện nút Cancel
        btnCancel.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Save
        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường nhập
            String group = etGroup.getText().toString();
            String amount = etAmount.getText().toString();
            String date = etDate.getText().toString();

            if (group.isEmpty() || amount.isEmpty() || date.isEmpty()) {
                // Hiển thị thông báo lỗi nếu thiếu dữ liệu
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu dữ liệu vào Firestore
            saveToFirestore(group, amount, date);

            // Quay lại FragmentBudget
            finish(); // Đóng Activity
        });
    }

    private void saveToFirestore(String group, String amount, String date) {
        // Khởi tạo Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo đối tượng dữ liệu
        Map<String, Object> item = new HashMap<>();
        item.put("group", group);
        item.put("amount", amount);
        item.put("date", date);

        // Lưu dữ liệu vào bộ sưu tập "budgets" trong Firestore
        db.collection("budgets")
                .add(item)
                .addOnSuccessListener(documentReference -> {
                    // Thành công
                    Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Thất bại
                    Toast.makeText(this, "Error saving item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
