package zzg.o2o.util;

public class PathUtil {
    private static String separator=System.getProperty("file.separator");
    public static String getImageBasePath(){
        String os=System.getProperty("os.name");
        String basePath="";
        if(os.toLowerCase().startsWith("win")){
            basePath="F:/myPictures";
        }else
            basePath="/home/gordon/Pictures/images";
        basePath=basePath.replace("/",separator);
        return basePath;
    }
    public static String getShopImagePath(long shopId){
        String imagePath="/upload/item/shop/"+shopId;
        return imagePath.replace("/",separator);
    }
}
