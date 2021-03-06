package view.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.vsolv.bigflow.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DataBase.GetData;
import models.Common;
import models.CustomerAdapter;
import models.ListAdapter;
import models.ScheduleForAdapter;
import models.Variables;
import presenter.NetworkResult;
import view.activity.CollectionActivity;
import view.activity.MapsActivity;
import view.activity.SalesActivity;
import view.activity.ServiceActivity;
import view.activity.StatusActivity;
import view.activity.StockActivity;


public class DayScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View fragmentView;
    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private TextView empty_view, reload;
    private SearchView customerSearch;
    private Bundle sessiondata;
    private List<Variables.Customer> customerList;
    private ProgressDialog progressDialog;
    private GetData getData;
    private CustomerAdapter adapter;
    private ScheduleForAdapter scheduleForAdapter;
    private ListView listView;
    private List<Object> scheduleTypeList;
    private AlertDialog alertDialog;


    public DayScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSchedule.
     */
    // TODO: Rename and change types and number of parameters
    public static DayScheduleFragment newInstance(String param1, String param2) {
        DayScheduleFragment fragment = new DayScheduleFragment();
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

        getActivity().setTitle("Today Schedule");
        fragmentView = inflater.inflate(R.layout.fragment_day_schedule, container, false);
        loadView();
        initializeView();
        loadData();
        return fragmentView;
    }

    private void loadView() {
        linearLayout = (LinearLayout) fragmentView.findViewById(R.id.linearDirect);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.RV_DaySchedule);
        empty_view = fragmentView.findViewById(R.id.empty_view);
        customerSearch = fragmentView.findViewById(R.id.customer_search);
        reload = fragmentView.findViewById(R.id.custReload);
    }

    private void initializeView() {
        sessiondata = new Bundle();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        customerList = new ArrayList<>();
        customerSearch.setQueryHint("Search");
        //reload.setOnClickListener();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        getData = new GetData(getActivity());
        /*customerSearch.addTextChangedListener(new TextWatcher() {
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
        });*/
    }

    private void loadData() {
        progressDialog.show();
        if (!Common.isOnline(getContext())) {
            Snackbar.with(getActivity(), null)
                    .type(Type.WARNING)
                    .message("Please Check Internet Connection.")
                    .duration(Duration.SHORT)
                    .fillParent(true)
                    .textAlign(Align.LEFT)
                    .show();
            setVisibility(View.GONE, View.GONE, View.VISIBLE);
            progressDialog.cancel();
            return;
        }

        customerList = getData.ScheduledCustomerList(13, Common.convertDateString(new Date(), "yyyy-MM-dd"), new NetworkResult() {
            @Override
            public void handlerResult(String result) {
                int i = 0;
                setAdapter();
                progressDialog.cancel();
            }

            @Override
            public void handlerError(String result) {

            }
        });

    }

    public void setAdapter() {

        adapter = new CustomerAdapter(getActivity(), customerList);
        recyclerView.setAdapter(adapter);


        adapter.setOnclickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Variables.Customer item, int position) {

                getScheduleType(item.cust_gid);

            }

            @Override
            public void onViewDetailsClick(Variables.Customer item, int position) {
                Toast.makeText(getContext(), "This Module implement into new version.", Toast.LENGTH_LONG).show();
            }
        });

        if (adapter.getItemCount() == 0) {
            empty_view.setText("" + getActivity().getResources().getString(R.string.loading));
            setVisibility(View.GONE, View.VISIBLE, View.GONE);
        } else {
            setVisibility(View.VISIBLE, View.GONE, View.GONE);
        }
    }

    public void getScheduleType(final int customer_gid) {
        progressDialog.show();
        scheduleTypeList = getData.ScheduledScheduleType(customer_gid, Common.convertDateString(new Date(), "yyyy-MM-dd"), new NetworkResult() {
            @Override
            public void handlerResult(String result) {
                sessiondata = new Bundle();
                sessiondata.putInt("customer_id", customer_gid);
                createDialog();
                progressDialog.cancel();
            }

            @Override
            public void handlerError(String result) {
                progressDialog.cancel();
            }
        });
    }

    public void createDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Schedule");
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_list, null, false);
        scheduleForAdapter = new ScheduleForAdapter(getContext(), R.layout.item_schedule_for, scheduleTypeList);
        listView = (ListView) customView.findViewById(R.id.listView_dialog);
        listView.setAdapter(scheduleForAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (scheduleTypeList.get(position) instanceof Variables.ScheduleType) {

                    Variables.ScheduleType scheduleType = (Variables.ScheduleType) scheduleTypeList.get(position);
                    sessiondata.putInt("schedule_gid", scheduleType.schedule_gid);
                    sessiondata.putInt("scheduletype_id", scheduleType.schedule_type_id);
                    Class aClass = null;
                    switch (scheduleType.schedule_type_name) {
                        case "BOOKING":
                            gotoSales(scheduleType);
                            aClass = SalesActivity.class;
                            break;
                        case "COLLECTION":
                            if (scheduleType.schedule_gid == 0) {
                                gotoActivity(CollectionActivity.class);
                            } else {
                                Toast.makeText(getContext(), "Your Schedule Closed", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "SERVICE":
                            if (scheduleType.schedule_gid == 0) {
                                gotoActivity(StatusActivity.class);
                            } else {
                                Toast.makeText(getContext(), "Your Schedule Closed", Toast.LENGTH_LONG).show();
                            }

                            break;
                        case "STOCK":
                            if (scheduleType.schedule_gid == 0) {
                                gotoActivity(StockActivity.class);
                            } else {
                                Toast.makeText(getContext(), "Your Schedule Closed", Toast.LENGTH_LONG).show();
                            }

                            break;
                        case "OTHERS":
                            gotoActivity(null);
                            break;
                    }


                }
            }
        });
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void gotoSales(Variables.ScheduleType scheduleType) {
        List<Variables.Details> detailsList = (List<Variables.Details>) scheduleType.schedule_details;
        if (detailsList != null) {
            alertDialog.cancel();
            final AlertDialog salesDialog;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select Invoice");
            View salesView = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_list, null, false);

            final ListAdapter listAdapter = new ListAdapter(getActivity(), detailsList);
            ListView listView = (ListView) salesView.findViewById(R.id.listView_dialog);

            listView.setAdapter(listAdapter);
            builder.setView(salesView);
            salesDialog = builder.create();
            salesDialog.show();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sessiondata.putLong("sales_gid", listAdapter.getItemId(position));
                    salesDialog.cancel();
                    gotoActivity(SalesActivity.class);
                }
            });


        } else {
            Class aClass = SalesActivity.class;
            gotoActivity(aClass);
        }
    }

    public void gotoActivity(Class aClass) {
        if (aClass != null) {
            Intent intent = new Intent(getActivity(), aClass);
            intent.putExtras(sessiondata);
            startActivity(intent);

        } else {
            Toast.makeText(getContext(), "This Module implement into new version.", Toast.LENGTH_LONG).show();
        }
    }

    public void setVisibility(int recycleView, int emptyView, int reloadView) {
        linearLayout.setVisibility(recycleView);
        empty_view.setVisibility(emptyView);
        reload.setVisibility(reloadView);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}