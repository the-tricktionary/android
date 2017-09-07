package trictionary.jumproper.com.jumpropetrictionary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;

/**
 * Created by jumpr_000 on 1/15/2017.
 */

public class DownloadImageTask extends AsyncTask<String, Void, ImageView> {
    Bitmap bmImage;
    FirebaseAuth mAuth;
    ImageView current;

    public DownloadImageTask(FirebaseAuth mAuth,ImageView current) {
        this.mAuth = mAuth;
        this.current = current;
    }

    protected ImageView doInBackground(String... urls) {
        String urldisplay = urls[0];
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return current;
    }

    protected void onPostExecute(ImageView current) {
        if(bmImage==null){
            return;
        }
        current.setImageBitmap(getRoundedShape(bmImage));
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
            int targetWidth = 100;
            int targetHeight = 100;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);

            canvas.clipPath(path);
            Bitmap sourceBitmap = scaleBitmapImage;
            canvas.drawBitmap(sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(),
                            sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);
            return targetBitmap;
    }
}
