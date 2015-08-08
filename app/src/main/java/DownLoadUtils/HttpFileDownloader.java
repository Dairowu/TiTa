package DownLoadUtils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 *  文件下载工具类�
 * @author Administrator
 *
 */

public class HttpFileDownloader {
	
	URL url = null;
		
	/**
	 * 下载文本文件
	 * @param sourse下载源
	 * @param targetDir本地存储目录
	 * @param fileName存储存储名
	 * @return -1代表已经存在，0代表下载失败，1代表下载成功
	 */
	
	BufferedReader bufferedReader = null;
	BufferedWriter bufferedWriter = null;
	
	
	public int downTextFile(String sourse,String targetDir,String fileName){
		
		try {
			url = new URL(sourse);
			URLConnection conn = url.openConnection();
			
			InputStream inputStream = conn.getInputStream();
			//构建一个带缓存区的字符输入流�����������ַ�������
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			File file = new File(targetDir+File.separator+fileName);
			if(file.exists()){
				return -1;
			}
			else{
			//构建一个带缓冲区的字符输出流����
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			
			String line = "";
			while((line=bufferedReader.readLine())!=null){
				bufferedWriter.write(line+"\n");
			}
			//�关闭资源
			bufferedWriter.flush();
			bufferedWriter.close();
			bufferedReader.close();
			return 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} 
		finally{
			try {
				bufferedWriter.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 下载非文本文件
	 * @param sourse下载源
	 * @param targetDir本地存储目录
	 * @param fileName存储存储名
	 * @return -1代表已经存在，0代表下载失败，1代表下载成功
	 */
	
	BufferedInputStream bufferedInputStream = null;
	BufferedOutputStream bufferedOutputStream = null;
		
	public int downBinaryFile(String sourse,String targetDir,String fileName,Handler handler){
		
		URLConnection conn = null;
		InputStream inputStream = null;
			try {
				url = new URL(sourse);
				conn = url.openConnection();
				inputStream = conn.getInputStream();			
				//构建一个带缓存区的字节输入流��������ֽ�������
				bufferedInputStream = new BufferedInputStream(inputStream);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			File file = new File(targetDir+File.separator+fileName);
			
			if(file.exists()){
				return -1;
			}
			else{
			//构建一个带缓冲区的字节输出流����ֽ������
			try {
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
				byte[] b = new byte[4*1024];
				while((bufferedInputStream.read(b))!=-1){
				bufferedOutputStream.write(b);
				}
				//关闭资源
				bufferedOutputStream.flush();
				Log.i("info", bufferedOutputStream.toString());
				bufferedOutputStream.close();
				bufferedInputStream.close();
				return 1;
				}
			catch (Exception e) {
				e.printStackTrace();
				return 0;
				}	
			}
	}
	
	/**
	 * 用于得到服务端回传的json数据，将其存放在StringBuffered中
	 * @param sourse URL
	 * @param context
	 * @return 包含了服务端回传的json数据�
	 */

	public StringBuffer downJson(String sourse,Context context){
		
		StringBuffer stringBuffer = null;
		ToastNetState netState = new ToastNetState(context);
		if(!netState.isNetAvailable()){
			Toast.makeText(context,"网络异常", Toast.LENGTH_LONG).show();
		}else{	
			try {
				URL url = new URL(sourse);
				URLConnection conn = url.openConnection();

				InputStream inputStream = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
				stringBuffer = new StringBuffer();
				String line = "";
				while((line=bufferedReader.readLine())!=null){
				stringBuffer.append(line);
				}
				bufferedReader.close();
				} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return stringBuffer;
	}
	
}
			
			
		

