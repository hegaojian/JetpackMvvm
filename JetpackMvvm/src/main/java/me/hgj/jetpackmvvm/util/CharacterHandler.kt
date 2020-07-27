package me.hgj.jetpackmvvm.util

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import java.util.regex.Pattern
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import kotlin.experimental.and

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/26
 * 描述　:
 */
class CharacterHandler private constructor() {
    companion object {
        //emoji过滤器
        val EMOJI_FILTER: InputFilter = object : InputFilter {
            var emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
            )

            override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int,dend: Int): CharSequence? {
                val emojiMatcher = emoji.matcher(source)
                return if (emojiMatcher.find()) {
                    ""
                } else null
            }
        }
        /**
         * json 格式化
         *
         * @param json
         * @return
         */
        @JvmStatic
        fun jsonFormat(json: String): String {
            var json = json
            if (TextUtils.isEmpty(json)) {
                return "Empty/Null json content"
            }
            var message: String
            try {
                json = json.trim { it <= ' ' }
                message = if (json.startsWith("{")) {
                    val jsonObject = JSONObject(json)
                    jsonObject.toString(4)
                } else if (json.startsWith("[")) {
                    val jsonArray = JSONArray(json)
                    jsonArray.toString(4)
                } else {
                    json
                }
            } catch (e: JSONException) {
                message = json
            } catch (error: OutOfMemoryError) {
                message = "Output omitted because of Object size"
            }
            return message
        }

        /**
         * xml 格式化
         *
         * @param xml
         * @return
         */
        @JvmStatic
        fun xmlFormat(xml: String?): String? {
            if (TextUtils.isEmpty(xml)) {
                return "Empty/Null xml content"
            }
            val message: String?
            message = try {
                val xmlInput: Source =
                    StreamSource(StringReader(xml))
                val xmlOutput =
                    StreamResult(StringWriter())
                val transformer =
                    TransformerFactory.newInstance().newTransformer()
                transformer.setOutputProperty(OutputKeys.INDENT, "yes")
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
                transformer.transform(xmlInput, xmlOutput)
                xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
            } catch (e: TransformerException) {
                xml
            }
            return message
        }
    }

    init {
        throw IllegalStateException("you can't instantiate me!")
    }
}