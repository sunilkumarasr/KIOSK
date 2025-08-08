package com.provizit.kioskcheckin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.Contractor;
import com.provizit.kioskcheckin.services.SubContractor;

import java.util.List;

public class ContractorAdapter extends RecyclerView.Adapter<ContractorAdapter.ViewHolder> {
    private List<Contractor> subContractorList;

    public ContractorAdapter(List<Contractor> list) {
        this.subContractorList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contractor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contractor contractor = subContractorList.get(position);
        holder.txtName.setText(contractor.name);
        holder.txtEmail.setText(contractor.email);
        holder.txtMobile.setText(contractor.mobile);
        holder.txtCompany.setText(contractor.companyName);
        holder.txtIDNumber.setText(contractor.id_number);
        holder.txtSelectId.setText(contractor.id_name);
        holder.txtNational.setText(contractor.nationality);
    }

    @Override
    public int getItemCount() {
        return subContractorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtMobile, txtCompany, txtIDNumber, txtSelectId, txtNational;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtCompany = itemView.findViewById(R.id.txtCompany);
            txtIDNumber = itemView.findViewById(R.id.txtIDNumber);
            txtSelectId = itemView.findViewById(R.id.txtSelectId);
            txtNational = itemView.findViewById(R.id.txtNational);
        }
    }
}

