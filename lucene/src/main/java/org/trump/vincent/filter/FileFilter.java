package org.trump.vincent.filter;

import java.io.File;

/**
 * Created by Vincent on 2017/12/4 0004.
 */
public abstract class FileFilter implements java.io.FileFilter {

    public static boolean accept(File file,String ...suffix){
        if(file == null)
            return false;
        return accept(file.getName(),suffix);
    }
    public static boolean accept(String file,String ...suffix){
        if(file.isEmpty()||file.equals(""))
            return false;
        String lowerCap = file.toLowerCase();
        if(suffix !=null &&suffix.length>0){
            for (String sts : suffix){
                if(lowerCap.endsWith(sts))
                    return true;
            }
        }
        return false;
    }
}
