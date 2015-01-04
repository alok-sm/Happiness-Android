package sri.sri.happy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class NewPostActivity extends ActionBarActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        Log.e("access token", pref.getString("ACCESS_TOKEN",""));
        Log.e("access token secret", pref.getString("ACCESS_TOKEN_SECRET",""));
        Log.e("consumer key", pref.getString("CONSUMER_KEY",""));
        Log.e("consumer secret", pref.getString("CONSUMER_SECRET",""));




        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        String path = Environment.getExternalStorageDirectory()+"/pic1.jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        startActivityForResult(intent, 0);
//        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_place, menu);
        return true;
    }

    int resultcode;

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent data) {

        resultcode = _resultCode;
        String path = Environment.getExternalStorageDirectory() + "/pic1.jpg";
        File file = new File(path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        int x = bm.getWidth();
        int y = bm.getHeight();


        if(x>y){
            bm = Bitmap.createBitmap(bm, (int)((x-y)/2), 0, y, y);
        }else{
            bm = Bitmap.createBitmap(bm, 0, (int)((y-x)/2), x, x);
        }
        bm = Bitmap.createScaledBitmap(bm, 500, 500, false);


        ImageView v = (ImageView) findViewById(R.id.imageView);
        v.setImageBitmap(bm);
        File f2 = new File(Environment.getExternalStorageDirectory()+"/pic2.jpg");
        try{
            FileOutputStream fOut = new FileOutputStream(f2);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        }catch(Exception e){}
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void report(MenuItem item){
        String placeid = pref.getString("PLACE_ID", "");

        if (resultcode != Activity.RESULT_OK) {
            finish();
        } else {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
            SimpleLocation location = new SimpleLocation(this);
            double lati = 0;
            double longi = 0;
            if (location.hasLocationEnabled()) {
                // ask the device to update the location data
                location.beginUpdates();


                // get the location from the device (alternative A)
                lati = location.getLatitude();
                longi = location.getLongitude();

                // get the location from the device (alternative B)
//                SimpleLocation.Point coords = location.getPosition();

                // ask the device to stop location updates to save battery
                location.endUpdates();
            } else {
                // ask the user to enable location access
                location.openSettings(this);
            }
            EditText et = (EditText) findViewById(R.id.editText);
            RequestParams params = new RequestParams();
            params.put("access_token", pref.getString("ACCESS_TOKEN", ""));
            params.put("access_token_secret", pref.getString("ACCESS_TOKEN_SECRET", ""));
            params.put("caption", et.getText().toString());
            params.put("post", placeid);
            params.put("lat", lati);
            params.put("lon", longi);
            params.put("user", pref.getString("USER_ID", ""));
            try {
                params.put("image", new File(Environment.getExternalStorageDirectory() + "/pic2.jpg"), "image/jpg");
            } catch (FileNotFoundException f) {
                f.printStackTrace();
                Log.e("NO FILE", "NO FILE");
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://gentle-bayou-7778.herokuapp.com/android/createPost", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.w("async", "success!!!!");
                    startActivity(new Intent(getApplicationContext(), CardsActivity.class));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("FAIL", "On Failure");
                }
            });
        }
    }

}
