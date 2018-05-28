package com.autodownloadimages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.foji.downloadimage.DownloadImageHelper;

public class MainActivity extends AppCompatActivity {

    ImageView image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image_view = findViewById(R.id.image_view);
        DownloadImageHelper.getInstance().loadImage(this, image_view, "https://i.imgur.com/yq3U4zB.jpg");
    }
}
