package com.example.jay.databindingdemo;

import android.view.View;
import android.widget.Toast;

/**
 * 第二种点击绑定：绑定监听器
 */
public class SecondClickHandle {

    /**
     * 函数的签名不需要和来的 Listener 函数签名一致，但是返回值必须一样，除非返回值是void
     *
     * @param view
     * @param user
     */
    public void onClickSecond(View view, User user) {
        Toast.makeText(view.getContext(), "xml传递来的user =" + user.getName(), Toast.LENGTH_LONG).show();
    }
}
