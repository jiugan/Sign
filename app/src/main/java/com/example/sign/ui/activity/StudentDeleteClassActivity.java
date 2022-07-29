package com.example.sign.ui.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.gjylibrary.GjySerialnumberLayout;
import com.example.sign.MainActivity;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.LoginResponse;
import com.example.sign.ui.entity.PersonalDataBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.entity.SignBody;
import com.example.sign.ui.entity.StudentSignListResponse;
import com.example.sign.ui.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDeleteClassActivity extends BaseActivity {

    private String courseName;
    private String collegeName;
    private String courseId;
    private String coursePhoto;
    private TextView courseAddr;
    private TextView createTime;
    private TextView userSignId;
    private Button sign;
    private TextView noDataText;
    private CardView cardView;
    String Signcode;
    private int signid;
    private ListView listView;
    public List DataList = new ArrayList<>();
    @Override
    protected int initLayout() {
        return R.layout.activity_student_delete_class;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        collegeName = intent.getStringExtra("collegeName");
        courseId = intent.getStringExtra("courseId");
        coursePhoto = intent.getStringExtra("coursePhoto");

        courseAddr = findViewById(R.id.courseAddr);
        createTime = findViewById(R.id.createTime);
        userSignId = findViewById(R.id.userSignId);
        noDataText = findViewById(R.id.noData);
        sign = findViewById(R.id.sign);
        cardView = findViewById(R.id.cardView);
        listView = findViewById(R.id.listView);
        toolbar.setTitle(courseName);
    }

    @Override
    protected void initData() {
        getListData();

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

        getListData1();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student,menu);
        return true;
    }

    //返回的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                Delete();
                break;
            default:break;
        }
        if(item.getItemId()==android.R.id.home){
            navigateTo(MainActivity.class);
            finish();
        }
        return true;
    }

    private void Delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("您确定要退课吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteClass();
            }
        });
        builder.setNeutralButton("取消",null);
        builder.show(); //调用show()方法来展示对话框
    }

    private void DeleteClass() {
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(courseId) && !StringUtils.isEmpty(id)){
            Call<RegisterResponse> call = Api.api.StudentDropoutClass(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id), Integer.parseInt(courseId));
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body().code == 200){
                        showToast("退课成功");
                        navigateTo(MainActivity.class);
                        finish();
                    }else {
                        showToast("请勿重复退课");
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable throwable) {

                }
            });
        }
    }

    public void getListData(){
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(id)){
            Call<StudentSignListResponse> call = Api.api.StudentGetSignList(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id)
                    , 0,Integer.parseInt(courseId));
            call.enqueue(new Callback<StudentSignListResponse>() {
                @Override
                public void onResponse(Call<StudentSignListResponse> call, Response<StudentSignListResponse> response) {
                    if (response.body().code == 200){
                        if (response.body().data == null || response.body().data.records == null){
                            cardView.setVisibility(View.GONE);
                            noDataText.setVisibility(View.VISIBLE);
                        }else {
                            signid = response.body().data.records.get(0).userSignId;
                            userSignId.setText(String.valueOf(response.body().data.records.get(0).userSignId));
                            courseAddr.setText(response.body().data.records.get(0).courseAddr);
                            createTime.setText(String.valueOf(response.body().data.records.get(0).createTime));
                        }
                    }else {
                        System.out.println(response.body().code+" error");
                    }
                }

                @Override
                public void onFailure(Call<StudentSignListResponse> call, Throwable throwable) {
                    System.out.println("error  "+throwable.getMessage());
                }
            });
        }
    }

    protected void showAddDialog() {
        String id = getStringFromSp("id");
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog2, null);
        GjySerialnumberLayout verificationCode = textEntryView
                .findViewById(R.id.verification_code);

        verificationCode.setOnInputListener(new GjySerialnumberLayout.OnInputListener() {
            @Override
            public void onSucess(String code) {
                System.out.println("内容是:"+code);
                Signcode = code;
            }
        });

        AlertDialog.Builder ad1 = new AlertDialog.Builder(this);
        ad1.setTitle("请输入信息:");
        ad1.setView(textEntryView);

        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if (!StringUtils.isEmpty(Signcode)){
                    SignBody signBody = new SignBody(Integer.parseInt(Signcode),Integer.parseInt(id),signid);
                    Call<RegisterResponse> call = Api.api.Sign(HiRetrofit.appId, HiRetrofit.appSecret,signBody);
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if (response.body().code == 200){
                                showToast("签到成功");
                            }else {
                                showToast(response.body().msg);
                            }
                        }

                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                            System.out.println(throwable.getMessage());
                        }
                    });
                }else {
                    showToast("请输入完整的签到码");
                }
            }
        });
        ad1.setNegativeButton("取消", null);
        ad1.show();// 显示对话框
    }

    public void getListData1(){
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(id)){
            Call<StudentSignListResponse> call = Api.api.StudentGetSignList(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id)
                    , 1,Integer.parseInt(courseId));
            call.enqueue(new Callback<StudentSignListResponse>() {
                @Override
                public void onResponse(Call<StudentSignListResponse> call, Response<StudentSignListResponse> response) {
                    if (response.body().code == 200){
                        System.out.println("getListData1:"+response.body().code);
                        for (int i = 0; i < response.body().data.total; i++) {
                            DataList.add("签到时间： "+response.body().data.records.get(i).createTime);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(StudentDeleteClassActivity.this, android.R.layout.simple_list_item_1,DataList);
                        listView.setAdapter(adapter);
                    }else {
                        System.out.println(response.body().code+" error");
                    }
                }

                @Override
                public void onFailure(Call<StudentSignListResponse> call, Throwable throwable) {
                    System.out.println("error  "+throwable.getMessage());
                }
            });
        }
    }

}