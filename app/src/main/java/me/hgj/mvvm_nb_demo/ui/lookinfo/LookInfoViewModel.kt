package me.hgj.mvvm_nb_demo.ui.lookinfo

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.databind.StringObservableField
import me.hgj.mvvm_nb_demo.app.CollectViewModel
import me.hgj.mvvm_nb_demo.app.util.ListDataUiState
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class LookInfoViewModel : CollectViewModel() {

    var name: StringObservableField = StringObservableField("--")

    var imageUrl: StringObservableField =
        StringObservableField("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var info: StringObservableField = StringObservableField()

    var shareListDataUistate: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

}