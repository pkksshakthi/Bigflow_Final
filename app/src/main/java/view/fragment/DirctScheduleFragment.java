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
import android.widget.ListView;
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
import models.CustomerAdapter;
import models.ScheduleForAdapter;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;
import view.activity.SalesActivity;


public class DirctScheduleFragment extends Fragment {

    private static final String ARG_Title = "title";
    private static final String ARG_PARAM2 = "param2";
    EditText customerSearch;
    private RecyclerView recyclerView;
    private TextView empty_view;
    public CustomerAdapter adapter;
    private ArrayList<Variables.Customer> customerList;

    public ListView listView;
    public ScheduleForAdapter scheduleForAdapter;
    private List<Variables.ScheduleType> scheduleTypeList;
    private AlertDialog alertDialog;
    private Bundle sessiondata;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        sessiondata = new Bundle();
        View view = inflater.inflate(R.layout.fragment_dirct_schedule, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.customerRecyclerView);
        empty_view = view.findViewById(R.id.empty_view);
        customerSearch = view.findViewById(R.id.customer_search);
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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        customerList = new ArrayList<>();

        //Model dialog
        GetData getData = new GetData(getActivity());
        scheduleTypeList = getData.scheduleTypeList();

        String URL = Constant.URL + "Customer_Mapped?emp_gid=57&action=execmapping&Entity_gid=1";
        CallbackHandler.sendReqest(getContext(), Request.Method.GET, "", URL, new VolleyCallback() {


            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("MESSAGE");
                    adapter = new CustomerAdapter(getActivity(), customerList);
                    if (message.equals("FOUND")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            String display_name = obj_json.getString("display_name");
                            String location_name = obj_json.getString("location_name");
                            int customer_gid = obj_json.getInt("customer_gid");
                            customerList.add(new Variables.Customer(display_name, location_name, customer_gid));
                        }

                        recyclerView.setAdapter(adapter);

                        createDialog();

                        adapter.setOnclickListener(new CustomerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(Variables.Customer item, int position) {
                                sessiondata.putInt("customer_id", item.cust_gid);
                                alertDialog.show();
                            }

                            @Override
                            public void onViewDetailsClick(Variables.Customer item, int position) {
                                Toast.makeText(getContext(), "" + position + "test", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (adapter.getItemCount() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        empty_view.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        empty_view.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Login", result);
            }
        });

        return view;

    }

    public void filter(String text) {
        List<Variables.Customer> temp = new ArrayList();
        for (Variables.Customer d : customerList) {

            if (d.getCust_name().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }

        adapter.updateList(temp);
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
                Intent intent = new Intent(getActivity(), SalesActivity.class);
                intent.putExtras(sessiondata);
                startActivity(intent);
                Toast.makeText(getContext(), "" + "Name: " + scheduleType.getSchedule_type_name(), Toast.LENGTH_LONG).show();
                alertDialog.cancel();
            }
        });
        builder.setView(customView);
        alertDialog = builder.create();

    }


    // TODO: Rename method, update argument and hook method into UI event
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}