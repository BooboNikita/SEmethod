package com.example.baodi.zhihu.index;

/**
 * Created by lenovo on 2018/12/15.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.baodi.zhihu.R;
import com.example.baodi.zhihu.Request_Interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//public class Tab3Fragment extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.index_fragment_one, container, false);
//        final String[] title=new String[]{"为什么苍井空在国内有很高的人气？","如何评价诸葛亮挥泪斩马谡？","如何看待明年的约会大作战第三季？",
//                "为什么说火影忍者其实在传递绝望？","如何评价司马懿的一生？","如何评价青春猪头少年不会梦到兔女郎学姐？","从曹操、曹丕、曹叡三代的事迹中我们可以学到什么？","客观评价虐杀器官、尸者的帝国、和谐这三部作品",
//                "如何看待国内信托的发展现状，对投资者有何启示？","如何正确学习日语，尽快通过N1？"};
//        final String[] attend_num=new String[]{"87 人关注","154 人关注","59 人关注","68 人关注","298 人关注","365 人关注",
//                "541 人关注","215 人关注","98 人关注","158 人关注"};
//        final String[] answer_num=new String[]{"25 个回答","48 个回答","17 个回答","65 个回答","54 个回答","96 个回答",
//                "110 个回答","32 个回答","146 个回答","73 个回答"};
//
//        List<Integer> myArray = new ArrayList<Integer>(title.length);
//        for (int i = 0; i < title.length; i++)
//            myArray.add(i);
//        Collections.shuffle(myArray);
//
//        final List<ContentItem> data=new ArrayList<>();
//        final ListView contacts_list=(ListView) view.findViewById(R.id.list_view);
//        for(int i=0;i<10;i++){
//            ContentItem tmp=new ContentItem(title[myArray.get(i).intValue()],attend_num[myArray.get(i).intValue()],answer_num[myArray.get(i).intValue()]);
//            data.add(tmp);
//        }
//
//        final MyAdapter content_adapter=new MyAdapter(getActivity(),data);
//        contacts_list.setAdapter(content_adapter);
////        return inflater.inflate(R.layout.fragment_one, container, false);
//        return view;
//    }
//}

public class Tab3Fragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.index_fragment_one, container, false);
//        final String[] title=new String[]{"为什么苍井空在国内有很高的人气？","如何评价诸葛亮挥泪斩马谡？","如何看待明年的约会大作战第三季？",
//                "为什么说火影忍者其实在传递绝望？","如何评价司马懿的一生？","如何评价青春猪头少年不会梦到兔女郎学姐？","从曹操、曹丕、曹叡三代的事迹中我们可以学到什么？","客观评价虐杀器官、尸者的帝国、和谐这三部作品",
//                "如何看待国内信托的发展现状，对投资者有何启示？","如何正确学习日语，尽快通过N1？"};
//        final String[] attend_num=new String[]{"87 人关注","154 人关注","59 人关注","68 人关注","298 人关注","365 人关注",
//                "541 人关注","215 人关注","98 人关注","158 人关注"};
//        final String[] answer_num=new String[]{"25 个回答","48 个回答","17 个回答","65 个回答","54 个回答","96 个回答",
//                "110 个回答","32 个回答","146 个回答","73 个回答"};

        // 获取登陆后本地token
//        SharedPreferences sp = getSharedPreferences("loginToken", 0);
//        final String token = sp.getString("token", null);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Request_Interface.ENDPOINT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final Request_Interface request_interface = retrofit.create(Request_Interface.class);

        final List<ContentItem> data=new ArrayList<>();
        final ListView contacts_list=(ListView) view.findViewById(R.id.list_view);
        final MyAdapter content_adapter=new MyAdapter(getActivity(),data);


        try {
            // GET 方法调用
//            Call call = request_interface.getFlowQuestionList(token);
            Call call = request_interface.getAnswers();
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        String responseBodyString = response.body().toString();
                        Log.d("Response body", responseBodyString);
                        try {
                            JSONObject job_tmp = new JSONObject(responseBodyString);
                            Log.d("results",job_tmp.getString("results"));
                            JSONArray json = new JSONArray(job_tmp.getString("results"));
                            if(json.length()>0){
                                for(int i=0;i<json.length();i++){
                                    JSONObject job = json.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
//                                    System.out.println(job.get("name")+"=") ;  // 得到 每个对象中的属性值
//                                    Log.d("body", job.getString("body"));
//                                    ContentItem tmp=new ContentItem(job.getString("body"),job.getString("vote"),job.getString("author_name"));
                                    ContentItem tmp=new ContentItem(job.getString("body"),job.getString("vote")+" 人赞成","作者："+job.getString("author_name"));
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

        Log.d("data_size",""+data.size());
        Log.d("data_size2",""+data.size());

        return view;
    }
}