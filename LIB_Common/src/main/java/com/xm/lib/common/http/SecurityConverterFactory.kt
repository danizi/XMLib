package com.xm.lib.common.http

//import com.caobug.wind.core.ProxyHelp
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class SecurityConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, Any>? {
        val delegate = retrofit!!.nextResponseBodyConverter<Any>(this, type!!, annotations!!)
        return Converter<ResponseBody, Any> { body ->
            //            val cipherText = ProxyHelp(null).json(body.string())
//            val clearBody = ResponseBody.create(body.contentType(), cipherText)
//            delegate.convert(clearBody)
        }
    }
}