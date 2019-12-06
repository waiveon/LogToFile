package com.sweetsound.logtofile;

import android.content.Context;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LogToFile {
    private String mFilePath;

    /** Log 파일 정보 설정
     * @param filePath 파일 위치
     */
    public LogToFile(String filePath) {
        mFilePath = filePath;
    }

    public LogToFile(Context context) {
        mFilePath = context.getFilesDir().getAbsolutePath();
    }

    /** Log 추가
     * @param logMessage 로그 메시
     */
    public void wirte(String fileName, String logMessage) throws IOException {
        synchronized (LogToFile.class) {
            File logFile = createFile(fileName);

            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm:ss");
            logMessage = sdf.format(new Date()) + " :  " + logMessage + "\r\n\r\n";

            RandomAccessFile raf = null;

            try {
                raf = new RandomAccessFile(logFile, "rw");
                raf.seek(raf.length());
                raf.write(logMessage.getBytes());

            } catch(IOException ex) {
                throw ex;
            } finally {
                if (raf != null) {
                    raf.close();
                }
            }
        }
    }

    /** 로그 파일 읽기
     * @return 읽은 로그 파일 내용
     */
    public String read(String fileName) throws IOException {
        StringBuffer readBuff = new StringBuffer();

        File logFile = createFile(fileName);

        String read = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(logFile));

            while ((read = br.readLine()) != null) {
                readBuff.append(read);
                readBuff.append("\r\n");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return readBuff.toString();
    }

    /** 로그 파일 삭제
     * @return true - 삭제 성공<br>false - 삭제 실패
     */
    public boolean delete(String fileName) {
        return createFile(fileName).delete();
    }

    /** 설정한 Dir에 있는 파일 목록
     * @return 파일 목록
     */
    public ArrayList<String> list() {
        return new ArrayList<String>(Arrays.asList(new File(mFilePath).list()));
    }

    /** 설정한 Dir에 있는 파일 목록
     * @return 파일 목록
     */
    public ArrayList<File> listFiles() {
        return new ArrayList<File>(Arrays.asList(new File(mFilePath).listFiles()));
    }

    private File createFile(String fileName) {
        return new File(mFilePath + File.separator + fileName);
    }

    public ArrayList<String> getFileNameList() {
        List<File> files = listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();

        for (File file : files) {
            fileNames.add(file.getName());
        }

        return fileNames;
    }
}