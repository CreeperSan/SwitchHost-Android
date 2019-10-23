package com.creepersan.switchhost.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.creepersan.switchhost.R
import com.creepersan.switchhost.widget.adapter.WelcomePagerAdapter
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {
    private val mAdapter = WelcomePagerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initViewPager()
        initIndicator()
    }

    private fun initViewPager(){
        welcomeViewPager.adapter = mAdapter
        welcomeViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                welcomePageIndicator.setProgress(position + 1 + positionOffset)
            }
        })
    }

    private fun initIndicator(){
        welcomePageIndicator.setCount(mAdapter.count)
        welcomePageIndicator.setProgress(welcomeViewPager.currentItem + 1f)
    }

}