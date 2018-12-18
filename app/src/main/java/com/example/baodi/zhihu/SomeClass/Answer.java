package com.example.baodi.zhihu.SomeClass;

import java.util.List;

public class Answer {
  public int answerID;
  public int quesID;
  public String quesContent;
  public String author_name;
  public String answerContent;
  public boolean anonymity;
  public int like_number;
  public int dislike_number;
  public int comment_number;
  public List<Comment> comment_list;
}
