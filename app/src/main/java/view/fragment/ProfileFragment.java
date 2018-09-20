package view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import constant.Constant;
import models.CustomerAdapter;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TextView Employee_mailid_text, Employee_phoneno_text, Employee_address_text, text_changepswd, EmployeeName, EmployeeCode;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public ProfileFragment() {

    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        getActivity().setTitle("Profile");
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        EmployeeName = rootView.findViewById(R.id.txtEmployeeName);
        EmployeeName.setText(UserDetails.getUser_name());
        EmployeeCode = rootView.findViewById(R.id.txtEmployeeCode);
        EmployeeCode.setText(UserDetails.getUser_code());
        Employee_mailid_text = rootView.findViewById(R.id.txtEmployeeMailId);
        Employee_phoneno_text = rootView.findViewById(R.id.txtEmployeePhoneNo);
        Employee_address_text = rootView.findViewById(R.id.txtEmployeeAddress);
        text_changepswd = rootView.findViewById(R.id.text_changepswd);

        text_changepswd.setOnClickListener(this);
        String URL = Constant.URL + "/Employee_Profile?Emp_gid=" + UserDetails.getUser_id() + "&Action=EMPLOYEE_EDIT" + "&Entity_gid=" + UserDetails.getEntity_gid();
        CallbackHandler.sendReqest(getContext(), Request.Method.GET, "", URL, new VolleyCallback() {

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("MESSAGE");
                    String employee_emailid = null, Phn_no = null, address = null;
                    if (message.equals("FOUND")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            employee_emailid = obj_json.getString("employee_emailid");
                            Phn_no = obj_json.getString("employee_mobileno");
                            address = obj_json.getString("address_1");
                        }
                        Employee_mailid_text.setText("Email Id:" + employee_emailid);
                        Employee_phoneno_text.setText("Contact No:" + Phn_no);
                        Employee_address_text.setText("Address:" + address);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String result) {

                Log.e("Changepassword_fail", result);
            }

        });


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

    @Override
    public void onClick(View view) {
        if (view == text_changepswd) {
            builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_dialog_changepasswd, null);
            builder.setTitle("change password");
            final EditText text_oldpasswd = (EditText) dialogView.findViewById(R.id.txtOldPassword);
            builder.setView(dialogView);
            dialog = builder.create();

            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getActivity(), text_oldpasswd.getText().toString(), Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialog.dismiss();
                }
            });

            dialog.show();


        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}