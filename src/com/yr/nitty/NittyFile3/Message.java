package com.yr.nitty.NittyFile3;

import java.io.File;

public class Message {
    //�ȷ��ļ���������,�ļ�����    ====     �ļ�������,�ļ���,�ļ����ݳ���,û�з�������
    //�����ļ�
    private int nameLength;//���ֳ���
    private String name;//����
    private long contentLength;//���ݳ���
    //�����ļ���
    private int fileDirectoryLength;
    private String fileDirectory;//�������·�����ļ�Ŀ¼�������Ϊnull,������͵����ļ�Ŀ¼
    private Integer state;//���ļ�����������Ϣ���ͳɹ�Ϊok
    private File file;
    //���ݹ����ܷ���������
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
