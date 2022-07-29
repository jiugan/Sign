package com.example.sign.ui.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.sign.R;
import com.example.sign.databinding.ActivityTeacherBinding;
import com.example.sign.ui.activity.CreateLessonActivity;
import com.example.sign.ui.activity.TeacherActivity;
import com.google.android.material.card.MaterialCardView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class Teacher_HomeFragment extends BaseFragment {

    private Banner banner;
    private MaterialCardView createClass;
    private MaterialCardView accessClass;



    public Teacher_HomeFragment() {
        // Required empty public constructor
    }

    //定义图片加载器
    public class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }


    // TODO: Rename and change types and number of parameters
    public static Teacher_HomeFragment newInstance(String param1, String param2) {
        Teacher_HomeFragment fragment = new Teacher_HomeFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_teacher__home;
    }

    @Override
    protected void initView() {
        banner = (Banner) mRootView.findViewById(R.id.banner);
        createClass = mRootView.findViewById(R.id.Teacher_home_card1);
        accessClass = mRootView.findViewById(R.id.Teacher_home_card2);
        //初始化轮播图
        initPlayer();
    }

    @Override
    protected void initData() {
        //处理创建课堂
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(CreateLessonActivity.class);
            }
        });

        //处理进入课堂
        accessClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(TeacherActivity.class);
                getActivity().finish();
            }
        });
    }

    private void initPlayer() {
        //初始化图片数据
        List images = new ArrayList();
        images.add(R.drawable.b1);
        images.add(R.drawable.b2);
        images.add(R.drawable.b3);

        //初始化标题数据
        List titles = new ArrayList();
        titles.add("线下");
        titles.add("线上");
        titles.add("签到");


        //设置图片加载器
        banner.setImageLoader(new MyImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置轮播的动画效果
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间（设置2.5秒切换下一张图片）
        banner.setDelayTime(2000);
        //设置banner显示样式（带标题的样式）
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //增加监听事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
}