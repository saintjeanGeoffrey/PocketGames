package be.pocketgames.asyncTask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import be.pocketgames.utils.Tools;

public class UrlToBitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {
        return Tools.getBitmapFromURL(strings[0]);
    }
}
