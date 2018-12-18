package com.example.baodi.zhihu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;
import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Topic;
import com.example.baodi.zhihu.SomeClass.User;
import com.example.baodi.zhihu.UnScrollListView;
import com.example.baodi.zhihu.MyScrollView;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class questionPage extends AppCompatActivity {

    private LinearLayout tag_layout;
    private TextView question_text,question_description,questionpage_follow_number,
            questionpage_question_comment_number, questionpage_answer_number;
    private TextView list_item_authorname,list_item_answer;
    private UnScrollListView answer_list;
    private MyScrollView mScrollView;
    private List<Answer> answerList;
    private List<Integer> answerID_list;
    private int questionId;
    private Question question;
    private BaseAdapter adapter;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        answerList = new ArrayList<>();
        answerID_list = new ArrayList<>();
        update_Info();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
//                    Log.d("numberlist", String.valueOf(tmp.answer_number));

                    for(int i=0;i<answerList.size();i++){
                        Answer tmp = answerList.get(i);
                        Log.d("title",tmp.answerContent);
                    }
                    initUI();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void initUI(){
        tag_layout = (LinearLayout) findViewById(R.id.tabbar_layout);
        question_text = (TextView) findViewById(R.id.question_text);
        question_description = (TextView) findViewById(R.id.question_description_text);
        questionpage_follow_number = (TextView) findViewById(R.id.questionpage_follow_number);
//        questionpage_question_comment_number = (TextView) findViewById(R.id.questionpage_question_comment_number);
        questionpage_answer_number = (TextView) findViewById(R.id.questionpage_answer_number);
        answer_list = (UnScrollListView) findViewById(R.id.questionpage_answer_list);



        final LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.questionpage_tag_tab);
        TextView topic_text = (TextView) inflater.inflate(R.layout.questionpage_topic_tag,null).findViewById(R.id.questionpage_topic_tag);
        topic_text.setText(question.topic_tag.text);

        linearLayout.addView(topic_text);


        answer_list.setFocusable(false);



        question_text.setText(question.title);
        question_description.setText(question.quesDescription);
        questionpage_follow_number.setText(String.valueOf(question.follow_number)+"人关注");
        questionpage_answer_number.setText(String.valueOf(question.answer_number)+"个回答");
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                Log.d("numberoflist", String.valueOf(answerList.size()));
                return answerList.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                LayoutInflater layoutInflater = questionPage.this.getLayoutInflater();
                View v;
                if(view == null){
                    v = layoutInflater.inflate(R.layout.quesionpage_list_item,null);
                }
                else {
                    v = view;
                    Log.d("info","有缓存，不需要重新生成"+i);
                }
                list_item_authorname = (TextView) v.findViewById(R.id.questionpage_author_name);
                list_item_answer = (TextView) v.findViewById(R.id.questionpage_answer_content);

                Answer tmp = (Answer) answerList.get(i);
                list_item_authorname.setText(tmp.author_name);
                list_item_answer.setText(tmp.answerContent);

                return v;
            }
        };
        answer_list.setAdapter(adapter);
        answer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                Answer tmp = (Answer) answerList.get(i);
                bundle.putInt("answerID",tmp.answerID);
                bundle.putSerializable("answer_list", (Serializable) answerID_list);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(questionPage.this,answerPage.class);
                startActivity(intent);
            }
        });
    }

    private void update_Info() {
        // 获取登陆后本地token
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            // GET 方法调用
            Call call = request_interface.getQuestionsinID("1");
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONObject json = new JSONObject(responseBodyString);
                            question = new Question();
                            question.quesID = Integer.parseInt(json.get("id").toString());
                            question.anonymity = Boolean.parseBoolean(json.get("anonymous").toString());
                            question.title = json.get("title").toString();
                            question.quesDescription = json.get("body").toString();
                            question.answer_number = Integer.parseInt(json.get("answers_count").toString());
                            question.follow_number = Integer.parseInt(json.get("flows").toString());
                            question.topic_tag = new Topic();
                            question.topic_tag.text = json.get("topic_name").toString();
                            if (question.answer_number != 0) {
                                getAnswer(json);
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.d("Response errorBody", response.toString());
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d("connect:", "failure");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAnswer(JSONObject json) throws JSONException {
            JSONArray jsonArray = json.getJSONArray("answers");
            for(int j=0;j<jsonArray.length();j++){
                JSONObject jsonObject = jsonArray.getJSONObject(j);
                Answer ans = new Answer();
                ans.answerID = Integer.parseInt(jsonObject.get("id").toString());
                answerID_list.add(ans.answerID);
                ans.quesID = Integer.parseInt(jsonObject.get("question_id").toString());
                ans.quesContent = jsonObject.get("question_title").toString();
                ans.author_name = jsonObject.get("author_name").toString();
                ans.answerContent = jsonObject.get("body").toString();
                ans.anonymity = Boolean.parseBoolean(jsonObject.get("anonymous").toString());
                answerList.add(ans);
            }
    }

}
