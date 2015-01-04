package sri.sri.happy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.*;



public class PostsDisplayActivity extends ActionBarActivity {
    SharedPreferences pref;
    GoogleMap googleMap;

    String placeId;
    String placeCaption;
    String placeImageUrl;
    String placeLat;
    String placeLon;
    String placeUpvotes;
    String placeDownvotes;
    String placesTime;

    private void addMarker(){

        /** Make sure that the map has been initialised **/
        if(googleMap != null){
            googleMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(
                                            Double.parseDouble(placeLat),
                                            Double.parseDouble(placeLon)
                                    )
                            )
                            .anchor(0, 0)
                            .title("Location")
                            .draggable(false)
            );
        }
    }

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_display);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        placeId = pref.getString("POST_ID", "");
        placeCaption = pref.getString("POST_CAPTION", "");
        placeImageUrl = pref.getString("POST_IMAGE_URL", "");
        placeLat = pref.getString("POST_LAT", "");
        placeLon = pref.getString("POST_LON", "");
        placeUpvotes = pref.getString("POST_UPVOTES", "");
        placeDownvotes = pref.getString("POST_DOWNVOTES", "");
        placesTime = pref.getString("POST_TIME", "");

        TextView tv = (TextView) findViewById(R.id.placetxtv);
//        tv.setGravity(CENTER);
        placesTime = placesTime.replace("\\n", "\n");
        tv.setText(placeUpvotes+"↑\n"+placeDownvotes+"↓\n\n"+placesTime+"\nago");
        createMapView();
        addMarker();
        getSupportActionBar().setTitle(placeCaption);
        new Img2ImgView((ImageView) findViewById(R.id.placeimgv), false).execute(placeImageUrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_display, menu);
        return true;
    }
}
