package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.databinding.UixDialogContentEditSimpleBinding
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.dialog.footer.IDialogFooter
import me.shouheng.uix.widget.dialog.title.IDialogTitle
import me.shouheng.uix.widget.utils.dp
import me.shouheng.uix.widget.utils.gone
import me.shouheng.uix.widget.utils.stringOf
import me.shouheng.uix.widget.utils.drawableOf

/**
 * Simple dialog editor content
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-14 11:24
 */
class SimpleEditor private constructor(): ViewBindingDialogContent<UixDialogContentEditSimpleBinding>(), TextWatcher {

    private var content: String? = null
    private var hint: String? = null
    private var textColor: Int? = null
    private var hintTextColor: Int? = null
    private var lengthTextColor: Int? = null
    private var bottomLineColor: Int? = null
    private var textSize: Float = 16f
    private var singleLine = false
    private var numeric = false
    private var inputRegex: String? = null
    private var maxLength: Int? = null
    private var maxLines: Int? = null
    private var showLength = true
    private var clearDrawable: Drawable? = null

    private var dialogTitle: IDialogTitle? = null
    private var dialogFooter: IDialogFooter? = null

    override fun doCreateView(ctx: Context) {
        binding.et.addTextChangedListener(this)
        binding.et.setText(content)
        binding.et.hint = hint
        textColor?.let { binding.et.setTextColor(it) }
        hintTextColor?.let { binding.et.setHintTextColor(it) }
        lengthTextColor?.let { binding.tv.setTextColor(it) }
        bottomLineColor?.let { binding.v.setBackgroundColor(it) }
        binding.et.textSize = textSize
        binding.et.setSingleLine(singleLine)
        if (numeric) binding.et.addInputType(EditorInfo.TYPE_CLASS_NUMBER)
        inputRegex?.let { binding.et.inputRegex = inputRegex }
        maxLength?.let  { binding.et.addFilters(InputFilter.LengthFilter(it)) }
        maxLines?.let   { binding.et.maxLines = it }
        if (!showLength) binding.tv.gone()
        clearDrawable?.let { binding.et.setClearDrawable(it) }
    }

    fun getContent(): Editable? = binding.et.text

    override fun afterTextChanged(s: Editable?) {
        if (showLength) {
            val text = if (maxLength != null)
                "${binding.et.text?.length?:0}/${maxLength}"
            else "${binding.et.text?.length?:0}"
            binding.tv.text = text
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { /*noop*/ }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { /*noop*/ }

    override fun setDialogTitle(dialogTitle: IDialogTitle?) {
        this.dialogTitle = dialogTitle
    }

    override fun setDialogFooter(dialogFooter: IDialogFooter?) {
        this.dialogFooter = dialogFooter
    }

    @BeautyDialogDSL
    class Builder {
        private var content: String? = null
        private var hint: String? = null
        private var textColor: Int? = null
        private var hintTextColor: Int? = null
        private var lengthTextColor: Int? = null
        private var bottomLineColor: Int? = null
        private var textSize: Float = 16f
        private var singleLine = false
        private var numeric = false
        private var inputRegex: String? = null
        private var maxLength: Int? = null
        private var maxLines: Int? = null
        private var showLength = true
        private var clearDrawable: Drawable? = null

        /** Specify the editor content. */
        fun withContent(content: String) {
            this.content = content
        }

        /** Specify the editor hint. */
        fun withHint(hint: String) {
            this.hint = hint
        }

        /** Specify the editor text color. */
        fun withTextColor(textColor: Int) {
            this.textColor = textColor
        }

        /** Specify the editor hint text color. */
        fun withHintTextColor(hintTextColor: Int) {
            this.hintTextColor = hintTextColor
        }

        /** Specify the editor clear drawable. */
        fun withClearDrawable(clearDrawable: Drawable) {
            this.clearDrawable = clearDrawable
        }

        /** Specify the editor length text color. */
        fun withLengthTextColor(lengthTextColor: Int) {
            this.lengthTextColor = lengthTextColor
        }

        /** Specify the editor bottom line color. */
        fun withBottomLineColor(bottomLineColor: Int) {
            this.bottomLineColor = bottomLineColor
        }

        /** Specify the editor text size. */
        fun withTextSize(textSize: Float) {
            this.textSize = textSize
        }

        /** Specify the editor single line. */
        fun withSingleLine(singleLine: Boolean) {
            this.singleLine = singleLine
        }

        /** Specify the editor numeric. */
        fun withNumeric(numeric: Boolean) {
            this.numeric = numeric
        }

        /** Specify the editor input regex. */
        fun withInputRegex(inputRegex: String) {
            this.inputRegex = inputRegex
        }

        /** Specify the editor max length. */
        fun withMaxLength(maxLength: Int) {
            this.maxLength = maxLength
        }

        /** Specify the editor max lines. */
        fun withMaxLines(maxLines: Int) {
            this.maxLines = maxLines
        }

        /** Specify the editor show length or not. */
        fun withShowLength(showLength: Boolean) {
            this.showLength = showLength
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(): SimpleEditor {
            val editor = SimpleEditor()
            editor.content = content
            editor.hint = hint
            editor.textColor = textColor
            editor.hintTextColor = hintTextColor
            editor.lengthTextColor = lengthTextColor
            editor.bottomLineColor = bottomLineColor
            editor.textSize = textSize
            editor.singleLine = singleLine
            editor.numeric = numeric
            editor.inputRegex = inputRegex
            editor.maxLength = maxLength
            editor.maxLines = maxLines
            editor.showLength = showLength
            editor.clearDrawable = clearDrawable
            return editor
        }
    }
}

/** Create a simple editor of DSL style. */
inline fun simpleEditor(
    init: SimpleEditor.Builder.() -> Unit
): SimpleEditor {
    val builder = SimpleEditor.Builder()
    builder.init()
    return builder.build()
}
