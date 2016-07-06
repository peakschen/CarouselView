package com.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.carouselview.CarouselView;
import com.imageloader.ImageLoaderUtils;
import com.util.ConvertUtils;

public class MainActivity extends AppCompatActivity {

    private String[] mImageUrls = {
            "http://p0.so.qhimg.com/sdr/200_200_/t0135b6f66226f51c4f.jpg",
            "http://p0.so.qhimg.com/sdr/200_200_/t01f6f0dd0be69bb225.jpg",
            "http://p3.so.qhimg.com/sdr/200_200_/t0172b38e771f227643.jpg",
            "http://p0.so.qhimg.com/sdr/200_200_/t01635603130ec7b415.jpg"
    };
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInflater = LayoutInflater.from(this);

        CarouselView carouselView = (CarouselView)findViewById(R.id.CarouselView);
        carouselView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dip2px(this,200)));
        carouselView.setAdapter(new CarouselView.Adapter() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public View getView(int position) {
                View view = mInflater.inflate(R.layout.item,null);
                ImageView imageView = (ImageView) view.findViewById(R.id.image);
                ImageLoaderUtils.loadBitmap(mImageUrls[position],imageView);
                return view;
            }

            @Override
            public int getCount() {
                return mImageUrls.length;
            }
        });
    }
}
