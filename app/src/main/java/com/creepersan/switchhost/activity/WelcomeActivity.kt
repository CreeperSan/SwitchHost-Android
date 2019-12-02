package com.creepersan.switchhost.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.creepersan.switchhost.R
import com.creepersan.switchhost.bean.WelcomeItem
import com.creepersan.switchhost.manager.SelfManager
import com.creepersan.switchhost.widget.adapter.WelcomePagerAdapter
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity() {
    private lateinit var mAdapter : WelcomePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        initAdapter()
        initViewPager()
        initIndicator()
    }

    private fun initAdapter(){
        mAdapter = WelcomePagerAdapter(listOf(
            WelcomeItem(
                img = R.mipmap.ic_launcher_round,
                hintString = "欢迎使用 SwitchHost ${SelfManager.getVersionName(this)}"
            ),
            WelcomeItem(
                img = R.mipmap.ic_launcher_round,
                hintString = "极速切换Host文件"
            ),
            WelcomeItem(
                img = R.mipmap.ic_launcher_round,
                hintString = "随时备份,随时恢复"
            ),
            WelcomeItem(
                img = R.mipmap.ic_launcher_round,
                hintString = "使用前,请明白应用会配合你的操作修改替换系统Host文件,如使用了不恰当的Host文件将可能导致设备变砖!\n\n继续使用则代表你已知晓并资源自行承担所有风险.\n\n由于Host文件为系统文件,因此如果要修改此文件,需要您授予root权限"
            ),
            WelcomeItem(
                img = R.mipmap.ic_launcher_round,
                hintString = "欢迎使用 SwitchHost ${SelfManager.getVersionName(this)}",
                buttonText = "进入应用",
                buttonClickAction = {
                    finish()
                }
            )
        ))
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