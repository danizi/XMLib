package com.xm.lib.downloader.task.runnable

import android.os.Environment
import com.xm.lib.common.log.BKLog
import com.xm.lib.downloader.DownManager
import com.xm.lib.downloader.enum_.DownErrorType
import com.xm.lib.downloader.task.DownTask
import com.xm.lib.downloader.utils.CommonUtil
import com.xm.lib.downloader.utils.CommonUtil.getFileName
import com.xm.lib.downloader.utils.FileUtil
import com.xm.lib.downloader.utils.FileUtil.del
import com.xm.lib.downloader.utils.FileUtil.getSizeUnit
import com.xm.lib.downloader.utils.FileUtil.mergeFiles
import java.io.File
import java.io.RandomAccessFile
import java.util.concurrent.ExecutorService

/**
 * 任务多个线程下载（分段）
 */
class MultiRunnable2 : BaseRunnable() {

    companion object {
        const val TAG = "MultiRunnable2"
        fun newMultiRunnable2(task: DownTask?, downManager: DownManager?, listener: BaseRunnable.OnListener?): MultiRunnable2 {
            val multiRunnable = MultiRunnable2()
            multiRunnable.name = task?.name!!
            multiRunnable.url = task.url
            multiRunnable.total = task.total
            multiRunnable.process = task.progress
            multiRunnable.present = task.present.toFloat()
            multiRunnable.pool = downManager?.downConfig()?.downTaskerPool

            multiRunnable.threadName = "MultiRunnable2"
            multiRunnable.threadNum = downManager?.downConfig()?.threadNum?.toInt()!!
            multiRunnable.dir = task.dir
            multiRunnable.path = task.path
            multiRunnable.listener = listener
            return multiRunnable
        }
    }


    var threadNum = 0   //限定线程数量
    var pool: ExecutorService? = null
    private var subThreadCompleteCount = 0 //下载线程完成集合数量
    private var downRunnables: ArrayList<SingleRunnable2> = ArrayList() //线程集合

    override fun down() {
        /*执行下载操作*/
        //1 获取资源的大小
        val conn = iniConn()
        total = conn.contentLength.toLong()

        val lump = total / threadNum
        BKLog.d(TAG, "分成${threadNum}块 total:${total}B lump:${lump}B  块大小:${getSizeUnit(lump)} 总大小:${getSizeUnit(total)} ")
        //2 分配子线程数量,并开始下载

        //创建临时文件夹与文件
        val files = ArrayList<File>()
        val rafs = ArrayList<RandomAccessFile>()
        for (i in 0..(threadNum - 1)) {
            val dir = "$dir/${CommonUtil.getFileName(url)}_Temp"
            val fileName = "$i.temp"
            val file = FileUtil.createNewFile(path, dir, fileName)
            val raf = RandomAccessFile(file, "rw")
            raf.seek(file.length())
            files.add(file)
            rafs.add(raf)
        }

        //线程启动
        for (i in 0..(threadNum - 1)) {
            val file = files[i]
            val length = file.length()
            val startIndex = if (length == (lump * (i + 1) - 1)) {
                BKLog.d(TAG, "${file.name} 块下载完成")
                return
            } else {
                length + i * lump
            }
            val endIndex = if (i == (threadNum - 1)) {
                total
            } else {
                lump * (i + 1) - 1
            }

            val singleRunnable = SingleRunnable2()
            singleRunnable.threadName = "$name MultiRunnable_SingleRunnable_$i"
            singleRunnable.url = url
            singleRunnable.process = files[i].length()
            singleRunnable.raf = rafs[i]
            singleRunnable.rangeStartIndex = startIndex
            singleRunnable.rangeEndIndex = endIndex
            singleRunnable.listener = object : BaseRunnable.OnListener {

                override fun onProcess(singleRunnable: BaseRunnable, process: Long, total: Long, present: Float) {
                    //3 获取下载的进度
                    callbackProcess(singleRunnable, process, total, present)
                }

                override fun onComplete(singleRunnable: BaseRunnable, total: Long) {
                    //4 下载完成
                    callbackComplete(singleRunnable, total)
                }

                override fun onError(singleRunnable: BaseRunnable, type: DownErrorType, s: String) {
                    //下载失败
                    callbackError(singleRunnable, type, s)
                }

                private fun callbackProcess(singleRunnable: BaseRunnable, process: Long, total: Long, present: Float) {
//                    Thread.sleep(1000)
                    this@MultiRunnable2.process = 0
                    for (downRunnable in downRunnables) {
                        this@MultiRunnable2.process += downRunnable.process
                    }
                    listener?.onProcess(this@MultiRunnable2, this@MultiRunnable2.process, this@MultiRunnable2.total, (this@MultiRunnable2.process * 100 / this@MultiRunnable2.total).toFloat())
                }

                private fun callbackComplete(singleRunnable: BaseRunnable, total: Long) {
                    BKLog.d(TAG, "${singleRunnable.threadName} onComplete total:$total")
                    subThreadCompleteCount++
                    if (subThreadCompleteCount == threadNum) {
                        runing(false)
                        exit()
                        val path = Environment.getExternalStorageDirectory().absolutePath
                        val outFile = FileUtil.createNewFile(path, dir, getFileName(url))
                        val inFile = File(path + File.separator + "$dir/${getFileName(url)}_Temp")
                        mergeFiles(outFile, inFile)
                        del(inFile)
                        listener?.onComplete(this@MultiRunnable2, this@MultiRunnable2.total)
                    }
                }

                private fun callbackError(singleRunnable: BaseRunnable, type: DownErrorType, s: String) {
                    listener?.onError(this@MultiRunnable2, type, s)
                }
            }
            pool?.submit(singleRunnable)
            downRunnables.add(singleRunnable)
        }
    }

    override fun exit() {
        /*用户退出停止一切线程操作*/
        runing(false)
        for (downRunnable in downRunnables) {
            downRunnable.exit()
        }
    }
}

