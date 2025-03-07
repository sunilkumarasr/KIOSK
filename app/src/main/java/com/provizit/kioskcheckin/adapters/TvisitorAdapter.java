package com.provizit.kioskcheckin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.provizit.kioskcheckin.R;
import com.provizit.kioskcheckin.utilities.TvisitorsList;

import java.util.ArrayList;
import java.util.List;

public class TvisitorAdapter extends ArrayAdapter<TvisitorsList> {
    Context context;
    int resource;
    int textViewResourceId;
    List<TvisitorsList> items;
    List<TvisitorsList> tempItems;
    List<TvisitorsList> suggestions;
    int status;

    public TvisitorAdapter(Context context, int resource, int textViewResourceId, List<TvisitorsList> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
        }
        TvisitorsList people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null) {
                lblName.setText(people.getName());


            }

        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((TvisitorsList) resultValue).getName()  ;
            if (status == 1){
                return "";
            }
            else{
                return str;
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (TvisitorsList people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                suggestions.addAll(tempItems);
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<TvisitorsList> filterList = (ArrayList<TvisitorsList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (TvisitorsList people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
