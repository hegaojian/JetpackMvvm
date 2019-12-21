package me.hgj.mvvm_nb_demo

import me.hgj.mvvm_nb_demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main


    override fun initView() {

    }

    override fun createObserver() {

    }


}
