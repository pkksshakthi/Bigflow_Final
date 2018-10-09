package view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import constant.Constant;
import models.AutoProductAdapter;
import models.Common;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;

public class Sales_order extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public Bundle customer_details;
    public Button btnsubmit_order;
    public ArrayList<EditText> val;
    int cust_gid;
    // TODO: Rename and change types of parameters
    private String mParam1;
    // Table Layout
    private String mParam2;
    private ProspectFragment.OnFragmentInteractionListener mListener;
    private TableRow tableRow;
    private AutoCompleteTextView auto_product;
    private int i = 100;
    private TableLayout tableLayout;

    private AutoProductAdapter autoProductAdapter;
    private ArrayList<Integer> favProduct;
    private AdapterView.OnItemClickListener autoItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Variables.Product product = autoProductAdapter.getSelectedItem(position);
            Generate_Layout(product.product_name, Common.convertDateString(new Date(), "dd-MMM-yyyy"), "0", product.product_id, i);
            auto_product.setText("");
        }
    };
    private ProgressDialog progressDialog;

    public static ProspectFragment newInstance(String param1, String param2) {
        ProspectFragment fragment = new ProspectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        customer_details = getActivity().getIntent().getExtras();
        if (getActivity().getIntent() != null) {
            cust_gid = customer_details.getInt("customer_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        favProduct = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_sales_order, container, false);
        btnsubmit_order = rootView.findViewById(R.id.btnsubmit_order);
        btnsubmit_order.setOnClickListener(this);
        btnsubmit_order.setEnabled(false);

        auto_product = rootView.findViewById(R.id.auto_product);
        auto_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                autoProductAdapter.productfilter(s.toString(), favProduct);
            }
        });
        auto_product.setThreshold(1);
        auto_product.setOnItemClickListener(autoItemClickListener);
        autoProductAdapter = new AutoProductAdapter(getActivity(), R.layout.item_product);
        auto_product.setAdapter(autoProductAdapter);
        tableLayout = rootView.findViewById(R.id.tbl_layout);
        load_favProduct();
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void load_favProduct() {
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String URL = Constant.URL + "Product_SalesFav?Customer_gid=" + cust_gid + "&Entity_gid=1";
        CallbackHandler.sendReqest(getActivity(), Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    val = new ArrayList<>();
                    setHeader();
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    if (status.equals("FOUND")) {

                        JSONArray jsonArray;
                        jsonArray = jsonObject.getJSONArray("DATA");
                        jsonArray = jsonArray;
//                        val = new ArrayList<>();
//                        setHeader(Sales_order.this);


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            String ProductName = obj_json.getString("product_displayname");
                            String lsDate = obj_json.getString("dat");
                            String Qty = obj_json.getString("sodetails_qty");
                            int ProductGid = obj_json.getInt("sodetails_product_gid");
                            String Date = Common.convertDateString(Common.convertDate(lsDate,"yyyy-MM-dd"),
                                    "dd-MMM-yyyy");

                            Generate_Layout(ProductName, Date, Qty, ProductGid, i);// Values will be passed from here
                        }
                        btnsubmit_order.setEnabled(true);
                    }
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());
                    if (progressDialog!=null){
                        progressDialog.dismiss();
                    }
                }
            }


            public void onFailure(String result) {
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                Log.e("Login", result);

            }


        });
    }

    private void Generate_Layout(String Name, String Date, String Qty, int ProductGid, int i) {
        try {
            favProduct.add(Integer.valueOf(ProductGid));
            tableLayout.setStretchAllColumns(true);
            tableLayout.setShrinkAllColumns(true);

            tableRow = new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

            tableRow.setLayoutParams(lp);
            tableRow.setBackgroundColor(Color.WHITE);

            TextView sNo = new TextView(getActivity());

            sNo.setBackground(getResources().getDrawable(R.drawable.table_body));
            sNo.setLayoutParams(lp);
            sNo.setText("1");
            sNo.setGravity(Gravity.CENTER);
            int total = tableLayout.getChildCount();
            for (int j = 1; j <= total; j++) {
                View view = tableLayout.getChildAt(j);
                if (view instanceof TableRow) {
                    TableRow row = (TableRow) view;
                    TextView t = (TextView) row.getChildAt(0);
                    t.setText("" + (j + 1));
                }
            }


            TextView productName = new TextView(getActivity());
            productName.setText(Name);
            productName.setBackground(getResources().getDrawable(R.drawable.table_body));
            productName.setLayoutParams(lp);
            productName.setFocusable(false);

            TextView date = new TextView(getActivity());
            date.setText(Date);
            date.setBackground(getResources().getDrawable(R.drawable.table_body));
            date.setLayoutParams(lp);


            final TextView qty = new TextView(getActivity());
            qty.setText(Qty);
            qty.setBackground(getResources().getDrawable(R.drawable.table_body));
            qty.setLayoutParams(lp);
            qty.setGravity(Gravity.CENTER);


            EditText orderQty = new EditText(getActivity());
            orderQty.setTag(ProductGid);
            orderQty.setBackground(getResources().getDrawable(R.drawable.table_body));
            orderQty.setLayoutParams(lp);
            orderQty.setHint("Qty");
            orderQty.setGravity(Gravity.CENTER);
            orderQty.setFocusable(true);
            orderQty.setInputType(InputType.TYPE_CLASS_NUMBER);

            val.add(orderQty);

            tableRow.addView(sNo);
            tableRow.addView(productName);
            tableRow.addView(date);
            tableRow.addView(qty);
            tableRow.addView(orderQty);
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.hideKeyboard(getActivity());
                    TableRow row = (TableRow) v;
                    TextView qty = (TextView) ((TableRow) v).getChildAt(3);
                    EditText re = (EditText) row.getChildAt(4);
                    re.setText(qty.getText().toString());
                }
            });
            tableLayout.addView(tableRow, 1);

            i = i - 1;
        } catch (Exception e) {
            String Log = e.getMessage();
        }


    }

    private void setHeader() {

        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);


        TextView sNo = new TextView(getActivity());
        sNo.setText("S.No");
        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);
        sNo.setGravity(Gravity.CENTER);
        sNo.setLayoutParams(lp);
        sNo.setBackgroundResource(R.drawable.table_header);


        TextView productName = new TextView(getActivity());
        productName.setText("Product Name");
        productName.setTextColor(0xFFFFFFFF);
        productName.setTextSize(15);
        productName.setGravity(Gravity.CENTER);
        productName.setLayoutParams(lp);
        productName.setBackgroundResource(R.drawable.table_header);

        TextView date = new TextView(getActivity());
        date.setText("Date");
        date.setGravity(Gravity.CENTER);
        date.setTextColor(0xFFFFFFFF);
        date.setTextSize(15);
        date.setLayoutParams(lp);
        date.setBackgroundResource(R.drawable.table_header);

        TextView qty = new TextView(getActivity());
        qty.setText("Qty");
        qty.setGravity(Gravity.CENTER);
        qty.setTextColor(0xFFFFFFFF);
        qty.setTextSize(15);
        qty.setLayoutParams(lp);
        qty.setBackgroundResource(R.drawable.table_header);

        TextView orderQty = new TextView(getActivity());
        orderQty.setText("Order Qty");
        orderQty.setTextColor(0xFFFFFFFF);
        orderQty.setTextSize(15);
        orderQty.setGravity(Gravity.CENTER);
        orderQty.setLayoutParams(lp);
        orderQty.setBackgroundResource(R.drawable.table_header);


        tableRow.addView(sNo);
        tableRow.addView(productName);
        tableRow.addView(date);
        tableRow.addView(qty);
        tableRow.addView(orderQty);

        tableLayout.addView(tableRow, 0);


    }


    public void onClick(View view) {

        if (view == btnsubmit_order) {
            try {
                if (val.size() > 0) {

                    final JSONArray soDetails = new JSONArray();
                    for (i = 0; i < val.size(); i++) {
                        String pdct_gid = val.get(i).getTag().toString().trim();
                        String order_qty = val.get(i).getText().toString().trim();

                        if (pdct_gid.trim().length() > 0 && order_qty.trim().length() > 0 && pdct_gid != null && order_qty != null
                                && Float.parseFloat(order_qty.trim()) > 0) {

                            JSONObject objSoDetails = new JSONObject();

                            objSoDetails.put("sodetails_product_gid", val.get(i).getTag().toString());
                            objSoDetails.put("quantity", val.get(i).getText().toString());
                            soDetails.put(objSoDetails);
                        }
                    }

                    if (soDetails.length() > 0) {
                            // Get Confirm from User
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Confirm");
                        builder.setMessage("Are You Sure To Submit?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {
                                    JSONObject Full_Json = new JSONObject();
                                    JSONObject params_Json = new JSONObject();
                                    params_Json.put(Constant.emp_gid, Integer.parseInt(UserDetails.getUser_id()));
                                    params_Json.put(Constant.soheader_gid, 0);
                                    params_Json.put(Constant.customer_gid, cust_gid);
                                    params_Json.put(Constant.Action, "Insert");
                                    params_Json.put(Constant.Data, new JSONObject().put(Constant.sodetails, soDetails));
                                    Full_Json.put(Constant.params, params_Json);
//                                    Log.v("JSONPON", Full_Json.toString());

                                    if (Full_Json.length() > 0) {
                                        String OutMessage = SalesOrderSet(Full_Json);
                                    }
                                } catch (JSONException e) {
                                    Log.e("Sales-Json", e.getMessage());
                                }


                            }

                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    } else {
                        Toast.makeText(getActivity(), "No Data To Save.", Toast.LENGTH_LONG).show();
                    }


                }
            } catch (Exception e) {
                Log.e("Sales", e.getMessage());
            }
        }

    }


    public String SalesOrderSet(JSONObject jsonObject) {

        // To Set in Main Server-SALES.
        String URL = Constant.URL + "FET_SalesOrder?Emp_gid=" + UserDetails.getUser_id() +
                "&Entity_gid=1&Date=" + Common.convertDateString(new Date(), "dd-MMM-yyyy");

        CallbackHandler.sendReqest(getActivity(), Request.Method.POST, jsonObject.toString(), URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("SUCCESS")) {
                        Toast.makeText(getActivity(), "Sales Saved Successfully.", Toast.LENGTH_LONG).show();

                        getActivity().finish();


                    } else {
                        Toast.makeText(getActivity(), "Sales Not Saved Successfully.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("Sales", e.getMessage());
                }
            }

            @Override
            public void onFailure(String result) {
                Log.e("Location", result);

            }
        });

        return "";
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}

