package com.xm.lib.common.util

import java.text.DecimalFormat

class NumUtil {
    companion object {
        /**
         * 带小数点的数值
         */
        fun getDecimalPoint(obj: Any): String {
            return DecimalFormat("00.00").format(obj)
        }
    }

}