package com.j1ang.plugin

import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskAction

import java.awt.datatransfer.DataFlavor

/**
 * 与自定义PLugin进行参数传递
 */
class SendMsgToWechatTask extends DefaultTask {

    SendMsgToWechatTask() {
        description = 'send messgae  to wechatbot'
        group = 'hopemobi'
        dependsOn("publishApp")
    }


    @TaskAction
    void doAction() {
        def webhook = project.extensions.workWxbot.webhook
        def botUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${webhook}&debug=1"
        def contentText = project.extensions.workWxbot.content
        def type = project.extensions.workWxbot.type
        def builder
        if (type == 1) {
            builder = new JsonBuilder({
                msgtype "text"
                text {
                    content contentText
                }
            })
        } else if (type == 2) {
            builder = new JsonBuilder({
                msgtype "markdown"
                markdown {
                    content contentText
                }
            })
        }
        print(builder.toPrettyString())
        HttpUtils.postMsgJson(botUrl, builder.toString())

    }


}