package com.j1ang.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PublishAppPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("publishAppInfo", PublishAppInfoExtension.class)
        project.extensions.create("pgyerInfo", PgyerInfoExtension.class)
        project.extensions.create("workWxbot", WeChatBotExtension.class)
        project.tasks.create("publishApp", PgyerUploadTask.class)
        project.tasks.create("sendBotMsg", SendMsgToWechatTask.class)

    }
}