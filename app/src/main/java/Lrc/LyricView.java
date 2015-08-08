package Lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.List;

import DownLoadUtils.DownPictureOrLrc;
import xietong.tita.R;

public class LyricView extends View implements View.OnClickListener{

	DownPictureOrLrc downPictureOrLrc;
	Handler handler;
	String title;
	//用来标示是下载图片还是歌词
	int DOWNLRC  = 1;
	int DOWNPICTURE = 2;

	private static List<LyricObject> lrc_list;
	private static boolean blLrc = false;
	private int INTERVAL=12;
	private int CURRENTEXTSIZE = 24;
	private int TEXTSIZE=20;
	private int index;
	private int offsetY;
	private int width;
	private int height;
	private Paint paint;
	private Paint paintHL;
	private Context context;

	public void setlrc_list(List<LyricObject> lrc_list){
		this.lrc_list = lrc_list;
	}

	public LyricView(Context context,AttributeSet attrs){
		super(context,attrs);
		this.context = context;
		init();
	}

	private void init() {

		setFocusable(true);

		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);//设置绘制文本时的对齐方式
		paint.setColor(Color.argb(0,250,250,250));
		paint.setAntiAlias(true);//设置抗锯齿
		paint.setDither(true);//设置防止抖动
		paint.setTextSize(20);
		paint.setAlpha(180);

		paintHL = new Paint();
		paintHL.setTextAlign(Paint.Align.CENTER);
		paintHL.setColor(Color.WHITE);
		paintHL.setAntiAlias(true);
		paintHL.setDither(true);
		paintHL.setTextSize(24);
		paintHL.setAlpha(255);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		float tempY = height/2;
		if(blLrc){
			LyricObject temp = lrc_list.get(index);
			canvas.drawText(temp.lrcline, width/2, tempY , paintHL);

			//画出当前歌词之前的歌词
			for(int i = index -1;i> index - 8&&i>0;i--){
				temp = lrc_list.get(i);
				if(tempY<0){
					continue;
				}
				tempY = tempY - (TEXTSIZE+INTERVAL);
				canvas.drawText(temp.lrcline, width/2, tempY, paint);
			}

			tempY = height/2;
			//画出当前歌词之后的歌词
			for(int i = index +1;i<index+8&&i<lrc_list.size();i++){
				temp = lrc_list.get(i);
				if(tempY>height){
					continue;
				}
				tempY = tempY + (TEXTSIZE+INTERVAL);
				canvas.drawText(temp.lrcline, width/2, tempY, paint);
			}

		}else{
			paint.setTextSize(25);
			canvas.drawText("没有找到歌词，点击屏幕下载", width/2, height/2, paint);
		}
		super.onDraw(canvas);
	}

//	public boolean onTouchEvent(MotionEvent event){
//
//		if(!blLrc){
//			return super.onTouchEvent(event);
//		}
//		switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//
//				break;
//			case MotionEvent.ACTION_UP:
//
//				break;
//			case MotionEvent.ACTION_MOVE:
//
//				break;
//			default:
//				break;
//		}
//		return true;
//	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}

	public void setIndex(int index){
		this.index = index;
	}
	public void setBlLrc(boolean blLrc){this.blLrc = blLrc;}
	public void setHandler(Handler handler){this.handler = handler;}
	public void setTile(String title){this.title = title;}

	@Override
	public void onClick(View v) {
		//为泡泡窗口加载视图
		PopupWindow popupWindow;
		LayoutInflater inflater = LayoutInflater.from(context);
		View root = inflater.inflate(R.layout.popupwindow, null);
		popupWindow = new PopupWindow(root, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,true );
		popupWindow.setAnimationStyle(R.style.popupanimation);
		popupWindow.setFocusable(true);
		ColorDrawable color = new ColorDrawable(0x7DC0C0C0);
		popupWindow.setBackgroundDrawable(color);
		popupWindow.showAtLocation(root, Gravity.BOTTOM,0,0);

		Button bt_downpicture = (Button) root.findViewById(R.id.bt_downpicture);
		bt_downpicture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				downPictureOrLrc = new DownPictureOrLrc(DOWNPICTURE,context,title,handler);
				Thread downPicture = new Thread(downPictureOrLrc);
				downPicture.start();
			}
		});

		Button bt_downlrc = (Button) root.findViewById(R.id.bt_downlrc);
		bt_downlrc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				downPictureOrLrc = new DownPictureOrLrc(DOWNLRC,context,title,handler);
				Thread downPicture = new Thread(downPictureOrLrc);
				downPicture.start();
			}
		});

		Button bt_huanfu = (Button) root.findViewById(R.id.bt_huanfu);
	}
}
