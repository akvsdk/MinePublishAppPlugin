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
        dependsOn("assembleRelease")
    }

    @TaskAction
    void doAction() {
        def log = project.logger
        log.error "========================";
        log.error "生成加密Dex .... ";
        log.error "========================";

        def apkPath = project.fileTree("${project.buildDir}/outputs/apk/release/").find {
            return it.name.endsWith(".apk")
        }
        if (apkPath == null) {
            log.error "apk目录异常,请检查Release包";
            return
        }
        def rebuildToolPath = project.extensions.dexRebuild.rebuildToolPath
        def dexName = project.extensions.dexRebuild.dexName
        def off = project.extensions.dexRebuild.off
        def lastName = project.extensions.dexRebuild.lastName
        def mappingPath = project.extensions.dexRebuild.mappingPath
        def clzName = project.extensions.dexRebuild.clzName
        def destPath = project.extensions.dexRebuild.destPath
        if (null == mappingPath) {
            log.error "apk目录异常,请检查Release包"
            return
        }
        if (null == rebuildToolPath) {
            log.error "工具包不能为空"
            return
        }
        if (null == dexName) {
            log.error "需处理的dex不能为空"
            return
        }
        if (null == off) {
            log.error "加密偏移量不能为空"
            return
        }
        if (null == mappingPath) {
            log.error "mapping 不能为空"
            return
        }
        if (null == clzName) {
            log.error "混淆类名不能为空"
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
        def baksmali = "${rebuildToolPath}/baksmali-2.4.0.jar"
        def smali = "${rebuildToolPath}/smali-2.4.0.jar"
        def dexPath = "${project.buildDir}/outputs/apk/release/${lastName}"
        def dexPath2 = "${project.buildDir}/outputs/apk/release/${lastName}.1"

        // 3. 使用工具,处理apk
        def cmd = "java -jar ${baksmali} d ${apkUnzipFile}${dexName} -o ${romDir}"
        cmd.execute().waitForProcessOutput(System.out, System.err)
//        execCmd(cmd)


        // 4. 截取mapping 字符串

        def mapPath = getClzPath(clzName, mappingPath)

        def smaliFile = mapPath.split("/")

        if (smaliFile.size() < 1) {
            log.error "获取混淆文件异常,请检查";
            return
        }

        //5. 获取需要生成.smali
        def newDex = newDir.absolutePath + '/' + mapPath
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
        byte[] arr = new byte[1024]
        rd.nextBytes(arr)
        def writer = new FileWriter(dexPath)
        def reader = new FileReader(dexPath2)
        writer.append(arr, 0, off)
        def lastFile = writer.append(reader.readBytes(), 0, reader.readBytes().length)
        println("dex生成成功 \n ${lastFile.absolutePath}")


        //8.移动覆盖到指定路径

        if (null == destPath) {
            log.error "destpath 为空,不移动文件";
            return
        }

        File destfile = new File(destPath)
        FileUtil.copy(lastFile, destfile, true)

    }


    void execCmd(cmd) {
        project.exec {
            executable = 'cmd'
            args = ['java -jar ', cmd]
        }

    }

    String getClzPath(String clzZZ, String path) {
        FileReader fileReader = new FileReader(path);
        def mapping = fileReader.readLines()
        def clzName
        mapping.each {
            if (it.contains(clzZZ)) {
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