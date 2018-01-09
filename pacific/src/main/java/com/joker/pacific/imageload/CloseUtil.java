package com.joker.pacific.imageload;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by joker on 18-1-9.
 */

public class CloseUtil {
    public static void close(Closeable closeable){
        if(closeable != null){
            try{
                closeable.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
