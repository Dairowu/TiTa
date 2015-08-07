package Picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2015/8/6.
 */
public class ProcessPicture {

    private  Bitmap bitmap;

    public void  readPciture(String picturePath){

        File f = new File(picturePath);
        if(f.isFile()) {
            try {
                FileInputStream inputStream = new FileInputStream(f);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                bitmap = BitmapFactory.decodeStream(bufferedInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            bitmap =null;
        }
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
}
