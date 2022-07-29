package com.example.sign.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sign.R;
import com.example.sign.ui.activity.PersonalDataActivity;
import com.example.sign.ui.activity.SplashActivity;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.utils.StringUtils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Teacher_NotificationsFragment extends BaseFragment {

    private LinearLayout personalData;
    private LinearLayout back;
    private CircleImageView image;

    public Teacher_NotificationsFragment() {
        // Required empty public constructor
    }
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    public static Teacher_NotificationsFragment newInstance(String param1, String param2) {
        Teacher_NotificationsFragment fragment = new Teacher_NotificationsFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_teacher__notifications;
    }

    @Override
    protected void initView() {
        personalData = mRootView.findViewById(R.id.teacher_notifications_personalData);
        back = mRootView.findViewById(R.id.teacher_notifications_back);
        init();
    }

    private void init() {
        TextView name =  mRootView.findViewById(R.id.name);
        TextView email =  mRootView.findViewById(R.id.email);
        image = mRootView.findViewById(R.id.login_image);

        String realName = getStringFromSp("realName");
        String email1 = getStringFromSp("email");
        String touXiang = getStringFromSp("avatar");

        name.setText(realName);
        email.setText(email1);

        //处理头像的加载问题
        if (!StringUtils.isEmpty(touXiang)){
            Glide.with(this).load(touXiang).into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    }
                    else {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, 2);
                    }
                }
            }
        });
        if (!StringUtils.isEmpty(touXiang)){
            System.out.println(touXiang);
        }
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
                File file1 = new File(uriToFile(uri),"image.jpg");

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

//                RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"));

                Call<RegisterResponse> call = Api.api.uploadImage(HiRetrofit.appId, HiRetrofit.appSecret, body);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.body().code == 200){
                            Glide.with(getActivity()).load(response.body().data).into(image);
                            saveStringToSp("avatar",response.body().data);
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

    @Override
    protected void initData() {
        //点击进入个人信息页面
        personalData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(PersonalDataActivity.class);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("您确定退出程序吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteStringFromSp("appKey");
                        deleteStringFromSp("id");
                        deleteStringFromSp("phone");
                        deleteStringFromSp("realName");
                        deleteStringFromSp("email");
                        deleteStringFromSp("collegeName");
                        navigateToWithFlag(SplashActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                });
                builder.setNeutralButton("取消",null);
                builder.show(); //调用show()方法来展示对话框
            }
        });
    }

    private File uriToFile(Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null,
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