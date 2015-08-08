package xietong.tita;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

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

    //在每个Activity启动时调用该方法，加载所有的Activity
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void finishAllActivity(Context context) {
        //遍历所有加载过的Activity，如果还存活就结束掉他们
        for (Activity activity : activityList) {
            if (!activity.isFinishing())
                activity.finish();
        }
        //通知栏清除
        MyNotification.finishNotify();
        //结束MusicService
        Intent intent = new Intent(Utils.ACTION_TO_MUSICSERVICE);
        intent.putExtra("finish", true);
        context.sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public FinishApp() {
        super();
    }


}
