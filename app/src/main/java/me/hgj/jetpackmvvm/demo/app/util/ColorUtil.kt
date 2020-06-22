package me.hgj.jetpackmvvm.demo.app.util

import android.graphics.Color
import java.util.*

object ColorUtil {
    //自定义颜色，过滤掉与白色相近的颜色
    var ACCENT_COLORS = intArrayOf(
            Color.parseColor("#EF5350"),
            Color.parseColor("#EC407A"),
            Color.parseColor("#AB47BC"),
            Color.parseColor("#7E57C2"),
            Color.parseColor("#5C6BC0"),
            Color.parseColor("#42A5F5"),
            Color.parseColor("#29B6F6"),
            Color.parseColor("#26C6DA"),
            Color.parseColor("#26A69A"),
            Color.parseColor("#66BB6A"),
            Color.parseColor("#9CCC65"),
            Color.parseColor("#D4E157"),
            Color.parseColor("#FFEE58"),
            Color.parseColor("#FFCA28"),
            Color.parseColor("#FFA726"),
            Color.parseColor("#FF7043"),
            Color.parseColor("#8D6E63"),
            Color.parseColor("#BDBDBD"),
            Color.parseColor("#78909C"))

    val PRIMARY_COLORS_SUB = arrayOf(
            intArrayOf(Color.parseColor("#EF5350"), Color.parseColor("#F44336"), Color.parseColor("#E53935"), Color.parseColor("#D32F2F"), Color.parseColor("#C62828"), Color.parseColor("#B71C1C")),
            intArrayOf(Color.parseColor("#EC407A"), Color.parseColor("#E91E63"), Color.parseColor("#D81B60"), Color.parseColor("#C2185B"), Color.parseColor("#AD1457"), Color.parseColor("#880E4F")),
            intArrayOf(Color.parseColor("#AB47BC"), Color.parseColor("#9C27B0"), Color.parseColor("#8E24AA"), Color.parseColor("#7B1FA2"), Color.parseColor("#6A1B9A"), Color.parseColor("#4A148C")),
            intArrayOf(Color.parseColor("#7E57C2"), Color.parseColor("#673AB7"), Color.parseColor("#5E35B1"), Color.parseColor("#512DA8"), Color.parseColor("#4527A0"), Color.parseColor("#311B92")),
            intArrayOf(Color.parseColor("#5C6BC0"), Color.parseColor("#3F51B5"), Color.parseColor("#3949AB"), Color.parseColor("#303F9F"), Color.parseColor("#283593"), Color.parseColor("#1A237E")),
            intArrayOf(Color.parseColor("#42A5F5"), Color.parseColor("#2196F3"), Color.parseColor("#1E88E5"), Color.parseColor("#1976D2"), Color.parseColor("#1565C0"), Color.parseColor("#0D47A1")),
            intArrayOf(Color.parseColor("#29B6F6"), Color.parseColor("#03A9F4"), Color.parseColor("#039BE5"), Color.parseColor("#0288D1"), Color.parseColor("#0277BD"), Color.parseColor("#01579B")),
            intArrayOf(Color.parseColor("#26C6DA"), Color.parseColor("#00BCD4"), Color.parseColor("#00ACC1"), Color.parseColor("#0097A7"), Color.parseColor("#00838F"), Color.parseColor("#006064")),
            intArrayOf(Color.parseColor("#26A69A"), Color.parseColor("#009688"), Color.parseColor("#00897B"), Color.parseColor("#00796B"), Color.parseColor("#00695C"), Color.parseColor("#004D40")),
            intArrayOf(Color.parseColor("#66BB6A"), Color.parseColor("#4CAF50"), Color.parseColor("#43A047"), Color.parseColor("#388E3C"), Color.parseColor("#2E7D32"), Color.parseColor("#1B5E20")),
            intArrayOf(Color.parseColor("#9CCC65"), Color.parseColor("#8BC34A"), Color.parseColor("#7CB342"), Color.parseColor("#689F38"), Color.parseColor("#558B2F"), Color.parseColor("#33691E")),
            intArrayOf(Color.parseColor("#D4E157"), Color.parseColor("#CDDC39"), Color.parseColor("#C0CA33"), Color.parseColor("#AFB42B"), Color.parseColor("#9E9D24"), Color.parseColor("#827717")),
            intArrayOf(Color.parseColor("#FFEE58"), Color.parseColor("#FFEB3B"), Color.parseColor("#FDD835"), Color.parseColor("#FBC02D"), Color.parseColor("#F9A825"), Color.parseColor("#F57F17")),
            intArrayOf(Color.parseColor("#FFCA28"), Color.parseColor("#FFC107"), Color.parseColor("#FFB300"), Color.parseColor("#FFA000"), Color.parseColor("#FF8F00"), Color.parseColor("#FF6F00")),
            intArrayOf(Color.parseColor("#FFA726"), Color.parseColor("#FF9800"), Color.parseColor("#FB8C00"), Color.parseColor("#F57C00"), Color.parseColor("#EF6C00"), Color.parseColor("#E65100")),
            intArrayOf(Color.parseColor("#FF7043"), Color.parseColor("#FF5722"), Color.parseColor("#F4511E"), Color.parseColor("#E64A19"), Color.parseColor("#D84315"), Color.parseColor("#BF360C")),
            intArrayOf(Color.parseColor("#8D6E63"), Color.parseColor("#795548"), Color.parseColor("#6D4C41"), Color.parseColor("#5D4037"), Color.parseColor("#4E342E"), Color.parseColor("#3E2723")),
            intArrayOf(Color.parseColor("#BDBDBD"), Color.parseColor("#9E9E9E"), Color.parseColor("#757575"), Color.parseColor("#616161"), Color.parseColor("#424242"), Color.parseColor("#212121")),
            intArrayOf(Color.parseColor("#78909C"), Color.parseColor("#607D8B"), Color.parseColor("#546E7A"), Color.parseColor("#455A64"), Color.parseColor("#37474F"), Color.parseColor("#263238")))

