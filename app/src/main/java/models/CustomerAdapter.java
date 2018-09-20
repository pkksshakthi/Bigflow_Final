package models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsolv.bigflow.R;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context mCtx;
    private List<Variables.Customer> customerList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Variables.Customer item,int position);
        void onViewDetailsClick(Variables.Customer item, int position);
    }

    public void setOnclickListener(OnItemClickListener Listener) {
        mListener = Listener;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, locationName, txtCustViewDetails;

        public CustomerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            customerName = itemView.findViewById(R.id.txtCustomerName);
            locationName = itemView.findViewById(R.id.txtLocationName);
            txtCustViewDetails = itemView.findViewById(R.id.txtCustViewDetails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position =getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(getitem(position),position);
                        }
                    }

                }
            });
            txtCustViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position =getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            listener.onViewDetailsClick(getitem(position),position);
                        }
                    }
                }
            });
        }
    }

    public CustomerAdapter(Context mCtx, List<Variables.Customer> productList) {
        this.mCtx = mCtx;
        this.customerList = productList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_customer, null);
        return new CustomerViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {

        Variables.Customer customer = customerList.get(position);

        holder.customerName.setText(customer.getCust_name());
        holder.locationName.setText(customer.getCust_location());
        holder.txtCustViewDetails.setText("View Details");
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void updateList(List<Variables.Customer> list){
        customerList = list;
        notifyDataSetChanged();
    }
    public Variables.Customer getitem(int position){
        return customerList.get(position);
    }
}
