package sri.sri.happy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;


public class DisplayActivity extends ActionBarActivity {
    ImageView iv;
    TextView t1,t2;
    SharedPreferences pref;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        t1 = (TextView)findViewById(R.id.textView);
        t2 = (TextView)findViewById(R.id.textView2);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        String accessToken = pref.getString("ACCESS_TOKEN", "");
        String username = pref.getString("NAME", "");
        String photo_url = pref.getString("IMAGE_URL","");
        new Img2ImgView((ImageView) findViewById(R.id.imageView2)).execute(photo_url);
        AsyncHttpClient client = new AsyncHttpClient();
        String loginURL = "https://gentle-bayou-7778.herokuapp.com/android/login?token="+accessToken+"&name="+username+"&photo="+photo_url;
        client.get(loginURL, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                //
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String res = new String(response);
                System.out.println("hisss"+res);
                String[] repArr = res.split(",");
                t2.setText("Points:"+repArr[3]);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("USER_ID", repArr[0]);
                edit.commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

        t1.setText(username);
        next = (Button)findViewById(R.id.button2);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CardsActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
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
