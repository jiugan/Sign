package com.example.sign.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将当前Activity赋值给mContext
        mContext = this;
        setContentView(initLayout());
        initView();
        initData();
    }

    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();

    public void showToast(String s){
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    //子线程中的Toast
    public void showToastSync(String s){
        Looper.prepare();
        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls){
        Intent intent = new Intent(mContext, cls);
        startActivity(intent);
    }

    public void navigateToWithFlag(Class cls,int flags){
        Intent intent = new Intent(mContext, cls);
        intent.setFlags(flags);
        startActivity(intent);
    }

    protected void saveStringToSp(String key,String value){
        SharedPreferences sp = getSharedPreferences("sp_ttit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    protected String getStringFromSp(String key){
        SharedPreferences sp = getSharedPreferences("sp_ttit",MODE_PRIVATE);
        return sp.getString(key,"");
    }
}
