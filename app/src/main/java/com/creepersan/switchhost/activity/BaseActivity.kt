package com.creepersan.switchhost.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.creepersan.switchhost.application.SwitchHostApplication

abstract class BaseActivity : AppCompatActivity() {
    protected val application get() = getApplication() as SwitchHostApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun <T:BaseActivity> toActivity(clazz:Class<T>, isFinish:Boolean = false){
        startActivity(Intent(this, clazz))
        if (isFinish){
            finish()
        }
    }
}