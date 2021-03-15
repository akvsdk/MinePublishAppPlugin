package com.j1ang.plugin

import groovy.json.JsonBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskAction
import org.json.JSONObject

/**
 * 与自定义PLugin进行参数传递
 */
class SendMsgToWechatTask extends DefaultTask {

    SendMsgToWechatTask() {
        description = 'send messgae  to wechatbot'
        //    dependsOn("publishApp")
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

        println(HttpUtils.postMsgJson(botUrl, builder.toString()))

    }


    def getGitLog() {
        return 'git log --pretty=format:"%Cred%h%Creset -%C(yellow)%d%Cblue %s %Cgreen(%cd) %C(bold blue)<%an>%Creset" -5 --date=format:%Y-%m-%d_%H:%M'.execute().text
    }

    def getBuildType() {
        Gradle gradle = getGradle()
        String args = gradle.getStartParameter().getTaskRequests().toString()
        println(args)
        if (args.contains("assembleRelease")) {
            return "Release"
        } else if (args.contains("assembleDebug")) {
            return "Debug"
        }
        return "Debug"
    }

}