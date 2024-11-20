package com.example.qlct;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private final List<BudgetItem> budgetList;
    private final Context context;

    // Constructor nhận danh sách dữ liệu và context
    public BudgetAdapter(List<BudgetItem> budgetList, Context context) {
        this.budgetList = budgetList;
        this.context = context;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_budget cho từng mục trong RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        // Lấy dữ liệu từ danh sách tại vị trí hiện tại
        BudgetItem item = budgetList.get(position);
        holder.groupTextView.setText(item.getGroup());
        holder.amountTextView.setText(item.getAmount());
        holder.dateTextView.setText(item.getDate());

        // Xử lý sự kiện khi bấm vào mục
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để chuyển sang DialogDetail
            Intent intent = new Intent(context, DialogDetail.class);

            // Truyền dữ liệu của mục đã chọn sang DialogDetail
            intent.putExtra("id", item.getId()); // Truyền document ID
            intent.putExtra("group", item.getGroup());
            intent.putExtra("amount", item.getAmount());
            intent.putExtra("date", item.getDate());

            // Bắt đầu Activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng mục trong danh sách
        return budgetList != null ? budgetList.size() : 0;
    }

    // Lớp ViewHolder cho RecyclerView
    static class BudgetViewHolder extends RecyclerView.ViewHolder {

        TextView groupTextView, amountTextView, dateTextView;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các TextView từ item_budget.xml
            groupTextView = itemView.findViewById(R.id.groupTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
