package com.yr.nitty.NittyFile;

public class Msg {
         //�ļ���
    private int fileNameLength;//�ļ�������
    private String fileName;//�ļ���
    private long fileLength;//�ļ����ݳ���
        //�ļ���
    private int filepathLength;//�ļ��г���
    private String filepath;//�������·�����ļ�Ŀ¼�������Ϊnull,������͵����ļ�Ŀ¼

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private Integer state;//���ļ�����������Ϣ���ͳɹ�Ϊok

    public int getFileNameLength() {
        return fileNameLength;
    }

    public void setFileNameLength(int fileNameLength) {
        this.fileNameLength = fileNameLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getFilepathLength() {
        return filepathLength;
    }

    public void setFilepathLength(int filepathLength) {
        this.filepathLength = filepathLength;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "fileNameLength=" + fileNameLength +
                ", fileName='" + fileName + '\'' +
                ", fileLength=" + fileLength +
                ", filepathLength=" + filepathLength +
                ", filepath='" + filepath + '\'' +
                '}';
    }
}
