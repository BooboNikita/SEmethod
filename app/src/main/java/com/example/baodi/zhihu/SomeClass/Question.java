package com.example.baodi.zhihu.SomeClass;

import java.util.List;

public class Question {
    public int quesID;
    public int userID;
    public String title;
    public  String quesDescription;
    public boolean anonymity;
    public Topic topic_tag;
    public int comment_number;
    public List<Comment> comment_list;
    public int follow_number;
    public List<User> follow_list;
    public int answer_number;
    public List<Answer> answer_list;

}
