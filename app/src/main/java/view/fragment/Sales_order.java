package view.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import DataBase.GetData;
import constant.Constant;
import models.AutoProductAdapter;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;

public class Sales_order extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Bundle customer_details;

    private ProspectFragment.OnFragmentInteractionListener mListener;

    // Table Layout

    int cust_gid;
    ArrayList<EditText> val;
    int padding_view = 3;
    private TableRow tableRow;
    private AutoCompleteTextView auto_product;
    private int i = 100;
    private TableLayout tableLayout;

    public Sales_order() {

    }

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

        View rootView = inflater.inflate(R.layout.fragment_sales_order, container, false);

        auto_product = rootView.findViewById(R.id.auto_product);
        auto_product.setThreshold(1);
        auto_product.setOnItemClickListener(autoItemClickListener);
        final AutoProductAdapter autoProductAdapter = new AutoProductAdapter(getActivity(), R.layout.item_product);
        auto_product.setAdapter(autoProductAdapter);


        auto_product.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        auto_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(auto_product.getText())) {
                        // makeApiCall(autoCompleteTextView.getText().toString());
                        GetData.productList(auto_product.getText().toString());
                        autoProductAdapter.setData(GetData.productList(auto_product.getText().toString()));
                       // autoProductAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });
        tableLayout = (TableLayout) rootView.findViewById(R.id.tbl_layout);
        load_favProduct();
        return rootView;
    }

    private AdapterView.OnItemClickListener autoItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT);
        }
    };

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

        String URL = Constant.URL + "Product_SalesFav?Customer_gid=" + cust_gid + "&Entity_gid=1";
        CallbackHandler.sendReqest(getActivity(), Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("FOUND")) {

                        JSONArray jsonArray;
                        jsonArray = jsonObject.getJSONArray("DATA");
                        jsonArray = jsonArray;

                        setHeader(Sales_order.this);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);

                            String ProductName = obj_json.getString("product_name");
                            String Date = obj_json.getString("dat");
                            String Qty = obj_json.getString("sodetails_qty");
                            String ProductGid = obj_json.getString("sodetails_product_gid");

//                            ContentValues contentValues = new ContentValues();

                            Generate_Layout(ProductName, Date, Qty, ProductGid, i);// Values will be passed from here

                        }


                    }
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());

                }
            }


            public void onFailure(String result) {
                //pd.hide();
                Log.e("Login", result);

            }

            @Override
            public List<Variables.Product> onAutoComplete(String result) {
                return null;
            }


        });
    }

    private void Generate_Layout(String Name, String Date, String Qty, String ProductGid, int i) {
        //tableLayout = tableLayout.findViewById(R.id.tbl_layout);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        val = new ArrayList<>();

        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        tableRow.setLayoutParams(lp);
        tableRow.setBackgroundColor(Color.WHITE);

        TextView sNo = new TextView(getActivity());

        sNo.setPadding(padding_view, padding_view, padding_view, padding_view);
        sNo.setBackground(getResources().getDrawable(R.drawable.border));
        sNo.setLayoutParams(lp);
        sNo.setText("1");
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
        productName.setPadding(padding_view, padding_view, padding_view, padding_view);
        productName.setBackground(getResources().getDrawable(R.drawable.border));
        productName.setLayoutParams(lp);
        productName.setFocusable(false);

        TextView date = new TextView(getActivity());
        date.setText(Date);
        date.setPadding(padding_view, padding_view, padding_view, padding_view);
        date.setBackground(getResources().getDrawable(R.drawable.border));
        date.setLayoutParams(lp);


        TextView qty = new TextView(getActivity());
        qty.setText(Qty);
        qty.setPadding(padding_view, padding_view, padding_view, padding_view);
        qty.setBackground(getResources().getDrawable(R.drawable.border));
        qty.setLayoutParams(lp);

        EditText orderQty = new EditText(getActivity());
        orderQty.setTag(ProductGid);
        orderQty.setPadding(padding_view, padding_view, padding_view, padding_view);
        orderQty.setText(String.valueOf(i));
        orderQty.setBackground(getResources().getDrawable(R.drawable.border));
        orderQty.setLayoutParams(lp);
        orderQty.setGravity(Gravity.CENTER);
        orderQty.setFocusable(true);
        val.add(orderQty);

        tableRow.addView(sNo);
        tableRow.addView(productName);
        tableRow.addView(date);
        tableRow.addView(qty);
        tableRow.addView(orderQty);

        tableLayout.addView(tableRow, 1);

        i = i - 1;


    }

    private void setHeader(Sales_order tableView) {


        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);

        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(lp);
        tableRow.setBackgroundColor(Color.parseColor("#3a79d8"));

        ShapeDrawable border = new ShapeDrawable(new RectShape());
        border.getPaint().setStyle(Paint.Style.STROKE);

        border.setPadding(padding_view, padding_view, padding_view, padding_view);

        TextView sNo = new TextView(getActivity());
        sNo.setText("S.No");
        sNo.setPadding(padding_view, padding_view, padding_view, padding_view);
        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);
        sNo.setGravity(View.TEXT_ALIGNMENT_CENTER);
        sNo.setLayoutParams(lp);


        TextView productName = new TextView(getActivity());
        productName.setText("ProductName");
        productName.setPadding(padding_view, padding_view, padding_view, padding_view);
        productName.setTextColor(0xFFFFFFFF);
        productName.setTextSize(15);
        productName.setGravity(Gravity.CENTER);
        productName.setLayoutParams(lp);

        TextView date = new TextView(getActivity());
        date.setText("Date");
        date.setPadding(padding_view, padding_view, padding_view, padding_view);
        date.setGravity(Gravity.CENTER);

        date.setTextColor(0xFFFFFFFF);
        date.setTextSize(15);
        date.setLayoutParams(lp);

        TextView qty = new TextView(getActivity());
        qty.setText("Qty");
        qty.setPadding(padding_view, padding_view, padding_view, padding_view);
        qty.setGravity(Gravity.CENTER);
        qty.setTextColor(0xFFFFFFFF);
        qty.setTextSize(15);
        qty.setLayoutParams(lp);

        TextView orderQty = new TextView(getActivity());

        orderQty.setPadding(padding_view, padding_view, padding_view, padding_view);
        orderQty.setText("Order Qty");
        orderQty.setTextColor(0xFFFFFFFF);
        orderQty.setTextSize(15);
        orderQty.setGravity(Gravity.CENTER);
        orderQty.setLayoutParams(lp);


        tableRow.addView(sNo);
        tableRow.addView(productName);
        tableRow.addView(date);
        tableRow.addView(qty);
        tableRow.addView(orderQty);

        tableLayout.addView(tableRow, 0);

//        i = i - 1;


    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}