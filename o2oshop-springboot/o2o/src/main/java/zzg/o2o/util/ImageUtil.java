package zzg.o2o.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import zzg.o2o.dto.ImageHolder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {
    private static String basePath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r=new Random();
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
        String realFileName=getRandomFileName();
        String extension=getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension;
        File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnail.getImage()).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    private static void makeDirPath(String targetAddr) {
        String realFileParentPath= PathUtil.getImgBasePath()+targetAddr;
        File dirPath=new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }

    }

    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private static String getRandomFileName() {
        int randomNum=r.nextInt(89999)+10000;
        String nowTimeStr=sDateFormat.format(new Date());
        return nowTimeStr+nowTimeStr;
    }

    public static void deleteFileOrPath(String storePath) {
        File fileOrPath=new File(PathUtil.getImgBasePath()+storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[]=fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }

    }

    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        String realFileName=getRandomFileName();
        String extension=getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension;
        File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnail.getImage()).size(337,640).watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basePath+"/watermark.jpg")),0.25f)
                    .outputQuality(0.9f).toFile(dest);
        }catch (IOException e){
            throw new RuntimeException("创建缩略图片失败"+e.toString());
        }
        return relativeAddr;
    }
}
