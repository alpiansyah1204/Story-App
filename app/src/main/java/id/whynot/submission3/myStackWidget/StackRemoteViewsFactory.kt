package id.whynot.submission3.myStackWidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import id.whynot.submission3.R
import id.whynot.submission3.model.ModelImagePreference
import id.whynot.submission3.preference.Imagepreference
import java.net.URL

class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private lateinit var modelImagePreference: ModelImagePreference
    private lateinit var imagepreference: Imagepreference
    private val mWidgetItems = ArrayList<Bitmap>()


    override fun onCreate() {
        imagepreference = Imagepreference(mContext)
        modelImagePreference = imagepreference.getUser()

        if (modelImagePreference.image1.toString().isNotEmpty()) {
            Log.e("image satu ", modelImagePreference.image1.toString())
        }

    }

    override fun onDataSetChanged() {

        val url = URL(modelImagePreference.image1.toString())
        mWidgetItems.add(BitmapFactory.decodeStream(url.openConnection().getInputStream()))
        val url2 = URL(modelImagePreference.image2.toString())
        mWidgetItems.add(BitmapFactory.decodeStream(url2.openConnection().getInputStream()))
        val url3 = URL(modelImagePreference.image3.toString())
        mWidgetItems.add(BitmapFactory.decodeStream(url3.openConnection().getInputStream()))
        val url4 = URL(modelImagePreference.image4.toString())
        mWidgetItems.add(BitmapFactory.decodeStream(url4.openConnection().getInputStream()))


    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])
        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0
    override fun hasStableIds(): Boolean = false
}