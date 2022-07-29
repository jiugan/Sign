package com.example.sign.ui.entity;

import java.util.List;

public class StudentSignListResponse {

    public String msg;
    public int code;
    public DataBean data;

    public  class DataBean {
        public int current;
        public List<RecordsBean> records;
        public int size;
        public int total;

        public  class RecordsBean {
            public String courseAddr;
            public String courseName;
            public Long createTime;
            public int userSignId;
        }
    }
}
