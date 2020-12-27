package com.android.kotlin_map_custom

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Person_Test(
    private val mPosition: LatLng,
    private val mTitle: String,
    private val mSnippet: String,
    val profilePhoto: Int
) :
    ClusterItem {
    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }
}