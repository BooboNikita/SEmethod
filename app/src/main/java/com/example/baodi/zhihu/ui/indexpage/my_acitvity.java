package com.example.baodi.zhihu.ui.indexpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.baodi.zhihu.R;

public class my_acitvity extends AppCompatActivity implements View.OnClickListener
{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my);
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_collect:

                break;
        }

    }
}