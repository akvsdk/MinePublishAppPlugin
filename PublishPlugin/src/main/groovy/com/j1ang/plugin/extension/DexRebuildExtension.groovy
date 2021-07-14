package com.j1ang.plugin.extension
/**
 * 与自定义PLugin进行参数传递
 */
class DexRebuildExtension {

    /**
     *  工具包绝对路径
     */
    String rebuildToolPath

    /**
     *  dex 名字
     */
    String dexName

    /**
     * 偏移量
     */
    Integer off

    /**
     * 生成的文件名
     */
    String lastName

    /**
     * mapping 对应位置
     */
    String mappingPath

    /**
     * 需混淆的类名
     */
    String clzName

    /**
     * 目标地址
     */
    String destPath

    DexRebuildExtension() {

    }
}