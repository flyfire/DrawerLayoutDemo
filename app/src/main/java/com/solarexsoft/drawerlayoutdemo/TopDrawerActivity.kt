package com.solarexsoft.drawerlayoutdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.solarexsoft.drawerlayoutdemo.databinding.ActivityTopDrawerBinding


/*
 * Creadted by houruhou on 2022/12/21 15:47
 */
class TopDrawerActivity : AppCompatActivity() {
    lateinit var binding: ActivityTopDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvItem0.setOnClickListener {
            binding.topDrawerLayout.closeDrawer()
            binding.tvContent.setText("item 0 clicked")
        }
        binding.tvItem1.setOnClickListener {
            binding.topDrawerLayout.closeDrawer()
            binding.tvContent.setText("item 1 clicked")
        }
    }
}