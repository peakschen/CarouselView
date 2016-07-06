package com.carouselview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.test.R;
import com.util.ConvertUtils;

import java.util.Timer;
import java.util.TimerTask;

public class CarouselView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private Context context;
    private int totalCount =100;
    private int showCount;
    private int currentPosition =0;
    private ViewPager viewPager;
    private LinearLayout carouselLayout;
    private Adapter adapter;
    private int pageItemWidth;
    private boolean isUserTouched = false;
    private Timer mTimer = new Timer();

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (!isUserTouched) {
                currentPosition = (currentPosition + 1) % totalCount;
                handler.sendEmptyMessage(100);
            }
        }
    };

    public void cancelTimer(){
        if (this.mTimer!=null){
            this.mTimer.cancel();
        }
    }

    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler{
        private CarouselView mCarouselView;
        public MyHandler(CarouselView carouselView){
            this.mCarouselView = carouselView;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mCarouselView.currentPosition == mCarouselView.totalCount - 1) {
                mCarouselView.viewPager.setCurrentItem(mCarouselView.showCount - 1, false);
            } else {
                mCarouselView.viewPager.setCurrentItem(mCarouselView.currentPosition);
            }
        }
    }

    public CarouselView(Context context) {
        super(context);
        this.context = context;
    }

    public CarouselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private void init() {
        viewPager.setAdapter(null);
        carouselLayout.removeAllViews();
        if (adapter.isEmpty()){
            return;
        }
        int count = adapter.getCount();
        showCount = adapter.getCount();
        for (int i=0;i<count;i++){
            View view = new View(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pageItemWidth,pageItemWidth);
            params.setMargins(pageItemWidth,0,0,0);
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.carousel_layout_page);
            carouselLayout.addView(view);
        }
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.setCurrentItem(0);
        this.viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isUserTouched = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isUserTouched = false;
                        break;
                }
                return false;
            }
        });
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    public void setAdapter(Adapter adapter){
        this.adapter = adapter;
        if (adapter!=null){
            init();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_layout,null);
        this.viewPager = (ViewPager) view.findViewById(R.id.gallery);
        this.carouselLayout = (LinearLayout)view.findViewById(R.id.CarouselLayoutPage);
        pageItemWidth = ConvertUtils.dip2px(context,5);
        this.viewPager.addOnPageChangeListener(this);
        addView(view);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        int count = carouselLayout.getChildCount();
        for (int i = 0;i<count;i++){
            View view = carouselLayout.getChildAt(i);
            if(position%showCount==i){
                view.setSelected(true);
            }else {
                view.setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return totalCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= showCount;
            View view = adapter.getView(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
            int position = viewPager.getCurrentItem();
            if (position==0){
                position=showCount;
                viewPager.setCurrentItem(position,false);
            }else if (position==totalCount-1){
                position = showCount - 1;
                viewPager.setCurrentItem(position,false);
            }
        }
    }

    public interface Adapter{
        boolean isEmpty();
        View getView(int position);
        int getCount();
    }
}
