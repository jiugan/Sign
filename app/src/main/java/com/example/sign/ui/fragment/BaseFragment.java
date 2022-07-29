package com.example.sign.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.sign.R;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null){
            mRootView = inflater.inflate(initLayout(),container,false);
            initView();
        }
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    protected abstract int initLayout();
    protected abstract void initView();
    protected abstract void initData();

    public void showToastSync(String s){
        Looper.prepare();
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void showToast(String s){
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public void navigateTo(Class cls){
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    protected void saveStringToSp(String key,String value){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }

    protected String getStringFromSp(String key){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit",MODE_PRIVATE);
        return sp.getString(key,"");
    }

    //删除token
    protected void deleteStringFromSp(String key){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ttit",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }


    public void navigateToWithFlag(Class cls,int flags){
        Intent intent = new Intent(getActivity(), cls);
        intent.setFlags(flags);
        startActivity(intent);
    }
}
