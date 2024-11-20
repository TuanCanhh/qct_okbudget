package com.example.qlct;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetFragment extends Fragment {

    // Khai báo các thành phần
    private Button btn_createNew;
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;
    private BudgetAdapter adapter;
    private List<BudgetItem> budgetList;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gán layout cho Fragment
        View rootView = inflater.inflate(R.layout.fragment_budget, container, false);

        // Ánh xạ RecyclerView, Layout trống và nút bấm
        recyclerView = rootView.findViewById(R.id.recyclerView);
        btn_createNew = rootView.findViewById(R.id.btn_createNew);
        emptyLayout = rootView.findViewById(R.id.emptyLayout);

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        budgetList = new ArrayList<>();
        adapter = new BudgetAdapter(budgetList, getContext());
        recyclerView.setAdapter(adapter);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy UID của người dùng hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để sử dụng tính năng này", Toast.LENGTH_SHORT).show();
            return rootView;
        }

        String uid = user.getUid();

        // Lắng nghe thay đổi từ Firestore chỉ theo UID của người dùng hiện tại
        db.collection("budgets")
                .whereEqualTo("uid", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(getContext(), "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Cập nhật danh sách khi dữ liệu thay đổi
                    budgetList.clear();
                    if (value != null) {
                        for (DocumentSnapshot doc : value) {
                            BudgetItem item = doc.toObject(BudgetItem.class);
                            if (item != null) {
                                item.setId(doc.getId()); // Gán ID của document từ Firestore
                                budgetList.add(item);
                            }
                        }
                    }

                    // Kiểm tra xem danh sách có trống hay không
                    if (budgetList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyLayout.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged(); // Thông báo dữ liệu thay đổi cho RecyclerView
                });

        // Thiết lập sự kiện khi nhấn nút Create New
        btn_createNew.setOnClickListener(v -> {
            // Tạo một đối tượng Dialog
            Dialog dialog = new Dialog(v.getContext());

            // Gán layout cho Dialog
            dialog.setContentView(R.layout.dialog_layout);
            dialog.setCancelable(true); // Cho phép nhấn ra ngoài để đóng dialog

            // Ánh xạ các thành phần trong dialog
            EditText etGroup = dialog.findViewById(R.id.etGroup);
            EditText etAmount = dialog.findViewById(R.id.etAmount);
            EditText etDate = dialog.findViewById(R.id.etDate);
            Button btnSave = dialog.findViewById(R.id.btn_save);
            Button btnCancel = dialog.findViewById(R.id.btn_cancel);

            // Xử lý nút Save
            btnSave.setOnClickListener(saveView -> {
                String group = etGroup.getText().toString().trim();
                String amount = etAmount.getText().toString().trim();
                String date = etDate.getText().toString().trim();

                if (group.isEmpty() || amount.isEmpty() || date.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lưu dữ liệu vào Firestore
                saveToFirestore(uid, group, amount, date);

                // Đóng dialog
                dialog.dismiss();
            });

            // Xử lý nút Cancel
            btnCancel.setOnClickListener(cancelView -> dialog.dismiss());

            // Hiển thị Dialog
            dialog.show();
        });

        return rootView; // Trả về rootView thay vì view
    }

    // Hàm lưu dữ liệu vào Firestore
    private void saveToFirestore(String uid, String group, String amount, String date) {
        Map<String, Object> item = new HashMap<>();
        item.put("uid", uid); // Thêm UID để phân biệt dữ liệu
        item.put("group", group);
        item.put("amount", amount);
        item.put("date", date);

        db.collection("budgets")
                .add(item)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Thêm mục thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi thêm mục: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
