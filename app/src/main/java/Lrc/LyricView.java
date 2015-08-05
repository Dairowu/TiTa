package Lrc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class LyricView extends View{

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

	public void setlrc_list(List<LyricObject> lrc_list){
		this.lrc_list = lrc_list;
	}

	public LyricView(Context context,AttributeSet attrs){
		super(context,attrs);
		init();
	}

	private void init() {

		setFocusable(true);

		paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);//设置绘制文本时的对齐方式
		paint.setColor(Color.GRAY);
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
			for(int i = index -1;i> index - 5&&i>0;i--){
				temp = lrc_list.get(i);
				if(tempY<0){
					continue;
				}
				tempY = tempY - (TEXTSIZE+INTERVAL);
				canvas.drawText(temp.lrcline, width/2, tempY, paint);
			}

			tempY = height/2;
			//画出当前歌词之后的歌词
			for(int i = index +1;i<index+5&&i<lrc_list.size();i++){
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

	public boolean onTouchEvent(MotionEvent event){

		if(!blLrc){
			return super.onTouchEvent(event);
		}
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_UP:

				break;
			case MotionEvent.ACTION_MOVE:

				break;
			default:
				break;
		}
		return true;
	}

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
}
