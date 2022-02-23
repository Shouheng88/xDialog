package me.shouheng.uix.widget.rv

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.anno.EmptyViewState
import me.shouheng.uix.widget.anno.LoadingStyle
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.databinding.UixLayoutEmptyViewBinding

/**
 * 列表为空控件的一个实现类
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-22 22:21
 */
class EmptyView : FrameLayout, IEmptyView {

    private lateinit var binding: UixLayoutEmptyViewBinding

    @LoadingStyle
    var loadingStyle: Int = LoadingStyle.STYLE_ANDROID
        set(value) {
            field = value
            isAndroidStyle = loadingStyle == LoadingStyle.STYLE_ANDROID
            updateStyleState()
        }
    @EmptyViewState
    var emptyViewState: Int = EmptyViewState.STATE_LOADING
        set(value) {
            field = value
            isLoading = emptyViewState == EmptyViewState.STATE_LOADING
            updateEmptyState()
        }
    var emptyImage: Int? = null
        set(value) {
            field = value
            updateEmptyImageState()
        }
    var emptyImageSize: Int? = null
        set(value) {
            field = value
            if (value != null) {
                binding.ivEmpty.layoutParams = binding.ivEmpty.layoutParams.apply {
                    width = value
                    height = value
                }
            }
        }
    var emptyTitle: String? = null
        set(value) {
            field = value
            binding.tvEmptyTitle.text = value
        }
    @ColorInt
    var emptyTitleColor: Int? = null
        set(value) {
            field = value
            value?.let { binding.tvEmptyTitle.setTextColor(it) }
        }
    var emptyDetails: String? = null
        set(value) {
            field = value
            binding.tvEmptyDetail.text = value
        }
    @ColorInt
    var emptyDetailsColor: Int? = null
        set(value) {
            field = value
            value?.let { binding.tvEmptyDetail.setTextColor(it) }
        }
    var loadingTips: String? = null
        set(value) {
            field = value
            binding.tvLoading.text = value
        }
    @ColorInt
    var loadingTipsColor: Int? = null
        set(value) {
            field = value
            value?.let { binding.tvLoading.setTextColor(it) }
        }

