package aws.apps.usbDeviceEnumerator.ui.common.views

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class ScrollViewNestedViewPager(cont: Context, attr: AttributeSet?) : ViewPager(cont, attr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val h: Int = child.measuredHeight
            if (h > height) height = h
        }
        val heightMeasure = MeasureSpec.makeMeasureSpec(
            height,
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }
}