package com.j1ang.plugin

import com.mzlion.core.http.ContentType
import com.mzlion.easyokhttp.HttpClient
import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject

import java.util.concurrent.TimeUnit

/**
 * 与自定义PLugin进行参数传递
 */
public class HttpUtils {

    public static String uploadFile(String url, List<PgyerInfoExtension> apks) {
        def result
        for (PgyerInfoExtension apk in apks) {
            String responseData = HttpClient
                    .post(url)
                    .param("file", new File(apk.inputPath), apk.inputPath)
                    .param("_api_key", apk.pgyerKey)
                    .param("buildInstallType", apk.buildInstallType + "")
                    .param("buildPassword", apk.buildPassword)
                    .asString()
            result
            print(responseData)
        }

    }

    public static String postMsgJson(String url, String json) {

        String responseData = HttpClient
                .textBody(url)
                .json(json)
                .charset("utf-8")
                .asString();

        print(responseData)
    }

}