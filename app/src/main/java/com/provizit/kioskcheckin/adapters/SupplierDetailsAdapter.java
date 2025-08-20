package com.provizit.kioskcheckin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.services.SupplierDetails;
import java.util.List;

public class SupplierDetailsAdapter extends RecyclerView.Adapter<SupplierDetailsAdapter.ViewHolder> {
    private List<SupplierDetails> supplierDetailsList;

    public SupplierDetailsAdapter(List<SupplierDetails> list) {
        this.supplierDetailsList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_supplier_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplierDetails supplierDetails = supplierDetailsList.get(position);
        holder.txtName.setText(supplierDetails.contact_person);
        holder.txtEmail.setText(supplierDetails.supplier_email);
        holder.txtNational.setText(supplierDetails.nationality);
        holder.txtIDNumber.setText(supplierDetails.id_number);
        holder.txtVehicleNumber.setText(supplierDetails.vehicle_no);
        holder.txtVehicleType.setText(supplierDetails.vehicle_type);

    }

    @Override
    public int getItemCount() {
        return supplierDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtNational, txtIDNumber, txtVehicleNumber, txtVehicleType ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtNational = itemView.findViewById(R.id.txtNational);
            txtIDNumber = itemView.findViewById(R.id.txtIDNumber);
            txtVehicleNumber = itemView.findViewById(R.id.txtVehicleNumber);
            txtVehicleType = itemView.findViewById(R.id.txtVehicleType);

        }
    }
}

