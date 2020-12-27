package com.android.kotlin_map_custom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator


/**
 * Simple activity demonstrating ClusterManager.
 */
class ClusteringDemoTestActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var mClusterManager: ClusterManager<Person_Test?>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        // 구글 맵 프래그먼트를 띄운다
        // SupprotMapFragment 를 통해 레이아웃에 만든 fragment 의 ID 를 참조하고 구글맵을 호출한다.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.test_map) as SupportMapFragment?
        // getMapAsync 는 반드시 main Thread 에서 호출되어야한다
        mapFragment!!.getMapAsync(this)
    }

    //맵이 사용할 준비가 되었을 때 호출되는 메서드
    //(NULL이 아닌 GoogleMap 객체를 파라미터로 제공해 줄 수 있을 때)
    override fun onMapReady(map: GoogleMap) {
        if (mMap != null) {
            return
        }
        mMap = map

        //맵 시작할 때 카메라 zoom in 할 위치
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.56, 126.97), 10f))
        //클러스터 매니저
        mClusterManager = ClusterManager(this, mMap)

        mMap!!.setOnCameraIdleListener(mClusterManager)
        mMap!!.setOnMarkerClickListener(mClusterManager)
        //클러스터링되는 마커를 커스텀하기 위해 필요한 것

        //클러스터링되는 마커를 커스텀하기 위해 필요한 것
        mClusterManager!!.setRenderer(MyClusterRenderer(this, mMap, mClusterManager))


        //클러스터 클릭 리스너
        mClusterManager!!.setOnClusterClickListener { cluster ->
            Toast.makeText(
                this@ClusteringDemoTestActivity,
                cluster.position.toString() + "클러스터 클릭",
                Toast.LENGTH_LONG
            ).show()
            val latLng = LatLng(cluster.position.latitude, cluster.position.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10f)
            map.moveCamera(cameraUpdate)
            false
        }


        //개별 마커 정보창 클릭 리스너
        mMap!!.setOnInfoWindowClickListener { marker ->
            Toast.makeText(
                this@ClusteringDemoTestActivity,
                marker.title + " 정보창 클릭",
                Toast.LENGTH_LONG
            )
                .show()
            val intent = Intent(applicationContext, SubActivity::class.java)
            intent.putExtra("nameKey", marker.title + marker.snippet + marker.position)
            startActivity(intent)
        }

        //아이템 추가하기
        addItems()
    }




    private fun addItems() {

        // Set some lat/lng coordinates to start with.
        var lat = 37.56
        var lng = 126.97

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..14) {
            val offset = i / 60.0
            lat = lat + offset
            lng = lng + offset
            val offsetItem =
                Person_Test(LatLng(lat, lng), "Title $i", "Snippet $i", R.drawable.walter)
            mClusterManager!!.addItem(offsetItem)
        }
    }



    class MyClusterRenderer(
        context: Context?, map: GoogleMap?,
        clusterManager: ClusterManager<Person_Test?>?
    ) :
        DefaultClusterRenderer<Person_Test>(context, map, clusterManager) {
        private val mIconGenerator = IconGenerator(MyApplication.ApplicationContext())
        private val mImageView: ImageView


        //클러스터링 하기전에 아이템을 랜더링 하는 것입니다.
        override fun onBeforeClusterItemRendered(
            Person_Test: Person_Test,
            markerOptions: MarkerOptions
        ) {

            //BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
            markerOptions.icon(getItemIcon(Person_Test))
        }

        override fun onClusterItemRendered(clusterItem: Person_Test, marker: Marker) {
            super.onClusterItemRendered(clusterItem, marker)

            //Toast.makeText(ClusteringDemoTestActivity.this,"클러스터 클릭", Toast.LENGTH_LONG).show();
        }

        private fun getItemIcon(Person_Test: Person_Test): BitmapDescriptor {
            mImageView.setImageResource(Person_Test.profilePhoto)
            val icon = mIconGenerator.makeIcon()
            return BitmapDescriptorFactory.fromBitmap(icon)
        }

        init {
            mImageView = ImageView(MyApplication.ApplicationContext())
            mImageView.layoutParams = ViewGroup.LayoutParams(120, 120)
            mImageView.setPadding(2,2,2,2)
            mIconGenerator.setContentView(mImageView)

        }

    }


}