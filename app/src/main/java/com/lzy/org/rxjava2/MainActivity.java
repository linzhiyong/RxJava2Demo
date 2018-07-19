package com.lzy.org.rxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * RxJava2Test
 *
 * @author linzhiyong
 * @email wflinzhiyong@163.com
 * @blog https://blog.csdn.net/u012527802
 *       https://github.com/linzhiyong
 *       https://www.jianshu.com/u/e664ba5d0800
 * @time 2018/7/16
 * @desc
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick(View view) {
//        RxJava2Test.test();
        new RxJava2Test2().test();
    }

}
