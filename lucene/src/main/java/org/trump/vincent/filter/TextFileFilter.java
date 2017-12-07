package org.trump.vincent.filter;

import java.io.File;

/**
 * Created by Vincent on 2017/12/4 0004.
 */
public class TextFileFilter extends org.trump.vincent.filter.FileFilter{


    public boolean accept(File file){
        return accept(file,".txt");
    }
}
