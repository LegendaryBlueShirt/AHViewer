package application;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
 
public class Unpk3 {
   
    private static final char[] copyBuffer = new char[0x1000];
    private static int position, loop,control,offset;
    private static char code;
   
    public static void decompress(File file, OutputStream os) {
        try {
            decompress(new RandomAccessFile(file,"r"), os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
   
    public static synchronized void decompress(RandomAccessFile raf, OutputStream outputBuffer)
    {
        //Since we can't dynamically allocate a byte array we'll do two passes over the data.
        //the first pass will simply determine the size of our output buffer.
        //2014 LBS to the rescue, fuck that noise ^
       
        position = 0;
        do {
            try{
            control = raf.readUnsignedByte() | (raf.readUnsignedByte()<<8);
            for(int n =15;n >=0;n--) {
                code = (char) (raf.readUnsignedByte() | (raf.readUnsignedByte()<<8));
               
                if((control & 0x8000) == 0) {
                    outputBuffer.write((byte)(code&0xff));
                    outputBuffer.write((byte)(code>>>8));
                    copyBuffer[(position++)%copyBuffer.length] = code;
                } else {
                    loop = (code>>>12) + 2;
                    offset = position - ((code & 0xfff) + 1);
                    offset = offset%copyBuffer.length;
                   
                    for(int m = 0;m < loop;m++) {
                        outputBuffer.write((byte)(copyBuffer[offset%copyBuffer.length]&0xff));
                        outputBuffer.write((byte)(copyBuffer[offset%copyBuffer.length]>>>8));
                        copyBuffer[(position++)%copyBuffer.length] = copyBuffer[(offset++)%copyBuffer.length];
                    }
                }
                control = control<<1;
            }
            } catch(EOFException eof) {
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        } while(true);
    }
}