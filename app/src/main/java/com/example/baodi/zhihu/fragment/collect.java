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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class collect extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView question_text, questionpage_follow_number,
            questionpage_answer_number;
    private ListView question_list;
    private List<Answer> questionList;
    private List<Integer> questionID_list;
    private int questionId;
    private Question question;
    private BaseAdapter adapter;
    Handler handler;


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

    private OnFragmentInteractionListener mListener;

    public collect() {
        // Required empty public constructor
    }

    /**
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_collect, container, false);
        questionList = new ArrayList<>();
        questionID_list = new ArrayList<>();
        SharedPreferences sp = getActivity().getSharedPreferences("loginToken", 0);
//        final String token = sp.getString("token", null);
        final String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozLCJ1c2VybmFtZSI6ImJhb2RpIiwiZXhwIjoxNTQ1NzA2NjIwLCJlbWFpbCI6IiJ9.kD729UXByGTD5-nhQ7zoSahNRhOform4VsXE9dNsllU";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        //        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 1) {
//           //        Log.d("numberlist", String.valueOf(tmp.answer_number));
//                    initUI(view);
//                }
//                super.handleMessage(msg);
//            }
//        };
        return view;
    }





    //获取数据的函数，具体修改其中的json赋值
//    private void update_Info() {
//        // 获取登陆后本地token
//        SharedPreferences sp = getSharedPreferences("loginToken", 0);
//        final String token = sp.getString("token", null);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Request_Interface.ENDPOINT)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        final Request_Interface request_interface = retrofit.create(Request_Interface.class);
//
//        try {
//            // GET 方法调用
//            Call call = request_interface.getQuestionsinID("1");
//            call.enqueue(new Callback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    if (response.isSuccessful()) {
//                        String responseBodyString = response.body().toString();
//                        Log.d("Response body", responseBodyString);
//                        try {
//                            JSONObject json = new JSONObject(responseBodyString);
//                            question = new Question();
//                            question.quesID = Integer.parseInt(json.get("id").toString());
//                            question.  = Boolean.parseBoolean(json.get("anonymous").toString());
//                            question.title = json.get("title").toString();
//                            question.quesDescription = json.get("body").toString();
//                            question.answer_number = Integer.parseInt(json.get("answers_count").toString());
//                            question.follow_number = Integer.parseInt(json.get("flows").toString());
//                            question.topic_tag = new Topic();
//                            question.topic_tag.text = json.get("topic_name").toString();
//                            if (question.answer_number != 0) {
//                                getAnswer(json);
//                                Message msg = new Message();
//                                msg.what = 1;
//                                handler.sendMessage(msg);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }else {
//                        Log.d("Response errorBody", response.toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call call, Throwable t) {
//                    Log.d("connect:", "failure");
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }




    private void initUI(View v) {
        question_text = (TextView) v.findViewById(R.id.question_text);
        questionpage_follow_number = (TextView) v.findViewById(R.id.questionpage_follow_number);
        questionpage_answer_number = (TextView) v.findViewById(R.id.questionpage_answer_number);

        questionpage_follow_number.setText(String.valueOf(question.follow_number) + "人关注");
        questionpage_answer_number.setText(String.valueOf(question.answer_number) + "个回答");

        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                Log.d("numberoflist", String.valueOf(questionList.size()));
                return questionList.size();
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
                LayoutInflater layoutInflater = collect.this.getLayoutInflater();
                View v;
                if (view == null) {
                    v = layoutInflater.inflate(R.layout.collect_list_item, null);
                } else {
                    v = view;
                    Log.d("info", "有缓存，不需要重新生成" + i);
                }
                question_text = (TextView) v.findViewById(R.id.question_text);
                questionpage_follow_number = (TextView) v.findViewById(R.id.questionpage_follow_number);

                Question tmp = (Question) question;
                questionpage_follow_number.setText(tmp.follow_number);
                questionpage_answer_number.setText(tmp.answer_number);
                question_text.setText(tmp.title);

                return v;
            }
        };

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
