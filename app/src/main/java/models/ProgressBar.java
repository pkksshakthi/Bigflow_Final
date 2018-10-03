package models;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressBar {
    static ProgressDialog progressDialog;
    private static ProgressBar parent;


    public static void initializeProgressBar(Context parent, ProgressDialog mprogressDialog) {
        // ProgressDialog = null;
        progressDialog = mprogressDialog;
        progressDialog = new ProgressDialog(parent);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public static void closeProgressBar(ProgressDialog progressDialog) {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
    public static ProgressDialog cre(Context context, ProgressDialog progressDialog) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        return progressDialog;
    }
}
