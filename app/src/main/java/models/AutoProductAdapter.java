package models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vsolv.bigflow.R;

import java.util.List;

import DataBase.GetData;

public class AutoProductAdapter extends ArrayAdapter implements Filterable {

    private final Context _Context;
    private final int _resource;
    List<Variables.Product> productList;

    public AutoProductAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this._Context = context;
        this._resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(_Context);
        View view = inflater.inflate(_resource, null);
        TextView Product_name = view.findViewById(R.id.txtproduct_auto);
        Variables.Product product = productList.get(position);
        Product_name.setText(product.product_name);
        return view;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        Log.v("productList.size()", "" + productList.size());
        return productList.size();
    }

    @Override
    public Variables.Product getItem(int position) {
        return productList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = productList;
                    filterResults.count = productList.size();
                }


                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    public void setData(List<Variables.Product> products) {
        productList = products;
        productList.addAll(products);
    }
}
