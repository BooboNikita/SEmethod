package com.example.baodi.zhihu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;
import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Topic;
import com.example.baodi.zhihu.SomeClass.User;
import com.example.baodi.zhihu.UnScrollListView;
import com.example.baodi.zhihu.MyScrollView;
import com.example.baodi.zhihu.launcherPage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class questionPage extends AppCompatActivity {

    private LinearLayout tag_layout;
    private RelativeLayout add_answer,invite_answer;
    private TextView question_text,question_description,questionpage_follow_number,
            questionpage_question_comment_number, questionpage_answer_number;
    private TextView list_item_authorname,list_item_answer,list_item_likenumber;
    private UnScrollListView answer_list;
    private MyScrollView mScrollView;
    private List<Answer> answerList;
    private List<Integer> answerID_list,follow_list;
    private int questionId;
    private Question question;
    private BaseAdapter adapter;
    private ImageButton share_btn;
    private Button follow_quesion;
    Handler handler;
    private boolean follow_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_page);
        answerList = new ArrayList<>();
        answerID_list = new ArrayList<>();

        tag_layout = (LinearLayout) findViewById(R.id.tabbar_layout);
        question_text = (TextView) findViewById(R.id.question_text);
        question_description = (TextView) findViewById(R.id.question_description_text);
        questionpage_follow_number = (TextView) findViewById(R.id.questionpage_follow_number);
//        questionpage_question_comment_number = (TextView) findViewById(R.id.questionpage_question_comment_number);
        questionpage_answer_number = (TextView) findViewById(R.id.questionpage_answer_number);
        answer_list = (UnScrollListView) findViewById(R.id.questionpage_answer_list);
        add_answer = (RelativeLayout) findViewById(R.id.questionpage_add_answer);
        invite_answer = (RelativeLayout) findViewById(R.id.questionpage_invite_answer);
        share_btn = (ImageButton) findViewById(R.id.questionpage_share_btn);
        follow_quesion = (Button) findViewById(R.id.questionpage_follow_question);
        follow_list = new ArrayList<>();
        //TODO
        questionId = 4;

        getFollowList();

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
                else if(msg.what == 2){
                    follow_quesion.setText("√已关注");
                    follow_quesion.setTextColor(getResources().getColor(R.color.grey1,null));
                    follow_quesion.setBackground(getDrawable(R.drawable.shape_grey_coner));
                    follow_state = true;
                }
                else if(msg.what == 3){
                    follow_quesion.setText("+关注问题");
                    follow_quesion.setTextColor(getResources().getColor(R.color.white,null));
                    follow_quesion.setBackground(getDrawable(R.drawable.shape_corner));
                    follow_state = false;

                }
                else if(msg.what == 4){
                    questionpage_follow_number.setText(String.valueOf(follow_list.size())+"人关注");
                }
                super.handleMessage(msg);
            }
        };


    }

    private void initUI(){

        final LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.questionpage_tag_tab);
        TextView topic_text = (TextView) inflater.inflate(R.layout.questionpage_topic_tag,null).findViewById(R.id.questionpage_topic_tag);
        topic_text.setText(question.topic_tag.text);
        if(linearLayout.getChildCount() == 0 && !topic_text.getText().equals("null")){
            linearLayout.addView(topic_text);
        }
        else{
            linearLayout.setVisibility(View.GONE);
        }

        answer_list.setFocusable(false);

        question_text.setText(question.title);
        question_description.setText(question.quesDescription);
        questionpage_follow_number.setText(String.valueOf(follow_list.size())+"人关注");
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
                list_item_likenumber = (TextView) v.findViewById(R.id.questionpage_vote);

                Answer tmp = (Answer) answerList.get(i);
                list_item_authorname.setText(tmp.author_name);
                list_item_answer.setText(tmp.answerContent);
                list_item_likenumber.setText(tmp.like_number+"赞同");

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
                startActivityForResult(intent,1);
            }
        });
        add_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(questionPage.this,editAnswer.class);
                intent.putExtra("title",question.title);
                intent.putExtra("quesID",String.valueOf(question.quesID));
                startActivityForResult(intent,1);
            }
        });
        invite_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(questionPage.this,editQuestion.class);
                startActivityForResult(intent,1);
            }
        });
        follow_quesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(follow_state == true){
                    Log.d("state",String.valueOf(follow_state));
                    delfollow_question();
                }
                else {
                    Log.d("state",String.valueOf(follow_state));
                    follow_quesion();
                }
            }
        });
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("share_btn","nidaye");
            }
        });

        if(follow_list.contains(questionId)){
            follow_state = true;
            follow_quesion.setText("√已关注");
            follow_quesion.setTextColor(getResources().getColor(R.color.grey1,null));
            follow_quesion.setBackground(getDrawable(R.drawable.shape_grey_coner));
        }
        else{
            follow_state = false;
            follow_quesion.setText("+关注问题");
            follow_quesion.setTextColor(getResources().getColor(R.color.white,null));
            follow_quesion.setBackground(getDrawable(R.drawable.shape_corner));
        }
    }

    private void follow_quesion(){
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            Gson gson = new Gson();
            HashMap<String, String> paramsMap = new HashMap<>();
            // login
            paramsMap.put("question",String.valueOf(questionId));
            String strEntity = gson.toJson(paramsMap);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);

            Call call = request_interface.postFlowQuestion(body,token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        getFollowList();
//                        try {
//                            JSONArray jsonArray = new JSONArray(responseBodyString);
//                            for(int j=0;j<jsonArray.length();j++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(j);
//                                Integer tmp = jsonObject.getInt("question");
//                                follow_list.add(tmp);
//                            }
                            Message msg = Message.obtain();
                            msg.what = 2;
                            handler.sendMessage(msg);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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

    private void delfollow_question(){
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            Call call = request_interface.cancelFlowQuestion(token,String.valueOf(questionId));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
//                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
//                        try {
//                            JSONObject json = new JSONObject(responseBodyString);
                        getFollowList();
                        Message msg = Message.obtain();
                        msg.what = 3;
                        handler.sendMessage(msg);

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

    private void getFollowList(){
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            Call call = request_interface.getFlowQuestionList(token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        follow_list.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBodyString);
                            follow_list.clear();
                            for(int j=0;j<jsonArray.length();j++){
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                Integer tmp = jsonObject.getInt("question");
                                follow_list.add(tmp);
                            }
                            Message msg = Message.obtain();
                            msg.what = 4;
                            handler.sendMessage(msg);
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
            Call call = request_interface.getQuestionsinID("4");
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
                            }
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
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
                ans.like_number = Integer.parseInt(jsonObject.get("vote").toString());
                answerList.add(ans);
            }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("result",String.valueOf(resultCode));

        if(requestCode == 1 && resultCode == RESULT_OK){
            answerList.clear();
            answerID_list.clear();
            update_Info();
            adapter.notifyDataSetChanged();
        }
    }
}
