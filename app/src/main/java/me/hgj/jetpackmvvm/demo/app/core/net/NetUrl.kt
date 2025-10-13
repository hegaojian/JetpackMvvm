package me.hgj.jetpackmvvm.demo.app.core.net

import rxhttp.wrapper.annotation.DefaultDomain

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 描述　：
 */
object NetUrl {

    /** 代表请求成功的 code值， 这里写0 是因为 玩Android 后端成功状态为0 */
    const val SUCCESS_CODE = 0

    /** 登录过期code值 */
    const val EXPIRED_CODE = -1001

    @DefaultDomain //设置为默认域名
    const val BASE_URL = "https://wanandroid.com/"

    /** 用户信息 */
    object User {
        /** 登录 */
        const val LOGIN = "user/login"
        /** 注册 */
        const val REGISTER = "user/register"
    }

    /** 文章 */
    object Article {
        /** 获取首页文章数据 */
        const val HOME_LIST = "article/list/%s/json"
        /** 首页banner */
        const val BANNER = "banner/json"
        /** 置顶文章*/
        const val TOP = "article/top/json"
    }

    /** 收藏 */
    object Collect {
        /** 收藏文章 */
        const val COLLECT_ARTICLE = "lg/collect/%s/json"
        /** 取消收藏文章 */
        const val UN_COLLECT_ARTICLE = "lg/uncollect_originId/%s/json"
        /** 取消收藏文章 在收藏页面中 */
        const val UN_COLLECT_ON_COLLECT = "lg/uncollect/%s/json"
        /** 收藏网址 */
        const val COLLECT_URL = "lg/collect/addtool/json"
        /** 取消收藏网址 */
        const val UN_COLLECT_URL = "lg/collect/deletetool/json"
        /** 收藏的文章数据列表 */
        const val COLLECT_ARTICLE_LIST = "lg/collect/list/%s/json"
        /** 收藏的网址数据列表 */
        const val COLLECT_URL_LIST = "lg/collect/usertools/json"
    }

    /** 积分 */
    object Integral {
        /** 获取用户积分 */
        const val USER_INTEGRAL = "lg/coin/userinfo/json"
        /** 积分排行 */
        const val INTEGRAL_RANK = "coin/rank/%s/json"
        /** 积分历史 */
        const val INTEGRAL_HISTORY = "lg/coin/list/%s/json"
    }

    /** 项目 */
    object Project {
        /** 项目标题 */
        const val PROJECT_TITLE = "project/tree/json"

        /** 获取项目数据 */
        const val PROJECT_DATA = "project/list/%s/json"

        /** 获取最新项目数据 */
        const val NEW_PROJECT_DATA = "article/listproject/%s/json"
    }

    /** 公众号 */
    object PublicNumber {

        /** 公众号分类标题 */
        const val PUBLIC_TITLE = "wxarticle/chapters/json"

        /** 获取公众号 */
        const val PUBLIC_DATA = "wxarticle/list/%s/%s/json"

    }

    /** 广场，体系，每日一问 导航 */
    object Tree {
        /** 广场列表数据 */
        const val PLAZA_DATA = "user_article/list/%s/json"
        /** 每日一问列表数据 */
        const val ASK_DATA = "wenda/list/%s/json"
        /** 获取体系数据 */
        const val TREE_DATA = "tree/json"
        /** 获取体系下的文章数据  */
        const val TREE_CHILD_DATA = "article/list/%s/json"
        /** 获取导航数据 */
        const val NAVIGATION = "navi/json"
    }

    object Search {
        /** 热门数据 */
        const val HOT_DATA = "hotkey/json"

        /** 搜索出的数据 */
        const val SEARCH_DATA = "article/query/%s/json"

        /** 查看他人的信息 */
        const val SHARE_USER_DATA = "user/%s/share_articles/%s/json"
    }

    object Share {
        /** 删除自己分享的文章 */
        const val DELETE_SHARE_ARTICLE= "lg/user_article/delete/%s/json"
        /** 查看自己分享的文章 */
        const val SELECT_SHARE_ARTICLE = "user/lg/private_articles/%s/json"
        /** 分享文章 */
        const val ADD_SHARE_ARTICLE = "lg/user_article/add/json"
    }


}