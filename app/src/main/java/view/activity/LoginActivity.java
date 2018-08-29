package view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.vsolv.bigflow.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


import constant.Constant;
import models.UserDetails;
import network.CallbackHandler;
import presenter.UserSessionManager;
import presenter.VolleyCallback;

public class LoginActivity extends Activity {
    Button loginButton;
    EditText loginUserName, loginPassword;
    private ProgressDialog pd;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUserName = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        pd = new ProgressDialog(LoginActivity.this);
        loginButton = (Button) findViewById(R.id.loginButton);
        session = new UserSessionManager(getApplicationContext());

        if (session.isUserLoggedIn()) {
            HashMap<String, String> user = new HashMap<String, String>();
            user = session.getUserDetails();
            loginRequest(user.get(session.user_id), user.get(session.user_password));
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = loginUserName.getText().toString();
                String Pasword = loginPassword.getText().toString();
                if (userName.length() > 0 && Pasword.length() > 0) {
                    loginRequest(userName, Pasword);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the user name and password", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void loginRequest(final String user_id, final String user_password) {
        pd.setMessage("Signing In . . .");
        pd.show();
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(Constant.USERNAME, user_id);
            jsonObject.put(Constant.PASSWORD, user_password);
        } catch (JSONException e) {
            Log.e("Login", e.getMessage());
        }

        CallbackHandler.sendReqest(getApplicationContext(), jsonObject.toString(), Constant.URL+"login/", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                pd.hide();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("MESSAGE");
                    if (status.equals("SUCCESS")) {
                        loadData(jsonObject.getJSONObject("DATA"));
                        session.createUserLoginSession(user_id,user_password);
                    } else {

                        Toast.makeText(getApplicationContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("Login", e.getMessage());
                }

            }

            @Override
            public void onFailure(String result) {
                pd.hide();
                Log.e("Login", result);
            }
        });
    }

    private void loadData(JSONObject jsonObject) throws JSONException {

        UserDetails.setUser_code(jsonObject.getString("employee_code"));
        UserDetails.setToday_date(jsonObject.getString("date"));
        UserDetails.setUser_id(jsonObject.getString("employee_gid"));
        UserDetails.setUser_name(jsonObject.getString("employee_name"));
        UserDetails.setEntity_gid(jsonObject.getString("entity_gid"));


        /*Set values into data base*/

        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
        finish();
    }


}
