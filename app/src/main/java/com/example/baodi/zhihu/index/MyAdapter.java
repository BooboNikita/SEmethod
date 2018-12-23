package com.example.baodi.zhihu.index;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.baodi.zhihu.R;

import java.util.List;

/**
 * Created by lenovo on 2018/12/15.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<ContentItem> list;
    private class ViewHolder{
        public TextView title;
        public TextView attend_num;
        public TextView answer_num;
    }
    public MyAdapter(Context context,List<ContentItem> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount(){
        if(list==null)
            return 0;
        return list.size();
    }
    @Override
    public Object getItem(int i){
        if(list==null)
            return null;
        return list.get(i);
    }
    @Override
    public long getItemId(int i){
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        View convertView;
        ViewHolder viewHolder;
        if(view==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.content_item,null);
            viewHolder=new ViewHolder();
            viewHolder.title=(TextView)convertView.findViewById(R.id.txt_attention_question_title);
            viewHolder.attend_num=(TextView)convertView.findViewById(R.id.attend_num);
            viewHolder.answer_num=(TextView)convertView.findViewById(R.id.answer_num);
            convertView.setTag(viewHolder);
        }else{
            convertView=view;
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.attend_num.setText(list.get(i).getAttend_num());
        viewHolder.answer_num.setText(list.get(i).getAnswer_num());
        return convertView;
    }
}
