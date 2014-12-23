package sri.sri.happy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;


public class CameraActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
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
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            String path = Environment.getExternalStorageDirectory()+"/pic1.jpg";
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
            startActivityForResult(intent, 0);
        }else{
            String path = Environment.getExternalStorageDirectory() + "/pic1.jpg";
            File file = new File(path);
            ImageView v = (ImageView) findViewById(R.id.imageView);
            v.setImageURI(Uri.fromFile(file));

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            RequestParams params = new RequestParams();
            params.put("access_token", pref.getString("ACCESS_TOKEN", ""));
            params.put("access_token_secret", pref.getString("ACCESS_TOKEN_SECRET", ""));
            params.put("caption", "Hello World 1 ~Android");
            params.put("lat", "75.1");
            params.put("lon", "75.1");
            params.put("user", "1");
            try {
                params.put("image", new File(Environment.getExternalStorageDirectory() + "/pic1.jpg"), "image/jpg");
            }catch(FileNotFoundException f){
                f.printStackTrace();
                Log.e("NO FILE", "NO FILE");
            }
            AsyncHttpClient client = new AsyncHttpClient();
            client.post("http://gentle-bayou-7778.herokuapp.com/android/createPlace", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.w("async", "success!!!!");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.e("FAIL", "FAIL");
                }
            });


        }
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
}
