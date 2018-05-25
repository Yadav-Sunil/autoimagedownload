package com.autodownloadimages.utilites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.autodownloadimages.BuildConfig;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by bitu on 10/9/17.
 */

public class ImageDownloadHelper {

    static ImageDownloadHelper instance;

    static {

        instance = new ImageDownloadHelper();
    }

    public static ImageDownloadHelper getInstance() {
        return instance;
    }


    private void printLog(String msg) {
        if (BuildConfig.DEBUG && msg != null) {
            //   Log.e(TAG, msg);
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
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
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

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imageView.setImageDrawable(placeHolderDrawable);
            }
        };
        return target;
    }

    /*public void loadBlurImage (Context context, final ImageView imageView, final String url) {
        if (!checkValidString(url)) return;
        String filePath = generateFIlePathForUrl(context, url);
        if (!checkValidString(filePath)) return;


        int index = filePath.lastIndexOf("/");
        String filename = filePath.substring(index + 1);
        String parent = filePath.substring(0, index);
        String blurFilePath = parent + "/blur_" + filename;
        printLog("url=" + url + "\n" + " localFile=" + filePath + " BlurLocalFile=" + blurFilePath);

        final File file = new File(filePath);
        final File blurFile = new File(blurFilePath);
        if (blurFile.exists()) {
            Ion.with(context).load(blurFile).intoImageView(imageView).setCallback(new FutureCallback<ImageView>() {
                @Override
                public void onCompleted (Exception e, ImageView result) {
                    if (e != null) {
                        printLog("exception=" + e.getMessage());
                        if (blurFile != null && blurFile.exists()) {
                            blurFile.delete();
                        }
                    }
                }
            });
        } else {
            if (file.exists()) {
                printLog("blur file not exist load from localFile");
                Ion.with(context).load(filePath).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted (Exception e, Bitmap result) {
                        if (e != null) {
                            printLog("exception=" + e.getMessage());
                        } else {
                            try {
                                result = BlurImage.fastblur(result, 2, 8);
                                result.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(blurFile));
                                imageView.setImageBitmap(result);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                });
            } else {
                printLog("blur file and local file not exist load from server");
                loadBlurImageFromServer(imageView, url, filePath, blurFilePath);
            }

        }

    }*/

    /*public void loadBlurImageFromServer (final ImageView imageView, final String url,
                                         final String filePath, final String blurFilePath) {
        File downloadPath = new File(filePath);
        File downloadParent = downloadPath.getParentFile();
        if (!downloadParent.exists()) {
            downloadParent.mkdirs();
        }


        Ion.with(imageView.getContext()).load(url).write(new File(filePath)).setCallback(new FutureCallback<File>() {
            @Override
            public void onCompleted (Exception e, File result) {
                if (e != null) {
                    printLog("exception=" + e.getMessage());
                    if (result != null && result.exists()) {
                        result.delete();
                    }
                } else {
                    Ion.with(imageView.getContext()).load(filePath).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted (Exception e, Bitmap result) {
                            if (e != null) {
                                printLog("exception=" + e.getMessage());
                            } else {
                                printLog("blur file load complete from server " + "\n"
                                        + "url=" + url + "\n" + " localFile=" + filePath + "\n" + " blurFile=" + blurFilePath);
                                try {
                                    result = BlurImage.fastblur(result, 2, 8);
                                    result.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(blurFilePath));
                                    imageView.setImageBitmap(result);
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }
                    });
                }
            }
        });

    }*/

    private static class BlurImage {
        public static Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

            int width = Math.round(sentBitmap.getWidth() * scale);
            int height = Math.round(sentBitmap.getHeight() * scale);
            sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            if (radius < 1) {
                return (null);
            }

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int[] pix = new int[w * h];
            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.getPixels(pix, 0, w, 0, 0, w, h);

            int wm = w - 1;
            int hm = h - 1;
            int wh = w * h;
            int div = radius + radius + 1;

            int r[] = new int[wh];
            int g[] = new int[wh];
            int b[] = new int[wh];
            int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
            int vmin[] = new int[Math.max(w, h)];

            int divsum = (div + 1) >> 1;
            divsum *= divsum;
            int dv[] = new int[256 * divsum];
            for (i = 0; i < 256 * divsum; i++) {
                dv[i] = (i / divsum);
            }

            yw = yi = 0;

            int[][] stack = new int[div][3];
            int stackpointer;
            int stackstart;
            int[] sir;
            int rbs;
            int r1 = radius + 1;
            int routsum, goutsum, boutsum;
            int rinsum, ginsum, binsum;

            for (y = 0; y < h; y++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                for (i = -radius; i <= radius; i++) {
                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
                    sir = stack[i + radius];
                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);
                    rbs = r1 - Math.abs(i);
                    rsum += sir[0] * rbs;
                    gsum += sir[1] * rbs;
                    bsum += sir[2] * rbs;
                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }
                }
                stackpointer = radius;

                for (x = 0; x < w; x++) {

                    r[yi] = dv[rsum];
                    g[yi] = dv[gsum];
                    b[yi] = dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (y == 0) {
                        vmin[x] = Math.min(x + radius + 1, wm);
                    }
                    p = pix[yw + vmin[x]];

                    sir[0] = (p & 0xff0000) >> 16;
                    sir[1] = (p & 0x00ff00) >> 8;
                    sir[2] = (p & 0x0000ff);

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[(stackpointer) % div];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi++;
                }
                yw += w;
            }
            for (x = 0; x < w; x++) {
                rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
                yp = -radius * w;
                for (i = -radius; i <= radius; i++) {
                    yi = Math.max(0, yp) + x;

                    sir = stack[i + radius];

                    sir[0] = r[yi];
                    sir[1] = g[yi];
                    sir[2] = b[yi];

                    rbs = r1 - Math.abs(i);

                    rsum += r[yi] * rbs;
                    gsum += g[yi] * rbs;
                    bsum += b[yi] * rbs;

                    if (i > 0) {
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                    } else {
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                    }

                    if (i < hm) {
                        yp += w;
                    }
                }
                yi = x;
                stackpointer = radius;
                for (y = 0; y < h; y++) {
                    // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                    pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                    rsum -= routsum;
                    gsum -= goutsum;
                    bsum -= boutsum;

                    stackstart = stackpointer - radius + div;
                    sir = stack[stackstart % div];

                    routsum -= sir[0];
                    goutsum -= sir[1];
                    boutsum -= sir[2];

                    if (x == 0) {
                        vmin[y] = Math.min(y + r1, hm) * w;
                    }
                    p = x + vmin[y];

                    sir[0] = r[p];
                    sir[1] = g[p];
                    sir[2] = b[p];

                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];

                    rsum += rinsum;
                    gsum += ginsum;
                    bsum += binsum;

                    stackpointer = (stackpointer + 1) % div;
                    sir = stack[stackpointer];

                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];

                    rinsum -= sir[0];
                    ginsum -= sir[1];
                    binsum -= sir[2];

                    yi += w;
                }
            }

            Log.e("pix", w + " " + h + " " + pix.length);
            bitmap.setPixels(pix, 0, w, 0, 0, w, h);

            return (bitmap);
        }
    }
}
