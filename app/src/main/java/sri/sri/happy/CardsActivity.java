package sri.sri.happy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
import java.util.Timer;
import java.util.TimerTask;

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

    public static int sectionNumber = 0;
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
        sectionNumber = position;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private boolean liked;
        private boolean disliked;

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
//            Bundle args = new Bundle();
            Log.e("CONTAINER ID: ", ""+ sectionNumber);
            if(sectionNumber == 0) {
                View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
                CardContainer mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
                Resources r = getResources();
                SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bm = BitmapFactory.decodeFile(
                        Environment.getExternalStorageDirectory() + "/pic2.jpg", options);
                for (int i = 0; i < 10; i++) {
                    CardModel cardModel = new CardModel("Title" + i, " ",
                            new BitmapDrawable(getResources(),
                                    Bitmap.createScaledBitmap(bm, 800, 600, false)
                            )
                    );
//                    cardModel.set
                    cardModel.setOnClickListener(new CardModel.OnClickListener() {
                        @Override
                        public void OnClickListener() {
                            Log.i("places Swipeable Cards", "I am pressing the card");
//                            startActivity(new Intent(getActivity(), PlacesDisplayActivity.class));
                            liked = false;
                            disliked = false;

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if(liked){
                                        Log.e("places Swipeable Cards", "I like the card");
                                    }else if(disliked){
                                        Log.e("places Swipeable Cards", "I dislike the card");
                                    }else{
                                        Log.e("places Swipeable cards", "I clicked the card");
                                    }
                                }
                            }, 1000);
                        }

                    });

                    cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                        @Override
                        public void onLike() {

//                            getActivity().finish();
                            disliked = true;
                        }

                        @Override
                        public void onDislike() {
//                            getActivity().finish();
                            liked = true;
                        }
                    });

                    adapter.add(cardModel);
                }
                mCardContainer.setAdapter(adapter);


                return rootView;
            }else{
                View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
                CardContainer mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
                Resources r = getResources();
                SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bm = BitmapFactory.decodeFile(
                        Environment.getExternalStorageDirectory() + "/pic2.jpg", options);
                for (int i = 0; i < 10; i++) {
                    final CardModel cardModel = new CardModel("Title" + i, " ",
                            new BitmapDrawable(getResources(),
                                    Bitmap.createScaledBitmap(bm, 800, 600, false)
                            )
                    );
                    cardModel.setOnClickListener(new CardModel.OnClickListener() {
                        @Override
                        public void OnClickListener() {
//                            startActivity(new Intent(getActivity(), ActionsDisplayActivity.class));
                            liked = false;
                            disliked = false;

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if(liked){
                                        Log.e("actions Swipeable Cards", "I like the card");
                                    }else if(disliked){
                                        Log.e("actions Swipeable Cards", "I dislike the card");
                                    }else{
                                        Log.e("actions Swipeable cards", "I clicked the card");
                                    }
                                }
                            }, 1000);

                        }

                    });

                    cardModel.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                        @Override
                        public void onLike() {
                            disliked = true;
                        }

                        @Override
                        public void onDislike() {
                            liked = true;
                        }
                    });

                    adapter.add(cardModel);
                }
                mCardContainer.setAdapter(adapter);


                return rootView;
            }
        }
    }

    public void newPost(MenuItem m){
        startActivity(new Intent(this, CameraActivity.class));
    }

}
