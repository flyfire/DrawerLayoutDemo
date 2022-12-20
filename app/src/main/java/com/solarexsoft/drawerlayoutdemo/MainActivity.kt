package com.solarexsoft.drawerlayoutdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.solarexsoft.drawerlayoutdemo.databinding.ActivityMainBinding


/*
 * Creadted by houruhou on 2022/12/20 17:35
 */
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnVdh.setOnClickListener {
            startActivity(Intent(this, VDHActivity::class.java))
        }
    }
}