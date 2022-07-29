package com.example.sign.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sign.R;
import com.example.sign.ui.activity.SplashActivity;
import com.example.sign.ui.api.Api;
import com.example.sign.ui.entity.ClassDetailsResponse;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ClassDetailsResponse.DataBean.RecordsBean> mDatas;
    private OnRecyclerViewItemClickListener myClickItemListener;// 声明自定义的接口

    public ClassAdapter(Context context, List<ClassDetailsResponse.DataBean.RecordsBean> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    public ClassAdapter(Context context){
        this.mContext = context;
    }

    public void setDatas(List<ClassDetailsResponse.DataBean.RecordsBean> datas) {
        this.mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.classdetails_item,viewGroup,false);
        return new ViewHolder(view,myClickItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ClassDetailsResponse.DataBean.RecordsBean data = mDatas.get(position);
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.ClassName.setText(data.courseName);
        holder.ClassSchoolName.setText(data.collegeName);
        System.out.println(holder.ClassName);
        System.out.println(holder.ClassSchoolName);
        Glide.with(mContext).load(data.coursePhoto).into(holder.ClassImage);
    }

    @Override
    public int getItemCount() {
        if (mDatas!=null && mDatas.size() > 0){
            return mDatas.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView ClassName;
        private TextView ClassSchoolName;
        private ImageView ClassImage;
        private OnRecyclerViewItemClickListener mListener;// 声明自定义的接口

        public ViewHolder(@NonNull View view,OnRecyclerViewItemClickListener mListener) {
            super(view);
            ClassName = view.findViewById(R.id.ClassName);
            ClassSchoolName = view.findViewById(R.id.ClassSchoolName);
            ClassImage = view.findViewById(R.id.ClassImage);
            this.mListener = mListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //getAdapterPosition()为Viewholder自带的一个方法，用来获取RecyclerView当前的位置，将此作为参数，传出去
            mListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnRecyclerViewItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.myClickItemListener = listener;
    }
}
