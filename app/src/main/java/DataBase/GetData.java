package DataBase;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

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
import java.util.List;

import constant.Constant;
import models.UserDetails;
import models.Variables;
import network.CallbackHandler;
import presenter.NetworkResult;
import presenter.VolleyCallback;

public class GetData {
    private static String URL;
    private List<Variables.ScheduleType> mScheduleTypeList;
    private List<Variables.FollowupReason> mFollowupReasonList;
    private static List<Variables.Product> mProductList;
    private static Context mContext;

    public GetData(Context context) {
        this.mContext = context;
    }

    public List<Variables.ScheduleType> scheduleTypeList(final NetworkResult networkResult) {
        mScheduleTypeList = new ArrayList<>();
        URL = Constant.URL + "Schedule_Master?";
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
                networkResult.handlerResult("SUCCESS");

            }

            @Override
            public void onFailure(String result) {
                networkResult.handlerError("ERROR");
                Log.e("Getdata-scheduletype", result);
            }
        });

        return mScheduleTypeList;
    }

    public List<Variables.FollowupReason> followupList(int scheduletype_id) {
        mFollowupReasonList = new ArrayList<>();
        URL = Constant.URL + "Schedule_Master?Schedule_Type_gid=" + scheduletype_id;
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
        URL = Constant.URL + "Schedule_Master?";
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

    public List<Variables.Customer> ScheduledCustomerList(int employee_gid, String schedule_date, final NetworkResult networkResult) {
        final List<Variables.Customer> customerList = new ArrayList<>();

        URL = Constant.URL + "FET_Schedule?emp_gid=" + employee_gid;
        URL = URL + "&action=customerwise&date=" + schedule_date;

        CallbackHandler.sendReqest(mContext, Request.Method.GET, "", URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("FOUND")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("DATA");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            String display_name = obj_json.getString("display_name");
                            String location_name = obj_json.getString("location_name");
                            int customer_gid = obj_json.getInt("schedule_customer_gid");
                            customerList.add(new Variables.Customer(display_name, location_name, customer_gid));
                        }

                    }
                    networkResult.handlerResult("SUCCESS");
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());
                }

            }

            @Override
            public void onFailure(String result) {
                if (result.equals("NoConnectionError"))
                    ShowSnakbar(Type.WARNING, "Please Check Internet Connection.");
                networkResult.handlerError("ERROR");
                Log.e("Login", result);

            }
        });
        return customerList;
    }

    public List<Object> ScheduledScheduleType(int customer_gid, String Date, final NetworkResult networkResult) {

        final List<Object> scheduleTypeList = new ArrayList<>();

        String URL = Constant.URL + "FETScheduleCustomer?";
        URL = URL + "&Type=CUSTOMER&Sub_Type=UNIQUE";
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("Customer_Gid", customer_gid);
            jsonObject.put("Schedule_Date", Date);
        } catch (JSONException e) {
            Log.e("Login", e.getMessage());
        }


        CallbackHandler.sendReqest(mContext, Request.Method.POST, jsonObject.toString(), URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("MESSAGE");
                    if (message.equals("FOUND")) {
                        JSONObject objData = jsonObject.getJSONObject("DATA");
                        JSONArray scheduleTask = objData.getJSONArray("ScheudleTask");
                        JSONArray nonScheduleTask = objData.getJSONArray("Non_ScheduleTask");
                        JSONArray salesDetails = objData.getJSONArray("Sales_Details");
                        if (scheduleTask.length() != 0) {
                            scheduleTypeList.add("Scheduled");
                            for (int i = 0; i < scheduleTask.length(); i++) {
                                JSONObject obj_json = scheduleTask.getJSONObject(i);
                                Variables.ScheduleType scheduleType = new Variables.ScheduleType();
                                scheduleType.schedule_type_id = obj_json.getInt("ScheduleType_Gid");
                                scheduleType.schedule_type_name = obj_json.getString("ScheduleType_Name");
                                scheduleType.schedule_gid = obj_json.getInt("Schedule_Gid");
                                scheduleType.schedule_status = obj_json.getString("Schedule_Status");
                                if (scheduleType.schedule_type_name.equals("BOOKING") && salesDetails.length() > 0) {
                                    List<Variables.Details> detailsList = new ArrayList<>();
                                    for (int j = 0; j < salesDetails.length(); j++) {
                                        JSONObject sales_json = salesDetails.getJSONObject(j);
                                        Variables.Details details = new Variables.Details();
                                        details.data = sales_json.getString("SO_NO");
                                        details.gid = sales_json.getInt("SO_Gid");
                                        //details.dataColor=mContext.getResources().getColor(R.color.success);
                                        detailsList.add(details);
                                    }
                                    scheduleType.schedule_details = detailsList;
                                }
                                scheduleTypeList.add(scheduleType);
                            }
                        }
                        if (nonScheduleTask.length() != 0) {
                            scheduleTypeList.add("Non Scheduled");
                            for (int i = 0; i < nonScheduleTask.length(); i++) {
                                JSONObject obj_json = nonScheduleTask.getJSONObject(i);
                                Variables.ScheduleType scheduleType = new Variables.ScheduleType();
                                scheduleType.schedule_type_id = obj_json.getInt("ScheduleType_Gid");
                                scheduleType.schedule_type_name = obj_json.getString("ScheduleType_Name");
                                scheduleType.schedule_gid = 0;
                                scheduleType.schedule_status = "";
                                scheduleTypeList.add(scheduleType);
                            }
                        }

                    }
                    networkResult.handlerResult("SUCCESS");

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(String result) {
                if (result.equals("NoConnectionError"))
                    ShowSnakbar(Type.WARNING, "Please Check Internet Connection.");
                Log.e("Getdata-scheduletype", result);
                networkResult.handlerError("Error");
            }
        });
        return scheduleTypeList;
    }

    public void ShowSnakbar(Type type, String message) {

        Snackbar.with(mContext, null)
                .type(type)
                .message(message)
                .duration(Duration.SHORT)
                .fillParent(true)
                .textAlign(Align.LEFT)
                .show();
    }

}
