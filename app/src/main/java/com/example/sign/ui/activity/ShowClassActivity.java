package com.example.sign.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.gjylibrary.GjySerialnumberLayout;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.entity.SignDataBody;
import com.example.sign.ui.utils.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.widget.ImageView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowClassActivity extends BaseActivity {

    private String courseName;
    private String collegeName;
    private String courseId;
    private String coursePhoto;
    private TextView show_time;
    private ImageView timeSelect;
    private TextView show_time1;
    private ImageView timeSelect1;
    private Calendar selectedDate;
    private Calendar startDate;
    private Calendar endDate;
    private LinearLayout layout;
    private LinearLayout layout1;
    private String getContent;
    EditText courseNumber;
    private GjySerialnumberLayout verificationCode;
    private Button sign;
    String Signcode;
    private EditText address;
    private TextView name;
    private TextView Id;

    @Override
    protected int initLayout() {
        return R.layout.activity_show_class;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//???????????????????????????
        getSupportActionBar().setHomeButtonEnabled(true); //?????????????????????

        show_time = findViewById(R.id.show_time);
        timeSelect = findViewById(R.id.timeSelect);
        layout = findViewById(R.id.SelectTimeLayout);
        show_time = findViewById(R.id.show_time);
        timeSelect = findViewById(R.id.timeSelect);
        courseNumber = findViewById(R.id.ClassNumber);
        verificationCode = findViewById(R.id.verification_code);

        layout1 = findViewById(R.id.SelectTimeLayout1);
        show_time1 = findViewById(R.id.show_time1);
        timeSelect1 = findViewById(R.id.timeSelect1);
        sign = findViewById(R.id.ClassSign);
        address = findViewById(R.id.Class_address);
        name = findViewById(R.id.ClassName);
        Id = findViewById(R.id.ClassId);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        collegeName = intent.getStringExtra("collegeName");
        courseId = intent.getStringExtra("courseId");
        coursePhoto = intent.getStringExtra("coursePhoto");

        saveStringToSp("courseName",courseName);
        saveStringToSp("collegeName",collegeName);
        saveStringToSp("courseId",courseId);
        saveStringToSp("coursePhoto",coursePhoto);
    }

    @Override
    protected void initData() {
        name.setText(courseName);
        Id.setText(courseId);
        startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23,00,00,00);
        endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28,00,00,00);
        selectedDate = Calendar.getInstance();

        verificationCode.setOnInputListener(new GjySerialnumberLayout.OnInputListener() {
            @Override
            public void onSucess(String code) {
                System.out.println("?????????:"+code);
                Signcode = code;
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect.setImageResource(R.mipmap.down);
                showDate();
            }
        });

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect1.setImageResource(R.mipmap.down);
                showDate1();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Sign();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //???????????????????????????????????????
    private void Sign() throws java.text.ParseException {
        String time_start = show_time.getText().toString().trim();
        String time_end = show_time1.getText().toString().trim();
        String class_number = courseNumber.getText().toString().trim();
        String class_address = this.address.getText().toString().trim();
        if (!StringUtils.isEmpty(time_start)
                && !StringUtils.isEmpty(time_end) && !StringUtils.isEmpty(class_number)
                && !StringUtils.isEmpty(class_address) && !StringUtils.isEmpty(Signcode)){
            String id = getStringFromSp("id");
            int startDate = (int)(dateToStamp(time_start));
            int endDate = (int)(dateToStamp(time_end));
            showToast("startDate"+startDate);
            showToast("endDate"+endDate);
            SignDataBody signDataBody = new SignDataBody(startDate,class_address,Integer.parseInt(courseId),courseName,endDate,
                    Integer.parseInt(Signcode),Integer.parseInt(class_number),Integer.parseInt(id));

            Call<RegisterResponse> call = Api.api.ClassSign(HiRetrofit.appId, HiRetrofit.appSecret, signDataBody);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body().code == 200){
                        showToast("????????????");
                        Intent intent = new Intent(ShowClassActivity.this,SignDetailsActivity.class);
                        intent.putExtra("courseId",courseId);
                        intent.putExtra("num",class_number);
                        startActivity(intent);
                        finish();
                        // TODO: 2022/7/24 ???????????????????????????
                    }
                    else {
                        showToast(response.body().msg);
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable throwable) {

                }
            });

        }else {
            showToast("???????????????");
        }

    }


    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date);
                show_time.setText(time);
                timeSelect.setImageResource(R.mipmap.right);
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})// ??????????????????
                .setCancelText("??????")//??????????????????
                .setSubmitText("??????")//??????????????????
                .setDate(selectedDate)// ?????????????????????????????????????????????*/
                .setRangDate(startDate,endDate)//???????????????????????????
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setLabel(" ???", "???", "???", "???", "???", "???")
                .build();
        pvTime.show();
    }

    private void showDate1() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date);
                show_time1.setText(time);
                timeSelect1.setImageResource(R.mipmap.right);
            }
        })
                .setType(new boolean[]{true, true, true, true, true, true})// ??????????????????
                .setCancelText("??????")//??????????????????
                .setSubmitText("??????")//??????????????????
                .setDate(selectedDate)// ?????????????????????????????????????????????*/
                .setRangDate(startDate,endDate)//???????????????????????????
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .setLabel(" ???", "???", "???", "???", "???", "???")
                .build();
        pvTime.show();
    }

    /*
     * ???????????????????????????
     */
    @SuppressLint("SimpleDateFormat")
    public static long dateToStamp(String s) throws ParseException, java.text.ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = null;
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date date = null;
        date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    private String getTime(Date date) {//???????????????????????????????????????
        //"YYYY-MM-DD HH:MM:SS"        "yyyy-MM-dd"
        SimpleDateFormat format = null;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                Delete();
                break;
            default:break;
        }
        if(item.getItemId()==android.R.id.home){
            navigateTo(TeacherActivity.class);
            finish();
        }
        return true;
    }

    private void Delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("?????????????????????????????????");
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteClass();
            }
        });
        builder.setNeutralButton("??????",null);
        builder.show(); //??????show()????????????????????????
    }

    private void DeleteClass() {
        String id = getStringFromSp("id");
        if (!StringUtils.isEmpty(courseId) && !StringUtils.isEmpty(id)){
            Call<RegisterResponse> call = Api.api.deleteClass(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(id), Integer.parseInt(courseId));
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body().code == 200){
                        showToast("????????????");
                        navigateTo(TeacherActivity.class);
                        finish();
                    }else {
                        showToast("????????????");
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable throwable) {

                }
            });
        }

    }

}