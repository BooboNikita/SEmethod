package com.example.baodi.zhihu.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baodi.zhihu.R;

public class questionPage extends AppCompatActivity {

    private LinearLayout tag_layout;
    private TextView question_text,question_description,questionpage_follow_number,
            questionpage_question_comment_number, questionpage_answer_number;
    private ListView quesion_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        initUI();
    }

    private void initUI(){
        tag_layout = (LinearLayout) findViewById(R.id.tabbar_layout);
        question_description = (TextView) findViewById(R.id.question_description_text);
        questionpage_follow_number = (TextView) findViewById(R.id.questionpage_follow_number);
        questionpage_question_comment_number = (TextView) findViewById(R.id.questionpage_question_comment_number);
        questionpage_answer_number = (TextView) findViewById(R.id.questionpage_answer_number);
        quesion_list = (ListView) findViewById(R.id.questionpage_answer_list);
    }

    


}
