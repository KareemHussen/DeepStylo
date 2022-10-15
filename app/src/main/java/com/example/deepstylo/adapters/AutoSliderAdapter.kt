package com.example.deepstylo.adapters

import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deepstylo.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.slider_item.view.*

class AutoSliderAdapter(var images: ArrayList<Int>) : SliderViewAdapter<AutoSliderAdapter.Holder>() {
    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): AutoSliderAdapter.Holder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.slider_item , parent , false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: AutoSliderAdapter.Holder?, position: Int) {
        val current = images[position]

        viewHolder!!.itemView.apply {
            image_view.setImageResource(current)
        }
    }

    class Holder(ItemView: View) : SliderViewAdapter.ViewHolder(ItemView) {

    }
}