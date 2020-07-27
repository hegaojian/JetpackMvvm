package me.hgj.jetpackmvvm.util

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/26
 * 描述　:
 */
class UrlEncoderUtils private constructor() {
    companion object {
        /**
         * 判断 str 是否已经 URLEncoder.encode() 过
         * 经常遇到这样的情况, 拿到一个 URL, 但是搞不清楚到底要不要 URLEncoder.encode()
         * 不做 URLEncoder.encode() 吧, 担心出错, 做 URLEncoder.encode() 吧, 又怕重复了
         *
         * @param str 需要判断的内容
         * @return 返回 `true` 为被 URLEncoder.encode() 过
         */
        @JvmStatic
        fun hasUrlEncoded(str: String): Boolean {
            var encode = false
            for (i in str.indices) {
                val c = str[i]
                if (c == '%' && i + 2 < str.length) {
                    // 判断是否符合urlEncode规范
                    val c1 = str[i + 1]
                    val c2 = str[i + 2]
                    if (isValidHexChar(c1) && isValidHexChar(c2)) {
                        encode = true
                        break
                    } else {
                        break
                    }
                }
            }
            return encode
        }

        /**
         * 判断 c 是否是 16 进制的字符
         *
         * @param c 需要判断的字符
         * @return 返回 `true` 为 16 进制的字符
         */
        private fun isValidHexChar(c: Char): Boolean {
            return c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'
        }
    }

    init {
        throw IllegalStateException("you can't instantiate me!")
    }
}