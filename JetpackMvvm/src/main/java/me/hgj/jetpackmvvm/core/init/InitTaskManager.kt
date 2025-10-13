package me.hgj.jetpackmvvm.core.init

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.hgj.jetpackmvvm.ext.util.logD

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：初始化任务管理器
 * 示例  ：
 * ```kotlin
 * InitTaskManager
 *     .register(NetTask())
 *     .register(UtilTask())
 *     .register(WidgetTask())
 *     .execute(this)
 * ```
 */
object InitTaskManager {

    private val tasks = mutableListOf<InitTask>()

    fun register(task: InitTask): InitTaskManager {
        tasks.add(task)
        return this
    }

    fun execute(app: Application) {
        // 主线程任务
        tasks.filter { it.runOnMainThread && it.isBlocking }
            .forEach {
                runBlocking {
                    runTask(app, it)
                }
            }

        // 异步任务（包括非阻塞的主线程任务 & 子线程任务）
        tasks.filter { !it.isBlocking || !it.runOnMainThread }
            .forEach { task ->
                CoroutineScope(Dispatchers.Default).launch {
                    runTask(app, task)
                }
            }
    }

    private suspend fun runTask(app: Application, task: InitTask) {
        val start = System.currentTimeMillis()
        try {
            task.init(app)
            val cost = System.currentTimeMillis() - start
            "✅ ${task.name} 初始化完成，耗时 ${cost}ms".logD("InitTaskManager")
        } catch (e: Exception) {
            "❌ ${task.name} 初始化失败".logD("InitTaskManager")
        }
    }
}