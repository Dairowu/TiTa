package xietong.tita;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import constant.DbFinal;

/**
 * Created by acer-PC on 2015/8/3.
 * revised by CJJ on 2015/8/4.
 */
public class MyBaseAdapter extends BaseAdapter {
    // 上下文
    private Context context;
    // 数据源
    private Cursor cursor;
    // 用于判断checkbox
    private List<Boolean> popCheckStatus;
    // 正在播放的歌曲位置
    private int selectItem = -1;
    private List<Map<String, Object>> songLists;
//     List<Map<String, Object>> songLists, Utils.getList()

    public MyBaseAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.songLists = songLists;
        this.cursor = cursor;
        if (cursor != null) {
            popCheckStatus = new ArrayList<Boolean>();
            for (int i = 0; i < cursor.getCount(); i++) {
                popCheckStatus.add(false);
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub\
        if (cursor.equals(null)){
            return 0;
        }
        return
        cursor.getCount();
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


        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.music_listview_item, null);


            holder.title = (TextView) convertView.
                    findViewById(R.id.music_listitem_song_text);
            holder.artist = (TextView) convertView.
                    findViewById(R.id.music_listitem_artist_text);
            holder.popCheck = (CheckBox) convertView
                    .findViewById(R.id.music_listitem_action);
            holder.popdown = (LinearLayout) convertView
                    .findViewById(R.id.local_popdown);
            holder.favor = (Button) convertView
                    .findViewById(R.id.popdown_favor);
            holder.detail = (Button) convertView
                    .findViewById(R.id.popdown_detail);
//            holder.ringtone = (Button) convertView
//                    .findViewById(R.id.popdown_ringtone);
            holder.delete = (Button) convertView
                    .findViewById(R.id.popdown_del);
            holder.indicator = (ImageView) convertView
                    .findViewById(R.id.music_listitem_indicator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectItem) {
            holder.indicator.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(Color.argb(80, 0x00, 0xff, 0xff));
        } else {
            holder.indicator.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        cursor.moveToPosition(position);
        holder.title.setText(cursor.getString(cursor
                .getColumnIndex(DbFinal.LOCAL_TITLE)));
        holder.artist.setText(cursor.getString(cursor
                .getColumnIndex(DbFinal.LOCAL_ARTIST)));
        holder.popdown.setVisibility(View.GONE);

        if (popCheckStatus.get(position)) {
            holder.popdown.setVisibility(View.VISIBLE);
        } else {
            holder.popdown.setVisibility(View.GONE);
        }
        holder.popCheck.setTag(position);
        holder.favor.setTag(position);
        holder.detail.setTag(position);
//        holder.ringtone.setTag(position);
        holder.delete.setTag(position);
        holder.popCheck.setChecked(popCheckStatus.get(position));
        // checkbox为选中时，显示popdown
        holder.popCheck
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        popCheckStatus.set((Integer) buttonView.getTag(),
                                isChecked);
                        if (isChecked) {
                            holder.popdown.setVisibility(View.VISIBLE);
                        } else {
                            holder.popdown.setVisibility(View.GONE);
                        }
                    }
                });
        // 设置我喜欢按钮点击事件
        holder.favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("favor", "click");
//                Toast.makeText(context, "我喜欢", Toast.LENGTH_SHORT).show();
            }
        });
        // 设置歌曲信息按钮点击事件
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("detail", "click");
//                int p = (Integer) v.getTag();
//                MusicDBHelper helper = new MusicDBHelper(context, DbFinal.DB_NAME, null, DbFinal.DB_VERSION);
//                ArrayList<MusicInfo> musicList = helper
//                        .getMusicListFromLocal(cursor);
//                MusicInfo m = musicList.get(p);
//                String title = m.getTitle();
//                String artist = m.getArtist();
//                String album = m.getAlbum();
//                String size = m.getSize() / 1024.0f / 1024.0f + "";
//                size = size.substring(0, 3) + "M";
//                String duration = m.getDuration() / 1000.0f / 60.0f + "";
//                duration = duration.substring(0, 4) + "分";
//                String path = m.getPath();
//                ToastShow.toastShow(context, "歌曲名称：" + title + "\n歌手：" + artist
//                        + "\n专辑：" + album + "\n歌曲大小：" + size + "\n歌曲时长："
//                        + duration + "\n文件路径：" + path);
            }
        });
        // 设置删除按钮点击事件
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete","click");
//                Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    //        convertView = LayoutInflater.from(context).inflate
//                (R.layout.music_listview_item, null);
//
//        TextView songShow = (TextView) convertView.findViewById(R.id.music_listitem_song_text);
//        TextView songPath = (TextView) convertView.findViewById(R.id.music_listitem_artist_text);
//        songShow.setText(songLists.get(position).get("songTitle").toString());
//        songPath.setText(songLists.get(position).get("songArtist").toString());
//
//        if (position == selectItem) {
//            convertView.setBackgroundColor(Color.argb(80, 0x00, 0xff, 0xff));
//        } else {
//            convertView.setBackgroundColor(Color.TRANSPARENT);
//        }
//
//        return convertView;

    private class ViewHolder {
        TextView title;
        TextView artist;
        CheckBox popCheck;
        LinearLayout popdown;
        Button favor, detail, ringtone, delete;
        ImageView indicator;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public int getSelectItem() {
        return selectItem;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;

    }

}

