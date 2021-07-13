package com.j1ang.plugin

import com.j1ang.plugin.extension.DexRebuildExtension
import com.j1ang.plugin.extension.PgyerInfoExtension
import com.j1ang.plugin.extension.PublishAppInfoExtension
import com.j1ang.plugin.extension.WeChatBotExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublishAppPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("publishAppInfo", PublishAppInfoExtension.class)
        project.extensions.create("pgyerInfo", PgyerInfoExtension.class)
        project.extensions.create("workWxbot", WeChatBotExtension.class)
        project.extensions.create("dexRebuild", DexRebuildExtension.class)
        project.tasks.create("publishApp", PgyerUploadTask.class)
        project.tasks.create("sendBotMsg", SendMsgToWechatTask.class)
        project.tasks.create("rebuildDex", ReBuildTask.class)

    }
}