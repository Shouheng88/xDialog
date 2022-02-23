package me.shouheng.uix.widget.text

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import me.shouheng.uix.widget.R

import java.util.regex.Pattern

/**
 * 正则表达式控制输入的编辑器
 *
 * @author [Shouheng Wang](mailto:shouheng2020@gmail.com)
 * @version 2019-10-03 22:48
 */
open class RegexEditText : AppCompatEditText, InputFilter {

    /** 正则表达式规则 */
    private var mPattern: Pattern? = null

    /** 正则 */
    var inputRegex: String?
        get() =
            if (mPattern == null) { null }
            else mPattern!!.pattern()
        set(regex) {
            if (regex == null || "" == regex) return
            mPattern = Pattern.compile(regex)
            addFilters(this)
        }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs)
    }

    /** 初始化属性 */
    protected open fun initialize(context: Context, attrs: AttributeSet?) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.RegexEditText)

        if (array.hasValue(R.styleable.RegexEditText_inputRegex)) {
            inputRegex = array.getString(R.styleable.RegexEditText_inputRegex)
        } else {
            if (array.hasValue(R.styleable.RegexEditText_regexType)) {
                when (array.getInt(R.styleable.RegexEditText_regexType, 0)) {
                    0x01 -> inputRegex = REGEX_MOBILE
                    0x02 -> inputRegex = REGEX_CHINESE
                    0x03 -> inputRegex = REGEX_ENGLISH
                    0x04 -> inputRegex = REGEX_COUNT
                    0x05 -> inputRegex = REGEX_NAME
                    0x06 -> inputRegex = REGEX_NONNULL
                }
            }
        }

        array.recycle()
    }

    /** 是否有这个输入标记 */
    fun hasInputType(type: Int): Boolean {
        return inputType and type != 0
    }

    /** 添加一个输入标记 */
    fun addInputType(type: Int) {
        inputType = inputType or type
    }

    /** 移除一个输入标记 */
    fun removeInputType(type: Int) {
        inputType = inputType and type.inv()
    }

    /** 添加筛选规则 */
    fun addFilters(filter: InputFilter?) {
        if (filter == null) {
            return
        }

        val newFilters: Array<InputFilter?>
        val oldFilters = filters
        if (oldFilters != null && oldFilters.size != 0) {
            newFilters = arrayOfNulls(oldFilters.size + 1)
            // 复制旧数组的元素到新数组中
            System.arraycopy(oldFilters, 0, newFilters, 0, oldFilters.size)
            newFilters[oldFilters.size] = filter
        } else {
            newFilters = arrayOfNulls(1)
            newFilters[0] = filter
        }
        super.setFilters(newFilters)
    }

    /**
     * [InputFilter]
     *
     * @param source 新输入的字符串
     * @param start  新输入的字符串起始下标，一般为0
     * @param end    新输入的字符串终点下标，一般为source长度-1
     * @param dest   输入之前文本框内容
     * @param dstart 原内容起始坐标，一般为0
     * @param dend   原内容终点坐标，一般为dest长度-1
     * @return       返回字符串将会加入到内容中
     */
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence {
        if (mPattern == null) {
            return source
        }

        // 拼接出最终的字符串
        val begin = dest.toString().substring(0, dstart)
        val over = dest.toString().substring(dstart + (dend - dstart), dstart + (dest.toString().length - begin.length))
        val result = begin + source + over

        // 判断是插入还是删除
        if (dstart > dend - 1) {
            if (mPattern!!.matcher(result).matches()) {
                // 如果匹配就允许这个文本通过
                return source
            }
        } else {
            if (!mPattern!!.matcher(result).matches()) {
                // 如果不匹配则不让删除（删空操作除外）
                if ("" != result) {
                    return dest.toString().substring(dstart, dend)
                }
            }
        }

        // 注意这里不能返回 null，否则会和 return source 效果一致
        return ""
    }

    companion object {
        /** 手机号（只能以 1 开头） */
        const val REGEX_MOBILE = "[1]\\d{0,10}"
        /** 中文（普通的中文字符） */
        const val REGEX_CHINESE = "[\\u4e00-\\u9fa5]*"
        /** 英文（大写和小写的英文） */
        const val REGEX_ENGLISH = "[a-zA-Z]*"
        /** 计数（非 0 开头的数字） */
        const val REGEX_COUNT = "[1-9]\\d*"
        /** 用户名（中文、英文、数字）*/
        const val REGEX_NAME = "[$REGEX_CHINESE|$REGEX_ENGLISH|\\d*]*"
        /** 非空格的字符（不能输入空格）*/
        const val REGEX_NONNULL = "\\S+"
    }
}
