package me.hgj.jetpackmvvm.ext.lifecycle

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import me.hgj.jetpackmvvm.base.ui.BaseVmActivity
import me.hgj.jetpackmvvm.base.ui.BaseVmFragment
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import kotlin.also

/**
 * 作者　：hegaojian
 * 时间　：2025/9/17
 * 说明　：
 */

@MainThread
inline fun <reified T : BaseViewModel> BaseVmActivity<*>.getViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<T> {
    val factoryPromise = factoryProducer ?: { defaultViewModelProviderFactory }
    return ViewModelLazy(
        T::class,
        { viewModelStore },
        factoryPromise,
        { extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras }
    ).also { lazyViewModel ->
        // 添加生命周期观察者，在 CREATED 状态时执行
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                addLoadingUiChange(lazyViewModel.value)
                lifecycle.removeObserver(this)
            }
        })
    }
}

@MainThread
inline fun <reified T : BaseViewModel> BaseVmFragment<*>.getViewModel(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<T> {
    val owner by lazy(LazyThreadSafetyMode.NONE) { ownerProducer() }
    val lazyViewModel = createViewModelLazy(
        T::class,
        { owner.viewModelStore },
        {
            extrasProducer?.invoke()
                ?: (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelCreationExtras
                ?: CreationExtras.Empty
        },
        factoryProducer ?: {
            (owner as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                ?: defaultViewModelProviderFactory
        }
    )
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            addLoadingUiChange(lazyViewModel.value)
            lifecycle.removeObserver(this)
        }
    })
    return lazyViewModel
}

@MainThread
inline fun <reified T : ViewModel> Fragment.getActivityViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> Factory)? = null
): Lazy<T> = createViewModelLazy(
    T::class, { requireActivity().viewModelStore },
    { extrasProducer?.invoke() ?: requireActivity().defaultViewModelCreationExtras },
    factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)
