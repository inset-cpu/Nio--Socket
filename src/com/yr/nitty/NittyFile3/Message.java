package com.yr.nitty.NittyFile3;

import java.io.File;

public class Message {
    //先发文件夹名长度,文件夹名    ====     文件名长度,文件名,文件内容长度,没有发送内容
    //代表文件
    private int nameLength;//名字长度
    private String name;//名字
    private long contentLength;//内容长度
    //代表文件夹
    private int fileDirectoryLength;
    private String fileDirectory;//代表相对路径的文件目录，如果不为null,则代表发送的是文件目录
    private Integer state;//除文件内容以外信息发送成功为ok
    private File file;
    //内容过大不能放在这里面
    public long getContentLength() {
    return contentLength;
    }

    public void setContentLength(long contentLength) {
    this.contentLength = contentLength;
    }
    public int getNameLength() {
    return nameLength;
    }
    public void setNameLength(int nameLength) {
    this.nameLength = nameLength;
    }
    public String getName() {
    return name;
    }
    public void setName(String name) {
    this.name = name;
    }
    public String getFileDirectory() {
    return fileDirectory;
    }
    public void setFileDirectory(String fileDirectory) {
    this.fileDirectory = fileDirectory;
    }
    public int getFileDirectoryLength() {
    return fileDirectoryLength;
    }
    public void setFileDirectoryLength(int fileDirectoryLength) {
    this.fileDirectoryLength = fileDirectoryLength;
    }
    public Integer getState() {
    return state;
    }
    public void setState(Integer state) {
    this.state = state;
    }
    public File getFile() {
    return file;
    }
    public void setFile(File file) {
    this.file = file;
    }
}
