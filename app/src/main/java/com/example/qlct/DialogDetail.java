package com.example.qlct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class DialogDetail extends AppCompatActivity {
    private EditText etGroup, etAmount, etDate;
    private Button btnDelete, btnEdit;
    private FirebaseFirestore db;
    private String documentId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_detail); // Đảm bảo layout chính xác

        // Ánh xạ các thành phần
        etGroup = findViewById(R.id.etGroup);
        etAmount = findViewById(R.id.etAmount);
        etDate = findViewById(R.id.etDate);
        btnDelete = findViewById(R.id.btn_delete);
        btnEdit = findViewById(R.id.btn_edit);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu từ Intent
        documentId = getIntent().getStringExtra("id");
        String group = getIntent().getStringExtra("group");
        String amount = getIntent().getStringExtra("amount");
        String date = getIntent().getStringExtra("date");

        // Hiển thị dữ liệu lên EditText
        etGroup.setText(group);
        etAmount.setText(amount);
        etDate.setText(date);

        // Xử lý nút Edit
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ EditText
                String updatedGroup = etGroup.getText().toString();
                String updatedAmount = etAmount.getText().toString();
                String updatedDate = etDate.getText().toString();

                if (updatedGroup.isEmpty() || updatedAmount.isEmpty() || updatedDate.isEmpty()) {
                    Toast.makeText(DialogDetail.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Cập nhật dữ liệu trong Firestore
                db.collection("budgets").document(documentId)
                        .update("group", updatedGroup, "amount", updatedAmount, "date", updatedDate)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(DialogDetail.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Đóng Activity sau khi cập nhật thành công
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(DialogDetail.this, "Error updating item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị thông báo xác nhận trước khi xóa
                new android.app.AlertDialog.Builder(DialogDetail.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Xóa mục từ Firestore
                            db.collection("budgets").document(documentId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(DialogDetail.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                                        finish(); // Đóng Activity sau khi xóa
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(DialogDetail.this, "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .setNegativeButton("No", null) // Không làm gì nếu người dùng bấm "No"
                        .show();
            }
        });

    }
}
