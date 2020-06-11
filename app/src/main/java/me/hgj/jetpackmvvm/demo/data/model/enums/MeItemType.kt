package me.hgj.jetpackmvvm.demo.data.model.enums

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　: 个人中心类型
 */
enum class MeItemType(val type: Int) {
    //头部布局
    TopItem(1),
    //圆角Item
    RoundItem(2),
    //分割线
    BackGroundItem(3),
    //普通的列表
    ListItem(4),
    //右边有数据的列表
    ListRightItem(4)
}