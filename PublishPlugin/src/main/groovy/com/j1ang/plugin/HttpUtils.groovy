package com.j1ang.plugin

import com.mzlion.core.http.ContentType
import com.mzlion.easyokhttp.HttpClient
import groovy.json.JsonSlurper


import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * 与自定义PLugin进行参数传递
 */
public class HttpUtils {

    public static String uploadFile(String url, List<PgyerInfoExtension> apks) {
        for (PgyerInfoExtension apk in apks) {
            String responseData = HttpClient
                    .post(url)
                    .param("file", new File(apk.inputPath), apk.inputPath)
                    .param("_api_key", apk.pgyerKey)
                    .param("buildInstallType", apk.buildInstallType + "")
                    .param("buildPassword", apk.buildPassword)
                    .asString()
            //{"code":0,"message":"","data":{"buildKey":"24c6e8691a186b632d429811b52e2add","buildType":"2","buildIsFirst":"0","buildIsLastest":"1","buildFileKey":"ef58ec80b203a84217578049e678d7c6.apk","buildFileName":"app-debug.apk","buildFileSize":"4361190","buildName":"PublishAppPlugin","":"1.0","buildVersionNo":"1","buildBuildVersion":"16","buildIdentifier":"com.j1ang.publishappplugin","buildIcon":"fa11d8e72d80990cfb5b5b8619b875da","buildDescription":"","buildUpdateDescription":"","buildScreenshots":"","buildShortcutUrl":"0WHE","buildCreated":"2021-03-16 00:10:28","buildUpdated":"2021-03-16 00:10:28","buildQRCodeURL":"https:\/\/www.pgyer.com\/app\/qrcodeHistory\/972ac41d621d123c6b583c0759c160e3d86693f61ceef7ac8612d1d725f4de3e"}}
            print(responseData)
            def result = new JsonSlurper().parseText(responseData)
            if (result.code == 0) {
                Logger.getAnonymousLogger()
                print("""
                上传成功
                发布版本: ${result.data.buildVersion}
                蒲公英地址：${result.data.buildQRCodeURL}
""")
            }
            print(responseData)
        }

        return "aaa"

    }

    public static String postMsgJson(String url, String json) {

        String responseData = HttpClient
                .textBody(url)
                .json(json)
                .charset("utf-8")
                .asString();
        def result = new JsonSlurper().parseText(responseData)
        print(responseData)
        // {"errcode":0,"errmsg":"ok"}
        if (result.errcode == 0) {
            print("消息发送成功")
        } else {
            print("消息发送失败")
        }
        return "bbb"
    }


}