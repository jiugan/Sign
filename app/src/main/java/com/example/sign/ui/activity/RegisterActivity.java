package com.example.sign.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;

import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.LoginResponse;
import com.example.sign.ui.entity.RegisterBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private MaterialEditText username;
    private MaterialEditText password;
    private MaterialEditText passwordTwo;
    private Button register;
    RadioGroup rdg_gender;
    int id = -1;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        passwordTwo = findViewById(R.id.register_passwordTwo);
        register = findViewById(R.id.register_register);
        rdg_gender = findViewById(R.id.rdg_gender);
    }

    @Override
    protected void initData() {
        rdg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.register_student){
                    id = 0;
                }else{
                    id = 1;
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register() {
        String name = username.getText().toString().trim();
        String pwd1 = password.getText().toString().trim();
        String pwd2 = passwordTwo.getText().toString().trim();
//        rdg_gender.check(R.id.register_student);
        if (!StringUtils.isEmpty(name) &&
                !StringUtils.isEmpty(pwd1) && !StringUtils.isEmpty(pwd2)){
            if (pwd1.equals(pwd2)){
                if (id != -1){
                    RegisterBody registerBody = new RegisterBody(pwd1,name,id);
                    Call<RegisterResponse> call = Api.api.register(HiRetrofit.appId, HiRetrofit.appSecret, registerBody);
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if (response.body().code == 200){
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("password",pwd1);
                                startActivity(intent);
                                finish();
                            }else {
                                showToast(response.body().msg);
                            }

                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                            showToast(throwable.getMessage());
                        }
                    });
                }else {
                    //弹框警告
                    showToast("您必须选择身份");
                }
            }else {
                showToast("两次输入的密码不一致");
            }
        }else {
            showToast("账号或密码不能为空");
        }
    }
}