package com.example.baodi.zhihu.index;

/**
 * Created by lenovo on 2018/12/15.
 */



public class ContentItem {
    private String title;
//    private String content;
    private String attend_num;
    private String answer_num;
//    public ContentItem(String s1,String s2,String s3,String s4){
//        this.title=s1;
//        this.content=s2;
//        this.attend_num=s3;
//        this.answer_num=s4;
//    }
    public ContentItem(String s1,String s2,String s3){
        this.title=s1;
        this.attend_num=s2;
        this.answer_num=s3;
    }
    public String getTitle(){
        return title;
    }
//    public String getContent(){
//        return content;
//    }
    public String getAttend_num(){
        return attend_num;
    }
    public String getAnswer_num(){
        return answer_num;
    }
}