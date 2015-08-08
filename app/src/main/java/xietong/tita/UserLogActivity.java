package xietong.tita;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by acer-PC on 2015/8/5.
 */
public class UserLogActivity extends Activity implements TextWatcher {

    TextInputLayout userLayout, passwordLayout;
    EditText userText, userPassword;
    Button bnLog, bnSet;
    Button bnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log);
        FinishApp.addActivity(this);

        userLayout = (TextInputLayout) findViewById(R.id.user_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);

        userText = userLayout.getEditText();
        userPassword = passwordLayout.getEditText();

        userText.addTextChangedListener(this);
        userPassword.addTextChangedListener(this);

        bnLog = (Button) findViewById(R.id.bnLog);
        bnSet = (Button) findViewById(R.id.bnSet);
        bnBack = (Button) findViewById(R.id.bnUserBack);

        bnLog.setEnabled(false);

        //登录按钮
        bnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(UserLogActivity.this, "你点击了登录按钮", Toast.LENGTH_SHORT).show();
            }
        });

        //注册按钮
        bnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(UserLogActivity.this, "你点击了注册按钮", Toast.LENGTH_SHORT).show();
            }
        });

        bnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //设置默认提示文本
        userLayout.setHint("   请输入用户名");
        passwordLayout.setHint("   请输入用户密码");

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        //只有当两个输入框都输入了信息时，按钮才可以点击
        if (userPassword.getText().toString().equals("")
                || userPassword.getText().toString().equals("")) {
            bnLog.setEnabled(false);
        } else bnLog.setEnabled(true);

        Log.e("userLog","afterTextChange");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserLogActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}
