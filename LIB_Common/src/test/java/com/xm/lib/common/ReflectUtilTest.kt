package com.xm.lib.common

import android.app.Person
import com.xm.lib.common.util.ReflectUtil
import org.junit.Test

class ReflectUtilTest {

    open class Parent {
        var name = ""
    }

    class Bean /*: Parent() */ {
        var field1: String = ""
        var field2: Double = 1.0
        var field3: Int = 1
        var field4: ArrayList<String> = ArrayList()

        override fun toString(): String {
            return "Bean(field1='$field1', field2=$field2, field3=$field3, field4=$field4)"
        }
    }

    @Test
    fun reflect() {
        val bean = Bean()
        System.out.println("获取反射的字段 : ${ReflectUtil.getFiledName(bean)}")
        System.out.println("获取反射类信息 : ${ReflectUtil.getFiledInfo(bean).toString()}")
        System.out.println("获取指定字段值 : ${ReflectUtil.getFieldValueByName<Double>(bean, "field2")}")

        val reflectBean = ReflectUtil.newInstance(Bean::class.java as Class<Any>)
        ReflectUtil.setFieldValueByName(reflectBean, "field3", 2019)
        System.out.println("设置修改指定字段 : $reflectBean.toString()")


    }
}