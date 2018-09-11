package DataBase;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import constant.Constant;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.VolleyCallback;

public class GetData {
    private List<Variables.ScheduleType> mScheduleTypeList;
    private Context mContext;

    public GetData(Context context) {
        this.mContext = context;
    }

    public List<Variables.ScheduleType> scheduleTypeList() {
        mScheduleTypeList = new ArrayList<>();
        String URL = Constant.URL + "Customer_Mapped?emp_gid=" + UserDetails.getUser_id();
        URL = URL + "&action=execmapping&Entity_gid=" + UserDetails.getEntity_gid();

        CallbackHandler.sendReqest(mContext, Request.Method.GET, "", URL, new VolleyCallback() {
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
                            scheduleType.schedule_type_id = obj_json.getInt("display_name");
                            scheduleType.schedule_type_name = obj_json.getString("display_name");
                            mScheduleTypeList.add(scheduleType);
                        }
                    }
                    Variables.ScheduleType scheduleType = new Variables.ScheduleType();
                    scheduleType.schedule_type_id=1;
                    scheduleType.schedule_type_name="sales";
                    mScheduleTypeList.add(scheduleType);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Login", result);
            }
        });

        return mScheduleTypeList;
    }

}
