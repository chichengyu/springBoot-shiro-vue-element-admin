package com.shiro.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    // ================================ 文件上传与删除 ============================
    /**
     * 上传文件(单)
     * @param file
     * @param path
     * @return
     * @throws IOException
     */
    public static String upload(MultipartFile file,String path) throws IOException {
        String originalFilename = file.getOriginalFilename();// 原文件名称
        File dest = new File(path + "/" + originalFilename);
        if (!dest.getParentFile().exists()){
            dest.setWritable(true);
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return originalFilename;
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public static boolean delete(String filePath){
        File file = new File(filePath);
        if (file.isFile()){
            return file.delete();
        }
        return false;
    }

    // ================================ 视频与音频处理 ============================
    /**
     * 视频转码
     * @param ffmpegEXEPath  ffmpeg.exx 绝对路径
     * @param videoInputPath 视频绝对路径
     * @param videoOutputPath 转换后的新视频的绝对路径
     * @throws IOException
     */
    public static void converVideo(String ffmpegEXEPath,String videoInputPath,String videoOutputPath) throws IOException {
        // ffmpeg.exe -i 01.mp4 -y xin.mp4 // -y 是强制覆盖
        List<String> commod = new ArrayList<>();
        commod.add(ffmpegEXEPath);
        commod.add("-i");
        commod.add(videoInputPath);
        commod.add("-y");
        commod.add(videoOutputPath);

        // ProcessBuilder 执行一条命令
        ProcessBuilder processBuilder = new ProcessBuilder(commod);
        Process process = processBuilder.start();
        // ffmpeg 进行音频与视频整合或转码时或产生很多流与碎片文件，为了防止占用主线程卡主，所以使用流的方式
        InputStream inputStream = process.getErrorStream();// 获取字节流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);// 字节流转字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);// 吧字符流放到字符流缓冲流中读取
        String line = "";
        while ((line = bufferedReader.readLine()) != null){};
        // 关闭流
        if (bufferedReader != null){
            bufferedReader.close();
        }
        if (inputStreamReader != null){
            inputStreamReader.close();
        }
        if (inputStream != null){
            inputStream.close();
        }
    }

    /**
     * 视频与音频合并
     * @param ffmpegEXEPath  ffmpeg.exx 绝对路径
     * @param videoInputPath 视频绝对路径
     * @param mp3InputPath 音频绝对路径
     * @param videoSeconds 合并后的视频时长（秒）
     * @param videoOutputPath 合并后的新视频的绝对路径
     * @throws IOException
     */
    public static void converMergreVideoMp3(String ffmpegEXEPath, String videoInputPath, String mp3InputPath, double videoSeconds, String videoOutputPath) throws IOException {
        // ffmpeg.exe -i 01.mp4 -i bgm.mp3 -t 41 -y xin.mp4 // -y 是强制覆盖
        List<String> commod = new ArrayList<>();
        commod.add(ffmpegEXEPath);
        commod.add("-i");
        commod.add(videoInputPath);
        commod.add("-i");
        commod.add(mp3InputPath);
        commod.add("-t");
        commod.add(String.valueOf(videoSeconds));
        commod.add("-y");
        commod.add(videoOutputPath);

        // ProcessBuilder 执行一条命令
        ProcessBuilder processBuilder = new ProcessBuilder(commod);
        Process process = processBuilder.start();
        // ffmpeg 进行音频与视频整合或转码时或产生很多流与碎片文件，为了防止占用主线程卡主，所以使用流的方式
        InputStream inputStream = process.getErrorStream();// 获取字节流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);// 字节流转字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);// 吧字符流放到字符流缓冲流中读取
        String line = "";
        while ((line = bufferedReader.readLine()) != null){};
        // 关闭流
        if (bufferedReader != null){
            bufferedReader.close();
        }
        if (inputStreamReader != null){
            inputStreamReader.close();
        }
        if (inputStream != null){
            inputStream.close();
        }
    }

    /**
     * ffmpeg 截图视频封面（注意：截图时，目录必须存在，截图并不会自动创建目录）
     * @param ffmpegEXEPath ffmpeg.exx 绝对路径
     * @param videoPath 视频绝对路径
     * @param videoCoverOutputPath 保存视频封面的绝对路径
     */
    public static void converVideoCover(String ffmpegEXEPath,String videoPath,String videoCoverOutputPath) throws IOException {
        // 判断目录是否存在
        File file = new File(videoCoverOutputPath);
        if (!file.getParentFile().exists()){
            file.setWritable(true);
            file.getParentFile().mkdirs();
        }
        // ffmpeg.exe -ss 00:00:01 -y -i 0001.mp4 -vframes 1 0001.jpg // ffmpeg 截图视频封面 ，-vframes 1 表示截取一帧
        List<String> commod = new ArrayList<>();
        commod.add(ffmpegEXEPath);
        commod.add("-ss");
        commod.add("00:00:01");// 默认截图视频 01 秒的一帧
        commod.add("-y");
        commod.add("-i");
        commod.add(videoPath);
        commod.add("-vframes");
        commod.add("1");// 默认截图一帧
        commod.add(videoCoverOutputPath);

        // 执行 cmd 命令
        ProcessBuilder processBuilder = new ProcessBuilder(commod);
        Process process = processBuilder.start();
        // ffmpeg 进行音频与视频整合或转码时或产生很多流与碎片文件，为了防止占用主线程卡主，所以使用流的方式
        InputStream inputStream = process.getErrorStream();// 获取字节流
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);// 字节流转字符流
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);// 吧字符流放到字符流缓冲流中读取
        String line = "";
        while ((line = bufferedReader.readLine()) != null){};
        // 关闭流
        if (bufferedReader != null){
            bufferedReader.close();
        }
        if (inputStreamReader != null){
            inputStreamReader.close();
        }
        if (inputStream != null){
            inputStream.close();
        }
    }

    public static void main(String[] args) {
        // 测试
        String ffmpegEXE = "G:\\ffmpeg\\bin\\ffmpeg.exe";
        String videoInputPath = "G:\\ffmpeg\\bin\\01.mp4";
        String videoOutputPath = "G:\\ffmpeg\\bin\\01.avi";
        String mp3InputPath = "G:\\ffmpeg\\bin\\bgm.mp3";
        String videoCoverPath = "G:\\ffmpeg\\bin\\001\\0001.jpg";
        String conver = "G:/video-upload/1230681055802642432/cover/wx993172f7da9acf1f.jpg";
        try {
            //converVideo(ffmpegEXE,videoInputPath,videoOutputPath);// 视频格式转换
            //converMergreVideoMp3(ffmpegEXE,videoInputPath,mp3InputPath,7,videoOutputPath);// 视频与音频整合
            converVideoCover(ffmpegEXE,videoInputPath,conver);// 截图视频封面
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
