package com.example.baodi.zhihu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;
import com.example.baodi.zhihu.SomeClass.Topic;
import com.example.baodi.zhihu.Utils.ImageUtils;
import com.example.baodi.zhihu.Utils.ScreenUtils;
import com.example.baodi.zhihu.Utils.StringUtils;
import com.google.gson.Gson;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.RichTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class answerPage extends AppCompatActivity {


    private Answer answer;
    private TextView quesion_text,answer_number,answertext,toolbar_title,write_answer,collect_text,answerpage_scan_answer_number;
    private Button edit_answer,like_btn;
    private RichTextView answer_text;
    private ImageView back_btn2,collect_star,share;
    private ImageButton back_btn;
    private RelativeLayout answer_toolbar;
    private LinearLayout collect_btn;
    private Question question;
    private TextView quesion_text,answer_number,answer_text;
    int answerID;
    static Handler handler;
    private List<Answer> answerList;
    private List<Integer> voteList,favList;
    private CollapsingToolbarLayoutState state;
    private Boolean like_state,fav_state;
    private AppBarLayout appBarLayout;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_page);

        Bundle bundle = getIntent().getExtras();
        answerID = bundle.getInt("answerID");
        answerList = (List<Answer>) bundle.get("answer_list");
        voteList = new ArrayList<>();
        favList = new ArrayList<>();
//        answerList = (List<Answer>) bundle.get("answer_list");

        Log.d("answerID", String.valueOf(answerID));
//        Log.d("numberofList", String.valueOf(answerList.size()));

        getVoteList();
        getFavList();

        update_Info();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
//                    Log.d("numberlist", String.valueOf(tmp.answer_number));
                    initUI();
                    getAnswerCount();
                }
                else if(msg.what == 2) {
                    answer_number.setText("查看全部"+question.answer_number+"个回答");
                }
                else if (msg.what == 2){
                    like_state = true;
                    like_btn.setTextColor(getResources().getColor(R.color.colorPrimary,null));
                    like_btn.setText("已赞同"+answer.like_number);
                    getVoteList();
                }
                else if(msg.what == 3){
                    like_state = false;
                    like_btn.setTextColor(getResources().getColor(R.color.black,null));
                    like_btn.setText("赞同"+answer.like_number);
                    getVoteList();
                }
                else if(msg.what == 4){
                    collect_star.setImageResource(R.drawable.collect_highlight);
                    collect_text.setText("已收藏");
                    collect_text.setTextColor(getResources().getColor(R.color.colorPrimary,null));
                    fav_state = true;
                    getFavList();
                }
                else if(msg.what == 5){
                    collect_star.setImageResource(R.drawable.collect);
                    collect_text.setText("收藏");
                    collect_text.setTextColor(getResources().getColor(R.color.grey1,null));
                    fav_state = false;
                    getFavList();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void initUI(){
        appBarLayout = (AppBarLayout) findViewById(R.id.answerpage_appbar);
        quesion_text = (TextView) findViewById(R.id.answerpage_quesion_text);
        answer_number = (TextView) findViewById(R.id.answerpage_scan_answer_number);
        answertext = (TextView) findViewById(R.id.answerpage_answer);
        edit_answer = (Button) findViewById(R.id.answerpage_writeanswer_btn);
        answer_toolbar = (RelativeLayout) findViewById(R.id.answerpage_toolbar);
        back_btn = (ImageButton) findViewById(R.id.answerpage_back_btn);
        back_btn2 = (ImageView) findViewById(R.id.answerpage_back_btn2);
        toolbar_title = (TextView) findViewById(R.id.answerpage_quesion_toolbar);
//        answer_text = (RichTextView) findViewById(R.id.answerpage_answer_text);
        write_answer = (TextView) findViewById(R.id.answerpage_writeanswer_btn2);
        like_btn = (Button) findViewById(R.id.answerpage_like_btn);
        collect_btn = (LinearLayout) findViewById(R.id.answerpage_collect_btn);
        collect_star = (ImageView) findViewById(R.id.answerpage_collect_star);
        collect_text = (TextView) findViewById(R.id.answerpage_collect_text);
        answerpage_scan_answer_number = (TextView) findViewById(R.id.answerpage_scan_answer_number);
        share = (ImageView) findViewById(R.id.answerpage_share_btn);

        quesion_text.setText(answer.quesContent);
        answer_number.setText("查看全部"+answerList.size()+"个回答");
        answertext.setText(answer.answerContent);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
//                        collapsingToolbarLayout.setTitle("EXPANDED");//设置title为EXPANDED
                        answer_toolbar.setVisibility(View.INVISIBLE);
//                        Log.d("state","EXPANDED");
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
//                        collapsingToolbarLayout.setTitle("");//设置title不显示
//                        playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
                        answer_toolbar.setVisibility(View.VISIBLE);
                        toolbar_title.setText(answer.quesContent);

                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
//                        Log.d("state","COLLAPSED");
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
//                            playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
//                        collapsingToolbarLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间]
//                        Log.d("state","INTERNEDIATE");
                    }
                }

            }
        });

