package com.example.qlct;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Editprofile extends AppCompatActivity {

    private EditText name, phone, age1, gmail;
    private Button editProfile;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isEditing = false;  // Biến kiểm tra trạng thái chỉnh sửa
    private String userId;  // ID của người dùng hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        // Ánh xạ các thành phần giao diện
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        age1 = findViewById(R.id.age1);
        gmail = findViewById(R.id.gmail);
        editProfile = findViewById(R.id.editprofile);

        // Nhận dữ liệu từ Intent được truyền từ SignUp
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nameValue = extras.getString("name", "");
            String phoneValue = extras.getString("phone", "");
            String ageValue = extras.getString("age", "");
            String gmailValue = extras.getString("gmail", "");

            // Gán dữ liệu vào các EditText
            name.setText(nameValue);
            phone.setText(phoneValue);
            age1.setText(ageValue);
            gmail.setText(gmailValue);
        }

        // Tải thông tin người dùng từ Firestore (nếu có)
        loadUserProfile();

        // Thiết lập sự kiện onClick cho nút "Edit Profile"
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    saveUserData();  // Lưu dữ liệu khi đang ở chế độ chỉnh sửa
                } else {
                    enableEditing(true);  // Bật chế độ chỉnh sửa
                }
            }
        });
    }

    private void enableEditing(boolean enable) {
        // Bật hoặc tắt chế độ chỉnh sửa cho các EditText
        name.setEnabled(enable);
        phone.setEnabled(enable);
        age1.setEnabled(enable);
        gmail.setEnabled(enable);

        // Thay đổi văn bản nút khi ở chế độ chỉnh sửa hoặc chế độ xem
        if (enable) {
            editProfile.setText("SAVE");  // Đổi nút thành "SAVE"
            Toast.makeText(this, "Edit Mode Enabled", Toast.LENGTH_SHORT).show();
        } else {
            editProfile.setText("EDIT PROFILE");  // Đổi nút lại thành "EDIT PROFILE"
            Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();
        }

        isEditing = enable;  // Cập nhật trạng thái chỉnh sửa
    }

    private void saveUserData() {
        // Lấy dữ liệu từ EditText
        String userName = name.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();
        String ageString = age1.getText().toString().trim();
        String userGmail = gmail.getText().toString().trim();

        if (userName.isEmpty() || userPhone.isEmpty() || ageString.isEmpty() || userGmail.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi tuổi thành số nguyên
        Long userAge = Long.parseLong(ageString);

        // Tạo một bản đồ dữ liệu để lưu vào Firestore
        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", userName);
        userUpdates.put("phone", userPhone);
        userUpdates.put("age", userAge);
        userUpdates.put("gmail", userGmail);

        // Cập nhật dữ liệu người dùng trong Firestore
        db.collection("users").document(userId)
                .update(userUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            enableEditing(false);  // Tắt chế độ chỉnh sửa sau khi lưu
                        } else {
                            Toast.makeText(Editprofile.this, "Failed to update profile: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadUserProfile() {
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Truy xuất tài liệu người dùng từ Firestore
        db.collection("users").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Lấy dữ liệu từ document
                                String userName = document.getString("name");
                                String userPhone = document.getString("phone");
                                Long Age = document.getLong("age");
                                String ageString = String.valueOf(Age);
                                String userGmail = document.getString("gmail");

                                // Gán dữ liệu vào các EditText trên màn hình Profile
                                name.setText(userName != null ? userName : "No name");
                                phone.setText(userPhone != null ? userPhone : "No phone");
                                age1.setText(ageString);
                                gmail.setText(userGmail != null ? userGmail : "No email");
                            } else {
                                Toast.makeText(Editprofile.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Editprofile.this, "Failed to load user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
