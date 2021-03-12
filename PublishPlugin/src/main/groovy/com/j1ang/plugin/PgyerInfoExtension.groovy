package com.j1ang.plugin
/**
 * 与自定义PLugin进行参数传递
 */
class PgyerInfoExtension {

    /**
     * 文件路径
     */
    String inputPath
    /**
     * 蒲公英个人id
     */
    String pgyerKey
    /**
     * (选填) 应用安装方式，值为(1,2,3，默认为1 公开安装)。1：公开安装，2：密码安装，3：邀请安装
     */
    Integer buildInstallType

    Integer buildUpdateDescription
    /**
     *(选填)  配合 buildInstallType =2 使用
     */
    Integer buildPassword


    PgyerInfoExtension() {

    }
}