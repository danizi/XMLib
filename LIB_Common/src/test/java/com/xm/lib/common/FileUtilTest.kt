package com.xm.lib.common

import com.xm.lib.common.util.file.FileUtil
import org.junit.Test
import java.io.File

class FileUtilTest {

    @Test
    fun createNewFile() {
        val createFile = File("E:/dir1/dir2/file.text")
        println("${createFile.absolutePath} 创建状态 : ${FileUtil.createNewFile(createFile)}")
    }

    @Test
    fun delAll() {
        val delAllFile = File("E:/dir1/dir2/file.text")
        val delDir = File("E:/dir1")
        println("${delAllFile.absolutePath} 删除文件状态 : ${FileUtil.delAll(delDir)}")
    }

    @Test
    fun mergeFiles() {

        val resourcePath = "E:/resource"
        val targetPath = "E:/target.txt"

        val resource = File("$resourcePath")
        val target = File(targetPath)

        val merge1 = File("$resourcePath/res1.txt")
        val merge2 = File("$resourcePath/res2.txt")
        FileUtil.createNewFile(merge1)
        FileUtil.createNewFile(merge2)
        merge1.outputStream().write("合并文件1".toByteArray())
        merge2.outputStream().write("合并文件2".toByteArray())

        FileUtil.println("合并状态 : ${FileUtil.mergeFiles(resource, target)}")
    }

    @Test
    fun copy() {
        FileUtil.println("复制状态 :${FileUtil.copy(File("E:\\test\\json.zip"), File("E:\\copyTargetResource"))}")
    }

    @Test
    fun move() {
        FileUtil.println("移动状态 :${FileUtil.copy(File("E:\\test\\json.zip"), File("E:\\moveTargetResource"))}")
    }
}