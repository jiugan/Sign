package com.example.sign.ui.api;


import com.example.sign.ui.entity.ClassDataBody;
import com.example.sign.ui.entity.ClassDetailsResponse;
import com.example.sign.ui.entity.LoginResponse;
import com.example.sign.ui.entity.PersonalDataBody;
import com.example.sign.ui.entity.RegisterBody;
import com.example.sign.ui.entity.RegisterResponse;
import com.example.sign.ui.entity.SignBody;
import com.example.sign.ui.entity.SignDataBody;
import com.example.sign.ui.entity.SignResponse;
import com.example.sign.ui.entity.StudentCourseDetailsResponse;
import com.example.sign.ui.entity.StudentSignListResponse;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {
    Api api = HiRetrofit.retrofit.create(Api.class);
    //登录接口
    @POST(value = "/member/sign/user/login")
    @FormUrlEncoded
    Call<LoginResponse> login(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Field("username") String name,
            @Field("password") String pwd
    );

    //注册接口
    @POST(value = "/member/sign/user/register")
    Call<RegisterResponse> register(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Body RegisterBody body
//            @Field("userName") String name,
//            @Field("password") String pwd,
//            @Field("roleId")  int id
    );

    //修改用户信息
    @POST(value = "/member/sign/user/update")
    Call<RegisterResponse> change(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Body PersonalDataBody body
    );

    //上传图片
    @POST(value = "/member/sign/image/upload")
    @Multipart
    Call<RegisterResponse> uploadImage(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Part MultipartBody.Part file
    );

    //老师添加课程
    @POST(value = "/member/sign/course/teacher")
    Call<RegisterResponse> AddClass(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Body ClassDataBody body
            );

    //查看老师还没结课的课程
    @GET(value = "/member/sign/course/teacher/unfinished")
    Call<ClassDetailsResponse> GetNoFinish(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id
    );

    //查看老师结课的课程
    @GET(value = "/member/sign/course/teacher/finished")
    Call<ClassDetailsResponse> GetFinish(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id
    );

    //发起签到接口
    @POST(value = "/member/sign/course/teacher/initiate")
    Call<RegisterResponse> ClassSign(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Body SignDataBody body
            );


    @GET(value = "/member/sign/course/teacher/page")
    Call<SignResponse> SignData(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("courseId") int courseId
    );

    //老师删除课程
    @DELETE(value = "/member/sign/course/teacher")
    Call<RegisterResponse> deleteClass(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("courseId") int courseId
    );

    //学生获取所有课程
    @GET(value = "/member/sign/course/all")
    Call<ClassDetailsResponse> StudentGetData(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret
    );

    //学生获取当前课程的详细信息
    @GET(value = "/member/sign/course/detail")
    Call<StudentCourseDetailsResponse> getClassDetails(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("courseId") int courseId
    );

    //学生加入课程
    @POST(value = "/member/sign/course/student/select")
    Call<RegisterResponse> StudentAddClass(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("courseId") int courseId
    );

    //学生获取已经选课的课程
    @GET(value = "member/sign/course/student")
    Call<ClassDetailsResponse> StudentGetJoinClass(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id
    );

    //学生退课
    @DELETE(value = "/member/sign/course/student/drop")
    Call<RegisterResponse> StudentDropoutClass(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("courseId") int courseId
    );

    //StudentSignListResponse
    //学生获取签到列表
    @GET(value = "/member/sign/course/student/signList")
    Call<StudentSignListResponse> StudentGetSignList(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Query("userId") int id,
            @Query("status") int status,
            @Query("courseId") int courseId
    );

    //签到
    @POST(value = "/member/sign/course/student/sign")
    Call<RegisterResponse> Sign(
            @Header("appId") String appid,
            @Header("appSecret") String appSecret,
            @Body SignBody body
    );
}
