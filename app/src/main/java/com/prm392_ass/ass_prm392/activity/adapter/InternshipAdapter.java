package com.prm392_ass.ass_prm392.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.prm392_ass.ass_prm392.R;
import com.prm392_ass.ass_prm392.entity.Internship;

import java.util.List;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.ViewHolder> {

    /** 1) Interface để callback khi user click vào 1 internship */
    public interface OnItemClickListener {
        void onItemClick(Internship item);
    }

    private final List<Internship> internshipList;
    private final OnItemClickListener listener;
    private Context context;

    /** 2) Constructor nhận danh sách và listener */
    public InternshipAdapter(List<Internship> internshipList,
                             OnItemClickListener listener) {
        this.internshipList = internshipList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Internship internship = internshipList.get(position);

        // Bind dữ liệu lên UI
        Glide.with(context)
                .load(internship.getCompanyLogoUrl())
                .placeholder(R.drawable.a)
                .into(holder.imgCompanyLogo);

        holder.tvJobTitle.setText(internship.getTitle());
        holder.tvCompanyLocation.setText(
                internship.getCompanyName() + " - " + internship.getLocation()
        );
        holder.tvField.setText(internship.getField());
        holder.tvJobType.setText(internship.getJobType());
        holder.tvSeniority.setText(internship.getSeniority());
        holder.tvTimePosted.setText(internship.getTimePosted());
        holder.tvSalary.setText(internship.getSalary());

        // Gán sự kiện click cho cả itemView
        holder.itemView.setOnClickListener(v -> listener.onItemClick(internship));
    }

    @Override
    public int getItemCount() {
        return internshipList != null ? internshipList.size() : 0;
    }

    /** 3) ViewHolder */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCompanyLogo, imgMoreOptions;
        TextView tvJobTitle, tvCompanyLocation, tvField,
                tvJobType, tvSeniority, tvTimePosted, tvSalary;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCompanyLogo    = itemView.findViewById(R.id.imgCompanyLogo);
            imgMoreOptions    = itemView.findViewById(R.id.imgMoreOptions);
            tvJobTitle        = itemView.findViewById(R.id.tvJobTitle);
            tvCompanyLocation = itemView.findViewById(R.id.tvCompanyLocation);
            tvField           = itemView.findViewById(R.id.tvField);
            tvJobType         = itemView.findViewById(R.id.tvJobType);
            tvSeniority       = itemView.findViewById(R.id.tvSeniority);
            tvTimePosted      = itemView.findViewById(R.id.tvTimePosted);
            tvSalary          = itemView.findViewById(R.id.tvSalary);
        }
    }
}
