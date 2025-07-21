package com.prm392_ass.ass_prm392.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Internship;

import java.util.List;

public class RecruiterJobAdapter extends RecyclerView.Adapter<RecruiterJobAdapter.ViewHolder> {

    private List<Internship> internshipList;
    private Context context;
    private OnJobActionListener listener;

    public interface OnJobActionListener {
        void onEditClick(Internship internship);
        void onDeleteClick(Internship internship);
    }

    public RecruiterJobAdapter(List<Internship> internshipList, OnJobActionListener listener) {
        this.internshipList = internshipList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_recruiter, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Internship internship = internshipList.get(position);
        holder.tvJobTitle.setText(internship.getTitle());
        holder.tvCompanyLocation.setText(internship.getCompanyName() + " - " + internship.getLocation());
        holder.tvSalary.setText(internship.getSalary());

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(internship);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(internship);
        });
    }

    @Override
    public int getItemCount() {
        return internshipList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCompanyLocation, tvSalary;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompanyLocation = itemView.findViewById(R.id.tvCompanyLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}