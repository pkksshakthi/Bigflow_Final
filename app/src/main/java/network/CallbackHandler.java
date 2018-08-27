package network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import presenter.VolleyCallback;

import static com.android.volley.VolleyLog.TAG;

public class CallbackHandler {
    static Context mContext;
    private static StringRequest mStringRequest;
    private static RequestQueue mRequestQueue;

    public static RequestQueue sendReqest(final Context context, int method, final String requestBody, String URL, final VolleyCallback success) {


        mContext = context;

        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, SSLSocket.getSocketFactory(context)));

        mStringRequest = new StringRequest(method, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                success.onSuccess(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success.onFailure(error.toString());
            }

        }) {


            @Override
            public byte[] getBody() throws AuthFailureError {


                try {
                    return requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return new byte[0];
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //This appears in the log
                Log.d(TAG, "Does it assign headers?");
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("authorization", "Token " + "7111797114100105971106449505132");

                return headers;
            }
        };
        mRequestQueue.add(mStringRequest);
        return mRequestQueue;
    }}
