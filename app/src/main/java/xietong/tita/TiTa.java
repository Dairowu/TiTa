package xietong.tita;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import xietong.tita.Util.GetData;


public class TiTa extends AppCompatActivity {


    ListView leftNavList;

    //左边导航列表数据
    String []leftNav;
    //左边导航栏的图标；
    int[] id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_ta);

        leftNavList=(ListView)findViewById(R.id.leftNavList);
        initList(getResources().getStringArray(R.array.navlist),getResources().getIntArray(R.array.icon));




        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ti_ta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    初始化列表显示
    private void initList(String[] data,int []id){
        SimpleAdapter adapter=new SimpleAdapter(this,new GetData().getDate(data,id),
                R.layout.left_nav,new String[]{"music","icon"},new int[]{R.id.title,R.id.left_icon});
        leftNavList.setAdapter(adapter);

    }

}
