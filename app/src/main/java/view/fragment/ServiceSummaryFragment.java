package view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import constant.Constant;
import models.Common;
import models.CustomerAdapter;
import models.ServiceSummryAdapter;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;

public class ServiceSummaryFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_Title = "title";
    private static final String ARG_PARAM2 = "param2";
    public ServiceSummryAdapter adapter;
    private String mParam1, mParam2;
    private View fragmentView;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private TextView empty_view, reload;
    private Bundle sessiondata;
    private ProgressDialog progressDialog;
    private FloatingActionButton Courier;
    private ArrayList<Variables.ServiceSummary_List> customerList;

    public static ServiceSummaryFragment newInstance(String Title, String param2) {
        ServiceSummaryFragment fragment = new ServiceSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_Title, Title);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_Title);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Bigflow");
        View view = inflater.inflate(R.layout.fragment_servicesummary, container, false);
        fragmentView = view;
        loadView(view);
        initializeView();
        loadData();
        return view;
    }

    private void loadView(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearDirect);
        recyclerView = (RecyclerView) view.findViewById(R.id.ServiceRecyclerView);
        empty_view = view.findViewById(R.id.empty_view);
        reload = view.findViewById(R.id.custReload);
        Courier = view.findViewById(R.id.fab);

    }

    private void initializeView() {

        sessiondata = new Bundle();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        customerList = new ArrayList<Variables.ServiceSummary_List>();
        reload.setOnClickListener(this);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        Courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This Module implement into new version.", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadData() {
        if (!Common.isOnline(getContext())) {
            Snackbar.with(getActivity(), null)
                    .type(Type.WARNING)
                    .message("Please Check Internet Connection.")
                    .duration(Duration.SHORT)
                    .fillParent(true)
                    .textAlign(Align.LEFT)
                    .show();
            setVisibility(View.GONE, View.GONE, View.VISIBLE);
            return;
        }

        JSONObject Json = new JSONObject();
        JSONObject params_Json = new JSONObject();
        try {
            params_Json.put("from_date", "");
            params_Json.put("to_date", "");
            params_Json.put("customer_gid", 0);
            params_Json.put("product_gid", 0);
            params_Json.put("service_gid", 0);
            Json.put("params", params_Json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        String URL = Constant.URL + "Service_SummaryGetAPI?Emp_gid=" + UserDetails.getUser_id();
//
//        URL += "&Entity_gid=" + UserDetails.getEntity_gid();
//
//        CallbackHandler.sendReqest(getActivity(), Request.Method.POST, Json.toString(), URL, new VolleyCallback() {
//
//
//            @Override
//            public void onSuccess(String result) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    String message = jsonObject.getString("MESSAGE");
//
//                    if (message.equals("FOUND")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject obj_json = jsonArray.getJSONObject(i);
//                            String display_name = obj_json.getString("customer_name");
//                            String location_name = obj_json.getString("product_name");
//                            int customer_gid = obj_json.getInt("customer_gid");
//                            customerList.add(new Variables.ServiceSummary_List(display_name,display_name));
//                        }
//
//                        setAdapter();
//                    } else {
//                        empty_view.setText(getResources().getString(R.string.error_loading));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(String result) {
//
//                Log.e("DirectSchecule", result);
//            }
//
//        });


        String jsonObject = "{\n    \"MESSAGE\": \"FOUND\",\n    \"DATA\": [\n        {\n            \"service_gid\": 31,\n            \"service_code\": \"SER18042400031\",\n            \"service_date\": 1524528000000,\n            \"service_status\": \"SERVICE CLOSED\",\n            \"service_dispatchmode\": \"DIRECT\",\n            \"service_courierexp\": \"C\",\n            \"service_customer_gid\": 2,\n            \"service_product_gid\": 3,\n            \"service_productslno\": \"1\",\n            \"service_invoiceno\": null,\n            \"service_remarks\": \"1\",\n            \"service_dispatchgid\": 42,\n            \"service_codispatchgid\": 48,\n            \"service_dispatchgidBO\": null,\n            \"customer_name\": \"A.T.C ENTERPRISES - Gandhi Bhawan (Hyderabad)\",\n            \"courier_name\": \"S.T\",\n            \"courier_gid\": 1,\n            \"product_name\": \"GN 005 LX - 950MA\",\n            \"dispatch_mode\": \"1\",\n            \"producttype_gid\": 1,\n            \"producttype_name\": \"ELECTRONIC AUTOMATIC VOLTAGE REGULATOR & STABILISER\",\n            \"employee_gid\": 1,\n            \"employee_name\": \"PONSIVAKUMAR\",\n            \"dispatch_awbno\": \"1\"\n        },\n        {\n            \"service_gid\": 33,\n            \"service_code\": \"SER18042400033\",\n            \"service_date\": 1524528000000,\n            \"service_status\": \"SERVICE CLOSED\",\n            \"service_dispatchmode\": \"DIRECT\",\n            \"service_courierexp\": \"C\",\n            \"service_customer_gid\": 2,\n            \"service_product_gid\": 3,\n            \"service_productslno\": \"1\",\n            \"service_invoiceno\": null,\n            \"service_remarks\": \"1\",\n            \"service_dispatchgid\": 65,\n            \"service_codispatchgid\": 67,\n            \"service_dispatchgidBO\": null,\n            \"customer_name\": \"A.T.C ENTERPRISES - Gandhi Bhawan (Hyderabad)\",\n            \"courier_name\": \"S.T\",\n            \"courier_gid\": 1,\n            \"product_name\": \"GN 005 LX - 950MA\",\n            \"dispatch_mode\": \"1\",\n            \"producttype_gid\": 1,\n            \"producttype_name\": \"ELECTRONIC AUTOMATIC VOLTAGE REGULATOR & STABILISER\",\n            \"employee_gid\": 1,\n            \"employee_name\": \"PONSIVAKUMAR\",\n            \"dispatch_awbno\": \"1\"\n        },\n        {\n            \"service_gid\": 41,\n            \"service_code\": \"SER18042600041\",\n            \"service_date\": 1524700800000,\n            \"service_status\": \"SERVICE CLOSED\",\n            \"service_dispatchmode\": \"DIRECT\",\n            \"service_courierexp\": \"C\",\n            \"service_customer_gid\": 2,\n            \"service_product_gid\": 153,\n            \"service_productslno\": \"1\",\n            \"service_invoiceno\": null,\n            \"service_remarks\": \"1\",\n            \"service_dispatchgid\": 65,\n            \"service_codispatchgid\": 66,\n            \"service_dispatchgidBO\": null,\n            \"customer_name\": \"A.T.C ENTERPRISES - Gandhi Bhawan (Hyderabad)\",\n            \"courier_name\": \"S.T\",\n            \"courier_gid\": 1,\n            \"product_name\": \"TOP LOAD ( LG )\",\n            \"dispatch_mode\": \"1\",\n            \"producttype_gid\": 2,\n            \"producttype_name\": \"WASHING MACHINE COVER\",\n            \"employee_gid\": 1,\n            \"employee_name\": \"PONSIVAKUMAR\",\n            \"dispatch_awbno\": \"1\"\n        },\n        {\n            \"service_gid\": 79,\n            \"service_code\": \"SER18052300079\",\n            \"service_date\": 1527033600000,\n            \"service_status\": \"INITIATED\",\n            \"service_dispatchmode\": \"EXECUTIVE\",\n            \"service_courierexp\": \"C\",\n            \"service_customer_gid\": 2,\n            \"service_product_gid\": 152,\n            \"service_productslno\": \"1\",\n            \"service_invoiceno\": null,\n            \"service_remarks\": \"a\",\n            \"service_dispatchgid\": null,\n            \"service_codispatchgid\": null,\n            \"service_dispatchgidBO\": null,\n            \"customer_name\": \"A.T.C ENTERPRISES - Gandhi Bhawan (Hyderabad)\",\n            \"courier_name\": null,\n            \"courier_gid\": null,\n            \"product_name\": \"FRONT LOAD ( SMALL )\",\n            \"dispatch_mode\": null,\n            \"producttype_gid\": 2,\n            \"producttype_name\": \"WASHING MACHINE COVER\",\n            \"employee_gid\": 1,\n            \"employee_name\": \"PONSIVAKUMAR\",\n            \"dispatch_awbno\": null\n        }\n    ]\n}";
        try {
            JSONObject jsonObj = new JSONObject(jsonObject);
            String status = (String) jsonObj.get("MESSAGE");
            if (status.equals("FOUND")) {

                JSONArray json;
                json = jsonObj.getJSONArray("DATA");
                for (int i = 0; i < json.length(); i++) {

                    JSONObject obj_json = json.getJSONObject(i);
                    String display_name = obj_json.getString("customer_name");
                    customerList.add(new Variables.ServiceSummary_List(display_name, display_name));
                }
                setAdapter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setAdapter() {

        adapter = new ServiceSummryAdapter(getActivity(), customerList);
        recyclerView.setAdapter(adapter);


        adapter.setOnclickListener(new ServiceSummryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Variables.ServiceSummary_List item, int position) {


            }

            @Override
            public void onViewDetailsClick(Variables.ServiceSummary_List item, int position) {

            }
        });

        if (adapter.getItemCount() == 0) {
            empty_view.setText("" + getActivity().getResources().getString(R.string.loading));
            setVisibility(View.GONE, View.VISIBLE, View.GONE);
        } else {
            setVisibility(View.VISIBLE, View.GONE, View.GONE);
        }
    }

    public void setVisibility(int recycleView, int emptyView, int reloadView) {
        linearLayout.setVisibility(recycleView);
        empty_view.setVisibility(emptyView);
        reload.setVisibility(reloadView);
    }

    @Override
    public void onClick(View view) {

    }
}
