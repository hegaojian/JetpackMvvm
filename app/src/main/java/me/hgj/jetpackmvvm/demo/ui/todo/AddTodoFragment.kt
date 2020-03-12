package me.hgj.jetpackmvvm.demo.ui.todo

import android.text.TextUtils
import androidx.navigation.Navigation
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_addtodo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.navigation.NavHostFragment
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.getActivityMessageViewModel
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.app.util.DatetimeUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.weight.customview.PriorityDialog
import me.hgj.jetpackmvvm.demo.data.bean.TodoResponse
import me.hgj.jetpackmvvm.demo.data.enums.TodoType
import me.hgj.jetpackmvvm.demo.databinding.FragmentAddtodoBinding
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat
import java.util.*

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class AddTodoFragment:BaseFragment<TodoViewModel,FragmentAddtodoBinding>() {

    var todoResponse:TodoResponse? = null

    override fun layoutId() = R.layout.fragment_addtodo

    override fun initView() {
        mDatabind.vm = mViewModel
        arguments?.let {
            todoResponse = it.getSerializable("todo") as TodoResponse
            todoResponse?.let {todo ->
                mViewModel.todoTitle.set(todo.title)
                mViewModel.todoContent.set(todo.content)
                mViewModel.todoTime.set(todo.dateStr)
                mViewModel.todoLeve.set(TodoType.byType(todo.priority).content)
                mViewModel.todoColor.set(TodoType.byType(todo.priority).color)
            }
        }
        toolbar.initClose("添加TODO") {
            Navigation.findNavController(it).navigateUp()
        }
        appViewModel.appColor.value?.let {SettingUtil.setShapColor(add_todo_submit, it)}

        add_todo_date.setOnClickListener {
            activity?.let {
                MaterialDialog(it)
                    .lifecycleOwner(this).show {
                    cornerRadius(0f)
                    datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                        mViewModel.todoTime.set(DatetimeUtil.formatDate(date.time, DatetimeUtil.DATE_PATTERN))
                    }
                }
            }
        }
        add_todo_proxlinear.setOnClickListener {
            activity?.let {
                PriorityDialog(it, TodoType.byValue(mViewModel.todoLeve.get()).type).apply {
                    setPriorityInterface(object : PriorityDialog.PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            mViewModel.todoLeve.set(type.content)
                            mViewModel.todoColor.set(type.color)
                        }
                    })
                }.show()
            }
        }
        add_todo_submit.clickNoRepeat {
            when {
                TextUtils.isEmpty(add_todo_title.text.toString()) -> {
                    showMessage("请填写标题")
                }
                TextUtils.isEmpty(add_todo_content.text.toString()) -> {
                    showMessage("请填写内容")
                }
                TextUtils.isEmpty(add_todo_date.text.toString()) -> {
                    showMessage("请选择预计完成时间")
                }
                else -> {
                    todoResponse.notNull({
                        showMessage("确认提交编辑吗？",positiveButtonText = "提交",positiveAction = {
                            mViewModel.updateTodo(todoResponse!!.id)
                        },negativeButtonText = "取消")
                    },{
                        showMessage("确认添加吗？",positiveButtonText = "添加",positiveAction = {
                            mViewModel.addTodo()
                        },negativeButtonText = "取消")
                    })
                }
            }
        }

    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        mViewModel.updateDataState.observe(viewLifecycleOwner, Observer {
            if(it.isSuccess){
                NavHostFragment.findNavController(this).navigateUp()
                getActivityMessageViewModel().addTodo.postValue(true)
            }else{
                showMessage(it.errorMsg)
            }
        })
    }

}