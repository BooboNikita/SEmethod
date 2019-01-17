package com.example.baodi.zhihu.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;
import com.example.baodi.zhihu.index.ContentItem;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Fragment collect=new collect();
    Fragment currentFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    private RelativeLayout my_collect_btn,my_follow_btn;
    private TextView my_collect_num,my_follow_num,my_id;
    private Handler handler;
    int collect_num,focus_num;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        view=inflater.inflate(R.layout.fragment_my, container, false);
        my_id = (TextView) view.findViewById(R.id.my_id);
        my_collect_num = (TextView) view.findViewById(R.id.my_collect_num);
        my_follow_num = (TextView) view.findViewById(R.id.my_follow_num);


        SharedPreferences sp = getActivity().getSharedPreferences("loginToken", 0);
        String username = sp.getString("username", null);
        my_id.setText(username);

        update_info();

        my_collect_btn = (RelativeLayout)view.findViewById(R.id.my_collect);
        my_collect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.content_layout, new collect()).commit();
//                getFragmentManager().beginTransaction().hide(MyFragment.this).add(R.id.content_layout,new collect()).commit();

            }
        });


        my_follow_btn = (RelativeLayout)view.findViewById(R.id.my_follow);
        my_follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)  //将当前fragment加入到返回栈中
                        .replace(R.id.content_layout, new focus()).commit();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(msg.what == 1){
                    my_collect_num.setText(String.valueOf(collect_num));
                }
                if(msg.what == 2){
                    my_follow_num.setText(String.valueOf(focus_num));
                }

                super.handleMessage(msg);
            }
        };


        return view;

    }

    private void update_info(){

        SharedPreferences sp = getActivity().getSharedPreferences("loginToken", 0);
        final String token = sp.getString("token", null);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        try {

            Call call = request_interface.getFavList(token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONArray json = new JSONArray(responseBodyString);
                            collect_num = json.length();
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

        try {

            Call call = request_interface.getFlowQuestionList(token);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONArray json = new JSONArray(responseBodyString);
                            focus_num = json.length();
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




//        switch (getId()){
//            case R.id.my_collect:
//                getFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)  //将当前fragment加入到返回栈中
//                        .replace(R.id.container, new collect()).commit();
//                break;
//        }

}

