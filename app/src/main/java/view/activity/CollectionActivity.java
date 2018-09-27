package view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vsolv.bigflow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    private Button openCamera, upLoad;
    private ImageView imageView;
    static final int camResult = 0;
    private final int selectResult = 1;
    private ProgressDialog dialog = null;
    private JSONObject jsonObject;
    ArrayList<Uri> imagesUriList;
    ArrayList<String> encodedImageList;
    String imageURI;
    public static final String urlUpload = "http://sachinverma.co.in/services/upload_multiple_images/upload_multiple_images.php";
    public static final int REQCODE = 100;
    public static final String imageList = "imageList";
    public static final String imageName = "name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        imageView = findViewById(R.id.ivSelected);
        upLoad = findViewById(R.id.btnUpload);
        openCamera = findViewById(R.id.openCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        upLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    uploadImage();
                } catch (Exception e) {
                    Log.e(e.getClass().getName(), e.getMessage());
                }
            }
        });
    }

    private void uploadImage()throws Exception {
        dialog.show();

        JSONArray jsonArray = new JSONArray();

        if (encodedImageList.isEmpty()){
            Toast.makeText(this, "Please select some images first.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String encoded: encodedImageList){
            jsonArray.put(encoded);
        }

        try {
            jsonObject.put(imageName,"Sampleimage");
            jsonObject.put(imageList, jsonArray);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlUpload, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("Message from server", jsonObject.toString());
                        dialog.dismiss();
                        //messageText.setText("Images Uploaded Successfully");
                        Toast.makeText(getApplication(), "Images Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Message from server", volleyError.toString());
                Toast.makeText(getApplication(), "Error Occurred", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void selectImage() {
        final String[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectionActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, camResult);
                } else if (items[which].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), selectResult);

                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private File getFolder() {
        String directory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(directory + "/vsolv");
        if (!folder.exists()) {
            folder.mkdir();
        }

        return folder;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            // When an Image is picked
            if (requestCode == REQCODE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesUriList = new ArrayList<Uri>();
                encodedImageList.clear();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageURI  = cursor.getString(columnIndex);
                    cursor.close();

                }else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageURI  = cursor.getString(columnIndex);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                            encodedImageList.add(encodedImage);
                            cursor.close();

                        }
                        //noImage.setText("Selected Images: " + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case camResult:
                    Bundle bundle = data.getExtras();
                    final Bitmap bitmap = (Bitmap) bundle.get("data");
                    imageView.setImageBitmap(bitmap);
                    break;
                case selectResult:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Choose application"), REQCODE);
                    break;
            }
        }
    }
}
