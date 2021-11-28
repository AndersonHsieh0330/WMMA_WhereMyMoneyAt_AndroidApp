package com.AndersonHsieh.wmma_wheremymoneyat.ui.edit_activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.AndersonHsieh.wmma_wheremymoneyat.databinding.ActivityEditBinding

class EditActivity: AppCompatActivity() {

    private lateinit var binding:ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }
    private fun initUI(){
        binding.editTransactionCloseBTN.setOnClickListener{
            finish()
        }
    }
}