    //网络图片数据 给用户随机使用
    var IMAGE_URL = arrayListOf(
            "https://img2.woyaogexing.com/2019/08/30/abf3568adb8745ac9d5dc6cf9a3da895!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/246e7cea8e0849cc88140f1973fdb95d!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/516b60c208824015ab2fb736cea1eb8c!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/b6c6bce7acc84e7aabed05ccc5ec9f80!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/749c2d6c2f174a91b1f1f92c2de30004!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/38304054b90447b9bc21bb07176ab058!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/917e8cbef5344431a9b31c51a483f363!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/bdaae1c240af46ac98a95eaac3be843f!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/13908fcec57a4738917fec458d581bdf!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/7f9c86f6cbbf4f9892df28e50321f477!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/f6d7b8480a4b40298d7c9beb079ba8d4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/30/f240d46657cf446eb0b5eb6290ff4528!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/a85407f91da24953a619df9315993e34!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/d5d365f1458a41d891b7ada030e74232!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/11217143b1ef433b8a914c16548fa50e!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/c8436d280f49418b8825fadfeeb0c0f8!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/81513514d2c04d3e901c33823ba3c492!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/1e300689c6a845b99e8daf32d72a1929!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/3adc12526ac541b5a4efd4493b4a3e85!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/a0c0493a57164cf9985cdbc972c22dd1!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/62884a8178f543fabcfb47db2ef7003d!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/9204eb1b05844d5c9723d37fcd76dc77!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/15937d014e13450c9e11ad5c3a015cf2!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/0366969e21b14d479696d444302be8a4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/29/4739431c257643a29b3a3343a4f5fb2e!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/3a918ed489af4490884ebef741f8df91!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/9a4c7ca101434b46bf03751c88e378a4!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/4a1e7dde2b5c47f4b025c15388dc290f!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/626d23fe1c2b411fa33f92e6c94d7af0!600x600.jpeg",
            "https://img2.woyaogexing.com/2019/08/28/667ebc1b9d7c4783bad801a2a3be199d!600x600.jpeg")

    /**
     * 获取随机rgb颜色值
     */
    fun randomColor(): Int {
        Random().run {
            //0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
            val red = nextInt(190)
            val green = nextInt(190)
            val blue = nextInt(190)
            //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
            return Color.rgb(red, green, blue)
        }
    }

    /**
     * 获取随机一张图片路径
     */
    fun randomImage(): String {
        Random().run {
            val red = nextInt(IMAGE_URL.size)
            return IMAGE_URL[red]
        }
    }

}
