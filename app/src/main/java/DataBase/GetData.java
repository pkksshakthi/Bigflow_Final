package DataBase;

import android.content.Context;
import android.os.AsyncTask;
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
    private List<Variables.FollowupReason> mFollowupReasonList;

    private static Context mContext;

    public GetData(Context context) {
        this.mContext = context;
    }

    public List<Variables.ScheduleType> scheduleTypeList() {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }

            @Override
            public List<Variables.Product> onAutoComplete(String result) {
                return null;
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

            @Override
            public List<Variables.Product> onAutoComplete(String result) {
                return null;
            }
        });

        return mFollowupReasonList;
    }

    public static List<Variables.Product> productList(String s) {
        final List<Variables.Product> mProductList  = new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?";
        URL = URL + "&Action=SCHEDULE_TYPE&Entity_gid=" + UserDetails.getEntity_gid();

        CallbackHandler.sendReqest(mContext, Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {




            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }

            @Override
            public List<Variables.Product> onAutoComplete(String result) {
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
                    return mProductList;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        Log.e("mProductList", ""+mProductList.size());
        // Log.e("mProductList", ""+mProductList.get(0).product_id);
        return mProductList;
    }


     /*{
        final List<Variables.Product> mProductList  = new ArrayList<>();
        String URL = Constant.URL + "Schedule_Master?";
        URL = URL + "&Action=SCHEDULE_TYPE&Entity_gid=" + UserDetails.getEntity_gid();

        CallbackHandler.sendReqest(mContext, Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {




            }

            @Override
            public void onFailure(String result) {

                Log.e("Getdata-scheduletype", result);
            }

            @Override
            public List<Variables.Product> onAutoComplete(String result) {
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
                    return mProductList;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
       Log.e("mProductList", ""+mProductList.size());
       // Log.e("mProductList", ""+mProductList.get(0).product_id);
        return mProductList;
    }*/

}
