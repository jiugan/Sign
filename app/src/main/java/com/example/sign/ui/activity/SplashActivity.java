package com.example.sign.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sign.MainActivity;
import com.example.sign.R;
import com.example.sign.ui.utils.StringUtils;

public class SplashActivity extends BaseActivity {
    Button login;
    Button register;

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        login =  findViewById(R.id.Splash_login);
        register =  findViewById(R.id.Splash_register);
    }

    @Override
    protected void initData() {
        if (!StringUtils.isEmpty(getStringFromSp("appKey"))){
            if (getStringFromSp("position").equals("0")){
                navigateTo(MainActivity.class);
                finish();
            }else if (getStringFromSp("position").equals("1")) {
                navigateTo(TeacherActivity.class);
                finish();
            }
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(LoginActivity.class);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(RegisterActivity.class);
            }
        });
    }
}