package com.xm.lib.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.ClipboardManager
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DecimalFormat


@Deprecated("")
class CommonUtil {

    companion object {
        /**
         * 定制文本颜色值
         * @filter 约束的字符 务必填写一个空格" "
         * @souce 需要被定制的字符串
         * @color 颜色值
         */
        fun getSpannableColorSpanString(filter: String, souce: String?, color: Int): CharSequence? {
            val indexOfs = getIndexOf(filter, souce)
            val spannableString = SpannableString(souce)
            var index = 0
            while (index <= (indexOfs.size - 2)) {
                //文字背景颜色
                spannableString.setSpan(ForegroundColorSpan(color), indexOfs[index], indexOfs[index + 1] + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                index += 2
            }
            return spannableString
        }

        private fun getIndexOf(indexOfstr: String, soure: String?): ArrayList<Int> {
            val indexOf = ArrayList<Int>()
            for (index in 0..(soure?.length!! - 1)) {
                val d = soure[index]
                if (d == indexOfstr.toCharArray()[0]) {
                    indexOf.add(index)
                }
            }
            return indexOf
        }

        /**
         * 复制内容到剪切板上
         */
        fun copyToClipboard(context: Context, text: String) {
            //如要自由复制可将TextView等控件设置android:textIsSelectable="true"
            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            //clip.getText(); // 粘贴
            clip.text = text // 复制
        }

        /**
         * 将号码转化星号* 例如：15074770709 150****0709
         */
        fun phoneAsterisk(p: String?): String {
            if (TextUtils.isEmpty(p)) {
                return "000****0000"
            }
            val phone = StringBuilder(p)
            phone.replace(3, 7, "****")
            return phone.toString()
        }

        /**
         * 将号码分割 例如：15074770709 +86-150-7477-0709
         */
        fun phoneSeparate(p: String?, area: String?, separate: String?): String {
            val phone: StringBuilder = StringBuilder(p)
            phone.insert(3, "-")
            phone.insert(8, "-")
            phone.insert(0, "$area ")
            return phone.toString()
        }

        /**
         * 转化单位大小
         */
        fun readableFileSize(size: Long): String {
            if (size <= 0) return "0"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
            return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
        }

        /**
         * 动态设置marging值
         */
        fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
            if (v.layoutParams is ViewGroup.MarginLayoutParams) {
                val p = v.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(l, t, r, b)
                v.requestLayout()
            }
        }

        /**
         * view不可点击
         */
        fun clickable(view: View?, clickable: Boolean) {
            view?.isClickable = clickable
            view?.isFocusable = clickable
            view?.isFocusableInTouchMode = false
        }

        /**
         * bitmap转Base64
         */
        fun bitmapToBase64(bitmap: Bitmap?): String? {
            var result: String? = null
            var baos: ByteArrayOutputStream? = null
            try {
                if (bitmap != null) {
                    baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                    baos.flush()
                    baos.close()

                    val bitmapBytes = baos.toByteArray()
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return result
        }

        /**
         * Base64b转itmap
         */
        fun base64ToBitmap(base64Data: String): Bitmap {
            val bytes = Base64.decode(base64Data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }
}