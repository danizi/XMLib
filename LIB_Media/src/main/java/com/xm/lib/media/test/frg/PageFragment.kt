package com.xm.lib.media.test.frg

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xm.lib.media.R
import com.xm.lib.media.test.holder.MediaListViewHolder
import com.xm.lib.media.test.holder.MediaViewHolder

@SuppressLint("ValidFragment")
class PageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val args = arguments
        val title = args!!.getString("title")
        val type = args.getInt("type")
        var view: View? = null
        when (type) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.test_frg_media, null, false)
                MediaViewHolder.create(context, view)
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.test_frg_media_list, null, false)
                MediaListViewHolder.create(context, view)
            }
        }
        return view
    }

    companion object {

        fun create(title: String, type: Int): Fragment {
            val fragment = PageFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putInt("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}