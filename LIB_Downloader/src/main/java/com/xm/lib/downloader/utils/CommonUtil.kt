package com.xm.lib.downloader.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.xm.lib.common.log.BKLog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.os.Looper


object CommonUtil {
    private val TAG = this.javaClass.simpleName

    fun getFileName(url: String?): String {
        var fileName = "down.xm"
        if (TextUtils.isEmpty(url)) {
            BKLog.e(TAG, "下载地址为null")
            return fileName
        }
        val s = url?.split("/")

        if (s != null) {
            fileName = s[s.size - 1]
        }
        return fileName
    }

    /**
     * 写入文件
     */
    fun getSize(num: Long): Float {
        val unit = "M"
        return when (unit) {
            "KB" -> {
                num / 1024f
            }
            "M" -> {
                num / 1024f / 1024f
            }
            "G" -> {
                num / 1024f / 1024f
            }
            else -> {
                throw Throwable("输入单位错误")
            }
        }
    }

    fun md5(plainText: String?): String {
        if (TextUtils.isEmpty(plainText)) return ""
        //定义一个字节数组
        var secretBytes: ByteArray? = null
        try {
            // 生成一个MD5加密计算摘要
            val md = MessageDigest.getInstance("MD5")
            //对字符串进行加密
            md.update(plainText?.toByteArray()!!)
            //获得加密后的数据
            secretBytes = md.digest()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("没有md5这个算法！")
        }

        //将加密后的数据转换为16进制数字
        var md5code = BigInteger(1, secretBytes).toString(16)// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (i in 0 until 32 - md5code.length) {
            md5code = "0$md5code"
        }
        return md5code
    }

    fun saveBmp2Gallery(context: Context, bmp: Bitmap, picName: String) {

        var fileName: String? = null
        //系统相册目录
        val galleryPath = (Environment.getExternalStorageDirectory().toString()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator)


        // 声明文件对象
        var file: File? = null
        // 声明输出流
        var outStream: FileOutputStream? = null

        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = File(galleryPath, "$picName.jpg")

            // 获得文件相对路径
            fileName = file.toString()
            // 获得输出流，如果文件中有内容，追加内容
            outStream = FileOutputStream(fileName)
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream)
            }

        } catch (e: Exception) {
            e.stackTrace
        } finally {
            try {
                outStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        //通知相册更新
        MediaStore.Images.Media.insertImage(context.contentResolver,
                bmp, fileName, null)
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(file)
        intent.data = uri
        context.sendBroadcast(intent)

        Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show()

    }

    /**
     * 是否是主线程
     */
    fun isMainThread(): Boolean {
        //return Looper.getMainLooper().getThread() == Thread.currentThread();
        //return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId()
        return Looper.getMainLooper() == Looper.myLooper()
    }

}