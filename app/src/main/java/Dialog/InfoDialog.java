package Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import info.MusicInfo;
import xietong.tita.R;
import xietong.tita.Utils;

/**
 * Created by Administrator on 2015/8/7.
 */
public class InfoDialog extends TVAnimDialog{

    private String TAG="InfoDialog";
    private TextView name;
    private TextView artist;
    private TextView album;
    private TextView genre;
    private TextView time;
    private TextView format;
//    private TextView kbps;
    private TextView size;
    private TextView years;
//    private TextView hz;
    private TextView path;
    private Button button;
    public InfoDialog(Context context) {
        super(context);
    }

    public InfoDialog(Context context, int theme) {
        super(context, theme);
    }

    protected InfoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info);

        name = (TextView) findViewById(R.id.dialog_info_name);
        artist = (TextView) findViewById(R.id.dialog_info_artist);
        album = (TextView) findViewById(R.id.dialog_info_album);
        genre = (TextView) findViewById(R.id.dialog_info_genre);
        time = (TextView) findViewById(R.id.dialog_info_time);
//        format = (TextView) findViewById(R.id.dialog_info_format);
//        kbps = (TextView) findViewById(R.id.dialog_info_kbps);
        size = (TextView) findViewById(R.id.dialog_info_size);
//        years = (TextView) findViewById(R.id.dialog_info_years);
//        hz = (TextView) findViewById(R.id.dialog_info_hz);
        path = (TextView) findViewById(R.id.dialog_info_path);
        button = (Button) findViewById(R.id.dialog_info_btn_ok);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });

    }

    /**
     * 设置歌曲信息集合并显示集合里的信息
     *
     * @param songInfo
     *            歌曲信息集合
     */
    public void setInfo(ArrayList<MusicInfo> songInfo,int position) {
        name.setText("歌曲: " + songInfo.get(position).getTitle());
        artist.setText("歌手: " + songInfo.get(position).getArtist());
        album.setText("专辑：" + songInfo.get(position).getAlbum());
//        genre.setText(info.getGenre());
        String msize = Integer.parseInt(songInfo.get(position).getSize()) / 1024.0f / 1024.0f + "";
        size.setText("大小: " + msize.substring(0, 4) + "M");
//        format.setText(info.getFormat());
//        kbps.setText(info.getKbps());
        String duration = songInfo.get(position).getDuration() + "";
//        duration = Utils.millsToMinute(duration);
        time.setText("时长: " + Utils.millsToMinute(duration));
//        years.setText(info.getYears());
//        hz.setText(info.getHz());
        path.setText("路径: " + songInfo.get(position).getPath()+"");
    }

}
