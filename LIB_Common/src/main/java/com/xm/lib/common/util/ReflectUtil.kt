package com.xm.lib.common.util

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

    private fun getMethodName(name: String): String {
        // 把一个字符串的第一个字母大写、效率是最高的、
        val items = name.toByteArray()
        items[0] = (items[0].toChar() - 'a' + 'A'.toInt()).toByte()
        return String(items)
    }

    /**
     * 获取对象“公开属性”的信息列表
     * @param o 需要被反射的实例
     * @return 返回属 性名称 - 属性数据类型 - 属性值 列表
     */
     fun getFiledsInfo(o: Any): ArrayList<Info> {
        val info = ArrayList<Info>()
        val cls = o.javaClass
        val fields = cls.declaredFields
        for (field in fields) {
            val m = o.javaClass.getMethod("get" + getMethodName(field.name))
            val s = m.invoke(o)
            info.add(Info(field.name, field.genericType.toString(), s))
            //System.out.println(field.name + "-" + field.genericType.toString() + "-" + s)
        }
        return info
    }

    /**
     * 通过属性名称获取值
     * @param o 需要反射的对象
     * @param name 属性名称
     * @return 属性值
     */
    fun <T> getFieldValueByName(o: Any, name: String): T {
        val cls = o.javaClass
        val field = cls.getField(name)
        val m = o.javaClass.getMethod("get" + getMethodName(field.name))
        return m.invoke(o) as T
    }

    /**
     * 对象相关信息
     */
    class Info(var name: String, var genericType: String, var value: Any?){
        override fun toString(): String {
            return "Info(name='$name', genericType='$genericType', value=$value)"
        }
    }

    /**
     * 测试时使用
     */
    class Bean {
        var field1: String = ""
        var field2: Double = 1.0
        var field3: Int = 1
        var field4: ArrayList<String> = ArrayList()
    }
}