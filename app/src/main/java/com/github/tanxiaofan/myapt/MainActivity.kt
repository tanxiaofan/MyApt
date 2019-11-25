package com.github.tanxiaofan.myapt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.tanxiaofan.annotation.BindView
import com.github.tanxiaofan.api.MyBindView

/**
 * 在kotlin中使用时，添加JvmField注解，避免kotlin中字段的默认行为
 */
class MainActivity : AppCompatActivity() {

    @JvmField
    @BindView(R.id.tv_hello)
    var tvHello: TextView? = null


    @JvmField
    @BindView(R.id.btn)
    var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MyBindView.bind(this)

        tvHello?.text = "Hello BindView"
        button?.text = "main2"
        button?.setOnClickListener {
            startActivity(Intent(this, Main2Activity::class.java))
        }
    }
}
