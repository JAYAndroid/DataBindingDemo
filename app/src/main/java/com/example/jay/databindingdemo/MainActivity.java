package com.example.jay.databindingdemo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.jay.databindingdemo.databinding.CustomClassName;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 默认生成的Binding classes类命规则，布局名称（驼峰标识）+ Binding
 * 如：content_main.xml 生成的类名是 ContentMainBinding
 */
//import com.example.jay.databindingdemo.databinding.ContentMainBinding;

public class MainActivity extends AppCompatActivity {
    private CustomClassName viewDataBinding;
    private User mUser;
    private DataBindingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.content_main);

        initData();
    }

    private void initData() {
        mUser = new User("小明", "33", true);
        viewDataBinding.setUser(mUser);

        FirstClickHandle firstClickHandle = new FirstClickHandle();
        viewDataBinding.setFirstClickHandler(firstClickHandle);

        SecondClickHandle secondClickHandle = new SecondClickHandle();
        viewDataBinding.setSecondClickHnalder(secondClickHandle);

        InputHandler inputHandler = new InputHandler();
        viewDataBinding.setInputHandler(inputHandler);

        ItemClickHandler itemClickHandler = new ItemClickHandler();
        viewDataBinding.setItemHandler(itemClickHandler);

        /**
         *  可以直接通过viewDataBinding获取xml里，设置了ID的控件
         */
        viewDataBinding.tvNoFindBiewById.setText("不用执行 findViewById 了");

        viewDataBinding.rcList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < 5; i++) {
            User u = new User("Li si", 22 + i + "", true);
            userList.add(u);
        }
        mAdapter = new DataBindingAdapter(this, userList);
        viewDataBinding.rcList.setAdapter(mAdapter);
    }


    public class InputHandler {

        public void afterTextChangeCall(final CharSequence s, int start, int before, int count) {
            /**
             * 自动更新UI,不用关心线程切换问题
             */
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    mUser.setName(s.toString());
//                }
//            }).start();

            mUser.setName(s.toString());
        }
    }

    public class ItemClickHandler {

        public void addItem(View view) {
            Random r = new Random();
            mAdapter.addItem(new User("zhang san", r.nextInt(100) + "", false));
        }

        public void delItem(View view) {
            mAdapter.delItem();
        }
    }

}
