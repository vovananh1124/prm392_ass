package com.prm392_ass.ass_prm392.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Nếu bạn muốn load logo từ url
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.ApplicationItem;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {
    private Context context;
    private List<ApplicationItem> applicationList;

    public ApplicationAdapter(Context context, List<ApplicationItem> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    public void setData(List<ApplicationItem> list) {
        this.applicationList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        ApplicationItem item = applicationList.get(position);
        if (item == null) return;

        holder.tvCompany.setText(item.getCompanyName());
        holder.tvJobTitle.setText(item.getJobTitle());

        // Trạng thái đơn
        holder.tvStatus.setText(getStatusText(item.getStatus()));
        holder.tvStatus.setTextColor(getStatusColor(item.getStatus()));

        // Logo (nếu có url thì dùng Glide)
        Glide.with(context)
                .load(item.getCompanyLogoUrl())
                .placeholder(R.drawable.a)
                .error(R.drawable.a)
                .into(holder.imgLogo);

        // Xử lý View Details nếu muốn
        holder.tvViewDetails.setOnClickListener(v -> {
            // Mở chi tiết application
            // Intent sang màn chi tiết nếu muốn
        });
    }

    @Override
    public int getItemCount() {
        return applicationList == null ? 0 : applicationList.size();
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView tvCompany, tvJobTitle, tvStatus, tvViewDetails;
        public ApplicationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
        }
    }

    // Helper
    private String getStatusText(String status) {
        switch (status) {
            case "pending": return "Application Submitted";
            case "interview": return "Interview";
            case "accepted": return "Accepted";
            case "rejected": return "Rejected";
            default: return status;
        }
    }
    private int getStatusColor(String status) {
        switch (status) {
            case "pending":
                return ContextCompat.getColor(context, R.color.blue_status);
            case "interview":
                return ContextCompat.getColor(context, R.color.orange_status);
            case "accepted":
                return ContextCompat.getColor(context, R.color.green_status);
            case "rejected":
                return ContextCompat.getColor(context, R.color.red_status);
            default:
                return ContextCompat.getColor(context, R.color.gray_500);
        }
    }

}

