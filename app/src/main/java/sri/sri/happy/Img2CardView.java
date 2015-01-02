package sri.sri.happy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import com.andtinder.model.CardModel;
import java.io.InputStream;


public class Img2CardView extends AsyncTask<String, Void, Bitmap> {
    CardModel cm;
    Context context;
    public Img2CardView(Context _context, CardModel _cm) {
        this.context = _context;
        this.cm = _cm;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];

        Log.e("url", urldisplay);
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        Log.e("On Post Execute", "Img2CardView");
        cm.setCardImageDrawable(new BitmapDrawable(context.getResources(), result));
    }
}
