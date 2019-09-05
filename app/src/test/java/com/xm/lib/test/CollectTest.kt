package com.xm.lib.test

import org.junit.Test

class CollectTest {

    @Test
    fun hashMapTest() {
        /**
         * 数组 + 链表 + 红黑树（jdk>7）
         *
         * 初始容量
         * initialCapacity 必须是2的的指数次幂
         *
         * 加载因子
         * loadFactor=0.75
         *
         * 扩容阈值
         * threshold = loadFactor * initialCapacity 13
         */


        val hashMap = HashMap<String, String>()
        hashMap.put("1", "1")
        hashMap["2"] = "2"
        hashMap["3"] = "3"

        for (s in hashMap.entries) {
            println("key : ${s.key} value : ${s.value}")
        }
    }


}