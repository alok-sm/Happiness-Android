package sri.sri.happy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


    public void onClick_LaunchTwitterOAuthActivity(View button)
    {
        startActivity(new Intent(this, CardsActivity.class));
    }
}
