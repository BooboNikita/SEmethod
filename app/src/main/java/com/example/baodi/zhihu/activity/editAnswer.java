package com.example.baodi.zhihu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.google.gson.Gson;
import com.sendtion.xrichtext.RichTextEditor;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class editAnswer extends AppCompatActivity {

    private ImageView close_btn,send_btn;
    private RichTextEditor answer_edit;
    private TextView ques_title;
    String title;
    String quesID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_answer);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        quesID = intent.getStringExtra("quesID");
        Log.d("title",title);
        initUI();


    }

    private void initUI(){
        close_btn = (ImageView) findViewById(R.id.editanswer_close_btn);
        send_btn = (ImageView) findViewById(R.id.editanswer_send_btn);
        answer_edit = (RichTextEditor) findViewById(R.id.et_new_content);
        ques_title = (TextView) findViewById(R.id.editanswer_title);

        ques_title.setText(title);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = getEditData();
                Log.d("answer",answer);
                Log.d("question",quesID);

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
                    paramsMap.put("question",quesID);
                    paramsMap.put("body",answer);
                    paramsMap.put("anonymous", "false");
                    String strEntity = gson.toJson(paramsMap);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);

                    Call call = request_interface.postAnswer(body,token);
                    call.enqueue(new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                String responseBodyString = response.body().toString();
                                Log.d("Response body", responseBodyString);



                            }else {
                                Log.d("Response errorBody", response.toString());
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d("connect:", "failure");
                        }
                    });
                    Toast.makeText(editAnswer.this,"回答成功！",Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("key","123");
        setResult(RESULT_OK,intent);
        super.finish();
    }

    private String getEditData() {
        List<RichTextEditor.EditData> editList = answer_edit.buildEditData();
        StringBuffer content = new StringBuffer();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr);
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
            }
        }
        return content.toString();
    }


}
