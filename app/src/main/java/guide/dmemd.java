package guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xietong.tita.R;

/**
 * Created by Administrator on 2015/8/8.
 */
public class dmemd extends Activity implements ViewPager.OnPageChangeListener{

    private LinearLayout ll;
    private ImageView[] dots;
    private List<View> views;
    private ViewPager vp;
    private dsajkzajfeaukfa vpAdapter;

    private int currentIndex;

    private Map<Integer,View> map = new HashMap<Integer,View>();
    private static final int images_slide[] = {R.drawable.slide1,R.drawable.slide2,
            R.drawable.slide3,R.drawable.slide4,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.guide);
        initDots();
        initViews();
    }

    private void initDots(){
        ll = (LinearLayout)findViewById(R.id.ll);
        dots = new ImageView[images_slide.length];
        for(int i = 0;i<images_slide.length;i++){
            dots[i] = (ImageView)ll.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setTag(i);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    private void initViews(){
        views = new ArrayList<View>();
        for(int i =0;i<images_slide.length;i++){
            View view = getView(i,images_slide[i]);
            views.add(view);
        }

        vpAdapter = new dsajkzajfeaukfa(views,this);
        vp = (ViewPager)findViewById(R.id.pager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);

    }

    private View getView(int arg0,int image){
        View rowView = map.get(arg0);
        if(rowView==null){
            rowView = this.getLayoutInflater().inflate(R.layout.guide_item,null);
            ImageView image_item = (ImageView)rowView.findViewById(R.id.image);
            image_item.setBackgroundResource(image);

            map.put(arg0,rowView);
        }
        return rowView;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setCurrentDot(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {


    }

    private void setCurrentDot(int i){
        if(i<0||i>views.size()-1||currentIndex==i){
            return ;
        }
        dots[i].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = i;
    }
}
