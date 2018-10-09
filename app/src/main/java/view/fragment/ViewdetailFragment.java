package view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import constant.Constant;
import models.Common;
import models.UserDetails;
import network.CallbackHandler;
import presenter.VolleyCallback;
import view.activity.ServiceActivity;

public class ViewdetailFragment extends Fragment implements View.OnClickListener {

    private TableLayout tableLayout_sales, tableLayout_Collection, tableLayout_Outstanding;
    private TableRow tableRow;
    private int customer_gid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewdetails, container, false);

        tableLayout_sales = rootView.findViewById(R.id.tbl_layout_Sales);
        String[] header_sales = {"S.No", "Invoice No", "Invoice Date", "Invoice Amount"};
        setHeader(header_sales, tableLayout_sales);
        tableLayout_Collection = rootView.findViewById(R.id.tbl_layout_Collection);
        String[] header_Collection = {"S.No", "Branch Name", "Collected Amount", "Date", "Type", "Status"};
        setHeader(header_Collection, tableLayout_Collection);
        tableLayout_Outstanding = rootView.findViewById(R.id.tbl_layout_Outstanding);
        String[] header_Outstanding = {"S.No", "Invoice No", "Net Amount", "Pending Amount"};
        setHeader(header_Outstanding, tableLayout_Outstanding);
        load_Details();
        return rootView;

    }


    private void setHeader(String[] value, TableLayout layout) {

        layout.setStretchAllColumns(true);
        layout.setShrinkAllColumns(true);
        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT);//(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);

        String header_value[] = value;

        for (int i = 0; i < header_value.length; i++) {
            TextView sNo = new TextView(getActivity());
            sNo.setText(header_value[i]);
            sNo.setTextColor(0xFFFFFFFF);
            sNo.setTextSize(15);
            sNo.setGravity(Gravity.CENTER);
            sNo.setLayoutParams(lp);
            sNo.setBackgroundResource(R.drawable.table_header);
            tableRow.addView(sNo);
        }

        layout.addView(tableRow, 0);

    }


    private void setDetail(String[] value, TableLayout layout) {

        layout.setStretchAllColumns(true);
        layout.setShrinkAllColumns(true);
        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(100, TableRow.LayoutParams.MATCH_PARENT);//(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);
        tableRow.setBackgroundColor(Color.WHITE);
        String detail_value[] = value;
        int index = 0;

        for (int i = 0; i < detail_value.length; i++) {
            TextView dtl = new TextView(getActivity());
            dtl.setText(detail_value[i]);
            index = Integer.parseInt(detail_value[0]);

            dtl.setGravity(Gravity.CENTER);

            dtl.setBackground(getResources().getDrawable(R.drawable.table_body));
            dtl.setLayoutParams(lp);
            tableRow.addView(dtl);
        }

        layout.addView(tableRow, index);

    }

    public void load_Details() {
        Bundle bundle = this.getArguments();


        JSONObject Full_Json = new JSONObject();
        JSONObject params_Json = new JSONObject();
        try {
            params_Json.put(Constant.Action, "Common");
            params_Json.put("customer_gid", customer_gid);
            params_Json.put(Constant.emp_gid, Integer.parseInt(UserDetails.getUser_id()));
            params_Json.put("Outstanding_Group", "outstandingcustomer");
            Full_Json.put(Constant.params, params_Json);

        } catch (JSONException e) {
            Log.e("details", e.getMessage());
        }

        String URL = Constant.URL + "Customerview_get?Limit=5&Entity_gid=1";
        CallbackHandler.sendReqest(getActivity(), Request.Method.POST, Full_Json.toString(), URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    ;
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("FOUND")) {

                        JSONArray jsonoutstanding, jsonsales, jsoncollection;
                        jsonsales = jsonObject.getJSONArray("sales_history");
                        jsoncollection = jsonObject.getJSONArray("collection_history");
                        jsonoutstanding = jsonObject.getJSONArray("outstanding_detail");

                        for (int i = 0; i < jsonsales.length(); i++) {
                            JSONObject obj_json = jsonsales.getJSONObject(i);
                            String invoice_no = obj_json.getString("invoiceheader_no");
                            String lsDate = obj_json.getString("invoiceheader_date");
                            String invoice_amt = obj_json.getString("soheader_total");
                            //    Date Date = Common.convertDate(lsDate, lsDate);

                            String detail_value[] = {Integer.toString(i + 1), invoice_no, lsDate, invoice_amt};

                            setDetail(detail_value, tableLayout_sales);// Values will be passed from here
                        }
                        for (int i = 0; i < 5; i++) {
                            JSONObject obj_json = jsoncollection.getJSONObject(i);
                            String branch_name = obj_json.getString("branch_name");
                            String collection_amount = obj_json.getString("collection_amount");
                            String collection_date = obj_json.getString("collection_date");
                            String collection_mode = obj_json.getString("collection_mode");
                            String bankdeposit_status = obj_json.getString("bankdeposit_status");

                            String detail_value[] = {Integer.toString(i + 1), branch_name, collection_amount, collection_date, collection_mode, bankdeposit_status};
                            setDetail(detail_value, tableLayout_Collection);// Values will be passed from here
                        }

                        for (int i = 0; i < jsonoutstanding.length(); i++) {
                            JSONObject obj_json = jsonoutstanding.getJSONObject(i);
                            String invoiceno = obj_json.getString("soutstanding_invoiceno");
                            String netamount = obj_json.getString("soutstanding_netamount");
                            String pending = obj_json.getString("pending");
                            String detail_value[] = {Integer.toString(i + 1), invoiceno, netamount, pending};
                            setDetail(detail_value, tableLayout_Outstanding);// Values will be passed from here
                        }


                    } else {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("details", e.getMessage());
                }
            }

            @Override
            public void onFailure(String result) {
                Log.e("details", result);

            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle customer_details = this.getArguments();
        ;
        customer_gid = customer_details.getInt("customer_id", 0);

    }
}
