package me.hgj.mvvm_nb_demo.app.weight.loadCallBack


import com.kingja.loadsir.callback.Callback

import me.hgj.mvvm_nb_demo.R


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}
