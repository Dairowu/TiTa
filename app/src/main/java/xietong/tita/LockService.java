package xietong.tita;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by acer-PC on 2015/8/7.
 */
public class LockService extends Service {

    private String TAG = "LockService";
    private Intent intentToActivity = null;
    private KeyguardManager keyguardManager = null;
    private KeyguardManager.KeyguardLock keyguardLock = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intentToActivity = new Intent(this,LockActivity.class);
        //必须加上这一句，否则会出错
        //因为并不是从一个Activity里面启动另一个Activity
        intentToActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        IntentFilter intentFilterLockOn = new IntentFilter(Intent.ACTION_SCREEN_ON);
        this.registerReceiver(screenOnReceiver, intentFilterLockOn);
        IntentFilter intentFilterLockOff = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(screenOffReceiver,intentFilterLockOff);
    }

    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"screenOn");
            keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();

        }
    };

    private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG,"screenOff");
            keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();
//            startActivity(intentToActivity);
        }
    };

    @Override
    public void onDestroy() {
        Log.e(TAG,"Destroying");
        super.onDestroy();
        this.unregisterReceiver(screenOffReceiver);
        this.unregisterReceiver(screenOnReceiver);
        //使服务常驻内存
        startService(new Intent(this,LockService.class));
    }



}
