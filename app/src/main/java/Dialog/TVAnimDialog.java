package Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import xietong.tita.MyBaseAdapter;
import xietong.tita.R;

/**
 * Created by Administrator on 2015/8/7.
 */
public class TVAnimDialog extends Dialog{

    private String TAG="TVAnimDialog";
    private int dialogId = MyBaseAdapter.DIALOG_DISMISS;
    private OnTVAnimDialogDismissListener listener;

    public TVAnimDialog(Context context) {
        super(context, R.style.TVAnimDialog);// 此处附上Dialog样式
        // TODO Auto-generated constructor stub
    }

    public TVAnimDialog(Context context, int theme) {
        super(context, theme);
        // TODO Auto-generated constructor stub
    }

    protected TVAnimDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(R.style.TVAnimDialogWindowAnim);// 此处附上Dialog动画
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        if (listener != null) {
            listener.onDismiss(dialogId);
        }
    }

    /**
     * 用于区分Dialog用途
     *
     * @param dialogId
     *            Dialog ID
     */
    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }

    /**
     * 设置监听器
     *
     * @param listener
     *            OnTVAnimDialogDismissListener
     */
    public void setOnTVAnimDialogDismissListener(
            OnTVAnimDialogDismissListener listener) {
        this.listener = listener;
    }

    /**
     * 用于监听对话框关闭的接口
     */
    public interface OnTVAnimDialogDismissListener {
        /**
         * 对话框关闭
         *
         * @param dialogId
         *            Dialog ID
         */
        void onDismiss(int dialogId);
    }

}
