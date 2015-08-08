package xietong.tita;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by acer-PC on 2015/8/7.
 * 锁屏界面
 */
public class LockActivity extends Activity implements View.OnClickListener,GestureDetector.OnGestureListener {

    ImageButton bnLockLast,bnLockPlay,bnLockNext;
    TextView textTitle,textArtist;
    GestureDetector detector;
    ListServiceReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        bnLockLast = (ImageButton) findViewById(R.id.bnLockLast);
        bnLockNext = (ImageButton) findViewById(R.id.bnLockNext);
        bnLockPlay = (ImageButton) findViewById(R.id.bnLockPlay);
        textTitle = (TextView) findViewById(R.id.textLockTitle);
        textArtist = (TextView) findViewById(R.id.textLockArtist);

        bnLockPlay.setOnClickListener(this);
        bnLockNext.setOnClickListener(this);
        bnLockLast.setOnClickListener(this);

        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        textArtist.setText(artist);
        textTitle.setText(title);

        //添加手势检测
        detector = new GestureDetector(this,this);

        //表示从Service接受到的Broadcast将在ServiceReciver处理
        receiver = new ListServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_TO_MAIN);
        registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bnLockLast:
                Utils.bnSendBroadcast(this,Utils.BN_LAST);
                break;
            case R.id.bnLockNext:
                Utils.bnSendBroadcast(this,Utils.BN_NEXT);
                break;
            case R.id.bnLockPlay:
                Utils.bnSendBroadcast(this,Utils.BN_PLAY);
                break;
        }
    }

    //处理从Service传来的广播
    public class ListServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
            String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
            textArtist.setText(artist);
            textTitle.setText(title);
            //修改播放按钮的显示
            if (Utils.getStatus()!=Utils.PLAYING){
                bnLockPlay.setSelected(false);
            }
            else {
                bnLockPlay.setSelected(true);
            }
        }
    }

        @Override
    protected void onResume() {
        String artist = Utils.getList().get(Utils.getCurrentSong()).get("songArtist").toString();
        String title = Utils.getList().get(Utils.getCurrentSong()).get("songTitle").toString();
        textArtist.setText(artist);
        textTitle.setText(title);

        if (Utils.getStatus()!=Utils.PLAYING){
            bnLockPlay.setSelected(false);
        }
        else {
            bnLockPlay.setSelected(true);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(motionEvent1.getX()-motionEvent.getX()>100){
            this.finish();
        }
        return false;
    }
}
