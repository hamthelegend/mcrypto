package com.tqbfoxx.mcryptotwo.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding



const val ERROR_INT = -99
const val UNNECESSARY = -98


/**
 * The string content of an EditText.
 *
 * Gets or sets the content of an EditText as a String or using a String
 *
 * 
 */
var EditText.textString: String
	get() = this.text.toString()
	set(text) = this.setText(text, TextView.BufferType.EDITABLE)

/**
 *
 */
fun Context.getColorCompat(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

/**
 *
 */
fun ClipboardManager.copy(text: String) {
	this.primaryClip = ClipData.newPlainText("simple text", text)
}

fun View.setTooltipCompat(text: String) = TooltipCompat.setTooltipText(this, text)

fun ImageView.setTintCompat(@ColorInt colorInt: Int) = ImageViewCompat.setImageTintList(this, ColorStateList
	.valueOf(colorInt))


fun Context.goToActivity(activityClass: Class<*>) = startActivity(
	Intent(this, activityClass)
)

fun <T: ViewDataBinding> Activity.setBindingContentView(@LayoutRes layoutResId: Int) =
	DataBindingUtil.setContentView<T>(this, layoutResId)

fun Context.getAttributeColor(
	@AttrRes attributeId: Int
): Int {
	val typedValue = TypedValue()
	theme.resolveAttribute(attributeId, typedValue, true)
	val colorRes = typedValue.resourceId
	var color = -1
	try {
		color = getColorCompat(colorRes)
	} catch (e: Resources.NotFoundException) {
		Log.w(TAG, "Not found color resource by id: $colorRes")
	}

	return color
}