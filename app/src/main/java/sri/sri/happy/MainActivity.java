package sri.sri.happy;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

public class MainActivity extends ActionBarActivity {
    SharedPreferences pref;

    public static String CONSUMER_KEY = "QHyAADLyx89DqyhAruknm3zmP";
    public static String CONSUMER_SECRET = "QXd8DtGtpRwz0NxWgFwZy2ejE6cs9d5JVWlqYt9ppcomwopXhQ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getPreferences(0);


        String accessToken = pref.getString("ACCESS_TOKEN", "");
        String accessSecret = pref.getString("ACCESS_TOKEN_SECRET", "");

        if(accessToken.equals("") || accessSecret.equals("")) {

            SharedPreferences.Editor edit = pref.edit();
            edit.putString("CONSUMER_KEY", CONSUMER_KEY);
            edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
            edit.commit();

            Fragment login = new LoginFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, login);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
        else{
            startActivity(new Intent(this, CardsActivity.class));
            finish();
        }
    }
}
