package me.hgj.jetpackmvvm.demo.app.core.widget.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.ui.activity.LoginActivity
import me.hgj.jetpackmvvm.ext.util.currentActivity
import me.hgj.jetpackmvvm.ext.util.intent.openActivity


import per.goweii.reveallayout.RevealLayout

/**
 * @author CuiZhen
 */
class CollectView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RevealLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private var onCollectViewClickListener: OnCollectViewClickListener? = null

    override fun initAttr(attrs: AttributeSet) {
        super.initAttr(attrs)
        setCheckWithExpand(true)
        setUncheckWithExpand(false)
        setCheckedLayoutId(R.layout.layout_collect_view_checked)
        setUncheckedLayoutId(R.layout.layout_collect_view_unchecked)
        setAnimDuration(300)
        setAllowRevert(true)
        setOnTouchListener(this)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP ->{
                //震动一下
                if(UserManager.isLoggedIn){
                    onCollectViewClickListener?.onClick(this)
                }else{
                    isChecked = true
                    (currentActivity as? AppCompatActivity)?.openActivity<LoginActivity>()
                }

            }
        }
        return false
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        this.onCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(v: CollectView)
    }
}