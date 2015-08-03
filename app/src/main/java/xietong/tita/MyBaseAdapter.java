package xietong.tita;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by acer-PC on 2015/8/3.
 */
public class MyBaseAdapter extends BaseAdapter {
    private int selectItem = -1;
    private List<Map<String, Object>> songLists;

    Context context;
    public MyBaseAdapter(Context context,List<Map<String, Object>> songLists) {
        this.context = context;
        this.songLists = songLists;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songLists.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate
                (R.layout.music_listview_item, null);

        TextView songShow = (TextView) convertView.findViewById(R.id.music_listitem_song_text);
        TextView songPath = (TextView) convertView.findViewById(R.id.music_listitem_artist_text);
        songShow.setText(songLists.get(position).get("songTitle").toString());
        songPath.setText(songLists.get(position).get("songArtist").toString());


        if (position == selectItem) {
            convertView.setBackgroundColor(Color.argb(80, 0x00, 0xff, 0xff));
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }


        return convertView;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

}

