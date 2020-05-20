package me.hgj.jetpackmvvm.demo.data.repository.request

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.data.model.bean.*
import me.hgj.jetpackmvvm.network.AppException

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/4
 * 描述　: 从网络中获取数据
 */
class HttpRequestManger {

    companion object {
        val instance: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpRequestManger()
        }
    }
    /**
     * 添加文章
     */
    suspend fun addAriticle(title: String, content: String): ApiResponse<Any?> {
        return NetworkApi.service.addAriticle(title, content)
    }

    /**
     * 获取自己分享的文章集合
     */
    suspend fun getShareAriticle(pageNo: Int): ApiResponse<ShareResponse> {
        return NetworkApi.service.getShareData(pageNo)
    }

    /**
     * 删除自己分享的文章集合
     */
    suspend fun delShareAriticle(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.deleteShareData(id)
    }

    /**
     * 收藏文章
     */
    suspend fun collect(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.collect(id)
    }

    /**
     * 收藏网址
     */
    suspend fun collectUrl(name: String, link: String): ApiResponse<CollectUrlResponse> {
        return NetworkApi.service.collectUrl(name, link)
    }

    /**
     * 取消收藏文章
     */
    suspend fun uncollect(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.uncollect(id)
    }

    /**
     * 取消收藏网址
     */
    suspend fun uncollectUrl(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.deletetool(id)
    }

    /**
     * 收藏的文章数据
     */
    suspend fun collectAriticleData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<CollectResponse>>> {
        return NetworkApi.service.getCollectData(pageNo)
    }

    /**
     * 收藏的网址数据
     */
    suspend fun collectUrlData(): ApiResponse<ArrayList<CollectUrlResponse>> {
        return NetworkApi.service.getCollectUrlData()
    }

    /**
     * 获取首页文章数据
     */
     suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        //同时异步请求2个接口，请求完成后合并数据
        return withContext(Dispatchers.IO) {
            val data = async { NetworkApi.service.getAritrilList(pageNo) }
            //如果App配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { getTopData() }
                data.await().data.datas.addAll(0, topData.await().data)
                data.await()
            } else {
                data.await()
            }
        }
    }

    /**
     * 获取置顶文章数据
     */
    private suspend fun getTopData(): ApiResponse<ArrayList<AriticleResponse>> {
        return NetworkApi.service.getTopAritrilList()
    }

    /**
     * 获取轮播图数据
     */
    suspend fun getBannData(): ApiResponse<ArrayList<BannerResponse>> {
        return NetworkApi.service.getBanner()
    }

    /**
     * 获取积分数据
     */
    suspend fun getIntegralData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralResponse>>> {
        return NetworkApi.service.getIntegralRank(pageNo)
    }

    /**
     * 积分获取记录
     */
    suspend fun getIntegralHistoryData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralHistoryResponse>>> {
        return NetworkApi.service.getIntegralHistory(pageNo)
    }

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): ApiResponse<UserInfo> {
        return NetworkApi.service.login(username, password)
    }

    /**
     * 注册并登陆
     */
    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        val registerData = NetworkApi.service.register(username, password, password)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.isSucces()) {
            return login(username, password)
        } else {
            //抛出错误异常
            throw AppException(registerData.errorCode, registerData.errorMsg)
        }
    }

    /**
     * 获取个人信息积分
     */
    suspend fun getIntegral(): ApiResponse<IntegralResponse> {
        return NetworkApi.service.getIntegral()
    }

    /**
     * 获取公众号标题数据
     */
    suspend fun getTitleData(): ApiResponse<ArrayList<ClassifyResponse>> {
        return NetworkApi.service.getPublicTitle()
    }

    /**
     * 根据公众号标题获取数据
     */
    suspend fun getPublicData(
        pageNo: Int,
        cid: Int = 0
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getPublicData(pageNo, cid)
    }

    /**
     * 获取热门搜索数据
     */
    suspend fun getHotData(): ApiResponse<ArrayList<SearchResponse>> {
        return NetworkApi.service.getSearchData()
    }

    /**
     * 获取搜索数据
     */
    suspend fun getSearchResultData(
        pageNo: Int,
        searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getSearchDataByKey(pageNo, searchKey)
    }

    /**
     * 获取项目标题数据
     */
    suspend fun getProjecTitle(): ApiResponse<ArrayList<ClassifyResponse>> {
        return NetworkApi.service.getProjecTitle()
    }

    /**
     * 获取项目标题数据
     */
    suspend fun getProjectData(
        pageNo: Int,
        cid: Int = 0,
        isNew: Boolean = false
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return if (isNew) {
            NetworkApi.service.getProjecNewData(pageNo)
        } else {
            NetworkApi.service.getProjecDataByType(pageNo, cid)
        }
    }

    /**
     * 获取某某的个人信息
     */
    suspend fun getLookinfoById(id: Int, pageNo: Int): ApiResponse<ShareResponse> {
        return NetworkApi.service.getShareByidData(pageNo, id)
    }

    /**
     * 获取TODo列表数据
     */
    suspend fun getTodoListData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<TodoResponse>>> {
        return NetworkApi.service.getTodoData(pageNo)
    }

    /**
     * 删除TODo数据
     */
    suspend fun delTodotData(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.deleteTodo(id)
    }

    /**
     * 完成TODo
     */
    suspend fun doneTodotData(id: Int): ApiResponse<Any?> {
        //1完成
        return NetworkApi.service.doneTodo(id, 1)
    }

    /**
     * 添加TODo
     */
    suspend fun addTodotData(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): ApiResponse<Any?> {
        return NetworkApi.service.addTodo(title, content, date, type, priority)
    }

    /**
     * 修复TODo
     */
    suspend fun updateTodotData(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int,
        id: Int
    ): ApiResponse<Any?> {
        return NetworkApi.service.updateTodo(title, content, date, type, priority, id)
    }

    /**
     * 获取广场数据
     */
    suspend fun getPlazaData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getSquareData(pageNo)
    }

    /**
     * 获取每日一问数据
     */
    suspend fun getAskData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getAskData(pageNo)
    }

    /**
     * 获取体系数据
     */
    suspend fun getSystemData(): ApiResponse<ArrayList<SystemResponse>> {
        return NetworkApi.service.getSystemData()
    }

    /**
     * 获取导航数据
     */
    suspend fun getNavigationData(): ApiResponse<ArrayList<NavigationResponse>> {
        return NetworkApi.service.getNavigationData()
    }

    /**
     * 获取体系子数据
     */
    suspend fun getSystemChildData(
        pageNo: Int,
        cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getSystemChildData(pageNo, cid)
    }

    suspend fun download(){

    }
}