package com.example.sign.ui.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.ClassDataBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.utils.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.widget.ImageView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLessonActivity extends BaseActivity {

    private Calendar selectedDate;
    private Calendar startDate;
    private Calendar endDate;
    private LinearLayout layout;
    private LinearLayout layout1;
    private Spinner spinner;
    public ImageView courseImage;
    private EditText declare;
    private TextView show_time;
    private ImageView timeSelect;
    private TextView show_time1;
    private ImageView timeSelect1;

    private Button addBtn;
    private String getContent;
    private String collegeName;
    private String realName;
    private String id;
    private String userName;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected int initLayout() {
        return R.layout.activity_create_lesson;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.createLesson_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        layout = findViewById(R.id.SelectTimeLayout);
        spinner = findViewById(R.id.course);
        courseImage = findViewById(R.id.courseImage);
        declare = findViewById(R.id.courseDecrale);
        show_time = findViewById(R.id.show_time);
        timeSelect = findViewById(R.id.timeSelect);
        addBtn = findViewById(R.id.add_btn);

        layout1 = findViewById(R.id.SelectTimeLayout1);
        show_time1 = findViewById(R.id.show_time1);
        timeSelect1 = findViewById(R.id.timeSelect1);

        collegeName = getStringFromSp("collegeName");
        realName = getStringFromSp("realName");
        id = getStringFromSp("id");
        userName = getStringFromSp("userName");
    }

    @Override
    protected void initData() {
        //初始化时间选择器
        //系统当前时间
        selectedDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        endDate = Calendar.getInstance();
        endDate.set(2069, 2, 28);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getContent = getResources().getStringArray(R.array.atype)[i];
                System.out.println("haha:"+getContent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        courseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(CreateLessonActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CreateLessonActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                }
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

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CommitData();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void GetImageUrl() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (data != null) {
                //得到图片的全路径
                Uri uri = data.getData();
                String string = uri.toString();
                File file = uriToFile(uri);
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                Call<RegisterResponse> call = Api.api.uploadImage(HiRetrofit.appId, HiRetrofit.appSecret, body);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.body().code == 200){
                            Glide.with(CreateLessonActivity.this).load(response.body().data).into(courseImage);
                            saveStringToSp("ClassImage",response.body().data);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                        System.out.println(throwable.getMessage()+"haha");
                    }
                });
            }
        }
    }

    //网络请求提交数据
    private void CommitData() throws java.text.ParseException {
        String time_start = show_time.getText().toString().trim();
        String time_end = show_time1.getText().toString().trim();
        String del = declare.getText().toString().trim();
        if (!StringUtils.isEmpty(time_start) && !StringUtils.isEmpty(time_end)
                && !StringUtils.isEmpty(del)&& !StringUtils.isEmpty(collegeName)&& !StringUtils.isEmpty(getContent)
                && !StringUtils.isEmpty(id) && !StringUtils.isEmpty(userName)&& !StringUtils.isEmpty(realName)){
            long startDate = dateToStamp(time_start);
            long endDate = dateToStamp(time_end);
//            Long s = dateToStamp(time_start);
//            long l = s / 1000;
            showToast("startDate:"+startDate);
            showToast("endDate:"+endDate);
            String imageUrl = getStringFromSp("ClassImage");
            ClassDataBody body = new ClassDataBody(collegeName,getContent,imageUrl,(int)endDate,del,realName,(int)startDate,Integer.parseInt(id),userName);
            // TODO: 2022/7/16 网络请求
            Call<RegisterResponse> call = Api.api.AddClass(HiRetrofit.appId, HiRetrofit.appSecret, body);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.body().code == 200){
                        showToast("添加成功");
                        navigateTo(TeacherActivity.class);
                        finish();
                    }else {
                        showToast(response.body().msg);
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable throwable) {

                }
            });
        }else {
            showToast("数据不完整");
        }

    }

    /*
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static long dateToStamp(String s) throws ParseException, java.text.ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
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

    private void showDate() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String time = getTime(date);
                show_time.setText(time);
                timeSelect.setImageResource(R.mipmap.right);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
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
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate,endDate)//起始终止年月日设定
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();
        pvTime.show();
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        //"YYYY-MM-DD HH:MM:SS"        "yyyy-MM-dd"
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private File uriToFile(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = managedQuery(uri, proj, null,
                null, null);
        if (actualimagecursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor.getString(actual_image_column_index);
        }
        File file = new File(img_path);
        return file;
    }

}