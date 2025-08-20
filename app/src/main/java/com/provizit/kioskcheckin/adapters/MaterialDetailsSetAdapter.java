package com.provizit.kioskcheckin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.MaterialDetailsSet;
import java.util.List;

public class MaterialDetailsSetAdapter extends RecyclerView.Adapter<MaterialDetailsSetAdapter.ViewHolder> {
    private List<MaterialDetailsSet> supplierDetailsList;

    public MaterialDetailsSetAdapter(List<MaterialDetailsSet> list) {
        this.supplierDetailsList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meterial_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MaterialDetailsSet materialDetailsSet = supplierDetailsList.get(position);
        holder.txtMaterialName.setText(materialDetailsSet.description);
        holder.txtSerialNumber.setText(materialDetailsSet.serial_num);
        holder.txtQuantity.setText(materialDetailsSet.quantity+"");

    }

    @Override
    public int getItemCount() {
        return supplierDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterialName,txtSerialNumber,txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtSerialNumber = itemView.findViewById(R.id.txtSerialNumber);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

        }
    }

}
