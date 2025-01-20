package com.provizit.kioskcheckin.utilities.EntryPermit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.provizit.kioskcheckin.R;
import java.util.ArrayList;

public class MaterialDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<MaterialDetails> items;
    private final Context context;

    public MaterialDetailsAdapter(Context context, ArrayList<MaterialDetails> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meterial_list_items, parent, false);
        return new ViewHolderToday(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MaterialDetailsAdapter.ViewHolderToday Holder = (MaterialDetailsAdapter.ViewHolderToday) holder;

        Holder.txtMaterialName.setText(items.get(position).getDescription());
        Holder.txtSerialNumber.setText(items.get(position).getSerial_num());
        Holder.txtQuantity.setText(items.get(position).getQuantity()+"");

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolderToday extends RecyclerView.ViewHolder {


        TextView txtMaterialName,txtSerialNumber,txtQuantity;

        public ViewHolderToday(@NonNull View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtSerialNumber = itemView.findViewById(R.id.txtSerialNumber);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);

        }
    }

}
