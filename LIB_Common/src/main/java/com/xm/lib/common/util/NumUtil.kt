package com.xm.lib.common.util

import java.text.DecimalFormat

class NumUtil {
    companion object {
        /**
         * 带小数点的数值
         * @param obj 毫秒
         */
        fun getDecimalPoint(obj: Any): String {
            val df = DecimalFormat("00.00").format(obj)
            if (df.startsWith("0")) {
                return df.replaceRange(0, 1, "")
            }
            return df
        }
    }

}