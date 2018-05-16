package com.zy.kotlinutils.core

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zy.kotlinutils.AppInterface

/**
 * Created by zy on 17-12-26.
 */
fun <T: View> View.sss(id: Int): T? {
    return findViewById(id) as T
}

fun <T: View> T.detach() {
    var vp = parent
    if (vp is ViewGroup) {
        vp.removeView(this)
    }
}

fun <T: View> T.onClick(listener: (T) -> Unit) {
    setOnClickListener { listener(this) }
}

var View.leftPadding: Int
    set(value) {
        setPadding(value, paddingTop, paddingRight, paddingBottom)
    }
    get() = paddingLeft

var View.rightPadding: Int
    set(value) {
        setPadding(paddingLeft, paddingTop, value, paddingBottom)
    }
    get() = paddingRight

var View.topPadding: Int
    set(value) {
        setPadding(paddingLeft, value, paddingRight, paddingBottom)
    }
    get() = paddingTop

var View.bottomPadding: Int
    set(value) {
        setPadding(paddingLeft, paddingTop, paddingRight, value)
    }
    get() = paddingBottom

var TextView.leftIcon: Drawable?
    set(value) {
        setCompoundDrawablesWithIntrinsicBounds(value, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
    }
    get() = compoundDrawables[0]

var TextView.topIcon: Drawable?
    set(value) {
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], value, compoundDrawables[2], compoundDrawables[3])
    }
    get() = compoundDrawables[1]

var TextView.rightIcon: Drawable?
    set(value) {
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], value, compoundDrawables[3])
    }
    get() = compoundDrawables[2]

var TextView.bottomIcon: Drawable?
    set(value) {
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], value)
    }
    get() = compoundDrawables[3]

fun View.bg(d: Drawable) {
    ViewCompat.setBackground(this, d)
}

fun linearLayout(context: Context, init: LinearLayout.() -> Unit): LinearLayout {
    var layout = LinearLayout(context)
    layout.init()
    return layout
}

fun relativeLayout(context: Context, init: RelativeLayout.() -> Unit): RelativeLayout {
    var layout = RelativeLayout(context)
    layout.init()
    return layout
}

fun frameLayout(context: Context, init: FrameLayout.() -> Unit): FrameLayout {
    var layout = FrameLayout(context)
    layout.init()
    return layout
}

fun <V: View> view(layout: V, init: V.() -> Unit): V {
    layout.init()
    return layout
}

fun <G: ViewGroup> viewGroup(layout: G, init: G.() -> Unit): G {
    layout.init()
    return layout
}

fun <G: ViewGroup> G.linearLayout(init: LinearLayout.() -> Unit): LinearLayout {
    return hierarchy(LinearLayout(context), init)
}

fun <G: ViewGroup> G.frameLayout(init: FrameLayout.() -> Unit): FrameLayout {
    return hierarchy(FrameLayout(context), init)
}

fun <G: ViewGroup> G.relativeLayout(init: RelativeLayout.() -> Unit): RelativeLayout {
    return hierarchy(RelativeLayout(context), init)
}

fun <G: ViewGroup, V: View> G.view(create: (context: Context) -> V, init: V.() -> Unit): V {
    return hierarchy(create(context), init)
}

fun <G: ViewGroup> G.textView(init: TextView.() -> Unit): TextView {
    return hierarchy(TextView(context), init)
}

fun <G: ViewGroup> G.imageView(init: ImageView.() -> Unit): ImageView {
    return hierarchy(ImageView(context), init)
}

fun <G: ViewGroup> G.button(init: Button.() -> Unit): Button {
    return hierarchy(Button(context), init)
}

fun <G: ViewGroup> G.view(init: View.() -> Unit): View {
    return hierarchy(View(context), init)
}

private fun <G: ViewGroup, V: View> G.hierarchy(v: V, init: V.() -> Unit): V {
    v.init()
    addView(v)
    return v
}

private fun <V : View> RelativeLayout.hierarchy(v: V, init: V.() -> Unit): RelativeLayout.LayoutParams {
    v.init()
    val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
    addView(v, lp)
    return lp
}

inline fun <reified V: View> view(context: Context, init: V.() -> Unit): V {
    var constructor = V::class.java.getConstructor(Context::class.java)
    var layout: V = constructor.newInstance(context)
    layout.init()
    return layout
}

fun <V: View> ViewGroup.res(layout: Int, init: V.() -> Unit): V {
    return hierarchy(context.inflate(layout), init)
}

fun <V: View> V.lp(init: ViewGroup.LayoutParams.() -> Unit) {
    init(layoutParams)
}