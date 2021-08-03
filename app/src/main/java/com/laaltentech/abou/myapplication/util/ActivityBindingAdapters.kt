package com.laaltentech.abou.myapplication.util

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.laaltentech.abou.myapplication.R
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ActivityBindingAdapters @Inject constructor(val activity: Activity) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        try {
            if (url != null) {
                Glide.with(activity).load(url).into(imageView)
            } else
                Glide.with(activity).load(activity.getDrawable(R.drawable.circle_shape)).into(imageView)
        }catch (e : Exception){
        }
    }

    @BindingAdapter("drawableUrl")
    fun bindDrawableImage(imageView: ImageView, url: Drawable) {
        imageView.background = url
    }

    @BindingAdapter("imageResource")
    fun bindResourceImage(imageView: ImageView, resource: Int?) {
        resource?.let {
            imageView.setImageResource(it)
        }
    }

    @BindingAdapter("visibility")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @BindingAdapter(value = ["timeStamp", "format"], requireAll = false)
    fun bindDateTime(textView: TextView, timeStamp: String?, format: String) {

        if(timeStamp!=null) {
            var timeStamp1 =  timeStamp.toLong()
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val mDate = Date(timeStamp1)
            textView.text = dateFormat.format(mDate)
        }else {
            textView.text = "--/--"
        }

    }
}