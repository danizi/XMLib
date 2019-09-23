package com.xm.lib.common

import com.xm.lib.common.util.ReflectUtil
import org.junit.Test

class ReflectUtilTest {

    class Bean {
        var field1: String = ""
        var field2: Double = 1.0
        var field3: Int = 1
        var field4: ArrayList<String> = ArrayList()
    }

    @Test
    fun reflect() {
        val bean = Bean()
        System.out.println("获取反射的字段 : ${ReflectUtil.getFiledName(bean)}")
        System.out.println("获取反射类信息 : ${ReflectUtil.getFiledsInfo(bean).toString()}")
        System.out.println("获取指定字段值 : ${ReflectUtil.getFieldValueByName<Double>(bean, "field2")}")
    }
}