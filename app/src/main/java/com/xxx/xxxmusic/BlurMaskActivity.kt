package com.xxx.xxxmusic

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xxx.xxxmusic.databinding.ActivityBlurMaskBinding
import com.xxx.xxxmusic.databinding.ItemIamgeTxtBinding
import com.xxx.xxxmusic.ui.main.PageViewModel
import com.xxx.library.log.log
import java.lang.reflect.Method

class BlurMaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlurMaskBinding
    private lateinit var pageViewModel: PageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlurMaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fuckMIUI()
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        pageViewModel.list.observe(this) {
            binding.rvContainer.apply {
                adapter = ViewAdapter(it)
                layoutManager = LinearLayoutManager(this@BlurMaskActivity)
            }

        }
    }

    inner class ViewAdapter(val list: List<Int>) : RecyclerView.Adapter<ViewAdapter.Holder>() {
        private lateinit var binding: ItemIamgeTxtBinding

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            binding = ItemIamgeTxtBinding.inflate(layoutInflater)
            return Holder(binding.root)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            binding.ivContainer.setBackgroundResource(list[position])
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }


    private fun fuckMIUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.apply {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        binding.toolBar.clContainer.setPadding(0, getStatusBarHeight().log { }, 0, 0)
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

}