package me.hgj.jetpackmvvm

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.ProcessLifecycleOwner
import me.hgj.jetpackmvvm.ext.lifecycle.KtxAppLifeObserver
import me.hgj.jetpackmvvm.ext.lifecycle.KtxLifeCycleCallBack

/**
 * Created by luyao
 * on 2019/8/6 14:22
 */
class Ktx : ContentProvider() {

    companion object {
        lateinit var app: Application
        var watchActivityLife = true
        var watchAppLife = true
        var background = false
    }

    override fun onCreate(): Boolean {
        val application = context!!.applicationContext as Application
        install(application)
        return true
    }

    private fun install(application: Application) {
        app = application
        if (watchActivityLife) application.registerActivityLifecycleCallbacks(KtxLifeCycleCallBack())
        if (watchAppLife) ProcessLifecycleOwner.get().lifecycle.addObserver(KtxAppLifeObserver)
    }
    fun onLifeChange(action:(Boolean)->Unit){
        action.invoke(background)
    }
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? = null


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}