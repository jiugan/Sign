package com.example.sign.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sign.MainActivity;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.entity.StudentCourseDetailsResponse;
import com.example.sign.ui.utils.StringUtils;

import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCourseDetailsActivity extends BaseActivity {

    private String courseName;
    private String collegeName;
    private String courseId;
    private String coursePhoto;
    private TextView className;
    private TextView schoolName;
    private Button addClassBtn;
    private TextView schoolName1;
    private TextView startTime;
    private TextView endTime;
    private TextView teacherName;
    private TextView classstatus;
    private TextView classdeclare;
    private ImageView image;

    @Override
    protected int initLayout() {
        return R.layout.activity_student_course_details;
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

        className = findViewById(R.id.details_courseName);
        schoolName = findViewById(R.id.details_collageName);
        addClassBtn = findViewById(R.id.details_btn);
        schoolName1 = findViewById(R.id.details_collageName1);
        startTime = findViewById(R.id.details_startTime);
        endTime = findViewById(R.id.details_endTime);
        teacherName = findViewById(R.id.details_Teacher);
        classstatus = findViewById(R.id.details_status);
        classdeclare = findViewById(R.id.details_declare);
        image = findViewById(R.id.details_image);
    }

    @Override
    protected void initData() {
        getData();
        className.setText(courseName);
        schoolName1.setText(collegeName);
        schoolName.setText(collegeName);
        Glide.with(this).load(coursePhoto).into(image);

        addClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddClass();
            }
        });
    }

    private void AddClass() {
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(courseId)){
            Call<RegisterResponse> call = Api.api.StudentAddClass(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id), Integer.parseInt(courseId));
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentCourseDetailsActivity.this);
            builder.setTitle("提示");
            builder.setMessage("您确定加入这门课吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    call.enqueue(new Callback<RegisterResponse>() {
                        @Override
                        public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                            if (response.body().code == 200){
                                showToast("添加成功");
                            }else {
                                showToast("您已经选了这门课");
                            }
                        }
                        @Override
                        public void onFailure(Call<RegisterResponse> call, Throwable throwable) {

                        }
                    });
                }
            });
            builder.setNeutralButton("取消",null);
            builder.show(); //调用show()方法来展示对话框
        }

    }
    //网络请求拿到当前课程得数据
    private void getData() {
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(id) && !StringUtils.isEmpty(id) && !StringUtils.isEmpty(id)){
            Call<StudentCourseDetailsResponse> call = Api.api.getClassDetails(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id),
                    Integer.parseInt(courseId));
            call.enqueue(new Callback<StudentCourseDetailsResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<StudentCourseDetailsResponse> call, Response<StudentCourseDetailsResponse> response) {
                    if (response.body().code == 200){
                        StudentCourseDetailsResponse.show data = response.body().data;
                        classdeclare.setText(data.introduce);
                        String date = timeStamp2Date(data.startTime, "yyyy-MM-dd");
                        String date1 = timeStamp2Date(data.endTime, "yyyy-MM-dd");
                        startTime.setText(date);
                        endTime.setText(date1);
                        teacherName.setText(data.realName);
                        if (data.hasSelect){
                            classstatus.setText("已选");
                        }else {
                            classstatus.setText("未选");
                        }

                    }else {
                        showToast(response.body().msg);
                    }
                }

                @Override
                public void onFailure(Call<StudentCourseDetailsResponse> call, Throwable throwable) {

                }
            });

        }

    }

    //返回的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            navigateTo(MainActivity.class);
            finish();
        }
        return true;
    }

    // TODO: 2022/7/26  11:39
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String timeStamp2Date(String seconds, String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
}