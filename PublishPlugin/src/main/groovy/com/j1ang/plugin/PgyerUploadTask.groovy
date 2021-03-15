package com.j1ang.plugin


import org.gradle.api.DefaultTask
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskAction
import org.json.JSONObject

import java.util.concurrent.TimeUnit

/**
 * 与自定义PLugin进行参数传递
 */
class PgyerUploadTask extends DefaultTask {

    PgyerUploadTask() {
        description = 'publish your app into pgyer'
        group = 'hopemobi'
        dependsOn("assembleDebug")
    }

    @TaskAction
    void doAction() {
        def apiUrl = "https://www.pgyer.com/apiv2/app/upload"
        def apkPath = project.fileTree("${project.buildDir}/outputs/apk/debug/").find {
            return it.name.endsWith(".apk")
        }
        if (apkPath == null) {
            print("文件目录异常,请检查上传文件")
            return
        }
        def inputPath = project.extensions.pgyerInfo.inputPath
        def pgyerKey = project.extensions.pgyerInfo.pgyerKey
        def buildInstallType = project.extensions.pgyerInfo.buildInstallType
        def buildUpdateDescription = project.extensions.pgyerInfo.buildUpdateDescription
        def buildPassword = project.extensions.pgyerInfo.buildPassword
        // project.extensions.pgyerInfo.inputPath = apkPath
        PgyerInfoExtension info = new PgyerInfoExtension()
        info.buildPassword = buildPassword
        info.pgyerKey = pgyerKey
        info.buildInstallType = buildInstallType
        info.buildUpdateDescription = buildUpdateDescription
        info.inputPath = apkPath;
        List<PgyerInfoExtension> list = new ArrayList<PgyerInfoExtension>()
        list.add(info)
        def jsonMap = HttpUtils.uploadFile(apiUrl, list)
        print(getGitLog())
    }


    void execCmd(cmd) {
        project.exec {
            commandLine 'curl'
            executable 'bash'
            args '-c', cmd
        }

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