package com.example.baodi.zhihu.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Answer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class answerPage extends AppCompatActivity {


    private Answer answer;
    private TextView quesion_text, answer_number, answer_text;
    int answerID;
    static Handler handler;
    private List<Answer> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_page);

        Bundle bundle = getIntent().getExtras();
        answerID = bundle.getInt("answerID");
        answerList = (List<Answer>) bundle.get("answer_list");

        Log.d("answerID", String.valueOf(answerID));
        Log.d("numberofList", String.valueOf(answerList.size()));

        update_Info();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
//                    Log.d("numberlist", String.valueOf(tmp.answer_number));
                    initUI();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void initUI() {
        quesion_text = (TextView) findViewById(R.id.answerpage_quesion_text);
        answer_number = (TextView) findViewById(R.id.answerpage_scan_answer_number);
        answer_text = (TextView) findViewById(R.id.answerpage_answer_text);

        quesion_text.setText(answer.quesContent);
        answer_number.setText("查看全部" + answerList.size() + "个回答");
        answer_text.setText(answer.answerContent);


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
            Call call = request_interface.getFlowQuestionList(getPackageCodePath());
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
                        try {
                            JSONObject json = new JSONObject(responseBodyString);
                            answer = new Answer();
                            answer.quesID = Integer.parseInt(json.get("question_id").toString());
                            answer.anonymity = Boolean.parseBoolean(json.get("anonymous").toString());
                            answer.quesContent = json.get("question_title").toString();
                            answer.answerContent = json.get("body").toString();
                            answer.like_number = Integer.parseInt(json.get("vote").toString());
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
}
