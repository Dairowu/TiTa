package guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import xietong.tita.MainActivity;
import xietong.tita.R;

/**
 * Created by Administrator on 2015/8/8.
 */
public class dsajkzajfeaukfa extends PagerAdapter {

    private List<View> viewList;
    private Activity activity;
    private static final String SHAREDPREFERENCES_NAME = "fitst_pref";

    public dsajkzajfeaukfa(List<View> viewList, Activity activity){

        this.viewList = viewList;
        this.activity = activity;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewList.get(position), 0);
        if(position==viewList.size()-1){
            Button go_home = (Button)container.findViewById(R.id.go_home);
            go_home.setVisibility(View.VISIBLE);
            go_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGuide();
                    goHome();
                }
            });
        }
        return viewList.get(position);
    }

    public void setGuide(){
        SharedPreferences preferences = activity.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn",false);
        editor.commit();
    }

    public void goHome(){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
        activity.finish();}

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewList.get(position));
    }

    @Override
    public void finishUpdate(View container) {
        super.finishUpdate(container);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}
