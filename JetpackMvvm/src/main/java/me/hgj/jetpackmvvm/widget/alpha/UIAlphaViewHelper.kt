package me.hgj.jetpackmvvm.widget.alpha

import android.view.View
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.widget.alpha.UIResHelper.getAttrFloatValue
import java.lang.ref.WeakReference


class UIAlphaViewHelper(
    target: View,
    private var mPressedAlpha: Float = 0.5F,
    private var mDisabledAlpha: Float = 0.5F
) {
    private var mTarget: WeakReference<View>? = null

    /**
     * 设置是否要在 press 时改变透明度
     */
    private var mChangeAlphaWhenPress = true

    /**
     * 设置是否要在 disabled 时改变透明度
     */
    private var mChangeAlphaWhenDisable = true

    private val mNormalAlpha = 1f

    init {
        mTarget = WeakReference(target)
        val pressedAlpha = getAttrFloatValue(target.context, R.attr.ui_alpha_pressed)
        if (pressedAlpha != 0F) {
            mPressedAlpha = pressedAlpha
        }
        val disabledAlpha = getAttrFloatValue(target.context, R.attr.ui_alpha_disabled)
        if (disabledAlpha != 0F) {
            mDisabledAlpha = disabledAlpha
        }
    }

    /**
     * 在 [View.setPressed] 中调用，通知 helper 更新
     *
     * @param current the view to be handled, maybe not equal to target view
     * @param pressed [View.setPressed] 中接收到的参数
     */
    fun onPressedChanged(current: View, pressed: Boolean) {
        val target = mTarget!!.get() ?: return
        if (current.isEnabled) {
            target.alpha =
                if (mChangeAlphaWhenPress && pressed && current.isClickable) mPressedAlpha else mNormalAlpha
        } else {
            if (mChangeAlphaWhenDisable) {
                target.alpha = mDisabledAlpha
            }
        }
    }

    /**
     * 在 [View.setEnabled] 中调用，通知 helper 更新
     *
     * @param current the view to be handled, maybe not  equal to target view
     * @param enabled [View.setEnabled] 中接收到的参数
     */
    fun onEnabledChanged(current: View, enabled: Boolean) {
        val target = mTarget!!.get() ?: return
        val alphaForIsEnable: Float = if (mChangeAlphaWhenDisable) {
            if (enabled) mNormalAlpha else mDisabledAlpha
        } else {
            mNormalAlpha
        }
        if (current !== target && target.isEnabled != enabled) {
            target.isEnabled = enabled
        }
        target.alpha = alphaForIsEnable
    }

    /**
     * 设置是否要在 press 时改变透明度
     *
     * @param changeAlphaWhenPress 是否要在 press 时改变透明度
     */
    fun setChangeAlphaWhenPress(changeAlphaWhenPress: Boolean) {
        mChangeAlphaWhenPress = changeAlphaWhenPress
    }

    /**
     * 设置是否要在 disabled 时改变透明度
     *
     * @param changeAlphaWhenDisable 是否要在 disabled 时改变透明度
     */
    fun setChangeAlphaWhenDisable(changeAlphaWhenDisable: Boolean) {
        mChangeAlphaWhenDisable = changeAlphaWhenDisable
        val target = mTarget!!.get()
        if (target != null) {
            onEnabledChanged(target, target.isEnabled)
        }
    }

}