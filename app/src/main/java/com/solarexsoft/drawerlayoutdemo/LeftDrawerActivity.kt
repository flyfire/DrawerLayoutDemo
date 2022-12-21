package com.solarexsoft.drawerlayoutdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.solarexsoft.drawerlayoutdemo.databinding.ActivityLeftDrawerBinding


/*
 * Creadted by houruhou on 2022/12/21 14:33
 */
class LeftDrawerActivity : AppCompatActivity() {
    lateinit var binding: ActivityLeftDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeftDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvItem0.setOnClickListener {
            binding.leftDrawerLayout.closeDrawer()
            binding.tvContent.setText("item 0 clicked")
        }
        binding.tvItem1.setOnClickListener {
            binding.leftDrawerLayout.closeDrawer()
            binding.tvContent.setText("item 1 clicked")
        }
    }
}