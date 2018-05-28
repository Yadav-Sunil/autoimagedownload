package com.foji.downloadimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Sunil kumar Yadav
 * @Since 25/5/18
 */
public class DownloadImageHelper {

    static DownloadImageHelper instance;

    static {
        instance = new DownloadImageHelper();
    }

    public static DownloadImageHelper getInstance() {
        return instance;
    }


    private void printLog(String msg) {
        if (BuildConfig.DEBUG && msg != null) {
            Log.e("ImageDownload ", msg);
        }
    }

    private boolean checkValidString(String data) {
        return data != null && !data.trim().isEmpty();
    }

    public void loadImage(Context context, ImageView imageView, String url) {
        if (!checkValidString(url)) return;
        String filePath = generateFIlePathForUrl(context, url);
        if (!checkValidString(filePath)) return;
        printLog("url=" + url + "\n" + " localFile=" + filePath);
        File file = new File(filePath);
        if (file.exists()) {
            imageView.setImageURI(Uri.parse(filePath));
        } else {
            printLog("file not exist load from server");
            Picasso.get().load(url).into(imageView);
            loadImageFromServer(imageView, url, filePath);
        }
    }

    public void loadImageFromServer(ImageView imageView, String url, String filePath) {
        File downloadPath = new File(filePath);
        File downloadParent = downloadPath.getParentFile();
        if (!downloadParent.exists()) {
            downloadParent.mkdirs();
        }
        Picasso.get().load(url).into(getTarget(imageView, filePath));
    }

    private String generateFIlePathForUrl(Context context, String url) {
        if (!checkValidString(url)) return null;

        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        int index = url.lastIndexOf("/");
        String filename = url.substring(index + 1);
        String parent = url.substring(0, index);
        index = parent.lastIndexOf("/");
        String parentFolder = parent.substring(index + 1);
        String superParent = parent.substring(0, index);
        index = superParent.lastIndexOf("/");
        String superParentFolder = superParent.substring(index + 1);
        return new File(dir, superParentFolder + "/" + parentFolder + "/" + filename).getAbsolutePath();
    }

    //target to save
    private Target getTarget(final ImageView imageView, final String filePath) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        File file = new File(filePath);
                        try {
                            if (!file.exists()) {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                                ostream.flush();
                                ostream.close();
                            }
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

            /**
             * Callback indicating the image could not be successfully loaded.
             * <p>
             * <strong>Note:</strong> The passed {@link Drawable} may be {@code null} if none has been
             * @param e
             * @param errorDrawable
             */
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imageView.setImageDrawable(placeHolderDrawable);
            }
        };
        return target;
    }
}
