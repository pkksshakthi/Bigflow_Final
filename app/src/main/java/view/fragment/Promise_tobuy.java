package view.fragment;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import constant.Constant;
import models.UserDetails;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import models.Variables;
import network.CallbackHandler;

import presenter.VolleyCallback;
import view.activity.DashBoardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.List;

import static java.lang.Integer.parseInt;


public class Promise_tobuy extends Fragment implements View.OnClickListener {
    Button remark_submit;
    EditText txtDate, remark_text;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Bundle customer_details, Remark_intent;
    int schedule_type_gid, customer_gid;
    private String date, Remark_date;


    public Promise_tobuy() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Remark_intent = new Bundle();
        View rootView = inflater.inflate(R.layout.fragment_promise_tobuy, container, false);
        txtDate = (EditText) rootView.findViewById(R.id.in_date);
        remark_submit = (Button) rootView.findViewById(R.id.remark_submitbtn);
        remark_text = (EditText) rootView.findViewById(R.id.remark);
        txtDate.setOnClickListener(this);
        remark_submit.setOnClickListener(this);
        return rootView;

    }

    @Override
    public void onClick(View view) {
        if (view == txtDate) {

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener, mYear, mMonth, mDay);
            c.add(Calendar.DAY_OF_MONTH, 1);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.setCancelable(false);
            datePickerDialog.setTitle("Choose Remark date");
            datePickerDialog.show();

        } else if (view == remark_submit) {

            if (Remark_date != null &&  remark_text.getText().toString().trim().length() > 0) {

                if (getActivity().getIntent() != null) {
                    customer_details = getActivity().getIntent().getExtras();
                    customer_gid = customer_details.getInt("customer_id");
                    schedule_type_gid = customer_details.getInt("scheduletype_id");
                }

                JSONObject json = new JSONObject();
                JSONObject json1 = new JSONObject();

                try {

                    json.put("customer_gid", customer_gid);
                    json.put("schedule_type_gid", schedule_type_gid);
                    json.put("TYPE", "SCHEDULE");
                    json.put("Date", Remark_date);
                    json1.put("parms", json);
                    Log.e("Remark1", json1.toString());

                } catch (JSONException e) {
                    Log.e("Remark", e.getMessage());
                }

                String URL = Constant.URL + "/FET_Schedule_Set?Entity_gid=" + UserDetails.getEntity_gid() + "&Emp_gid=" + UserDetails.getUser_id();
                CallbackHandler.sendReqest(getActivity(), Request.Method.POST, json1.toString(), URL, new VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {
                        Log.e("result", result);
                        String replaced = result.replaceAll("[\\[\\]\"]", "");
                        String replaced2 = replaced.replace("\\", "");
                        Integer schedule_gid;
                        if (!"Schedule Already Created".equals(replaced2)) {
                            schedule_gid = Integer.valueOf(replaced2);

                        } else {
                            Toast.makeText(getActivity(), "Schedule Already Created", Toast.LENGTH_LONG).show();

                            return;
                        }
                        try {

                            if (schedule_gid != null) {
                                JSONObject followup_json = new JSONObject();
                                JSONObject followup_json1 = new JSONObject();
                                try {

                                    followup_json.put("schedule_gid", schedule_gid);
                                    followup_json.put("followupreason_gid", 20);
                                    followup_json.put("ls_followup_date", Remark_date);
                                    followup_json.put("ls_Remarks", remark_text.getText().toString());
                                    followup_json.put("TYPE", "INDATE");
                                    followup_json1.put("parms", followup_json);


                                } catch (JSONException e) {
                                    Log.e("Remark", e.getMessage());
                                }
                                String URL = Constant.URL + "/FET_Schedule_Set?Entity_gid=" + UserDetails.getEntity_gid() + "&Emp_gid=" + UserDetails.getUser_id();
                                CallbackHandler.sendReqest(getActivity(), Request.Method.POST, followup_json1.toString(), URL, new VolleyCallback() {

                                    @Override
                                    public void onSuccess(String result) {
                                        Log.e("Followup_Remark_pass", result);

                                            if (("\""+"SUCCESS"+"\"").equals(result)) {
                                                Toast.makeText(getActivity(), "Remark saved", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getActivity(), DashBoardActivity.class));
                                                
                                            } else {
                                                Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG).show();
                                            }
                                    }

                                    @Override
                                    public void onFailure(String result) {

                                        Log.e("Followup_Remark_p2b", result);
                                    }


                                });
                            } else {
                                Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("Remark", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(String result) {

                        Log.e("Remark", result);
                    }


                });
            } else {
                Toast.makeText(getActivity(), "Enter Valid Date and Remark", Toast.LENGTH_LONG).show();

            }

        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month_todb = String.valueOf(selectedMonth + 1);
            int month1 = selectedMonth + 1;
            String day1 = String.valueOf(selectedDay);
            String day = String.format("%02d", parseInt(day1));
            String month_to_db = String.format("%02d", parseInt(month_todb));
            String month = new DateFormatSymbols().getShortMonths()[month1 - 1];
            String Date = day + "/" + month + "/" + year1;
            String Date_db = day + "/" + month_to_db + "/" + year1;
            txtDate.setText(Date);
            Remark_date = Date_db;

        }
    };

}