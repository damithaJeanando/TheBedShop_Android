package com.example.thebedshop.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.thebedshop.R;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView imageView;
    private Toolbar toolbar;
    private Boolean isWhite = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView = findViewById(R.id.img_view_image);
        toolbar = findViewById(R.id.toolbar_view_img);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(Color.rgb(0,0,0));

        String prodImg = getIntent().getStringExtra("productImg");
        Glide.with(this).load(prodImg).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWhite) {
                    v.setBackgroundColor(Color.rgb(0, 0, 0));
                    toolbar.setTitleTextColor(Color.rgb(255,255,255));

                    isWhite = false;
                }else{
                    v.setBackgroundColor(Color.rgb(255,255,255));
                    toolbar.setTitleTextColor(Color.rgb(0,0,0));

                    isWhite = true;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
