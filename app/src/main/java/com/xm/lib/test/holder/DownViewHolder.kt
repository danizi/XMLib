package com.xm.lib.test.holder

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.enum_.DownStateType
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.task.DownTasker
import com.xm.lib.downloader.utils.FileUtil.getSizeUnit
import com.xm.lib.test.R
import java.io.File

class DownViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        var isEdit = false
        const val TAG = "DownViewHolder"
    }

    private var mIv_icon: ImageView? = null
    private var ivSelect: ImageView? = null
    private var mProgressBar: ProgressBar? = null
    private var mTv_name: TextView? = null
    private var mTv_state: TextView? = null
    private var mTv_down_des: TextView? = null

    private fun bindViews(view: View) {
        ivSelect = view.findViewById(R.id.iv_select)
        mIv_icon = view.findViewById(R.id.iv_icon)
        mProgressBar = view.findViewById(R.id.progressBar)
        mTv_name = view.findViewById(R.id.tv_name)
        mTv_state = view.findViewById(R.id.tv_state)
        mTv_down_des = view.findViewById(R.id.tv_down_des)
    }

    fun bind(tasker: DownTasker, downManager: DownManager?) {
        bindViews(itemView)
        display(tasker.task)
        initEvent(downManager, tasker)
    }

    @SuppressLint("SetTextI18n")
    private fun display(task: DownTask) {
        ivSelect?.visibility = if (isEdit) {
            View.VISIBLE
        } else {
            View.GONE
        }
        mProgressBar?.max = 100
        mProgressBar?.progress = task.present.toInt()
        mTv_name?.text = task.name
        mTv_state?.text = when (task.state) {
            DownStateType.COMPLETE.ordinal -> {
                "完成"
            }
            DownStateType.NOT_STARTED.ordinal -> {
                mTv_down_des?.text = "0M/0M"
                "点击下载"
            }
            DownStateType.PAUSE.ordinal -> {
                "暂停"
            }
            DownStateType.RUNNING.ordinal -> {
                "下载中..."
            }
            DownStateType.ERROR.ordinal -> {
                "下载错误,点击重新下载。"
            }
            else -> {
                "xxx"
            }
        }
        mTv_down_des?.text = getSizeUnit(task.progress) + "/" + getSizeUnit(task.total)
    }

    private fun initEvent(downManager: DownManager?, tasker: DownTasker?) {
        itemView.setOnClickListener {
            if (isEdit) {
                tasker?.task?.isSelect = !tasker?.task?.isSelect!!
                if (tasker.task.isSelect) {
                    ivSelect?.setImageResource(R.mipmap.select)
                } else {
                    ivSelect?.setImageResource(R.mipmap.un_select)
                }
            } else {
                when (tasker?.task?.state) {
                    DownStateType.COMPLETE.ordinal -> {
                        BKLog.d(TAG, "task click 跳转到文件夹。")
                        openAssignFolder(tasker.task.path + File.separator + tasker.task.dir)
                    }
                    DownStateType.NOT_STARTED.ordinal -> {
                        enqueue(downManager, tasker)
                        BKLog.d(TAG, "task click ${tasker.task.name}任务添加到下载队列。")
                    }
                    DownStateType.PAUSE.ordinal -> {
                        enqueue(downManager, tasker)
                        BKLog.d(TAG, "task click ${tasker.task.name}任务恢复下载。")
                    }
                    DownStateType.RUNNING.ordinal -> {
                        //运行 -> 暂停
                        downManager?.pauseDownTasker(tasker.task)
                        BKLog.d(TAG, "task click ${tasker.task.name}任务暂停下载。")
                    }
                    DownStateType.ERROR.ordinal -> {
                        //错误 -> 重新下载
                        downManager?.pauseDownTasker(tasker.task)
                        enqueue(downManager, tasker)
                        BKLog.d(TAG, "task click ${tasker.task.name}任务下载出错，重新下载。")
                    }
                }
            }
        }
    }

    private fun openAssignFolder(path: String) {
        val file = File(path)
        if (!file.exists()) {
            return
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setDataAndType(Uri.fromFile(file), "file/*")
        itemView.context.startActivity(intent)
    }

    private fun enqueue(downManager: DownManager?, tasker: DownTasker?) {
        downManager?.createDownTasker(tasker?.task!!)?.enqueue()    //创建下载
        mTv_state?.text = "加入下载队列"
    }
}