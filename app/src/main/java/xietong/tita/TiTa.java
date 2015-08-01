package xietong.tita;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;

import xietong.tita.Util.GetData;

/**
 * */
public class TiTa extends AppCompatActivity implements AdapterView.OnItemClickListener {


    //    设置标签
    private static final String TAG = "com.xietong.tita";
//    左边滑出列表的listview
    private ListView leftNavList;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
//    private Character DrawerTitle;
//    private Character mTitle;
//    记录当前选中的选项，默认为第一个
    private int id_now=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ti_ta);

        leftNavList = (ListView) findViewById(R.id.leftNavList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
//      初始化左边的导航栏
        initList(getResources().getStringArray(R.array.navlist));
//        高亮显示第一个选项
//        leftNavList.getChildAt(id_now).setSelected(true);
        initDrawer();
//      getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        leftNavList.setOnItemClickListener(this);


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
    private void initList(String[] data) {
        SimpleAdapter adapter = new SimpleAdapter(this, new GetData().getDate(data),
                R.layout.left_nav, new String[]{"music", "icon"}, new int[]{R.id.left_title, R.id.left_icon});
        leftNavList.setAdapter(adapter);
    }

    //    初始化滑动监听
    private void initDrawer() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.menu, R.string.title, R.string.title) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               // invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mToggle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        leftNavList.getChildAt(id_now).setSelected(false);
        id_now=position;
        leftNavList.getChildAt(id_now).setSelected(true);
        Log.v(TAG,""+id_now+"防止报错");
    }
}
