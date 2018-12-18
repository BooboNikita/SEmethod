package com.example.baodi.zhihu.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link collect.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link collect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class collect extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Answer> answerList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private LinearLayout tag_layout;
    private TextView question_text,question_description,questionpage_follow_number,
            questionpage_question_comment_number, questionpage_answer_number;
    private TextView list_item_authorname,list_item_answer;
    private int questionId;
    private Question question;
    private BaseAdapter adapter;
    Handler handler;
    private OnFragmentInteractionListener mListener;
    private ListView answer_list;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        answerList = new ArrayList<>();
        update_Info();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 1){
//                    Log.d("numberlist", String.valueOf(tmp.answer_number));
                    Log.d("numberoflist", String.valueOf(answerList.size()));
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

    public collect() {
        // Required empty public constructor
    }
    private JSONObject String2Json(String str) {
        try {
            JSONObject json = new JSONObject(str);
            Log.d("json", json.getString("token"));
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment collect.
     */
    // TODO: Rename and change types and number of parameters
    public static collect newInstance(String param1, String param2) {
        collect fragment = new collect();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initUI() {
        tag_layout = (LinearLayout) getView().findViewById(R.id.tabbar_layout);
        question_text = (TextView) getView().findViewById(R.id.question_text);
        questionpage_follow_number = (TextView) getView().findViewById(R.id.questionpage_follow_number);
//        questionpage_question_comment_number = (TextView) findViewById(R.id.questionpage_question_comment_number);
        questionpage_answer_number = (TextView) getView().findViewById(R.id.questionpage_answer_number);
        answer_list = (ListView) getView().findViewById(R.id.my_collect_list);



        question_text.setText(question.title);
        question_description.setText(question.quesDescription);
        questionpage_follow_number.setText(String.valueOf(question.follow_number)+"人关注");
        questionpage_answer_number.setText(String.valueOf(question.answer_number)+"个回答");
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
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
                //@看一下collect，原本是questionpage
                LayoutInflater layoutInflater = collect.this.getLayoutInflater();
                View v;
                if(view == null){
                    v = layoutInflater.inflate(R.layout.collect_list_item,null);
                }
                else {
                    v = view;
                    Log.i("info","有缓存，不需要重新生成"+i);
                }
//                list_item_authorname = (TextView) v.findViewById(R.id.questionpage_author_name);
//                list_item_answer = (TextView) v.findViewById(R.id.questionpage_answer_content);

                Answer tmp = (Answer) answerList.get(i);
                list_item_authorname.setText(tmp.author_name);
                list_item_answer.setText(tmp.answerContent);

                return v;


            }
        };
        answer_list.setAdapter(adapter);
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
//                            tmp.topic_tag = json.get("topic_name").toString();
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
            ans.quesID = Integer.parseInt(jsonObject.get("question_id").toString());
            ans.quesContent = jsonObject.get("question_title").toString();
            ans.author_name = jsonObject.get("author_name").toString();
            ans.answerContent = jsonObject.get("body").toString();
            ans.anonymity = Boolean.parseBoolean(jsonObject.get("anonymous").toString());
            answerList.add(ans);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
