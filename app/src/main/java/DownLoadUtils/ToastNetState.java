package DownLoadUtils;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ToastNetState {

	private ConnectivityManager nw ;
	private  NetworkInfo[] netinfo;
	private Context context;
	
	public ToastNetState(Context context){
		this.context=context.getApplicationContext();
	}

    /**
     * 给用户提示网络状态
     */
    
    public boolean isNetAvailable(){
    	nw = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	if(nw!=null){
    	netinfo = nw.getAllNetworkInfo();
    	if(netinfo!=null){
    		for(int i = 0;i<netinfo.length;i++){
    			if(netinfo[i].getState()==NetworkInfo.State.CONNECTED)
    			return netinfo[i].isAvailable();
    			}
    		}
    	}
    	return false;
    }
    
    public void Toast(){
    	nw = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo netinfo = nw.getActiveNetworkInfo();
    	Toast.makeText(context, "当前网络"+add(netinfo.isAvailable())+","+
    		    "网络"+app(netinfo.isConnected())+","+"网络连接"+adp(netinfo.isConnected()), Toast.LENGTH_LONG).show();
    }
    
	  String add(Boolean bl){
		    String s = "不可用";
		    if(bl==true){
		      s="可用";
		    }
		    return s;
		  }
		  String app(Boolean bl){
		    String s = "未连接";
		    if(bl==true){
		      s="已连接";
		    }
		    return s;
		  }
		  String adp(Boolean bl){
		    String s = "不存在！";
		    if(bl==true){
		      s="存在！";
		    }
		    return s;
		  }  
    
}
