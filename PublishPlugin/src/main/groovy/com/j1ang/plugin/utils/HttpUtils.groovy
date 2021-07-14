package com.j1ang.plugin.utils


import cn.hutool.http.HttpUtil
import com.j1ang.plugin.extension.PgyerInfoExtension
import groovy.json.JsonSlurper

/**
 * 与自定义PLugin进行参数传递
 */
public class HttpUtils {

    public static String uploadFile(String url, List<PgyerInfoExtension> apks) {
        for (PgyerInfoExtension apk in apks) {

            HashMap<String, Object> paramMap = new HashMap<>()
//文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
            paramMap.put("file", new File(apk.inputPath))
            paramMap.put("_api_key", apk.pgyerKey)
            paramMap.put("buildInstallType", apk.buildInstallType + "")
            paramMap.put("buildPassword", apk.buildPassword)

            String responseData = HttpUtil.post(url, paramMap)
            //{"code":0,"message":"","data":{"buildKey":"24c6e8691a186b632d429811b52e2add","buildType":"2","buildIsFirst":"0","buildIsLastest":"1","buildFileKey":"ef58ec80b203a84217578049e678d7c6.apk","buildFileName":"app-debug.apk","buildFileSize":"4361190","buildName":"PublishAppPlugin","":"1.0","buildVersionNo":"1","buildBuildVersion":"16","buildIdentifier":"com.j1ang.publishappplugin","buildIcon":"fa11d8e72d80990cfb5b5b8619b875da","buildDescription":"","buildUpdateDescription":"","buildScreenshots":"","buildShortcutUrl":"0WHE","buildCreated":"2021-03-16 00:10:28","buildUpdated":"2021-03-16 00:10:28","buildQRCodeURL":"https:\/\/www.pgyer.com\/app\/qrcodeHistory\/972ac41d621d123c6b583c0759c160e3d86693f61ceef7ac8612d1d725f4de3e"}}
            print(responseData)
            def result = new JsonSlurper().parseText(responseData)
            if (result.code == 0) {
                print("""
上传成功
发布版本: ${result.data.buildVersion}
蒲公英QR地址：${result.data.buildQRCodeURL}
""")
            }
        }

        return "aaa"

    }

    public static String postMsgJson(String url, String json) {

        String responseData = HttpUtil.post(url, json)

        def result = new JsonSlurper().parseText(responseData)
        println(responseData)
        // {"errcode":0,"errmsg":"ok"}
        if (result.errcode == 0) {
            println("消息发送成功")
        } else {
            println("消息发送失败")
        }
        return "bbb"
    }


}