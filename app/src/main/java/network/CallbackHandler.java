package network;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import presenter.VolleyCallback;

import static com.android.volley.VolleyLog.TAG;
import static models.ProgressBar.closeProgressBar;
import static models.ProgressBar.cre;
import static models.ProgressBar.initializeProgressBar;

public class CallbackHandler {
    private static ProgressDialog ProgressDialog;
    static Context mContext;
    private static StringRequest mStringRequest;
    private static RequestQueue mRequestQueue;

    private static CallbackHandler mInstance;

    public CallbackHandler(Context ctx) {
        mContext = ctx;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static synchronized CallbackHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CallbackHandler(context);
        }
        return mInstance;
    }

    public static RequestQueue sendReqest(Context context, int method, final String requestBody, String URL, final VolleyCallback success) {


        mRequestQueue = Volley.newRequestQueue(context, new HurlStack(null, SSLSocket.getSocketFactory(context)));

        mStringRequest = new StringRequest(method, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                success.onSuccess(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError) {
                    success.onFailure("TimeoutError");
                } else if (error instanceof NoConnectionError) {
                    success.onFailure("NoConnectionError");
                } else if (error instanceof AuthFailureError) {
                    success.onFailure("AuthFailureError");
                } else if (error instanceof ServerError) {
                    success.onFailure("ServerError");
                } else if (error instanceof NetworkError) {
                    success.onFailure("NetworkError");
                } else if (error instanceof ParseError) {
                    success.onFailure("ParseError");
                }
               // closeProgressBar(ProgressDialog);

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
        CallbackHandler.getInstance(mContext).addToRequestQueue(mStringRequest);
        //closeProgressBar(ProgressDialog);
        return mRequestQueue;
    }
}
