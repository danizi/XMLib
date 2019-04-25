package com.xm.lib.downloader.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.xm.lib.common.log.BKLog
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.DecimalFormat

/**
 * android 文件操作工具类
 * 文件的操作模式四种
 * MODE_PRIVATE          默认操作模式，文件私有，只能本应用访问，写入内容会覆盖原文件内容
 * MODE_APPEND           会检查文件是否存在，存在则追加，否则建立新的文件
 * MODE_WORLD_READABLE   可被其他应用读取
 * MODE_WORLD_WRITEABLE  可悲其他应用写入
 *
 * ps:若文件既要可读可写就可以用以下形式
 *    OpenFileOutput("test.txt",MODE_WORLD_READABLE+MODE_WORLD_WRITEABLE)
 *
 *
 * API
 * openFileOutput(filename,mode)   打开输出流，写入字节流数据
 * openFileInout(filename)         打开输出流，读取字节流数据
 * getDir(name,mode)               在app的data目录获取或者创建name对应的子目录
 * getFileDir()                    获取app的data目录file的绝对路径
 * fileList()                      返回app的data目录下的全部文件
 * deleteFile(filename)            删除app的data目录下的指定文件
 *
 * ps:sharedpreferences,数据库都是私有的，除非开放权限，才能被外部应用访问。data/data/<包名>/file
 *
 * 读取SD卡上面的文件
 * 操作步骤
 * 1 读写判断sd卡是否插入,且读写                                Environment.getExternalStorageState.equals(Environment.MEDIA_MOUNTED)
 * 2 获取sd卡的外部目录                                         Environment.getExternalStorageDirectory().getCanonicalPath()
 * 3 使用FileOutputStream FileInputStream进行SD卡的读写操作     mmt\sdcard 或者 /storage
 * 4 添加权限
 *
 * ps: 1 在android6.0以上就算添加了操作文件的权限还是报没有权限的错误。使用动态获取权限的开源库也还是出错。
 *     2 在android某些机型，创建文件必须添加创建目录（如果创建失败，请尝试一级目录一级目录创建），然后再创建文件。
 *
 *
 * RandomAccessFile简述
 * 没有继承字节流字符流家中的任何一个类，它只实现了DataInput DataOutput这两个接口，意味着它能读能写。并且它可以“自由访问文件的任意位置”如果需要读取文件的一部分内容，它无疑是最好的选择。
 * long getFilePointer()：返回文件记录指针的当前位置
 * void seek(long pos)  ：将文件记录指针定位到pos位置
 *
 * 读取raw和assets文件夹下的文件
 * 菜鸟学院 IO流 http://www.runoob.com/java/java-files-io.html
 */
object FileUtil {

    private val TAG = this.javaClass.simpleName

    fun getTotalSpace(): Long {
        /*sd卡空间总大小*/
        if (filePermission()) {
            return Environment.getExternalStorageDirectory().totalSpace
        }
        return -1
    }

    fun getUsableSpace(context: Context? = null): Long {
        /*sd卡可用空间大小*/
        if (filePermission()) {
            return Environment.getExternalStorageDirectory().usableSpace
        }
        return -1
    }

    fun getSizeUnit(var0: Long): String {
        /*获取字节转换成KB MB GB 单位的字符串*/
        val var2 = DecimalFormat("###.00")
        return when {
            var0 < 1024L -> (var0).toString() + "bytes"
            var0 < 1048576L -> var2.format((var0.toFloat() / 1024.0f).toDouble()) + "KB"
            else -> when {
                var0 < 1073741824L -> var2.format((var0.toFloat() / 1024.0f / 1024.0f).toDouble()) + "MB"
                var0 > 0L -> var2.format((var0.toFloat() / 1024.0f / 1024.0f / 1024.0f).toDouble()) + "GB"
                else -> "error"
            }
        }
    }

    fun filePermission(): Boolean {
        /*判断Android是否具备文件读写权限*/
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            BKLog.e("请检查sd卡是否插入且具备读写权限")
            return false
        }
        return true
    }

    fun createNewFile(path: String?, dir: String?, fileName: String?): File {
        /*创建文件*/
        val file = File(path + File.separator + dir + File.separator + fileName)
        if (mkdirs(path, dir)) {
            if (!file.exists()) {
                BKLog.d(TAG, file.absolutePath + "文件创建状态：" + file.createNewFile())
            } else {
                BKLog.d(TAG, file.absolutePath + "文件已存在，文件大小：" + file.length() + "B")
            }
        }
        return file
    }

    fun mkdirs(path: String?, dir: String?): Boolean {
        /*创建目录*/
        if (TextUtils.isEmpty(path)) {
            return false
        }
        if (TextUtils.isEmpty(dir)) {
            return false
        }
        val dirFile = File(path + File.separator + dir)
        val mkdirsState: Boolean
        if (!dirFile.exists()) {
            mkdirsState = dirFile.mkdirs()
            if (mkdirsState) {
                BKLog.d(TAG, dirFile.absolutePath + "创建目录成功")
            } else {
                BKLog.e(TAG, dirFile.absolutePath + "创建目录失败")
            }
        } else {
            BKLog.d(TAG, dirFile.absolutePath + "目录已存在")
            mkdirsState = true
        }
        return mkdirsState
    }

    fun mergeFiles(outFile: File, inFile: File) {
        /* 合并文件
         * @param outFile 合成的文件
         * @param inFile 需要被合成的文件夹
         */
        val bufSize = 1024 * 4
        BKLog.d(TAG, "Merge " + inFile.absolutePath + "目录下的所有子文件，into " + "" + outFile.absolutePath)
        val outChannel = FileOutputStream(outFile).channel
        var fc: FileChannel? = null
        for (subFile in inFile.listFiles()) {  //获取inFile下的所有文件
            BKLog.d(TAG, "Merge 文件顺序:${subFile.name}")
            val charset = Charset.forName("utf-8")
            val dCoder = charset.newDecoder()
            val eCoder = charset.newEncoder()
            fc = FileInputStream(subFile).channel
            val bb = ByteBuffer.allocate(bufSize)
            val charBuffer = dCoder.decode(bb)
            val nbuBuffer = eCoder.encode(charBuffer)
            while (fc.read(nbuBuffer) != -1) {
                bb.flip()
                nbuBuffer.flip()
                outChannel.write(nbuBuffer)    // write failed: ENOSPC (No space left on device)
                bb.clear()
                nbuBuffer.clear()
            }
        }
        fc?.close()
        outChannel.close()
    }

    fun del(f: File) {
        /*递归删除文件*/
        val b = f.listFiles()
        if (b != null) {
            for (i in (0 until b.size)) {
                if (b[i].isFile) {
                    b[i].delete()
                    BKLog.d(TAG, "删除:${b[i].absoluteFile}")
                } else {
                    del(b[i])
                }
            }
        }
        f.delete()
    }
}
