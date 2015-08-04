package xietong.tita;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by acer-PC on 2015/8/4.
 *
 *用来处理发送到通知栏的信息
 */
public class MyNotification  {

    private static RemoteViews remoteViews;
    private static NotificationManager notificationManager;
    private static int notifyId = 0;
    static Context context;
    static NotifyReceiver notifyReceiver;

    //加载RemoteViews并且设置打开应用时跳出的Notification
    public static void prepareNotification(Context fromContext) {

        //初始化remoteView
        context = fromContext;

        //注册Notification广播
        notifyReceiver = new NotifyReceiver();//----注册广播
        IntentFilter intentNotify = new IntentFilter();
        intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_LAST);
        intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_PLAY);
        intentNotify.addAction(Utils.NOTIFICATION_ITEM_BUTTON_NEXT);
        context.registerReceiver(notifyReceiver, intentNotify);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_notification);

        //注册Action，并为按钮添加监听，发送广播
        Intent intentLast = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_LAST);
        PendingIntent pendingIntentlast = PendingIntent.getBroadcast(context, 0,
                intentLast, 0);
        remoteViews.setOnClickPendingIntent(R.id.notifyPrevious, pendingIntentlast);

        Intent intentNext = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                intentNext, 0);
        remoteViews.setOnClickPendingIntent(R.id.notifyNext, pendingIntentNext);


        Intent intentPause = new Intent(Utils.NOTIFICATION_ITEM_BUTTON_PLAY);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context, 0,
                intentPause, 0);
        remoteViews.setOnClickPendingIntent(R.id.notifyPlay, pendingIntentPause);

        Intent intentActivity = new Intent(context, TiTa.class);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(context, 0,
                intentActivity, 0);
        remoteViews.setOnClickPendingIntent(R.id.notifLayout, pendingIntentActivity);

        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        //打开应用时的Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle("")
                .setTicker("尊享属于自己的音乐播放器")
                .setContentInfo("");
        notificationManager.notify(notifyId, builder.build());

    }

    //显示Notification
    public static void showNotifica(String songTitle, String songArtist, Bitmap bitmapStar) {

        remoteViews.setTextViewText(R.id.songName,songTitle);
        remoteViews.setTextViewText(R.id.songer,songArtist);
        remoteViews.setImageViewBitmap(R.id.imageStar,bitmapStar);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.app_icon)
                .setOngoing(true)
                .setContentTitle("")
                .setTicker(songArtist+"-"+songTitle)
                .setContentInfo("");

        notificationManager.notify(notifyId, builder.build());
    }

    //处理从Notification传来的广播
    public static class NotifyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Intent intentNotify = new Intent(Utils.ACTION_TO_MUSICSERVICE);
            String action = intent.getAction();
            if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_LAST)) {//----通知栏播放按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_LAST);

            } else if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_PLAY)) {//----通知栏播放按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_PLAY);
            } else if (action.equals(Utils.NOTIFICATION_ITEM_BUTTON_NEXT)) {//----通知栏下一首按钮响应事件
                intentNotify.putExtra("buttonChoose", Utils.BN_NEXT);
            }
            intentNotify.putExtra("progress", 0);
            context.sendBroadcast(intentNotify);
        }

    }

//    public static void unRegist(Context context){
//        context.unregisterReceiver(notifyReceiver);
//    }

}
