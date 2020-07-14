package me.hgj.jetpackmvvm.demo.app.network

import me.hgj.jetpackmvvm.demo.data.model.bean.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
        const val SERVER_URL1 = "https://wanandroid.com/"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): ApiResponse<UserInfo>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String, @Field("password") pwd: String, @Field(
            "repassword"
        ) rpwd: String
    ): ApiResponse<Any>

    /**
     * 获取banner数据
     */
    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerResponse>>

    /**
     * 获取置顶文章集合数据
     */
    @GET("article/top/json")
    suspend fun getTopAritrilList(): ApiResponse<ArrayList<AriticleResponse>>

    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getAritrilList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 项目分类标题
     */
    @GET("project/tree/json")
    suspend fun getProjecTitle(): ApiResponse<ArrayList<ClassifyResponse>>

    /**
     * 根据分类id获取项目数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjecDataByType(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取最新项目数据
     */
    @GET("article/listproject/{page}/json")
    suspend fun getProjecNewData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    suspend fun getPublicTitle(): ApiResponse<ArrayList<ClassifyResponse>>

    /**
     * 获取公众号数据
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicData(
        @Path("page") pageNo: Int,
        @Path("id") id: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取热门搜索数据
     */
    @GET("hotkey/json")
    suspend fun getSearchData(): ApiResponse<ArrayList<SearchResponse>>

    /**
     * 根据关键词搜索数据
     */
    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 广场列表数据
     */
    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 每日一问列表数据
     */
    @GET("wenda/list/{page}/json")
    suspend fun getAskData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取体系数据
     */
    @GET("tree/json")
    suspend fun getSystemData(): ApiResponse<ArrayList<SystemResponse>>

    /**
     * 知识体系下的文章数据
     */
    @GET("article/list/{page}/json")
    suspend fun getSystemChildData(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationData(): ApiResponse<ArrayList<NavigationResponse>>

    /**
     * 收藏文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 取消收藏文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 收藏网址
     */
    @POST("lg/collect/addtool/json")
    suspend fun collectUrl(
        @Query("name") name: String,
        @Query("link") link: String
    ): ApiResponse<CollectUrlResponse>

    /**
     * 取消收藏网址
     */
    @POST("lg/collect/deletetool/json")
    suspend fun deletetool(@Query("id") id: Int): ApiResponse<Any?>

    /**
     * 获取收藏文章数据
     */
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<CollectResponse>>>

    /**
     * 获取收藏网址数据
     */
    @GET("lg/collect/usertools/json")
    suspend fun getCollectUrlData(): ApiResponse<ArrayList<CollectUrlResponse>>

    /**
     * 获取他人分享文章列表数据
     */
    @GET("user/{id}/share_articles/{page}/json")
    suspend fun getShareByidData(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): ApiResponse<ShareResponse>

    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getIntegral(): ApiResponse<IntegralResponse>

    /**
     * 获取积分排行榜
     */
    @GET("coin/rank/{page}/json")
    suspend fun getIntegralRank(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralResponse>>>

    /**
     * 获取积分历史
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getIntegralHistory(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralHistoryResponse>>>


    /**
     * 获取自己分享的文章列表数据
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun getShareData(@Path("page") page: Int): ApiResponse<ShareResponse>


    /**
     *  删除自己分享的文章
     */
    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteShareData(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 添加文章
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    suspend fun addAriticle(
        @Field("title") title: String,
        @Field("link") content: String
    ): ApiResponse<Any?>

    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun getTodoData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<TodoResponse>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): ApiResponse<Any?>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): ApiResponse<Any?>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): ApiResponse<Any?>


}