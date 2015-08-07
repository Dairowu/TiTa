package xietong.tita;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer-PC on 2015/8/6.
 * 在加载每一个Activity时调用addActivity的方法
 * 当点击抽屉的退出时，调用finishAllActivity的方法，
 *       结束掉所有的Activity并且关掉service和service里面的线程
 */
public class FinishApp extends Application {

    private static List<Activity> activityList = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void finishAllActivity(Context context) {
        for (Activity activity : activityList) {
            if (!activity.isFinishing())
                activity.finish();
        }
        MyNotification.finishNotify();
        Log.e("FinishApp", "发送消息");
        Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
        intent.putExtra("finish", true);
        context.sendBroadcast(intent);

//        Intent intentoLock = new Intent(Intent.ACTION_SCREEN_OFF);
//        intentoLock.putExtra("finish",true);
//        context.sendBroadcast(intentoLock);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public FinishApp() {
        super();
    }


}
