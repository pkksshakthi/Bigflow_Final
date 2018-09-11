package view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Sales_order extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProspectFragment.OnFragmentInteractionListener mListener;

    // Table Layout

    ArrayList<EditText> val;
    int padding_view = 3;
    private TableRow tableRow;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_sales_order,container,false);
        tableLayout = (TableLayout) rootView.findViewById(R.id.tbl_layout);
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

    private void load_favProduct() {
//        Volley Success Result
        final String sample_jsondata = "{'MESSAGE':'FOUND',\n" +
                "'DATA':[{'sodetails_product_gid':7,'product_name':'GN 010 TX - 950MA','product_displayname':'10 TX','sodetails_qty':'24','fav':'Y','dat':'2016-01-02','quantity':''},\n" +
                "{'sodetails_product_gid':14,'product_name':'GN 040 WX - 8500MA','product_displayname':'40 WX','sodetails_qty':'25','fav':'Y','dat':'2016-02-08','quantity':''},\n" +
                "{'sodetails_product_gid':15,'product_name':'GN 040 ZX- 8500MA','product_displayname':'40 ZX','sodetails_qty':'26','fav':'Y','dat':'2016-02-08','quantity':''},\n" +
                "{'sodetails_product_gid':18,'product_name':'GN 050 ZX - 10500MA','product_displayname':'50 ZX','sodetails_qty':'15','fav':'Y','dat':'2016-04-13','quantity':''}]}";
        try {
            JSONObject jsonObject = new JSONObject(sample_jsondata);
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
//
                    Generate_Layout(ProductName,Date,Qty,ProductGid);// Values will be passed from here

                }


            }
        } catch (JSONException e) {
            Log.e("Login", e.getMessage());

        }

    }

    private void Generate_Layout(String Name,String Date,String Qty,String ProductGid){
        //tableLayout = tableLayout.findViewById(R.id.tbl_layout);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        val = new ArrayList<>();

        tableRow = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        tableRow.setLayoutParams(lp);
        tableRow.setBackgroundColor(Color.WHITE);

        TextView sNo = new TextView(getActivity());
        sNo.setText("");
        sNo.setPadding(padding_view, padding_view, padding_view, padding_view);
        sNo.setBackground(getResources().getDrawable(R.drawable.border));
        sNo.setLayoutParams(lp);


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

        sNo.setLayoutParams(lp);


        TextView productName = new TextView(getActivity());
        productName.setText("ProductName");
        productName.setPadding(padding_view, padding_view, padding_view, padding_view);
        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);

        productName.setLayoutParams(lp);

        TextView date = new TextView(getActivity());
        date.setText("Date");
        date.setPadding(padding_view, padding_view, padding_view, padding_view);

        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);
        date.setLayoutParams(lp);

        TextView qty = new TextView(getActivity());
        qty.setText("Qty");
        qty.setPadding(padding_view, padding_view, padding_view, padding_view);

        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);
        qty.setLayoutParams(lp);

        TextView orderQty = new TextView(getActivity());

        orderQty.setPadding(padding_view, padding_view, padding_view, padding_view);
        orderQty.setText("Order Qty");
        sNo.setTextColor(0xFFFFFFFF);
        sNo.setTextSize(15);

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