# 优雅的 Android 对话框类库封装 xDialog

对 Android 开发而言，对话框是打交道比较频繁的 UI 控件之一。对话框对大部分程序员而言并不陌生。然而，当考虑到复杂的交互效果、组件复用、自定义和各种 UI 风格的时候，事情变得麻烦了起来。xDialog 就是我设计的用来整合以上多种情况的 UI 组件。相比于大部分开源库，它可自定义程度更高，能满足更多的应用场景。

## 1、整体设计

xDialog 基于 Kotlin 开发，也吸取了 KotlinDSL 的特性，提供了更加方便使用的 API. 该对话框基于 DialogFragment 开发，在复用性方面，分别将对话框的头部、内容和底部进行了抽象，提取了三个接口，所以，用户只需要实现这三个接口就可以拼凑出自己的对话框，真正实现了三个部分的随意组装，

```kotlin
class BeautyDialog : DialogFragment() {

    private var iDialogTitle: IDialogTitle?     = null
    private var iDialogContent: IDialogContent? = null
    private var iDialogFooter: IDialogFooter?   = null

    // ...

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // ...
    }

    @BeautyDialogDSL class Builder {
        private var dialogTitle: IDialogTitle?      = null
        private var dialogContent: IDialogContent?  = null
        private var dialogFooter: IDialogFooter?    = null

        // ...

        /** 为对话框指定头部 */
        fun withTitle(iDialogTitle: IDialogTitle) {
            this.dialogTitle = iDialogTitle
        }

        /** 为对话框指定内容 */
        fun withContent(iDialogContent: IDialogContent) {
            this.dialogContent = iDialogContent
        }

        /** 为对话框指定底部 */
        fun withBottom(iDialogFooter: IDialogFooter) {
            this.dialogFooter = iDialogFooter
        }

        fun build(): BeautyDialog {
            val dialog = BeautyDialog()
            // ...
            return dialog
        }
    }
}

/** 提供了基于 kotlin DSL 风格的对话框构建器 */
inline fun createDialog(
    init: BeautyDialog.Builder.() -> Unit
): BeautyDialog = BeautyDialog.Builder().apply(init).build()
```

如上代码所示，用户只需要将通过实现三个接口实现自己的各个部分，然后就可以做到指定的位置的 UI 的替换。

## 2、兼容平板、手机和横屏

基于上述设计思路，我为构建者增加了几个参数用来满足在不同分辨率上使用的场景的需求。该库内置了四种不同大小的对话框，对于用户，只需要判断上述不同的情景之后根据情况传入指定的样式即可。然后在对话框内部会使用不同的 style 作为对话框的主题，

```kotlin
val theme = when(dialogStyle) {
    STYLE_WRAP      -> R.style.BeautyDialogWrap
    STYLE_HALF      -> R.style.BeautyDialogHalf
    STYLE_TWO_THIRD -> R.style.BeautyDialogTwoThird
    else            -> R.style.BeautyDialog
}
val dialog = if (bottomSheet) {
    BottomSheetDialog(requireContext(), theme)
} else {
    AlertDialog.Builder(requireContext(), theme).create()
}
```

## 3、兼容 BottomSheet 和普通的对话框的场景

谷歌提供的 Material 库中提供了叫做 BottomSheetDialog 的对话框。相比于普通的对话框，这个类可以提供更好的交互效果。比如，比如自己的地图中就有类似的交互效果。底部弹出一部分，然后向上可以继续拖拽并展示全部内容。这种交互方式在许多情景下会非常有用，特别是，当我们需要提供的内容可能在普通的对话框装不下的情况而又不希望切换一个新的页面的时候。较少的切换页面，降低操作的层级，可以提供更好的用户交互体验。

在 xDialog 中，我增加了几个参数来支持这种应用场景，

```kotlin
val dialog = if (bottomSheet) {
    BottomSheetDialog(requireContext(), theme).apply {
        halfExpandedRatio?.let { this.behavior.halfExpandedRatio = it }
        isFitToContents  ?.let { this.behavior.isFitToContents   = it }
        peekHeight       ?.let { this.behavior.peekHeight        = it }
    }
} else {
    AlertDialog.Builder(requireContext(), theme).create()
}
```

所以，对于使用这个库的用户而言，只需要动态调整几个参数就可以随意在两种不同的交互方式之间切换。

## 4、提供对话框背景模糊效果

xDialog 为背景模糊效果提供了封装。为此 xDialog 提供了 BlurEngine 用来在 Dialog 显示的时候截取屏幕进行模糊并展示到对话框背部。用户启用的方式也非常简单，只需要传入一个参数就可以直接使用背景模糊的效果。

## 5、提供了默认的封装和多种是用 API

除了上述封装，xDialog 提供了几个默认的头部、内容和底部实现类。用户可以直接使用，也可以参照他们实现自己想要的效果，

此外，xDialog 还提供了其他的 API，比如自定义对话框显示的位置是头部、中间还是底部，自定义对话框是否可以撤销，自定义对话框的背景，监听对话框的的显示和隐藏事件等等。

## 其他

上述是对该对话框封装的分析。对话框相关的开源库还是挺多的，这里我只是觉得这个设计可以拿出来分享一下。如果你需要做相关的工作的话，可以参考一下。最后源代码地址，

[源码地址：https://github.com/Shouheng88/xDialog](https://github.com/Shouheng88/xDialog)
