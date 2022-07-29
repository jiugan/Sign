package com.example.sign.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.sign.MainActivity;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.LoginResponse;
import com.example.sign.ui.entity.PersonalDataBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    MaterialEditText username;
    MaterialEditText password;
    Button login;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
         username = findViewById(R.id.login_username);
         password = findViewById(R.id.login_password);
         login = findViewById(R.id.login_login);
        Intent intent = getIntent();
        String get_name = intent.getStringExtra("name");
        String get_pwd = intent.getStringExtra("password");
        username.setText(get_name);
        password.setText(get_pwd);
    }

    @Override
    protected void initData() {
        //处理登录逻辑
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    //处理登录逻辑
    private void Login() {
        String name = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(pwd)){
            Call<LoginResponse> call = Api.api.login(HiRetrofit.appId,HiRetrofit.appSecret,name,pwd);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse res = response.body();
                    if (res.code == 200 && res.msg.equals("登录成功")){
                        saveStringToSp("appKey",res.data.appKey);
                        saveStringToSp("position",String.valueOf(res.data.roleId));
                        saveStringToSp("id",String.valueOf(res.data.id));
                        saveStringToSp("userName",res.data.userName);
                        showToast("登录成功");
                        //处理完善信息
                        showAddDialog(res);
//                        if (res.data.roleId == 0){
//                            navigateTo(MainActivity.class);
//                            finish();
//                        }else {
//                            navigateTo(TeacherActivity.class);
//                            finish();
//                        }
                    }
                    else {
                        showToast(res.msg);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    showToast("登录失败！请检查网络");
                }
            });
        }else {
            showToast("账号或密码不能为空！");
        }
    }

    protected void showAddDialog(LoginResponse res) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog, null);

        final EditText realName = (EditText) textEntryView
                .findViewById(R.id.realName);
        final EditText phone = (EditText) textEntryView
                .findViewById(R.id.phone);
        final EditText email = (EditText) textEntryView
                .findViewById(R.id.email);
        final EditText collegeName = (EditText) textEntryView
                .findViewById(R.id.collegeName);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(this);
        ad1.setTitle("请输入信息:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(textEntryView);

        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String realName1 =   realName.getText().toString().trim();
                String  phone1 =  phone.getText().toString().trim();
                String email1 =  email.getText().toString().trim();
                String collegeName1 =  collegeName.getText().toString().trim();
                if (StringUtils.isEmpty(realName1) || StringUtils.isEmpty(phone1) || StringUtils.isEmpty(email1)
                        || StringUtils.isEmpty(collegeName1)) {
                    showToast("数据不可为空");
                } else {
                    String id = getStringFromSp("id");
                    int parseInt = Integer.parseInt(id);
                    PersonalDataBody body = new PersonalDataBody(collegeName1, email1, parseInt, phone1, realName1);
                    Call<RegisterResponse> call = Api.api.change(HiRetrofit.appId, HiRetrofit.appSecret, body);
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if (response.body().code == 200) {
                                saveStringToSp("phone", phone1);
                                saveStringToSp("realName", realName1);
                                saveStringToSp("email", email1);
                                saveStringToSp("collegeName", collegeName1);
                                if (res.data.roleId == 0) {
                                    navigateTo(MainActivity.class);
                                    finish();
                                } else {
                                    navigateTo(TeacherActivity.class);
                                    finish();
                                }
                            } else {
                                showToast(response.body().msg);
                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                            showToast(throwable.getMessage());
                        }
                    });
                }
            }
        });
        ad1.setNegativeButton("取消", null);
        ad1.show();// 显示对话框
    }
}