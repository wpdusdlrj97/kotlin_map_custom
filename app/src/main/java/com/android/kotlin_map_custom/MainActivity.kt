package com.android.kotlin_map_custom

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)


        button0.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, ClusteringDemoTestActivity::class.java)
            startActivity(intent)
        })

    }
}
