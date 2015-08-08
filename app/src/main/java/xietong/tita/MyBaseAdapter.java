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

import Dialog.InfoDialog;
import Dialog.TVAnimDialog;
import SQLite.MusicDBHelper;
import constant.DbFinal;
import info.MusicInfo;

/**
 * Created by acer-PC on 2015/8/3.
 * revised by CJJ on 2015/8/4.
 */
public class MyBaseAdapter extends BaseAdapter {
    public static final int DIALOG_DISMISS = 0;// 对话框消失
    // 上下文
    private Context context;
    // 数据源
    private Cursor cursor;
    // 用于判断checkbox
    private List<Boolean> popCheckStatus;
    // 正在播放的歌曲位置
    private int selectItem = -1;
    private List<Map<String, Object>> songLists;
    ArrayList<MusicInfo> songInfo;
//     List<Map<String, Object>> songLists, Utils.getList()

    public MyBaseAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.songLists = songLists;
        this.cursor = cursor;
        songInfo = MusicDBHelper.getMusicListFromLocal(cursor);
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
        if (cursor.equals(null)) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {


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

//        if (popCheckStatus.get(position)) {
//            holder.popdown.setVisibility(View.VISIBLE);
//        } else {
//            holder.popdown.setVisibility(View.GONE);
//        }
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
//                Toast.makeText(context, "我喜欢", Toast.LENGTH_LONG).show();
            }
        });
        // 设置歌曲信息按钮点击事件
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("detail", "click");
//                cursor.moveToPosition(position);
                InfoDialog infoDialog = new InfoDialog(v.getContext());
                infoDialog.setOnTVAnimDialogDismissListener(new TVAnimDialog.OnTVAnimDialogDismissListener() {
                    @Override
                    public void onDismiss(int dialogId) {

                    }
                });

                infoDialog.show();
                infoDialog.setInfo(songInfo,position);

//                String title = songInfo.get(position).getTitle();
//                String artist = songInfo.get(position).getArtist();
//                String album = songInfo.get(position).getAlbum();
//                String size = songInfo.get(position).getSize();
////                size = size.substring(0, 1) + "." + size.substring(2,3) + "M";
////                size = size.substring(0, 3) + "M";
//                String duration = songInfo.get(position).getDuration() + "";
//                duration = Utils.millsToMinute(duration);
//                String path = songInfo.get(position).getPath() + "";
//
//                Log.e("MyBaseAdapter", title);
//                Log.e("artist", artist);
//                Log.e("album", album);
//                Log.e("size", size + "");
//                Log.e("duration", duration);
//                Log.e("path", path);
////





            }
        });
        // 设置删除按钮点击事件
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("delete", "click");
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

