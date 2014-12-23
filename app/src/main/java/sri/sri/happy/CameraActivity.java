package sri.sri.happy;

//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Environment;
//import android.preference.PreferenceManager;
//import android.provider.MediaStore;
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ImageView;
//
//
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;
//
//import org.apache.http.Header;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//
//
//public class CameraActivity extends ActionBarActivity {
//
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_camera);
////
////
////        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
////        Log.e("access token", pref.getString("ACCESS_TOKEN",""));
////        Log.e("access token secret", pref.getString("ACCESS_TOKEN_SECRET",""));
////        Log.e("consumer key", pref.getString("CONSUMER_KEY",""));
////        Log.e("consumer secret", pref.getString("CONSUMER_SECRET",""));
////
////
////        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////        String path = Environment.getExternalStorageDirectory()+"/pic1.jpg";
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
////        startActivityForResult(intent, 0);
//////        finish();
////
////    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        dispatchTakePictureIntent();
//        setContentView(R.layout.activity_main);
//        LocationManager mlocManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0);
//        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0);
//        //	EditText e1=(EditText)findViewById(R.id.editText2);
//        //	e1.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        //    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_test";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath =  image.getAbsolutePath();
//        return image;
//    }
//    static final int REQUEST_TAKE_PHOTO = 1;
//
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_camera, menu);
//        return true;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode != Activity.RESULT_OK){
//            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            String path = Environment.getExternalStorageDirectory()+"/pic1.jpg";
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
//            startActivityForResult(intent, 0);
//        }else{
//            String path = Environment.getExternalStorageDirectory() + "/pic1.jpg";
//            File file = new File(path);
//            ImageView v = (ImageView) findViewById(R.id.imageView);
//            v.setImageURI(Uri.fromFile(file));
//
//
//            RequestParams params = new RequestParams();
//            params.put("access_token", "110102010-16EoZalz8mzrHNtPKnCqswZloLUCGQnqcivXXuXK");
//            params.put("access_token_secret", "LEmfCehebSlZIzVFXXdEB7B9TTw15XlfK1Yg2ldzADxBH");
//            params.put("caption", "Hello World 1 ~Android");
//            params.put("lat", "75.1");
//            params.put("lon", "75.1");
//            params.put("user", "1");
//            try {
//                params.put("image", new File(Environment.getExternalStorageDirectory() + "/pic.jpg"));
//            }catch(FileNotFoundException f){
//                f.printStackTrace();
//                Log.e("NO FILE", "NO FILE");
//            }
//            AsyncHttpClient client = new AsyncHttpClient();
//            client.post("http://gentle-bayou-7778.herokuapp.com/android/createPlace", params, new AsyncHttpResponseHandler() {
////            client.post("http://postcatcher.in/catchers/54995a4c0f0ba60200000118", params, new AsyncHttpResponseHandler() {
//
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    Log.w("async", "success!!!!");
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    Log.e("FAIL", error.getMessage());
//                }
//            });
//
//
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}


import java.io.File;
import java.io.IOException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class CameraActivity extends ActionBarActivity implements LocationListener{

    ImageView v1;
    public String[] types={"Bribe","Traffic Violation","Eve Teasing"};
    public String selectedType="Bribe";
    Location loc;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dispatchTakePictureIntent();
        setContentView(R.layout.activity_camera);
        v1=(ImageView)findViewById(R.id.imageView);
        LocationManager mlocManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
        //	EditText e1=(EditText)findViewById(R.id.editText2);
        //	e1.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        //    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_test";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath =  image.getAbsolutePath();
        return image;
    }
    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    public void report(View v){
//        TODO: WRITE POST REQUEST HERE
//        Intent i=new Intent(MainActivity.this,MainDisplay.class);
//        startActivity(i);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        int h,w;
        h=bitmap.getHeight();
        w=bitmap.getWidth();
        //  h=(h>w)?h:w;
        bitmap=bitmap.createScaledBitmap(bitmap, 800, 600, true);
        if(bitmap == null) Log.e("bitmap", "NULL");
        if(v1 == null) Log.e("view", "NULL");
        v1.setImageBitmap(bitmap);


    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        loc=location;
        latitude=loc.getLatitude();
        longitude=loc.getLongitude();
        TextView t = (TextView) findViewById(R.id.textView);
        t.setText(latitude+":"+longitude);
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }


}