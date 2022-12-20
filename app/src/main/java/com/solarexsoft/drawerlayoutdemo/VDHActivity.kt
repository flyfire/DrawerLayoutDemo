package com.solarexsoft.drawerlayoutdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.solarexsoft.drawerlayoutdemo.databinding.ActivityVdhBinding


/*
 * Creadted by houruhou on 2022/12/20 17:20
 */
class VDHActivity : AppCompatActivity() {
    lateinit var binding: ActivityVdhBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVdhBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}