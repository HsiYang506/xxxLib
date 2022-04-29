package com.xxx.xxxmusic

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xxx.xxxmusic.blurview.BlurAlgorithm
import com.xxx.xxxmusic.blurview.RenderEffectBlur
import com.xxx.xxxmusic.blurview.RenderEffectPrecision
import com.xxx.xxxmusic.blurview.RenderScriptBlur
import com.xxx.xxxmusic.databinding.ActivityMainBinding
import com.xxx.xxxmusic.ui.main.SectionsPagerAdapter
import com.xxx.library.log.log
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnPageChangeListener
import java.lang.reflect.Method


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBlurView()
        fuckMIUI()
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        binding.banner
            .addBannerLifecycleObserver(this)//添加生命周期观察者
            .setAdapter(BannerAdapter(resources))
            .indicator = CircleIndicator(this)

        binding.banner.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {


            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun fuckMIUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding.bvContainer.setPadding(0, getStatusBarHeight().log { }, 0, 0)
        hasNotch(this).log()
        setFullScreenWindowLayoutInDisplayCutout(window)
    }


    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    private fun hasNotch(context: Context): Boolean {
        var ret = false
        try {
            val cl: ClassLoader = context.classLoader
            val SystemProperties = cl.loadClass("android.os.SystemProperties")
            val get: Method = SystemProperties.getMethod(
                "getInt",
                String::class.java,
                Int::class.javaPrimitiveType
            )
            ret = get.invoke(SystemProperties, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return ret
        }
    }

    /*刘海屏全屏显示FLAG*/
    val FLAG_NOTCH_SUPPORT = 0x00000100 // 开启配置

    val FLAG_NOTCH_PORTRAIT = 0x00000200 // 竖屏配置

    val FLAG_NOTCH_HORIZONTAL = 0x00000400 // 横屏配置


    /**
     * 设置应用窗口在刘海屏手机使用刘海区
     *
     *
     * 通过添加窗口FLAG的方式设置页面使用刘海区显示
     *
     * @param window 应用页面window对象
     */
    private fun setFullScreenWindowLayoutInDisplayCutout(window: Window?) {
        // 竖屏绘制到耳朵区
        val flag = FLAG_NOTCH_SUPPORT or FLAG_NOTCH_PORTRAIT
        try {
            val method: Method = Window::class.java.getMethod(
                "addExtraFlags",
                Int::class.javaPrimitiveType
            )
            method.invoke(window, flag)
        } catch (e: java.lang.Exception) {
            Log.e("test", "addExtraFlags not found.")
        }
    }


    public fun getStatusBarHeight(): Int {
        var statusBarHeight = 0;
        var resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    private fun setupBlurView() {
        val radius = 25f

        //set background, if your root layout doesn't have one
        val windowBackground = window.decorView.background
        val algorithm: BlurAlgorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RenderEffectBlur(binding.bvTop, RenderEffectPrecision.EXACT)
        } else {
            RenderScriptBlur(this)
        }
        binding.bvTop.setupWith(binding.root)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(algorithm)
            .setBlurRadius(radius)
    }


    val resources = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
    )

    override fun onResume() {
        super.onResume()
        binding.banner.start()
    }

    override fun onPause() {
        super.onPause()
        binding.banner.stop()
    }

    inner class BannerAdapter(mData: List<Int>?) : BannerImageAdapter<Int>(mData) {

        override fun onBindView(
            holder: BannerImageHolder?,
            resource_id: Int,
            position: Int,
            size: Int
        ) {
            holder?.imageView?.setImageResource(resource_id)
        }

    }
}