package com.example.sign.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sign.R;
import com.example.sign.ui.adapter.ClassAdapter;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.api.HiRetrofit;
import com.example.sign.ui.entity.ClassDetailsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinishClassFragment extends BaseFragment {


    public FinishClassFragment() {
        // Required empty public constructor
    }

    public static FinishClassFragment newInstance(String param1, String param2) {
        FinishClassFragment fragment = new FinishClassFragment();
        return fragment;
    }

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ClassAdapter classAdapter;
    SwipeRefreshLayout swipe;
    int i = 0;
    List<ClassDetailsResponse.DataBean.RecordsBean> datas = new ArrayList<>();

    @Override
    protected int initLayout() {
        return R.layout.fragment_finish_class;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        swipe = mRootView.findViewById(R.id.swipe1);
    }

    @Override
    protected void initData() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getNewsList();
        classAdapter = new ClassAdapter(getActivity());
        classAdapter.setDatas(datas);
        recyclerView.setAdapter(classAdapter);

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
    }

    private void getNewsList() {
        String id = getStringFromSp("id");
        int parseInt = Integer.parseInt(id);
        Call<ClassDetailsResponse> call = Api.api.GetFinish(HiRetrofit.appId, HiRetrofit.appSecret, parseInt);
        call.enqueue(new Callback<ClassDetailsResponse>() {
            @Override
            public void onResponse(Call<ClassDetailsResponse> call, Response<ClassDetailsResponse> response) {
                if (response.body().code == 200){
                    System.out.println(response.body().msg);
                    System.out.println(response.body().code);
                    int size = response.body().data.total;
                    int j = 0;
                    for (j = i; j < size; j++) {
                        datas.add(response.body().data.records.get(j));
                    }
                    i = j;
                    classAdapter.notifyDataSetChanged();
                }
                else {
                    showToast(response.body().msg);
                }
            }

            @Override
            public void onFailure(Call<ClassDetailsResponse> call, Throwable throwable) {

            }
        });
    }


}