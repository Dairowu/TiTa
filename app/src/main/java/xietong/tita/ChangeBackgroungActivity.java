package xietong.tita;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileNotFoundException;

/**
 * Created by acer-PC on 2015/8/5.
 * 用来实现所有界面的换肤功能
 */
public class ChangeBackgroungActivity extends Activity implements View.OnClickListener {

    LinearLayout layoutThis;
    Button bnDefault, bnSetBack, bnBack;
    ImageView backgroundNow;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_background);
        FinishApp.addActivity(ChangeBackgroungActivity.this);

        bnBack = (Button) findViewById(R.id.bnBackgroundBack);
        bnDefault = (Button) findViewById(R.id.bnBackgroundDefault);
        bnSetBack = (Button) findViewById(R.id.bnSetBackground);
        backgroundNow = (ImageView) findViewById(R.id.backgroundNow);
        layoutThis = (LinearLayout) findViewById(R.id.layoutChangeBack);

        bnBack.setOnClickListener(this);
        bnDefault.setOnClickListener(this);
        bnSetBack.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("music_play", MODE_WORLD_READABLE);
        editor = sharedPreferences.edit();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.bnBackgroundBack:
                Intent intentToMain = new Intent(ChangeBackgroungActivity.this, MainActivity.class);
                intentToMain.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intentToMain);
                break;

            //点击恢复默认
            case R.id.bnBackgroundDefault:
                Resources r = getResources();
                Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + r.getResourcePackageName(R.drawable.bg) + "/"
                        + r.getResourceTypeName(R.drawable.bg) + "/"
                        + r.getResourceEntryName(R.drawable.bg));
                editor.putString("background",uri.toString());
                editor.commit();
                Toast.makeText(ChangeBackgroungActivity.this,"恢复默认成功",Toast.LENGTH_SHORT).show();
                break;

            //点击启动系统的Galley并且获取一张图片返回
            case R.id.bnSetBackground:
                Intent intentGetUri = new Intent(Intent.ACTION_PICK);
                intentGetUri.setType("image/*");
                startActivityForResult(intentGetUri, 0);
                break;
        }
    }

    //处理返回的图片
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0 && resultCode == -1) {
            Uri uri = data.getData();
            backgroundNow.setImageURI(uri);

            Drawable d = null;
            try {
                d = Drawable.createFromStream(getContentResolver().openInputStream(uri), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            editor.putString("background", uri.toString());
            editor.commit();
            Toast.makeText(ChangeBackgroungActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        Drawable drawable = Utils.getDrawableBackground(ChangeBackgroungActivity.this);
        if (drawable != null){
            backgroundNow.setImageDrawable(drawable);
        }
        super.onResume();
    }
}
