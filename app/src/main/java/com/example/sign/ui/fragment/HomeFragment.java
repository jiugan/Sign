package com.example.sign.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sign.R;
import com.example.sign.ui.activity.ShowClassActivity;
import com.example.sign.ui.activity.StudentCourseDetailsActivity;
import com.example.sign.ui.adapter.ClassAdapter;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.ClassDetailsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ClassAdapter classAdapter;
    SwipeRefreshLayout swipe;
    int i = 0;
    List<ClassDetailsResponse.DataBean.RecordsBean> datas = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerViewAll);
        swipe = mRootView.findViewById(R.id.swipe);
    }

    @Override
    protected void initData() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new ClassAdapter(getActivity());
        classAdapter.setDatas(datas);
        recyclerView.setAdapter(classAdapter);
        getNewsList();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipe.setRefreshing(false);
                    }
                }, 3000);
                getNewsList();
            }
        });

        classAdapter.setOnItemClickListener(new ClassAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                ClassDetailsResponse.DataBean.RecordsBean bean = datas.get(postion);
                System.out.println(bean.courseName);
                Intent intent = new Intent(getActivity(), StudentCourseDetailsActivity.class);
                intent.putExtra("courseName", bean.courseName);
                intent.putExtra("collegeName", bean.collegeName);
                intent.putExtra("courseId", bean.courseId);
                intent.putExtra("coursePhoto", bean.coursePhoto);
                startActivity(intent);
            }
        });
    }

    private void getNewsList() {
        Call<ClassDetailsResponse> call = Api.api.StudentGetData(HiRetrofit.appId,HiRetrofit.appSecret);
        call.enqueue(new Callback<ClassDetailsResponse>() {
            @Override
            public void onResponse(Call<ClassDetailsResponse> call, Response<ClassDetailsResponse> response) {
                if (response.body().code == 200) {
                    System.out.println(response.body().msg);
                    System.out.println(response.body().code);
                    int size = response.body().data.total;
                    int j = 0;
                    for (j = i; j < size; j++) {
                        datas.add(response.body().data.records.get(j));
                    }
                    i = j;
                    classAdapter.notifyDataSetChanged();
                } else {
                    showToast(response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ClassDetailsResponse> call, Throwable throwable) {

            }
        });
    }
}