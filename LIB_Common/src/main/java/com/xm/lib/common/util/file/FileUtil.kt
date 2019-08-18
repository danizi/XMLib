package com.xm.lib.common.util.file

import android.os.Environment
import com.xm.lib.common.log.BKLog
import com.xm.lib.common.util.unit.SpaceUnit
import java.io.*
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.text.DecimalFormat


object FileUtil : IFileAndroid(), IFileSize, IFileCommon, IFileZip {

    const val debug = true

    fun println(msg: String) {
        if (debug) {
            System.out.println("FileUtil : $msg")
        }
    }

    override fun getSizeUnit(size: Long, unit: SpaceUnit?): String {
        val var2 = DecimalFormat("###.00")
        return when {
            size < 1024L -> (size).toString() + "bytes"
            size < 1048576L -> var2.format((size.toFloat() / 1024.0f).toDouble()) + "KB"
            else -> when {
                size < 1073741824L -> var2.format((size.toFloat() / 1024.0f / 1024.0f).toDouble()) + "MB"
                size > 0L -> var2.format((size.toFloat() / 1024.0f / 1024.0f / 1024.0f).toDouble()) + "GB"
                else -> "error"
            }
        }
    }

    override fun copy(resource: File?, target: File?): Boolean {
        if (resource == null) {
            throw NullPointerException("copy failure,resource is null")
        }

        if (!resource.exists()) {
            println("copy failure,源文件不存在")
            return false
        }

        if (!target?.exists()!!) {
            createNewDirs(target)
        }

        var tempTarget: File?
        if (resource.isDirectory) {
            val resourceChild = resource.listFiles() ?: return false
            for (res in resourceChild) {
                tempTarget = File(target.absolutePath + File.separator + res.name)
                if (res.isDirectory) {
                    copy(res, tempTarget)
                } else {
                    createNewFile(tempTarget)
                    copyFile(tempTarget, res)
                }
            }
            createNewDirs(target)
        } else {
            tempTarget = File(target.absolutePath + File.separator + resource.name)
            createNewFile(tempTarget)
            copyFile(tempTarget, resource)
        }
        return true
    }

    private fun copyFile(target: File, res: File) {
        FileUtil.println(target.absolutePath)
        FileUtil.println(res.absolutePath)
        val bos = BufferedOutputStream(FileOutputStream(target))
        val bis = BufferedInputStream(FileInputStream(res))
        var length: Int
        val buf = ByteArray(1024)
        try {
            while (true) {
                length = bis.read(buf)
                if (length == -1) {
                    break
                }
                bos.write(buf, 0, length)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bos.close()
            bis.close()
        }
    }

    override fun move(resource: File?, target: File?): Boolean {
        if (resource == null) {
            throw NullPointerException("move failure,resource is null")
        }
        if (copy(resource, target)) {
            delAll(resource)
            return true
        }
        return false
    }

    override fun createNewDirs(file: File?): Boolean {
        if (file == null) {
            throw NullPointerException("createNewDirs failure,file is null")
        }
        val dirs = file.absolutePath.split(File.separator)
        var dir = ""
        var index = 0
        while (index < dirs.size) {
            dir += File.separator + dirs[index]
            val tempFile = File(dir)
            if (tempFile.exists() && tempFile.isDirectory) {
                index++
                continue
            }
            if (tempFile.mkdirs()) {
                println("${tempFile.absolutePath} 创建成功")
            } else {
                println("${tempFile.absolutePath} 创建失败")
                return false
            }
            index++
        }
        return true
    }

    override fun createNewFile(file: File?): Boolean {
        if (file == null) {
            throw NullPointerException("createNewFile failure,file is null")
        }

        if (file.exists()) {
            println("${file.absolutePath} 文件已存在")
            return true
        }

        //创建目录操作，首先先将文件的目录创建出来
        //为什么要逐级创建，因为在某些android系统上面直接使用 mkdirs 创建多级目录会失败，所以才“逐级创建”
        val path = file.absolutePath.split(File.separator)
        var dir = ""
        for (i in 0..(path.size - 2)) {
            dir += File.separator+ path[i]
        }

        //创建文件
        try {
            if (!file.exists() && createNewDirs(File(dir))) {
                val isCreateNewFile = file.createNewFile()
                if (isCreateNewFile) {
                    println("${file.absolutePath} 创建成功")
                } else {
                    println("${file.absolutePath} 创建失败")
                }
                return isCreateNewFile
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return false
    }

    override fun delAll(f: File?): Boolean {
        if (f == null) {
            throw NullPointerException("delAllFile failure , file is null")
        }
        val child = f.listFiles() ?: return false

        for (c in child) {
            if (c.isFile) {
                c.delete()
                println("删除文件 : ${c.absolutePath}")
            } else {
                delAll(c)
            }
        }
        f.delete()
        println("删除文件夹 : ${f.absolutePath}")
        return true
    }

    override fun del(f: File?): Boolean {
        if (f == null) {
            throw NullPointerException("delFile failure , file is null")
        }
        if (f.exists()) {
            return f.delete()
        }
        return false
    }

    override fun mergeFiles(resource: File?, target: File?): Boolean {
        try {
            val bufSize = 1024 * 4
            println("***********************************")
            println("注意     : 切割文件一定注意顺序 ~ ")
            println("合并     : ${resource?.absolutePath}下文件")
            val outChannel = FileOutputStream(target).channel
            var fc: FileChannel? = null
            for (subFile in resource?.listFiles()!!) {  //获取inFile下的所有文件
                println("文件顺序 : ${subFile.name}")
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
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun unZipFolder() {

    }

    override fun zipFolder() {

    }

}

/**
 * java 8 在线API ：http://www.matools.com/api/java8
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
abstract class IFileAndroid {
    /**
     * 获取手机sd卡空间总大小
     * @return -1 获取空间失败
     */
    fun getPhoneTotalSpace(): Long {
        if (filePermission()) {
            return Environment.getExternalStorageDirectory().totalSpace
        }
        return -1
    }

    /**
     * 获取手机sd卡可用大小
     * @param -1 获取空间失败
     */
    fun getPhoneUsableSpace(): Long {
        if (filePermission()) {
            return Environment.getExternalStorageDirectory().usableSpace
        }
        return -1
    }

    /**
     * 判断Android是否具备文件读写权限
     */
    fun filePermission(): Boolean {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            BKLog.e("请检查sd卡是否插入且具备读写权限")
            return false
        }
        return true
    }
}

interface IFileSize {
    /**
     * 字节转 KB MB GB 单位
     * @param
     */
    fun getSizeUnit(size: Long, unit: SpaceUnit? = null): String
}

interface IFileCommon {

    /**
     * 文件复制
     */
    fun copy(resource: File?, target: File?): Boolean

    /**
     * 文件移动
     */
    fun move(resource: File?, target: File?): Boolean

    /**
     * 创建目录
     */
    fun createNewDirs(file: File?): Boolean

    /**
     * 创建文件,即使文件目录不存在也自动创建
     */
    fun createNewFile(file: File?): Boolean

    /**
     * 删除所有文件
     */
    fun delAll(f: File?): Boolean

    /**
     * 删除指定文件或者文件夹，但是如果文件夹中有子文件则无法删除，需使用delAllFile(...)方法
     */
    fun del(f: File?): Boolean

    /**
     * 文件合并
     * @param resource 需要被合并的文件夹
     * @param target  合并之后的文件
     */
    fun mergeFiles(resource: File?, target: File?): Boolean
}

interface IFileZip {
    /**
     * 解压
     */
    fun unZipFolder()

    /**
     * 压缩
     */
    fun zipFolder()
}