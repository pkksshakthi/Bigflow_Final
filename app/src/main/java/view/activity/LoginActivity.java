package view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.vsolv.bigflow.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;


import DataBase.DataBaseHandler;
import constant.Constant;
import models.Common;
import models.UserDetails;
import network.CallbackHandler;
import presenter.UserSessionManager;
import presenter.VolleyCallback;

/**
 * @author sakthivel
 */
public class LoginActivity extends Activity {
    Button loginButton;
    EditText loginUserName, loginPassword;
    Integer errorCode;
    UserSessionManager session;
    // private Prog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                try {
                    backup();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        session = new UserSessionManager(getApplicationContext());
        loginUserName = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        errorCode = checkConnection();
        if (errorCode == 101) {
            Toast.makeText(getApplicationContext(), "Please switch on 'Internet' Connection", Toast.LENGTH_LONG).show();
            setVisibility(View.VISIBLE, View.GONE);//Layout progressdialog
        } else if (errorCode == 102) {
            setVisibility(View.VISIBLE, View.GONE);
        } else if (errorCode == 103) {
            setVisibility(View.GONE, View.VISIBLE);
            HashMap<String, String> user = new HashMap<String, String>();
            user = session.getUserDetails();
            loginRequest(user.get(UserSessionManager.user_id), user.get(UserSessionManager.user_password));
        } else if (errorCode == 104) {
            setVisibility(View.GONE, View.VISIBLE);
            HashMap<String, String> user = session.getUserDetails();
            try {
                JSONObject jobj = new JSONObject(user.get(UserSessionManager.user_details));
                loadData(jobj);
            } catch (JSONException ex) {

            }
        } else if (errorCode == 105) {
            Toast.makeText(getApplicationContext(), "Switch on 'Internet' to LoginActivity", Toast.LENGTH_LONG).show();
            setVisibility(View.VISIBLE, View.GONE);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = loginUserName.getText().toString();
                String password = loginPassword.getText().toString();
                if (checkConnection() == 101) {
                    Toast.makeText(getApplicationContext(), "Please switch on 'Internet' Connection", Toast.LENGTH_LONG).show();
                } else if (userName.length() > 0 && password.length() > 0) {
                    loginRequest(userName, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the user name and password", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void backup() throws IOException {
        final String inFileName = "/data/user/0/com.vsolv.bigflow/databases/VSOLV.db";
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory() + "/database_copy.db";

        // Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    private void setVisibility(int ui, int pd) {

        findViewById(R.id.ln_login).setVisibility(ui);
        findViewById(R.id.progressBar).setVisibility(pd);
    }

    private void loginRequest(final String user_id, final String user_password) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constant.USERNAME, user_id);
            jsonObject.put(Constant.PASSWORD, user_password);
        } catch (JSONException e) {
            Log.e("LoginActivity", e.getMessage());
        }

        String URL = Constant.URL + "login/";


        CallbackHandler.sendReqest(getApplicationContext(), Request.Method.POST, jsonObject.toString(), URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("SUCCESS")) {
                        loadData(jsonObject.getJSONObject("DATA"));
                        session.createUserLoginSession(user_id, user_password, UserDetails.getToday_date(), jsonObject.getJSONObject("DATA").toString());

                    } else {

                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("LoginActivity", e.getMessage());
                    setVisibility(View.VISIBLE, View.GONE);
                }

            }

            @Override
            public void onFailure(String result) {
                //pd.hide();
                Log.e("LoginActivity", result);
                setVisibility(View.VISIBLE, View.GONE);
            }
        });
    }

    private void loadData(JSONObject jsonObject) throws JSONException {

        UserDetails.setUser_code(jsonObject.getString("employee_code"));
        UserDetails.setToday_date(jsonObject.getString("date"));
        UserDetails.setUser_id(jsonObject.getString("employee_gid"));
        UserDetails.setUser_name(jsonObject.getString("employee_name"));
        UserDetails.setEntity_gid(jsonObject.getString("entity_gid"));
        setVisibility(View.GONE, View.VISIBLE);


//        Ramesh Temp Menu
        String URL = Constant.URL + "user_rights?emp_gid=13"; //Its from Session
        CallbackHandler.sendReqest(getApplicationContext(), Request.Method.GET, jsonObject.toString(), URL, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("result", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("FOUND")) {
//                        JSONObject test =  jsonObject.getJSONObject("DATA");
                        JSONArray jsonArray;
                        jsonArray = jsonObject.getJSONArray("DATA");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj_json = jsonArray.getJSONObject(i);
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("menu_gid", obj_json.getString("menu_gid"));
                            contentValues.put("menu_parent_gid", obj_json.getString("menu_parent_gid"));
                            contentValues.put("menu_name", obj_json.getString("menu_name"));
                            contentValues.put("menu_link", obj_json.getString("menu_link"));
                            contentValues.put("menu_displayorder", obj_json.getString("menu_displayorder"));
                            contentValues.put("menu_level", obj_json.getString("menu_level"));
                            DataBaseHandler dataBaseHandler = new DataBaseHandler(LoginActivity.this);
                            dataBaseHandler.Insert("gal_mst_tmenu", contentValues);
                            dataBaseHandler.close();
                        }


                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                        finish();
//                        To CHeck


                    } else {
                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("LoginActivity", e.getMessage());
                    setVisibility(View.VISIBLE, View.GONE);
                }

            }

            @Override
            public void onFailure(String result) {
                //pd.hide();
                Log.e("LoginActivity", result);
                setVisibility(View.VISIBLE, View.GONE);
            }
        });

//Ramesh Temp Menu Ends
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnected());
    }

    public int checkConnection() {
        if (!session.isUserLoggedIn() && !isOnline(getApplicationContext())) {
            //no session and no Internet
            return 101;
        }
        if (!session.isUserLoggedIn() && isOnline(getApplicationContext())) {
            //no session and with internet
            return 102;
        }
        if (session.isUserLoggedIn()) {
            if (isOnline(getApplicationContext())) {
                // Internet with session
                return 103;
            } else {
                // No internet with session
                HashMap<String, String> user = session.getUserDetails();
                Date d1 = Common.convertDate(user.get(UserSessionManager.user_loginDate), "dd-MMM-yyyy");
                Date d2 = new Date();
                d2.setTime(d1.getTime());
                int I = d1.compareTo(d2);
//                if ((Common.convertDate(user.get(session.user_loginDate), "dd-MMM-yyyy")).equals(new Date())) {
                if (d1.compareTo(d2) == 0) {
                    return 104;
                } else {
                    return 105;
                }

            }
        }
        return 0;
    }

}
