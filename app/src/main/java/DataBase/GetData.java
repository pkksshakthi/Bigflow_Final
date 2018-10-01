package DataBase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import constant.Constant;
import models.ScheduleForAdapter;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.NetworkResult;
import presenter.VolleyCallback;

import view.activity.CollectionActivity;
import view.activity.SalesActivity;
import view.activity.ServiceActivity;
import view.fragment.DirctScheduleFragment;

public class GetData implements NetworkResult {
    private List<Variables.ScheduleType> mScheduleTypeList;
    private List<Variables.FollowupReason> mFollowupReasonList;
    private static List<Variables.Product> mProductList ;
    private static Context mContext;
    NetworkResult mResult;
    private ScheduleForAdapter scheduleForAdapter;
    private ListView listView;
    private AlertDialog alertDialog;

    public GetData(Context context) {
        this.mContext = context;
//        mResult=new GetData(context);
    }

    public List<Variables.ScheduleType> scheduleTypeList(final NetworkResult context) {
        mScheduleTypeList = new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?";
        URL = URL + "&Action=SCHEDULE_TYPE&Entity_gid=" + UserDetails.getEntity_gid();

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
                            scheduleType.schedule_type_id = obj_json.getInt("scheduletype_gid");
                            scheduleType.schedule_type_name = obj_json.getString("scheduletype_name");
                            mScheduleTypeList.add(scheduleType);
                        }
                    }
                    context.handlerResult("success");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }
        });

        return mScheduleTypeList;
    }



    public List<Variables.FollowupReason> followupList(int scheduletype_id) {
        mFollowupReasonList = new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?Schedule_Type_gid=" + scheduletype_id;
        URL = URL + "&Action=FOLLOWUP_REASON&Entity_gid=" + UserDetails.getEntity_gid();

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
                            Variables.FollowupReason followup = new Variables.FollowupReason();
                            followup.followup_id = obj_json.getInt("followupreason_gid");
                            followup.followup_name = obj_json.getString("followupreason_name");
                            mFollowupReasonList.add(followup);
                        }
                    }
                    Variables.FollowupReason followup = new Variables.FollowupReason();
                    followup.followup_id = 1;
                    followup.followup_name = "BOOKING";
                    mFollowupReasonList.add(followup);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }
        });

        return mFollowupReasonList;
    }
    public static List<Variables.Product> productList(String product_name) {
        mProductList = new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?";
        URL = URL + "&Action=SCHEDULE_TYPE&Entity_gid=" + UserDetails.getEntity_gid();

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
                            Variables.Product product = new Variables.Product();
                            product.product_id = obj_json.getInt("scheduletype_gid");
                            product.product_name = obj_json.getString("scheduletype_name");
                            mProductList.add(product);
                        }
                    }
                    Variables.Product product = new Variables.Product();
                    product.product_id = 1;
                    product.product_name = "BOOKING";
                    mProductList.add(product);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }
        });

        return mProductList;
    }


    @Override
    public void handlerResult(String result) {

    }
}
