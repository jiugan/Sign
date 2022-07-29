package com.example.sign.ui.entity;

import java.util.List;

public class ClassDetailsResponse {
    public Integer code;
    public String msg;
    public DataBean data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        public List<RecordsBean> records;
        public Integer total;
        public Integer size;
        public Integer current;
        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public static class RecordsBean {
            public String courseId;
            public String courseName;
            public String coursePhoto;
            public String collegeName;

            public String getCourseId() {
                return courseId;
            }

            public void setCourseId(String courseId) {
                this.courseId = courseId;
            }

            public String getCourseName() {
                return courseName;
            }

            public void setCourseName(String courseName) {
                this.courseName = courseName;
            }

            public String getCoursePhoto() {
                return coursePhoto;
            }

            public void setCoursePhoto(String coursePhoto) {
                this.coursePhoto = coursePhoto;
            }

            public String getCollegeName() {
                return collegeName;
            }

            public void setCollegeName(String collegeName) {
                this.collegeName = collegeName;
            }
        }
    }
//    int code;
//    String msg;
//    show data;
//
//    public class show{
//
//    }

}