//        answer_text.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("content",answer.answerContent);
//                showEditData(answer.answerContent);
//            }
//        });


        edit_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(answerPage.this,editAnswer.class);
                intent.putExtra("title",answer.quesContent);
                intent.putExtra("quesID",String.valueOf(answer.quesID));
                startActivityForResult(intent,1);
            }
        });
        write_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(answerPage.this,editAnswer.class);
                intent.putExtra("title",answer.quesContent);
                intent.putExtra("quesID",String.valueOf(answer.quesID));
                startActivityForResult(intent,1);
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("back_btn","nidaye");
                finish();
            }
        });
        back_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("back_btn2","nidaye");
                finish();
            }
        });

        if(voteList.contains(answerID)){
            like_btn.setTextColor(getResources().getColor(R.color.colorPrimary,null));
            like_btn.setText("已赞同"+answer.like_number);
            like_state = true;
        }
        else {
            like_btn.setTextColor(getResources().getColor(R.color.black,null));
            like_btn.setText("赞同"+answer.like_number);
            like_state = false;
        }
        if(favList.contains(answerID)){
            collect_star.setImageResource(R.drawable.collect_highlight);
            collect_text.setText("已收藏");
            collect_text.setTextColor(getResources().getColor(R.color.colorPrimary,null));
            fav_state = true;
        }
        else {
            collect_star.setImageResource(R.drawable.collect);
            collect_text.setText("收藏");
            collect_text.setTextColor(getResources().getColor(R.color.grey1,null));
            fav_state = false;
        }

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like_state == false){
                    voteAnswer();
                }
                else if(like_state == true){
                    delvoteAnswer();
                }
            }
        });

        collect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fav_state == false){
                    addFav();
                }
                else if(fav_state == true){
                    delFavAnswer();
                }
            }
        });

        answerpage_scan_answer_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(answerPage.this,questionPage.class);
               intent.putExtra("ques_ID",answer.quesID);
               startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("share_btn","nidaye");
            }
        });


    }

    private void addFav(){
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
            paramsMap.put("answer",String.valueOf(answerID));
            String strEntity = gson.toJson(paramsMap);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);

            Call call = request_interface.postFav(body,token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONObject json = new JSONObject(responseBodyString);
                            answer.like_number = json.getInt("answer_vote");
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

    private void delFavAnswer(){
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            Call call = request_interface.deleteFav(token,String.valueOf(answerID));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Message msg = Message.obtain();
                        msg.what = 5;
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

    private void voteAnswer(){
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
            paramsMap.put("answer",String.valueOf(answerID));
            paramsMap.put("vote_type","up");
            String strEntity = gson.toJson(paramsMap);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);

            Call call = request_interface.postVote(body,token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONObject json = new JSONObject(responseBodyString);
                            answer.like_number = json.getInt("answer_vote");
                            Message msg = Message.obtain();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.d("Response errorBody", response.toString());
                    }
                }
//        answer_number.setText("查看全部"+answerList.size()+"个回答");
        answer_text.setText(answer.answerContent);

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.d("connect:", "failure");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delvoteAnswer(){
        SharedPreferences sp = getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {
            Call call = request_interface.cancelVote(token,String.valueOf(answerID));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
//                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
//                        try {
//                            JSONObject json = new JSONObject(responseBodyString);
                            answer.like_number -= 1;
                            Message msg = Message.obtain();
                            msg.what = 3;
                            handler.sendMessage(msg);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

//                        try {
//
//                            Message msg = new Message();
//                            msg.what = 1;
//                            handler.sendMessage(msg);
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

    protected void showEditData(String content) {
        answer_text.clearAllLayout();
        List<String> textList = StringUtils.cutStringByImgTag(content);
        for (int i = 0; i < textList.size(); i++) {
            String text = textList.get(i);
            Log.d("text",text);
            if (text.contains("<img")) {
                String imagePath = StringUtils.getImgSrc(text);
                int width = ScreenUtils.getScreenWidth(this);
                int height = ScreenUtils.getScreenHeight(this);
                answer_text.measure(0,0);
                Bitmap bitmap = ImageUtils.getSmallBitmap(imagePath, width, height);
                if (bitmap != null){
                    answer_text.addImageViewAtIndex(answer_text.getLastIndex(), imagePath);
                } else {
                    answer_text.addTextViewAtIndex(answer_text.getLastIndex(), text);
                }
                answer_text.addTextViewAtIndex(answer_text.getLastIndex(), text);
            }
        }
    }


    private void getAnswerCount(){
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
            Call call = request_interface.getQuestionsinID(String.valueOf(answer.quesID));
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
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
                            Message msg = new Message();
                            msg.what = 2;
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
            Call call = request_interface.getAnswersinID(String.valueOf(answerID));
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
                            answer.quesID = Integer.parseInt(json.get("question_id").toString());
                            Message msg = Message.obtain();
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

    private void getVoteList(){
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
            Call call = request_interface.getVoteList(token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
                        try {
                            JSONArray jsonArray = new JSONArray(responseBodyString);
                            voteList.clear();
                            for(int j=0;j<jsonArray.length();j++){
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                if(jsonObject.getString("vote_type").equals("up")){
                                    Integer tmp = jsonObject.getInt("answer");
                                    voteList.add(tmp);
                                }
                            }
//                            Log.d("length",String.valueOf(json.length()));
//                            Message msg = new Message();
//                            msg.what = 1;
//                            handler.sendMessage(msg);
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

    private void getFavList(){
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
            Call call = request_interface.getFavList(token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
                        favList.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBodyString);
                            for(int j=0;j<jsonArray.length();j++){
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                favList.add(jsonObject.getInt("answer"));
                            }
//                            Log.d("length",String.valueOf(json.length()));
//                            Message msg = new Message();
//                            msg.what = 1;
//                            handler.sendMessage(msg);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("result",String.valueOf(resultCode));

    }
    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("key","123");
        setResult(RESULT_OK,intent);
        super.finish();
    }
}
