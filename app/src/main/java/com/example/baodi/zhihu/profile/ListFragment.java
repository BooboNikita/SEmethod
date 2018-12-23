package com.example.baodi.zhihu.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.baodi.zhihu.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lancelot on 2018/12/21.
 */

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextAdapter adapter = new TextAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public class TextAdapter extends RecyclerView.Adapter<TextViewHolder> {

        final String[] title=new String[]{"为什么苍井空在国内有很高的人气？","如何评价诸葛亮挥泪斩马谡？","如何看待明年的约会大作战第三季？",
                "为什么说火影忍者其实在传递绝望？","如何评价司马懿的一生？","如何评价青春猪头少年不会梦到兔女郎学姐？","从曹操、曹丕、曹叡三代的事迹中我们可以学到什么？","客观评价虐杀器官、尸者的帝国、和谐这三部作品",
                "如何看待国内信托的发展现状，对投资者有何启示？","如何正确学习日语，尽快通过N1？"};
        List<Integer> myArray = new ArrayList<Integer>(title.length);

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            List<Integer> myArray = new ArrayList<Integer>(title.length);
            for (int i = 0; i < title.length; i++)
                myArray.add(i);
            Collections.shuffle(myArray);

            View view = LayoutInflater.from(getContext()).inflate
                    (R.layout.item_recyclerview, parent, false);
            return new TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TextViewHolder holder, int position) {
//            holder.mTextView.setText(position + "");
            holder.mTextView.setText(title[myArray.get(position).intValue()]);
        }

        @Override
        public int getItemCount() {
            return 6;
        }
    }

    ;

    public class TextViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TextViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.text_recycler_view);
        }
    }

}
