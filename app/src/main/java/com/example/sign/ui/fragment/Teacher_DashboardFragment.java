package com.example.sign.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sign.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class Teacher_DashboardFragment extends Fragment{

    public Teacher_DashboardFragment() {
        // Required empty public constructor
    }

    public static Teacher_DashboardFragment newInstance(String param1, String param2) {
        Teacher_DashboardFragment fragment = new Teacher_DashboardFragment();
        return fragment;
    }


    private View contextView;// 总视图
    private TabLayout tabLayout;
    private ViewPager viewpager;
    ArrayList fragmentList = new ArrayList<Fragment>();
    String[] temp = {"未结课","已结课"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_teacher__dashboard, container, false);
        tabLayout = contextView.findViewById(R.id.orderListTab);
        viewpager = contextView.findViewById(R.id.orderListContent);
        return contextView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // fragment中嵌套fragment, Manager需要用(getChildFragmentManager())
        MPagerAdapter mPagerAdapter = new MPagerAdapter(getChildFragmentManager());
        initFragment();
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(mPagerAdapter);
    }

    private void initFragment() {
        fragmentList.add(new NofinishclassFragment());
        fragmentList.add(new FinishClassFragment());
    }


    class MPagerAdapter extends FragmentPagerAdapter {


        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return (Fragment) fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        //返回tablayout的标题文字;
        @Override
        public CharSequence getPageTitle(int position) {
            return temp[position];
        }
    }

}