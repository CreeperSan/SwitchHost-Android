package com.creepersan.switchhost.widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.creepersan.switchhost.R
import com.creepersan.switchhost.bean.WelcomeItem
import com.creepersan.switchhost.extension.gone
import com.creepersan.switchhost.extension.visible

class WelcomePagerAdapter(val itemList : List<WelcomeItem>) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.page_welcome_normal, container, false)
        val iconImageView = view.findViewById<ImageView>(R.id.pageWelcomeNormalImage)
        val hintTextView = view.findViewById<TextView>(R.id.pageWelcomeNormalHint)
        val actionButton = view.findViewById<Button>(R.id.pageWelcomeNormalButton)

        val item = itemList[position]

        if (item.img == 0){
            iconImageView.gone()
        }else{
            iconImageView.visible()
            iconImageView.setImageResource(item.img)
        }
        hintTextView.text = item.hintString
        hintTextView.setTextColor(item.hintStringColor)
        hintTextView.textSize = item.hintStringSizeSP
        if (item.buttonText.isNotEmpty() && item.buttonClickAction != null){
            actionButton.visible()
            actionButton.text = item.buttonText
            actionButton.setOnClickListener {
                item.buttonClickAction?.invoke()
            }
        }else{
            actionButton.gone()
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}