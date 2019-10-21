package com.creepersan.switchhost.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.creepersan.switchhost.application.SwitchHostApplication
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    protected val application get() = getApplication() as SwitchHostApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun <T:BaseActivity> toActivity(clazz:Class<T>, isFinish:Boolean = false, requestCode:Int?=null){
        val intent = Intent(this, clazz)
        if (requestCode != null){
            startActivityForResult(intent, requestCode)
        }else{
            startActivity(intent)
        }
        if (isFinish){
            finish()
        }
    }

    private var mSnackBar : Snackbar? = null

    protected fun showSnack(view:CoordinatorLayout, resID:Int, length:Int=Snackbar.LENGTH_SHORT){
        showSnack(view, getString(resID), length)
    }

    protected fun showSnack(view:CoordinatorLayout, text:String, length:Int=Snackbar.LENGTH_SHORT){
        mSnackBar = Snackbar.make(view, text, length)
        mSnackBar?.show()
    }

    protected fun hideSnack(){
        mSnackBar?.dismiss()
    }

    protected fun toast(content:String){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

}