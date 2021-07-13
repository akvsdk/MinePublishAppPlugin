package com.j1ang.plugin

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.file.FileReader
import cn.hutool.core.io.file.FileWriter
import cn.hutool.core.util.ZipUtil
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 与自定义PLugin进行参数传递
 */
class ReBuildTask extends DefaultTask {

    ReBuildTask() {
        description = 'rebuild your app dex '
        group = 'hopemobi'
//        dependsOn("assembleRelease")
    }

    @TaskAction
    void doAction() {
        def apkPath = project.fileTree("${project.buildDir}/outputs/apk/release/").find {
            return it.name.endsWith(".apk")
        }
        if (apkPath == null) {
            print("文件目录异常,请检查上传文件")
            return
        }
        // 1. 解压apk 拿到class2.dex
        def apkUnzipFile = new File("${project.buildDir}/outputs/apk/release/unzipFile")
        def romDir = new File("${project.buildDir}/outputs/apk/release/dexrom")
        def newDir = new File("${project.buildDir}/outputs/apk/release/dexnew")
        FileUtil.del(romDir)
        ZipUtil.unzip(apkPath, apkUnzipFile)
        // 2.拿到 apk 中解压出来的 classes2.jar 文件
//        def toolsDir = project.extensions.dexRebuild.rebuildToolPath
        def baksmali = "D:/SdkRebuildTools/baksmali-2.4.0.jar"
        def smali = "D:/SdkRebuildTools/smali-2.4.0.jar"
        def dexPath = "${project.buildDir}/outputs/apk/release/bin_beat.1"
        def dexPath2 = "${project.buildDir}/outputs/apk/release/bin_beat.1.1"
        // 3. 使用工具,处理apk
        def cmd = "java -jar ${baksmali} d ${apkUnzipFile}\\classes2.dex -o ${romDir}"
        cmd.execute().waitForProcessOutput(System.out, System.err)
//        execCmd(cmd)
        // 4. 截取mapping 字符串

        def mapPath = getClzPath()

        def smaliFile = mapPath.split("/")

        if (smaliFile.size() < 1) {
            print("获取混淆文件异常,请检查")
            return
        }
        //5. 获取需要生成.smali
        def newDex = newDir.absolutePath + '/' + mapPath
        print(newDex)
        FileUtil.touch(newDex)
        FileUtil.copy(romDir.absolutePath + '/' + mapPath, newDex, true)
        //6. smali 转dex
        cmd = "java -jar ${smali} a ${newDir} -o ${dexPath2}"
        cmd.execute().waitForProcessOutput(System.out, System.err)
        FileUtil.del(romDir)
        FileUtil.del(newDir)
        FileUtil.del(apkUnzipFile)

        //7.生成随机字节破坏dex完整
        Random rd = new Random()
        byte[] arr = new byte[256]
        rd.nextBytes(arr)
        System.out.println(arr)
        def writer = new FileWriter(dexPath)
        def reader = new FileReader(dexPath2)
        writer.append(arr, 0, 128)
        def lastFile = writer.append(reader.readBytes(), 0, reader.readBytes().length)
        println("dex生成成功,位于${lastFile.absolutePath}")
    }


    void execCmd(cmd) {
        project.exec {
            executable = 'cmd'
            args = ['java -jar ', cmd]
        }

    }

    String getClzPath() {
        FileReader fileReader = new FileReader("${project.buildDir}/proguardMapping.txt");
        def mapping = fileReader.readLines()
        def clzName
        mapping.each {
            if (it.contains('UnZipMainrRx')) {
                clzName = it
            }
        }
        if (clzName == null) {
            print("获取混淆失败,请检查后重试")
            FileUtil.del(romDir)
            return ''
        }
        def methodName = clzName.split("->")
        String aa = methodName[1]
        aa = aa.trim()
        aa = "${aa}%smali"
        aa = aa.replace(".", "/")
        aa = aa.replace(":", "")
        aa = aa.replace("%", ".")
        println(aa)
        return aa
    }


}