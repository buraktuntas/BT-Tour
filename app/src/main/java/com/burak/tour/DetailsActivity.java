package com.burak.tour;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.burak.tour.adapter.ViewPagerAdapter;
import com.burak.tour.R;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DetailsActivity extends AppCompatActivity {
    TextView createdAt,name,city,date,category,price,tourid;
    ImageView avatar;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private Toolbar mTopToolbar;
    private ArrayList<String> imageUrls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mTopToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mTopToolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        createdAt=findViewById(R.id.createdAt);
        name=findViewById(R.id.name);
        avatar=findViewById(R.id.avatar);
        city=findViewById(R.id.city);
        date=findViewById(R.id.date);
        category=findViewById(R.id.category);
        price=findViewById(R.id.price);
        tourid=findViewById(R.id.tourid);

        Intent intent=getIntent();
        String name_data=intent.getStringExtra("name");
        String createdAt_data=intent.getStringExtra("createdAt");
        String avatar_data=intent.getStringExtra("avatar");
        String city_data=intent.getStringExtra("city");
        String date_data=intent.getStringExtra("date");
        String category_data=intent.getStringExtra("category");
        String price_Data=intent.getStringExtra("price");
        String tourid_Data=intent.getStringExtra("tourid");

        for(int j=0; j<3; j++){
            imageUrls.add(j,avatar_data);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, imageUrls);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);


        createdAt.setText("Created At\n"+
                createdAt_data);
        name.setText(name_data+" - "+city_data);
        city.setText("City\n"+city_data);
        date.setText("Date\n"+date_data);
        category.setText("Category\n"+category_data);
        price.setText("$"+price_Data);
        tourid.setText("Tour Id\n"+tourid_Data);


        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            DetailsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(viewPager.getCurrentItem() == 0){
                        viewPager.setCurrentItem(1);
                    } else if(viewPager.getCurrentItem() == 1){
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }

                }
            });

        }
    }
}
