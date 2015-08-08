package xietong.tita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

/**
 * Created by acer-PC on 2015/8/6.
 *
 */
public class AppInfoActivity extends Activity implements View.OnClickListener{

    Button bnBack,bnUpdate,bnParner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appinfo);

        bnBack = (Button) findViewById(R.id.bnAppinfoBack);
        bnUpdate = (Button) findViewById(R.id.bnAppinfoUpdate);
        bnParner = (Button) findViewById(R.id.bnAppinfoParner);
        bnBack.setOnClickListener(this);
        bnUpdate.setOnClickListener(this);
        bnParner.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnAppinfoBack:
                Intent intent = new Intent(AppInfoActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;

            case R.id.bnAppinfoParner:
                final Snackbar snackbar = Snackbar.make(view, " 制作成员:吴德永" +
                        "、邓贺文、陈俊均 、李振初",Snackbar.LENGTH_LONG);
                snackbar.setAction("滑动消失", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;

            case R.id.bnAppinfoUpdate:
                final Snackbar snackbar2 = Snackbar.make(view,"暂无更新哦",Snackbar.LENGTH_LONG);
                snackbar2.setAction("滑动消失", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar2.dismiss();
                    }
                });
                snackbar2.show();
                break;
        }
    }
}
