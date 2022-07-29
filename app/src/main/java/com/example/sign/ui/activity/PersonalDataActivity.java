package com.example.sign.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sign.MainActivity;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.LoginResponse;
import com.example.sign.ui.entity.PersonalDataBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.utils.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ImageView;

public class PersonalDataActivity extends BaseActivity {

    private TextView name;
    private TextView email;
    private TextView phone;
    private TextView collegeName;
    private TextView roleId;
    private String realName;
    private String email1;
    private String collegeName1;
    private String position;
    private String phone1;
    private Button update;
    private ImageView image;
    private String touXiang;

    @Override
    protected int initLayout() {
        return R.layout.activity_personal_data;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.personal_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        name = findViewById(R.id.personalData_realname);
        email = findViewById(R.id.personalData_email);
        phone = findViewById(R.id.personalData_phone);
        collegeName = findViewById(R.id.personalData_collegeName);
        roleId = findViewById(R.id.personalData_roleId);
        update = findViewById(R.id.personalData_updateBtn);
        image = findViewById(R.id.personalData_image);
    }

    @Override
    protected void initData() {
        //处理数据的导入
        init();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });
    }

    protected void init() {
        realName = getStringFromSp("realName");
        email1 = getStringFromSp("email");
        collegeName1 = getStringFromSp("collegeName");
        position = getStringFromSp("position");
        phone1 = getStringFromSp("phone");
        touXiang = getStringFromSp("avatar");

        name.setText(realName);
        email.setText(email1);
        collegeName.setText(collegeName1);
        if (position.equals("1")){
            roleId.setText("老师");
        }else {
            roleId.setText("学生");
        }
        phone.setText(phone1);

        //处理头像的加载问题
        if (!StringUtils.isEmpty(touXiang)){
            Glide.with(this).load(touXiang).into(image);
        }
    }


    protected void showAddDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog1, null);

        final EditText realName = (EditText) textEntryView
                .findViewById(R.id.realName);
        final EditText phone = (EditText) textEntryView
                .findViewById(R.id.phone);
        final EditText email = (EditText) textEntryView
                .findViewById(R.id.email);
        final EditText collegeName = (EditText) textEntryView
                .findViewById(R.id.collegeName);

        realName.setText(this.realName);
        phone.setText(this.phone1);
        email.setText(this.email1);
        collegeName.setText(this.collegeName1);

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
                                init();
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

    //返回的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            navigateTo(TeacherActivity.class);
            finish();
        }
        return true;
    }
}