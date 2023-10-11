package com.example.mestkom.ui.cluster

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.mestkom.R
import com.yandex.mapkit.map.Cluster

class ClusterView(context: Context) : LinearLayout(context) {

    private val text by lazy { findViewById<TextView>(R.id.text_pins) }
    private val layout by lazy { findViewById<View>(R.id.layout_group) }

    init {
        inflate(context, R.layout.cluster_view, this)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        setBackgroundResource(R.drawable.cluster_view_background)
    }

    fun updateViews(clusterSize: Int) {
        text.text = clusterSize.toString()
        layout.isVisible = clusterSize != 0
    }
}