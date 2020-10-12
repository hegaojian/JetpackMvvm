package me.hgj.jetpackmvvm.demo.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.IntObservableField
import me.hgj.jetpackmvvm.callback.databind.StringObservableField
import me.hgj.jetpackmvvm.demo.data.model.enums.TodoType
import me.hgj.jetpackmvvm.ext.launch

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class TodoViewModel : BaseViewModel() {

    //标题
    var todoTitle = StringObservableField()

    //内容
    var todoContent =
        StringObservableField()

    //时间
    var todoTime = StringObservableField()

    //优先级
    var todoLeve =
        StringObservableField(TodoType.TodoType1.content)

    //优先级颜色
    var todoColor =
        IntObservableField(TodoType.TodoType1.color)

    fun xx(): Unit {
        launch({

        },{

        })
    }
}