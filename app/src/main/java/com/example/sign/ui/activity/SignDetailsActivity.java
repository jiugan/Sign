package com.example.sign.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sign.R;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.ClassDetailsResponse;
import com.example.sign.ui.entity.SignResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignDetailsActivity extends BaseActivity {

    private TextView sign_num;
    private TextView unsign_num;
    private SwipeRefreshLayout swift;
    private String courseId;
    private String num;

    @Override
    protected int initLayout() {
        return R.layout.activity_sign_details;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    int shuzi =  (int)msg.obj;
                    sign_num.setText(String.valueOf(shuzi));
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        sign_num = findViewById(R.id.yes);
        unsign_num = findViewById(R.id.no);
        swift = findViewById(R.id.swipe);

        Intent intent = getIntent();
        courseId = intent.getStringExtra("courseId");
        num = intent.getStringExtra("num");
    }

    @Override
    protected void initData() {
        swift.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swift.setRefreshing(false);
                    }
                }, 3000);
                getNewsList();
            }
        });
    }

    //发起网络请求获得数据
    private void getNewsList() {
        String Userid = getStringFromSp("id");
        Call<SignResponse> call = Api.api.SignData(HiRetrofit.appId, HiRetrofit.appSecret, Integer.parseInt(Userid), Integer.parseInt(courseId));
        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                if (response.body().code == 200){
                    System.out.println(response.body().data.courseNum);
//                    int courseNum = response.body().data.courseNum;
//                    new Thread(new Runnable() {
//                        public void run() {
//                            Message message = mHandler.obtainMessage();
//                            message.what = 1;
//                            message.obj = courseNum;
//                            mHandler.sendMessage(message);
//                        }
//                    }).start();

                    sign_num.setText(String.valueOf(response.body().data.courseNum));
                    String s = sign_num.getText().toString().trim();
                    int parseInt = Integer.parseInt(s);
                    int parseInt1 = Integer.parseInt(num);
                    int i = parseInt1 - parseInt;
                    unsign_num.setText(String.valueOf(i));
                    if (parseInt1 == Integer.parseInt(sign_num.getText().toString().trim())){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignDetailsActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("学生已经全部签到");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.show(); //调用show()方法来展示对话框
                    }
                }
                else {
                    showToast(response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable throwable) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            navigateTo(TeacherActivity.class);
            finish();
        }
        return true;
    }
}