package view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DataBase.GetData;
import constant.Constant;
import models.Common;
import models.CustomerAdapter;
import models.ScheduleForAdapter;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;
import view.activity.SalesActivity;


public class DirctScheduleFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_Title = "title";
    private static final String ARG_PARAM2 = "param2";
    EditText customerSearch;
    private RecyclerView recyclerView;
    private TextView empty_view, reload;
    public CustomerAdapter adapter;
    private ArrayList<Variables.Customer> customerList;

    public ListView listView;
    private LinearLayout linearLayout;
    public ScheduleForAdapter scheduleForAdapter;
    private List<Variables.ScheduleType> scheduleTypeList;
    private AlertDialog alertDialog;
    private Bundle sessiondata;
    private String mParam1, mParam2;

    private OnFragmentInteractionListener mListener;
    private GetData getData;

    public DirctScheduleFragment() {
        // Required empty public constructor
    }

    public static DirctScheduleFragment newInstance(String Title, String param2) {
        DirctScheduleFragment fragment = new DirctScheduleFragment();
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
        View view = inflater.inflate(R.layout.fragment_dirct_schedule, container, false);

        loadView(view);
        initializeView();
        loadData();
        return view;
    }


    private void loadView(View view) {
        linearLayout = (LinearLayout) view.findViewById(R.id.linearDirect);
        recyclerView = (RecyclerView) view.findViewById(R.id.customerRecyclerView);
        empty_view = view.findViewById(R.id.empty_view);
        customerSearch = view.findViewById(R.id.customer_search);
        reload = view.findViewById(R.id.custReload);
    }

    private void initializeView() {
        sessiondata = new Bundle();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        customerList = new ArrayList<>();
        reload.setOnClickListener(this);
        getData = new GetData(getActivity());
        customerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void loadData() {
        if (!Common.isOnline(getContext())) {
            setVisibility(View.GONE, View.GONE, View.VISIBLE);
            return;
        }
        String URL = Constant.URL + "Customer_Mapped?emp_gid=" + UserDetails.getUser_id();
        URL += "&action=execmapping";
        URL += "&Entity_gid=" + UserDetails.getEntity_gid();

        CallbackHandler.sendReqest(getActivity(), Request.Method.GET, "", URL, new VolleyCallback() {


            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("MESSAGE");

                    if (message.equals("FOUND")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            String display_name = obj_json.getString("display_name");
                            String location_name = obj_json.getString("location_name");
                            int customer_gid = obj_json.getInt("customer_gid");
                            customerList.add(new Variables.Customer(display_name, location_name, customer_gid));
                        }

                        setAdapter();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("DirectSchecule", result);
            }

        });
    }

    public void setAdapter() {

        adapter = new CustomerAdapter(getActivity(), customerList);
        recyclerView.setAdapter(adapter);


        adapter.setOnclickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Variables.Customer item, int position) {
                sessiondata.putInt("customer_id", item.cust_gid);
                getScheduleType(item.cust_gid);

            }

            @Override
            public void onViewDetailsClick(Variables.Customer item, int position) {
                Toast.makeText(getContext(), "This Module implement into new version.", Toast.LENGTH_LONG).show();
            }
        });

        if (adapter.getItemCount() == 0) {
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

    public void filter(String text) {
        List<Variables.Customer> temp = new ArrayList();
        for (Variables.Customer d : customerList) {

            if (d.getCust_name().toLowerCase().replaceAll("\\s+", "").contains(text.toLowerCase().replaceAll("\\s+", ""))) {
                temp.add(d);
            }
        }

        adapter.updateList(temp);
    }

    public void getScheduleType(int customer_gid) {
        scheduleTypeList=new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?";
        URL = URL + "&Action=SCHEDULE_TYPE&Entity_gid=" + UserDetails.getEntity_gid();

        CallbackHandler.sendReqest(getContext(), Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("MESSAGE");
                    if (message.equals("FOUND")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            Variables.ScheduleType scheduleType = new Variables.ScheduleType();
                            scheduleType.schedule_type_id = obj_json.getInt("scheduletype_gid");
                            scheduleType.schedule_type_name = obj_json.getString("scheduletype_name");
                            scheduleTypeList.add(scheduleType);
                        }
                        createDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }
        });

    }

    public void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Schedule For");
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_list, null, false);
        scheduleForAdapter = new ScheduleForAdapter(getContext(), R.layout.item_schedule_for, scheduleTypeList);
        listView = (ListView) customView.findViewById(R.id.listView_dialog);
        listView.setAdapter(scheduleForAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Variables.ScheduleType scheduleType = scheduleTypeList.get(position);
                sessiondata.putInt("scheduletype_id", scheduleType.schedule_type_id);
                Class aClass = null;
                switch (scheduleType.schedule_type_name) {
                    case "BOOKING":
                        aClass = SalesActivity.class;
                        break;
                    case "COLLECTION":
                        break;
                }
                if (aClass != null) {
                    Intent intent = new Intent(getActivity(), aClass);
                    intent.putExtras(sessiondata);
                    startActivity(intent);

                } else {
                    Toast.makeText(getContext(), "This Module implement into new version.", Toast.LENGTH_LONG).show();
                }
                alertDialog.cancel();

            }
        });
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.show();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custReload:
                loadData();
                break;

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}