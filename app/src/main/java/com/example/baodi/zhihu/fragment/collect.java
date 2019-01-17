package com.example.baodi.zhihu.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.SomeClass.Answer;
import com.example.baodi.zhihu.SomeClass.Question;
import com.example.baodi.zhihu.SomeClass.Topic;
import com.example.baodi.zhihu.activity.answerPage;
import com.example.baodi.zhihu.index.ContentItem;
import com.example.baodi.zhihu.index.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
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
    private ImageView back;
    private View view;
    private List<Answer> answer_list;
    private ListView contacts_list;
    private List<ContentItem> data;



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
        answer_list = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_collect, container, false);
        update_info();
        back = (ImageView) view.findViewById(R.id.my_collect_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
//                getFragmentManager().beginTransaction().hide(collect.this).add(R.id.content_layout,new MyFragment()).commit();
                Log.d("bye","bye");
            }
        });

        contacts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
//                Answer tmp = (Answer) answerList.get(i);
                ContentItem item = data.get(i);
                bundle.putInt("answerID",item.answer_ID);
//                bundle.putSerializable("answer_list", (Serializable) answerID_list);
                Intent intent = new Intent(getActivity(),answerPage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }


    private void update_info(){
        // 获取登陆后本地token
        data=new ArrayList<>();
        contacts_list=(ListView) view.findViewById(R.id.my_collect_list);

        final MyAdapter content_adapter=new MyAdapter(getActivity(),data);
        SharedPreferences sp = getActivity().getSharedPreferences("loginToken", 0);
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
                        Log.d("Response body", responseBodyString);
                        try {
//                            JSONObject job_tmp = new JSONObject(responseBodyString);
//                            Log.d("results",job_tmp.getString("results"));
                            JSONArray json = new JSONArray(responseBodyString);
                            if(json.length()>0){
                                for(int i=0;i<json.length();i++){
                                    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//                                    System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
//                                    Log.d("body", job.getString("body"));
                                    ContentItem tmp=new ContentItem(job.getString("answer_text"),job.getString("answer_vote")+" 人赞成","作者："+job.getString("author_name"),
                                            job.getInt("id"));
                                    Log.d("title",tmp.getTitle());
                                    Log.d("attend_num",tmp.getAttend_num());
                                    Log.d("answer_num",tmp.getAnswer_num());
                                    data.add(tmp);
                                    Log.d("data_size2",""+data.size());
                                }
                            }
                            Collections.shuffle(data);
                            contacts_list.setAdapter(content_adapter);
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
