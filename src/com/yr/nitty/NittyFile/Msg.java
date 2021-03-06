package com.yr.nitty.NittyFile;

public class Msg {
         //文件名
    private int fileNameLength;//文件名长度
    private String fileName;//文件名
    private long fileLength;//文件内容长度
        //文件夹
    private int filepathLength;//文件夹长度
    private String filepath;//代表相对路径的文件目录，如果不为null,则代表发送的是文件目录

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private Integer state;//除文件内容以外信息发送成功为ok

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
