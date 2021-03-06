package models;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vsolv.bigflow.R;

import java.util.List;

public class ServiceSummryAdapter extends RecyclerView.Adapter<ServiceSummryAdapter.CustomerViewHolder> {
    private Context mCtx;
    private List<Variables.ServiceSummary_List> customerList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Variables.ServiceSummary_List item,int position);
        void onViewDetailsClick(Variables.ServiceSummary_List item, int position);
    }

    public void setOnclickListener(OnItemClickListener Listener) {
        mListener = Listener;
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, locationName, txtCustViewDetails;
        private View view;
        public CustomerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            view =itemView;
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

    public ServiceSummryAdapter(Context mCtx, List<Variables.ServiceSummary_List> productList) {
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
    public void onBindViewHolder(@NonNull final CustomerViewHolder holder, int position) {

        final Variables.ServiceSummary_List customer = customerList.get(position);

        holder.customerName.setText(customer.getText());
        holder.locationName.setText(customer.getText1());
        holder.txtCustViewDetails.setText("View Details");
        holder.view.setBackgroundColor(customer.isSelected() ? Color.CYAN : Color.WHITE);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customer.setSelected(!customer.isSelected());
                holder.view.setBackgroundColor(customer.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void updateList(List<Variables.ServiceSummary_List> list){
        customerList = list;
        notifyDataSetChanged();
    }
    public Variables.ServiceSummary_List getitem(int position){
        return customerList.get(position);
    }
}
