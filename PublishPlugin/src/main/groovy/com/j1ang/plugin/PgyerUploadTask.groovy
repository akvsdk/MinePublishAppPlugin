package com.j1ang.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 与自定义PLugin进行参数传递
 */
class PgyerUploadTask extends  DefaultTask {

    PgyerUploadTask() {
        group = 'j1ang'
        description = 'publish your app into pgyer'
        dependsOn("build")
    }

    @TaskAction
    void  doAction(){
        def apkPath = project.fileTree("${project.buildDir}/outputs/apk/release/").find {
            return it.name.endsWith(".apk")
        }
        print 'hell word :'+apkPath

    }


}