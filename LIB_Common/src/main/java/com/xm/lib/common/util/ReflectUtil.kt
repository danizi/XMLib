package com.xm.lib.common.util

import java.util.*

/**
 * 反射工具类
 */
object ReflectUtil {

    /**
     * 获取“公开属性”名称列表
     * @param o 需要被反射的实例
     * @return 字段列表
     */
    fun getFiledName(o: Any): ArrayList<String> {
        val names = ArrayList<String>()
        val cls = o.javaClass
        val fields = cls.declaredFields
        for (field in fields) {
            names.add(field.name)
        }
        return names
    }

    /**
     * 获取对象“公开属性”的信息列表
     * @param o 需要被反射的实例
     * @return 返回属 性名称 - 属性数据类型 - 属性值 列表
     */
    fun getFiledInfo(o: Any): ArrayList<Info> {
        val info = ArrayList<Info>()
        val cls = o.javaClass
        if (cls.superclass.name != "java.lang.Object") {
            throw IllegalAccessException("不能继承父类")
        }
//        val fields = ArrayList<Field>()
//        while (cls != null) {
//            fields.addAll(cls.declaredFields.asList())
//            if (cls.superclass != null) {
//                cls = cls.superclass as Class<Any>
//            } else {
//                break
//            }
//        }
        val fields = cls.declaredFields
        for (field in fields) {
            val m = cls.getMethod("get" + getMethodName(field.name))
            val s = m.invoke(o)
            info.add(Info(field.name, field.genericType.toString(), s))
            //System.out.println(field.name + "-" + field.genericType.toString() + "-" + s)
        }
        return info
    }

    private fun getMethodName(name: String): String {
        // 把一个字符串的第一个字母大写、效率是最高的、
        val items = name.toByteArray()
        items[0] = (items[0].toChar() - 'a' + 'A'.toInt()).toByte()
        return String(items)
    }

    /**
     * 通过属性名称获取值
     * @param o 需要反射的对象
     * @param name 属性名称
     * @return 属性值
     */
    fun <T> getFieldValueByName(o: Any, name: String): T {
        val cls = o.javaClass
        val field = cls.getDeclaredField(name)
        val m = cls.getMethod("get" + getMethodName(field.name))
        return m.invoke(o) as T
    }

    fun <T> setFieldValueByName(o: Any, name: String, value: T) {
        val cls = o.javaClass
        val field = cls.getDeclaredField(name)
        field.isAccessible = true
        field.set(o, value as T)

    }

    fun newInstance(cls: Class<Any>): Any {
        if (cls == null) {
            throw NullPointerException("cls is null")
        }
        return cls.newInstance()
    }

    /**
     * 对象相关信息
     */
    class Info(var name: String, var genericType: String, var value: Any?) {
        override fun toString(): String {
            return "Info(name='$name', genericType='$genericType', value=$value)"
        }
    }

}