    private var isLoading: Boolean = false
    private var isAndroidStyle: Boolean = true

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = UixLayoutEmptyViewBinding.inflate(LayoutInflater.from(context), this)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyView)
            loadingStyle = typedArray.getInt(
                R.styleable.EmptyView_empty_loading_style, LoadingStyle.STYLE_ANDROID)
            emptyViewState = typedArray.getInt(
                R.styleable.EmptyView_empty_state, EmptyViewState.STATE_LOADING)

            if (typedArray.hasValue(R.styleable.EmptyView_empty_image))
                emptyImage = typedArray.getResourceId(R.styleable.EmptyView_empty_image, -1)
            if (typedArray.hasValue(R.styleable.EmptyView_empty_image_size)) {
                val size = typedArray.getDimensionPixelSize(R.styleable.EmptyView_empty_image_size, 60f.dp(context))
                binding.ivEmpty.layoutParams = binding.ivEmpty.layoutParams.apply {
                    width = size
                    height = size
                }
            }

            emptyTitle = typedArray.getString(R.styleable.EmptyView_empty_title)
            emptyDetails = typedArray.getString(R.styleable.EmptyView_empty_detail)
            loadingTips = typedArray.getString(R.styleable.EmptyView_empty_loading_tips)

            if (typedArray.hasValue(R.styleable.EmptyView_empty_title_text_color))
                emptyTitleColor = typedArray.getColor(R.styleable.EmptyView_empty_title_text_color, 0)
            if (typedArray.hasValue(R.styleable.EmptyView_empty_detail_text_color))
                emptyDetailsColor = typedArray.getColor(R.styleable.EmptyView_empty_detail_text_color, 0)
            if (typedArray.hasValue(R.styleable.EmptyView_empty_loading_tips_text_color))
                loadingTipsColor = typedArray.getColor(R.styleable.EmptyView_empty_loading_tips_text_color, 0)
            typedArray.recycle()
        }

        binding.tvLoading.text = loadingTips
        loadingTipsColor?.let { binding.tvLoading.setTextColor(it) }
        binding.tvEmptyTitle.text = emptyTitle
        emptyTitleColor?.let { binding.tvEmptyTitle.setTextColor(it) }
        binding.tvEmptyDetail.text = emptyDetails
        emptyDetailsColor?.let { binding.tvEmptyDetail.setTextColor(it) }

        isAndroidStyle = loadingStyle == LoadingStyle.STYLE_ANDROID
        updateStyleState()
        val hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.uix_loading)
        binding.ivLoading.startAnimation(hyperspaceJumpAnimation)

        isLoading = emptyViewState == EmptyViewState.STATE_LOADING
        updateEmptyState()

        updateEmptyImageState()
    }

    override fun showLoading() {
        isLoading = true
        updateEmptyState()
    }

    override fun showEmpty() {
        isLoading = false
        updateEmptyState()
    }

    private fun updateStyleState() {
        binding.ivLoading.gone(isAndroidStyle)
        binding.pb.gone(!isAndroidStyle)
    }

    private fun updateEmptyState() {
        binding.llLoading.gone(!isLoading)
        binding.llEmpty.gone(isLoading)
    }

    private fun updateEmptyImageState() {
        emptyImage?.let { binding.ivEmpty.setImageResource(it) }
        binding.ivEmpty.gone(emptyImage == null)
    }

    override fun show() {
        this.visibility = View.VISIBLE
    }

    override fun hide() {
        this.visibility = View.GONE
    }

    override fun getView(): View = this

    @BeautyDialogDSL
    class Builder {
        @LoadingStyle private var style: Int            = LoadingStyle.STYLE_ANDROID
        @EmptyViewState private var state: Int          = EmptyViewState.STATE_LOADING
        private var emptyImage: Int?                    = null
        private var emptyImageSize: Int?                = null
        private var emptyTitle: String?                 = null
        @ColorInt private var emptyTitleColor: Int?     = null
        private var emptyDetails: String?               = null
        @ColorInt private var emptyDetailsColor: Int?   = null
        private var loadingTips: String?                = null
        @ColorInt private var loadingTipsColor: Int?    = null

        /** Loading style empty view. */
        fun withStyle(@LoadingStyle loadingStyle: Int) {
            this.style = loadingStyle
        }

        /** Loading state empty view. */
        fun withState(@LoadingStyle emptyViewState: Int) {
            this.state = emptyViewState
        }

        /** Empty image. */
        fun withEmptyImage(@DrawableRes image: Int) {
            this.emptyImage = image
        }

        /** Empty image size. */
        fun withEmptyImageSize(emptyImageSize: Int) {
            this.emptyImageSize = emptyImageSize
        }

        /** Empty title. */
        fun withEmptyTitle(title: String) {
            this.emptyTitle = title
        }

        /** Empty title text color. */
        fun withEmptyTitleColor(@ColorInt textColor: Int) {
            this.emptyTitleColor = textColor
        }

        /** Empty detail. */
        fun setEmptyDetails(details: String) {
            this.emptyDetails = details
        }

        /** Empty detail text color. */
        fun withEmptyDetailColor(@ColorInt textColor: Int) {
            this.emptyDetailsColor = textColor
        }

        /** Loading tips. */
        fun withLoadingTips(tips: String) {
            this.loadingTips = tips
        }

        /** Loading tips text color. */
        fun withLoadingTipsColor(@ColorInt textColor: Int) {
            this.loadingTipsColor = textColor
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(context: Context): EmptyView {
            val emptyView = EmptyView(context)
            emptyView.loadingStyle = style
            emptyView.emptyViewState = state
            emptyView.emptyImage = emptyImage
            emptyView.emptyImageSize = emptyImageSize
            emptyView.emptyTitle = emptyTitle
            emptyView.emptyTitleColor = emptyTitleColor
            emptyView.emptyDetails = emptyDetails
            emptyView.emptyDetailsColor = emptyDetailsColor
            emptyView.loadingTips = loadingTips
            emptyView.loadingTipsColor = loadingTipsColor
            return emptyView
        }
    }
}

/** Create an empty view by DSL. */
inline fun emptyView(
    context: Context,
    init: EmptyView.Builder.() -> Unit
): EmptyView {
    val builder = EmptyView.Builder()
    builder.init()
    return builder.build(context)
}

@RestrictTo(RestrictTo.Scope.LIBRARY) fun View.gone(goneIf: Boolean) {
    this.visibility = if (goneIf) View.GONE else View.VISIBLE
}

@RestrictTo(RestrictTo.Scope.LIBRARY) fun Float.dp(context: Context): Int {
    val scale: Float = context.getResources().getDisplayMetrics().density
    return (this * scale + 0.5f).toInt()
}

