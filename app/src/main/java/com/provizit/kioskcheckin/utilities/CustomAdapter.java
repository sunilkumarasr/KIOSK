package com.provizit.kioskcheckin.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.provizit.kioskcheckin.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Getdocuments> {

    Context context;
    int resource, textViewResourceId;
    List<Getdocuments> items, tempItems, suggestions;

    public CustomAdapter(Context context, int resource, int textViewResourceId, List<Getdocuments> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Getdocuments>(items); // this makes the difference.
        suggestions = new ArrayList<Getdocuments>();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
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
            String str = ((Getdocuments) resultValue).getName()  ;

                return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Getdocuments people : tempItems) {
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
            List<Getdocuments> filterList = (ArrayList<Getdocuments>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Getdocuments people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}