package com.example.baodi.zhihu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baodi.zhihu.fragment.IndexFragment;
import com.example.baodi.zhihu.fragment.MyFragment;
import com.example.baodi.zhihu.fragment.NotiFragment;
import com.example.baodi.zhihu.fragment.ThinkFragment;
import com.example.baodi.zhihu.fragment.UniFragment;

public class indexPage extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout indexLayout,thinkLayout,universityLayout,notiLayout,myLayout;
    private Fragment indexFragment,thinkFragment,universityFragment,notiFragment,myFragment,currentFragment;
    private ImageView indexImg,thinkImg,universityImg,notiImg,myImg;
    private TextView indexText,thinkText,universityText,notiText,myText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_page_activity);

        initUI();
        initTab();
    }

    private void initUI(){
        indexLayout = (RelativeLayout) findViewById(R.id.rl_index);
        thinkLayout = (RelativeLayout) findViewById(R.id.rl_think);
        universityLayout = (RelativeLayout) findViewById(R.id.rl_university);
        notiLayout = (RelativeLayout) findViewById(R.id.rl_noti);
        myLayout = (RelativeLayout) findViewById(R.id.rl_my);

        indexLayout.setOnClickListener(this);
        thinkLayout.setOnClickListener(this);
        universityLayout.setOnClickListener(this);
        notiLayout.setOnClickListener(this);
        myLayout.setOnClickListener(this);

        indexImg = (ImageView) findViewById(R.id.index_button);
        thinkImg = (ImageView) findViewById(R.id.think_button);
        universityImg = (ImageView) findViewById(R.id.university_button);
        notiImg = (ImageView) findViewById(R.id.noti_button);
        myImg = (ImageView) findViewById(R.id.my_button);

        indexText = (TextView) findViewById(R.id.index_text);
        thinkText = (TextView) findViewById(R.id.think_text);
        universityText = (TextView) findViewById(R.id.university_text);
        notiText = (TextView) findViewById(R.id.noti_text);
        myText = (TextView) findViewById(R.id.my_text);
    }

    private void initTab(){
        if(indexFragment == null){
            indexFragment = new IndexFragment();
        }
        if(!indexFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().add(R.id.content_layout,indexFragment).commit();

            currentFragment = indexFragment;

            indexImg.setImageResource(R.drawable.index_page_highlight);
            indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
            thinkImg.setImageResource(R.drawable.think);
            thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
            universityImg.setImageResource(R.drawable.university);
            universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
            notiImg.setImageResource(R.drawable.notification);
            notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
            myImg.setImageResource(R.drawable.my);
            myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_index:
                clickIndex();
                break;
            case R.id.rl_think:
                clickThink();
                break;
            case R.id.rl_university:
                clickUni();
                break;
            case R.id.rl_noti:
                clickNoti();
                break;
            case R.id.rl_my:
                clickMy();
                break;
            default:
                break;
        }
    }

    private void clickIndex(){
        if(indexFragment == null){
            indexFragment = new IndexFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(),indexFragment);
        indexImg.setImageResource(R.drawable.index_page_highlight);
        indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        thinkImg.setImageResource(R.drawable.think);
        thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        universityImg.setImageResource(R.drawable.university);
        universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        notiImg.setImageResource(R.drawable.notification);
        notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        myImg.setImageResource(R.drawable.my);
        myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
    }

    private void clickThink(){
        if(thinkFragment == null){
            thinkFragment = new ThinkFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(),thinkFragment);
        indexImg.setImageResource(R.drawable.index_page);
        indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        thinkImg.setImageResource(R.drawable.think_highlight);
        thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        universityImg.setImageResource(R.drawable.university);
        universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        notiImg.setImageResource(R.drawable.notification);
        notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        myImg.setImageResource(R.drawable.my);
        myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
    }

    private void clickUni(){
        if(universityFragment == null){
            universityFragment = new UniFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(),universityFragment);
        indexImg.setImageResource(R.drawable.index_page);
        indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        thinkImg.setImageResource(R.drawable.think);
        thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        universityImg.setImageResource(R.drawable.university_highlight);
        universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        notiImg.setImageResource(R.drawable.notification);
        notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        myImg.setImageResource(R.drawable.my);
        myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
    }

    private void clickNoti(){
        if(notiFragment == null){
            notiFragment = new NotiFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(),notiFragment);
        indexImg.setImageResource(R.drawable.index_page);
        indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        thinkImg.setImageResource(R.drawable.think);
        thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        universityImg.setImageResource(R.drawable.university);
        universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        notiImg.setImageResource(R.drawable.notification_highlight);
        notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
        myImg.setImageResource(R.drawable.my);
        myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
    }

    private void clickMy(){
        if(myFragment == null){
            myFragment = new MyFragment();
        }

        addOrShowFragment(getSupportFragmentManager().beginTransaction(),myFragment);
        indexImg.setImageResource(R.drawable.index_page);
        indexText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        thinkImg.setImageResource(R.drawable.think);
        thinkText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        universityImg.setImageResource(R.drawable.university);
        universityText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        notiImg.setImageResource(R.drawable.notification);
        notiText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.grey1));
        myImg.setImageResource(R.drawable.my_highlight);
        myText.setTextColor(ContextCompat.getColor(getBaseContext(),R.color.colorPrimary));
    }

    private void addOrShowFragment(FragmentTransaction transaction,
                                   Fragment fragment) {
        if (currentFragment == fragment)
            return;

        if (!fragment.isAdded()) { // 假设当前fragment未被加入，则加入到Fragment管理器中
            transaction.hide(currentFragment)
                    .add(R.id.content_layout, fragment).commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }

        currentFragment = fragment;
    }
}
