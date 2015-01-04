package sri.sri.happy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ProgressBar;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;

import java.util.Timer;
import java.util.TimerTask;


public class CardsActivity extends ActionBarActivity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */

    public static int sectionNumber = 0;
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
   static SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

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
        getMenuInflater().inflate(R.menu.menu_cards, menu);
        MenuItem newPost = menu.findItem(R.id.action_new_post);
        if(sectionNumber != 0){
            newPost.setVisible(false);
        }
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

    public void viewProfile(MenuItem item) {
        startActivity(new Intent(this, DisplayActivity.class));
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
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getActivity().invalidateOptionsMenu();
            Log.e("CONTAINER ID: ", ""+ sectionNumber);
            if(sectionNumber == 0) {

                //Create view
                final int topOfStack[] = {-1};
                View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
                final CardContainer mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
                final SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());


                //Get Location
                SimpleLocation location = new SimpleLocation(getActivity());
                double lati = 0;
                double longi = 0;
                if (location.hasLocationEnabled()) {
                    location.beginUpdates();
                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    System.out.println("Lat ad lon are"+lati+longi);
                    location.endUpdates();
                } else {
                    location.openSettings(getActivity());
                }

                final AsyncHttpClient mainclient = new AsyncHttpClient();
                String getURL = "http://gentle-bayou-7778.herokuapp.com/android/populatePlace?lat="+
                        lati+
                        "&lon="+
                        longi+
                        "&user="+
                        pref.getString("USER_ID", "");
                Log.e("GetURL", getURL);
                mainclient.get(getURL,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String res = new String(responseBody);
                        final String[] postArr = res.split("`;");
                        System.out.println("HAHA"+ postArr[0]);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                                pb.setVisibility(View.GONE);
                            }
                        });
                        Log.e("postArr.length", ""+postArr.length);
                        final boolean clientsDone[] = new boolean[postArr.length];
                        for(int i = 0; i < postArr.length; i++) {
                            String postitem = postArr[i];
                            final int index = i;
                            clientsDone[index] = false;
                            topOfStack[0]++;
                            final String[] items = postitem.split("`,");
                            if (items.length == 1) continue;
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(items[2], new BinaryHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {}

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] fileData) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                                    CardModel cardModel = new CardModel(items[1], " ", new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 800, 600, false)));
                                    cardModel.setOnClickListener(new CardModel.OnClickListener() {
                                        @Override
                                        public void OnClickListener() {
                                            Log.i("places Swipeable Cards", "I am pressing the card");
                                            liked = false;
                                            disliked = false;

                                            Timer timer = new Timer();

                                            Log.e("Timer", "set");
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (liked) {
                                                        Log.e("places Swipeable Cards", "I like the card " + topOfStack[0] + items[1]);
                                                        SyncHttpClient client = new SyncHttpClient();
                                                        String likeurl = "http://gentle-bayou-7778.herokuapp.com/android/upvotePlace?place=" + items[0] + "&user=" + pref.getString("USER_ID", "");
                                                        client.get(likeurl, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                System.out.println("sucessfully liked shiz00");
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                            }
                                                        });
                                                        topOfStack[0]--;
                                                    } else if (disliked) {
                                                        Log.e("places Swipeable Cards", "I dislike the card " + topOfStack[0]);
                                                        SyncHttpClient client = new SyncHttpClient();
                                                        String likeurl = "http://gentle-bayou-7778.herokuapp.com/android/downvotePlace?place=" + items[0] + "&user=" + pref.getString("USER_ID", "");
                                                        client.get(likeurl, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                System.out.println("sucessfully disliked shiz00");
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                            }
                                                        });
                                                        topOfStack[0]--;
                                                    } else {
                                                        SharedPreferences.Editor edit = pref.edit();
                                                        edit.putString("PLACE_ID", items[0]);
                                                        edit.commit();
                                                        Log.e("places Swipeable cards", "I clicked the card " + topOfStack[0]);
                                                        startActivity(new Intent(getActivity(), PlacesDisplayActivity.class));
                                                    }
                                                }
                                            }, 1000);
                                        }
                                    });
                                    Log.e("on dismisslistener", "set");

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
                                    Log.e("cardmodel", "added to adapter");

                                    synchronized(adapter){
                                        clientsDone[index] = true;
                                        boolean allDone = true;
                                        adapter.add(cardModel);
                                        for(boolean individualDone : clientsDone){
                                            allDone = allDone && individualDone;
                                        }
                                        if(allDone)
                                            mCardContainer.setAdapter(adapter);
                                    }
                                }
                            });
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

                return rootView;
            }else{

                //Create view
                final int topOfStack[] = {-1};
                View rootView = inflater.inflate(R.layout.fragment_cards, container, false);
                final CardContainer mCardContainer = (CardContainer) rootView.findViewById(R.id.layoutview);
                final SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());


                //Get Location
                SimpleLocation location = new SimpleLocation(getActivity());
                double lati = 0;
                double longi = 0;
                if (location.hasLocationEnabled()) {
                    location.beginUpdates();
                    lati = location.getLatitude();
                    longi = location.getLongitude();
                    System.out.println("Lat ad lon are"+lati+longi);
                    location.endUpdates();
                } else {
                    location.openSettings(getActivity());
                }

                final AsyncHttpClient mainclient = new AsyncHttpClient();
                String getURL = "http://gentle-bayou-7778.herokuapp.com/android/populatePost?lat="+
                        lati+
                        "&lon="+
                        longi+
                        "&user="+
                        pref.getString("USER_ID", "");
                Log.e("GetURL", getURL);
                mainclient.get(getURL,new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String res = new String(responseBody);
                        final String[] postArr = res.split("`;");
                        System.out.println("HAHA"+ postArr[0]);
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                ProgressBar pb = (ProgressBar) getActivity().findViewById(R.id.progressBar);
                                pb.setVisibility(View.GONE);
                            }
                        });
                        Log.e("postArr.length", ""+postArr.length);
                        final boolean clientsDone[] = new boolean[postArr.length];
                        for(int i = 0; i < postArr.length; i++) {
                            String postitem = postArr[i];
                            final int index = i;
                            clientsDone[index] = false;
                            topOfStack[0]++;
                            final String[] items = postitem.split("`,");
                            if (items.length == 1) continue;
                            AsyncHttpClient client = new AsyncHttpClient();
                            client.get(items[2], new BinaryHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {}

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] fileData) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(fileData, 0, fileData.length);
                                    CardModel cardModel = new CardModel(items[1], " ", new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 800, 600, false)));
                                    cardModel.setOnClickListener(new CardModel.OnClickListener() {
                                        @Override
                                        public void OnClickListener() {
                                            Log.i("posts Swipeable Cards", "I am pressing the card");
                                            liked = false;
                                            disliked = false;

                                            Timer timer = new Timer();

                                            Log.e("Timer", "set");
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (liked) {
                                                        Log.e("posts Swipeable Cards", "I like the card " + topOfStack[0] + items[1]);
                                                        SyncHttpClient client = new SyncHttpClient();
                                                        String likeurl = "http://gentle-bayou-7778.herokuapp.com/android/upvotePost?place=" + items[0] + "&user=" + pref.getString("USER_ID", "");
                                                        client.get(likeurl, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                System.out.println("sucessfully liked shiz00");
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                            }
                                                        });
                                                        topOfStack[0]--;
                                                    } else if (disliked) {
                                                        Log.e("places Swipeable Cards", "I dislike the card " + topOfStack[0]);
                                                        SyncHttpClient client = new SyncHttpClient();
                                                        String likeurl = "http://gentle-bayou-7778.herokuapp.com/android/downvotePost?place=" + items[0] + "&user=" + pref.getString("USER_ID", "");
                                                        client.get(likeurl, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                System.out.println("sucessfully disliked shiz00");
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                            }
                                                        });
                                                        topOfStack[0]--;
                                                    } else {
                                                        SharedPreferences.Editor edit = pref.edit();
                                                        edit.putString("PLACE_ID", items[0]);
                                                        edit.commit();
                                                        Log.e("posts Swipeable cards", "I clicked the card " + topOfStack[0]);
                                                        startActivity(new Intent(getActivity(), PostsDisplayActivity.class));
                                                    }
                                                }
                                            }, 1000);
                                        }
                                    });
                                    Log.e("on dismisslistener", "set");

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
                                    Log.e("cardmodel", "added to adapter");

                                    synchronized(adapter){
                                        clientsDone[index] = true;
                                        boolean allDone = true;
                                        adapter.add(cardModel);
                                        for(boolean individualDone : clientsDone){
                                            allDone = allDone && individualDone;
                                        }
                                        if(allDone)
                                            mCardContainer.setAdapter(adapter);
                                    }
                                }
                            });
                        }

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

                return rootView;
            }
        }
    }

    public void newPost(MenuItem m){
        startActivity(new Intent(this, NewPlaceActivity.class));
    }
}
