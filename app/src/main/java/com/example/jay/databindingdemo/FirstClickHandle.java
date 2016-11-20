package com.example.jay.databindingdemo;

import android.view.View;
import android.widget.Toast;

/**
 * 第一种点击绑定：方法引用
 */
public class FirstClickHandle {

    /**
     * 注意：函数的签名必须和原来的 Listener 函数签名一致，但是方法名可以自定义
     * 例：OnClickListener 中的 void onClick(View v);
     *
     * @param view
     */
    public void onClickFirst(View view) {
        Toast.makeText(view.getContext(), "第一种点击绑定：方法引用", Toast.LENGTH_LONG).show();
    }
}
