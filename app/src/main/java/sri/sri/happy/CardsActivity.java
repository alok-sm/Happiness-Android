package sri.sri.happy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


public class CardsActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                String url = postToTwitter(new File("/sdcard/pic.jpg"), "pls work upload");
//                Log.e("url ", url);
//            }
//        };
//
//        t.start();

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[]{
                                getString(R.string.title_section1),
                                getString(R.string.title_section2)
                        }),
                this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getSupportActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getSupportActionBar().getSelectedNavigationIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cards, menu);
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

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
//            outState.putInt("curChoice", mCurCheckPosition);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
            CardContainer mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
            Resources r = getResources();
            SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());

            for(int i = 0; i<100; i++) {
                final CardModel cardModel = new CardModel("Title"+i, " ", r.getDrawable(R.drawable.picture1));
                cardModel.setOnClickListener(new CardModel.OnClickListener() {
                    @Override
                    public void OnClickListener() {
                        Log.i("Swipeable Cards", "I am pressing the card");
                    }

                });

                cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    @Override
                    public void onLike() {
                        Log.i("Swipeable Cards", "I like the card");
                    }

                    @Override
                    public void onDislike() {
                        Log.i("Swipeable Cards", "I dislike the card");
                    }
                });

                adapter.add(cardModel);
            }
            mCardContainer.setAdapter(adapter);


            return rootView;
        }
    }

//    public String postToTwitter(File file, String message){
//        ConfigurationBuilder builder = new ConfigurationBuilder();
//
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
//
//        builder.setOAuthConsumerKey(pref.getString("CONSUMER_KEY", ""));
//        builder.setOAuthConsumerSecret(pref.getString("CONSUMER_SECRET", ""));
//
//        AccessToken accessToken = new AccessToken(pref.getString("ACCESS_TOKEN", ""), pref.getString("ACCESS_TOKEN_SECRET", ""));
//        Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
//
//        Log.e(pref.getString("ACCESS_TOKEN", ""), "access token");
//        Log.e(pref.getString("ACCESS_TOKEN_SECRET", ""), "access secret");
//        Log.e(pref.getString("CONSUMER_KEY", ""), "consumer key");
//        Log.e(pref.getString("CONSUMER_SECRET", ""), "consumer secret");
//
//        StatusUpdate statusUpdate;
//        Status status = null;
//
//        statusUpdate = new StatusUpdate(message);
//        statusUpdate.setMedia(file);
//        try{
//            status = twitter.updateStatus(statusUpdate);
//        }catch(TwitterException e){
//            e.printStackTrace();
//        }
//        String ret;
//        try{
//            ret = status.getMediaEntities()[0].getMediaURL();
//        }catch(NullPointerException e){
//            try {
//                ret = status.getText();
//            }catch (Exception e1){
//                ret = "another error";
//                if(status == null)
//                    ret = "status is null";
//            }
//        }
//        return ret;
//    }

    public void newPost(MenuItem m){
        startActivity(new Intent(this, CameraActivity.class));
    }

}
