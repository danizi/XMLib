package com.xm.lib.test

import org.junit.Test
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class CollectTest {

    @Test
    fun hashMapTest() {
        /**
         * 文章讲解 https://www.nowcoder.com/discuss/151172?type=1
         * 视频讲解
         *
         * 数组 + 链表 + 红黑树（jdk>7 当存储的数据 > 8链表就转化成红黑树）
         *
         * 初始容量
         * initialCapacity 必须是2的指数次幂
         *
         * 加载因子
         * loadFactor = 0.75
         *
         * 扩容阈值
         * threshold = loadFactor * initialCapacity 13
         */
        val hashMap = HashMap<String, String>()
        hashMap.put("1", "1")
        hashMap["1"] = "2"
        hashMap["3"] = "3"
        hashMap[""] = "3"

        for (s in hashMap.entries) {
            println("key : ${s.key} value : ${s.value}")
        }
    }

    @Test
    fun arrayListTest() {
        val list = ArrayList<Any>()
        list.add("1")
        list.add(Any())
        list.add("3")

        for (l in list) {
            println("$l")
        }

    }
}