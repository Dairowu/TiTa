package Dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xietong.tita.MainActivity;
import xietong.tita.R;

/**
 * Created by CJJ on 2015/8/8.
 */
public class DeleteDialog extends TVAnimDialog implements View.OnClickListener{

    private String TAG="DeleteDialog";
    private Button positive;
    private Button negative;

    public DeleteDialog(Context context) {
        super(context);
    }

    public DeleteDialog(Context context, int theme) {
        super(context, theme);
    }

    protected DeleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_del);

        positive = (Button) findViewById(R.id.dialog_delete_btn_positive);
        negative = (Button) findViewById(R.id.dialog_delete_btn_negative);
        positive.setOnClickListener(this);
        negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_delete_btn_positive:
                setDialogId(MainActivity.DIALOG_DELETE);
                break;

            case R.id.dialog_delete_btn_negative:
                setDialogId(MainActivity.DIALOG_DISMISS);
                break;
        }
        dismiss();
    }

}
