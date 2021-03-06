package com.yr.nitty.nittyfile1;

import java.io.File;
import java.io.Serializable;

public class FileContent implements Serializable {//必须序列化
    private File file;// 文件
    private String fileName;// 文件名
    private int StartPos;// 开始位置
    private byte[] bytes;// 文件字节数组
    private int endPos;// 偏移的大小

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStartPos() {
        return StartPos;
    }

    public void setStartPos(int startPos) {
        StartPos = startPos;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }
}
