package com.github.tanxiaofan.myapt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.tanxiaofan.annotation.BindView;
import com.github.tanxiaofan.api.MyBindView;

/**
 * @Description: 在Java中使用
 * @Author: fan.tan
 * @CreateDate: 2019/11/25 16:16
 */
public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.tv_hello)
    TextView tvHello;

    @BindView(R.id.btn)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyBindView.bind(this);

        tvHello.setText("Hello 2");
        button.setText("btn 2");
    }
}
