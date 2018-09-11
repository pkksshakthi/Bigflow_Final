package view.fragment;


import android.app.DatePickerDialog;
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

import com.android.volley.Request;
import com.vsolv.bigflow.R;
import network.CallbackHandler;

import presenter.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class Promise_tobuy extends Fragment implements View.OnClickListener {
    Button remark_submit;
    EditText txtDate, remark_text;
    private int mYear, mMonth, mDay, mHour, mMinute;



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
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.setCancelable(false);
            datePickerDialog.setTitle("Choose Remark date");
            datePickerDialog.show();
        } else if (view == remark_submit) {
            JSONObject json = new JSONObject();
            JSONObject json1 = new JSONObject();

            try {
                // Log.e("",remark_text.getText().toString());
                json.put("customer_gid", "990");
                json.put("schedule_type_gid", 1);
                json.put("TYPE", "SCHEDULE");
                json.put("Date", "04/09/2018");

                json1.put("parms", json);
                Log.e("Remark",json1.toString());
                //json.put(Constant.Remark, remark_text.getText().toString());


            } catch (JSONException e) {
                Log.e("Remark", e.getMessage());
            }

            //  String URL = Constant.URL + "FET_Schedule_Set?emp_gid=57,Entity_gid=1/";
            String URL = "https://174.138.120.196/bigflowdemo/FET_Schedule_Set?Entity_gid=1&Emp_gid=13";
            CallbackHandler.sendReqest(getActivity(), Request.Method.POST, json1.toString(), URL, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("result", result);
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("MESSAGE");
                        if (status.equals("SUCCESS")) {


                            Toast.makeText(getActivity(), "Remark saved", Toast.LENGTH_LONG).show();


                        } else {

                            Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("Remark", e.getMessage());
                    }

                }

                @Override
                public void onFailure(String result) {
                    //pd.hide();
                    Log.e("Remark", result);
                }
            });
        }
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            txtDate.setText(day1 + "/" + month1 + "/" + year1);
        }
    };

}