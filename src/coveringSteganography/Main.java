/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package coveringSteganography;

import Codes.COV295;
import Codes.Codes;
import PaletteImage.Palette;
import PaletteImage.PaletteImage;
import exception.MatrixInputException;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import matrix.Vector;

/**
 *
 * @author tebaomang
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    private static byte[] info;
    
    public static void main(String[] args) throws MatrixInputException, IOException {
        // TODO code application logic here
//        try{
//        
        
            Tester();
//        new Vector(9).populateCPT(3);
        
//        }catch(Exception e){
//            System.out.print(e.getMessage());
//        }
//        COV295.creator();
        
//        info = info();
//        reverseInfo();
//        infoExtracter2();
        System.exit(0);
    }
    
    
    public static void infoExtracter2(){
        
        FileDialog ChooseImage = new FileDialog(new Frame(), "Choose Secret Picture", FileDialog.LOAD);
        ChooseImage.setVisible(true);
        String infoName = ChooseImage.getFile();
        String infoPath = ChooseImage.getDirectory();
        
        File file = new File(infoPath+System.getProperty("file.separator")+infoName);
        byte[] result = new byte[(int)file.length()];
        try {
            InputStream input = null;
            try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while(totalBytesRead < result.length){
                    int bytesRemaining = result.length - totalBytesRead;
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
                    if (bytesRead > 0){
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
            } finally { input.close(); }
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) { }
        
        info = new byte[4*8+8*result.length];
        int offset = 0;
        byte[] length = Codes.int2Bin(result.length, 4*8);
        System.arraycopy(length, 0, info, offset, 4*8); offset += 4*8;
        for(int i=0; i<result.length;i++){
            byte[] b = Codes.int2Bin(result[i] & 0x0ff, 8);
            System.arraycopy(b, 0, info, offset, 8); offset += 8;
        }
        
        offset = 0;
        byte[] Len = new byte[4*8];
        System.arraycopy(info, offset, Len, 0, 4*8); offset += 4*8;
        int Leng = Codes.bin2Int(Len);
        result = new byte[Leng];
        for(int i=0; i<Leng;i++){
            byte[] b = new byte[8];
            System.arraycopy(info, offset, b, 0, 8); offset += 8;
            result[i] = (byte) Codes.bin2Int(b);
        }
        
        ChooseImage = new FileDialog(new Frame(), "Save Picture", FileDialog.SAVE);
        ChooseImage.setVisible(true);
        infoName = ChooseImage.getFile();
        infoPath = ChooseImage.getDirectory();
        
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(infoPath+System.getProperty("file.separator")+infoName));
                output.write(result);
            } finally {
                output.close();
            }
        }
        catch(FileNotFoundException ex){
        }
        catch(IOException ex){
        }
    }
    
    public static byte[] info() throws IOException{
        
        PaletteImage image = new PaletteImage();
        
        Palette palette = image.getPalette();
            
            byte[] result = new Vector(8*2*2+8+palette.getSize()*8*3+image.getHeight()*image.getWidth()*8).tobyte();
            int offset = 0;
            
            byte[] width = Codes.int2Bin(image.getWidth(), 8*2);
            System.arraycopy(width, 0, result, offset, 8*2); offset += 8*2;
            byte[] height = Codes.int2Bin(image.getHeight(), 8*2);
            System.arraycopy(height, 0, result, offset, 8*2); offset += 8*2;
            
            byte[] paletteSize = Codes.int2Bin(palette.getSize(),8);
            System.arraycopy(paletteSize, 0, result, offset, 8); offset += 8;
            
            System.out.println(palette.getSize());
            
            for(int i=0; i<palette.getSize();i++){
                byte[] red   = Codes.int2Bin(palette.getRed(i),8);
                System.arraycopy(red, 0, result, offset, 8); offset += 8;
            } for(int i=0; i<palette.getSize();i++){
                byte[] green = Codes.int2Bin(palette.getGreen(i),8);
                System.arraycopy(green, 0, result, offset, 8); offset += 8;
            } for(int i=0; i<palette.getSize();i++){
                byte[] blue  = Codes.int2Bin(palette.getBlue(i),8);
                System.arraycopy(blue, 0, result, offset, 8); offset += 8;
            }
            
            int count = 0;
            
            for(int i=0;i<image.getHeight();i++){
                for(int j=0; j<image.getWidth();j++){
                    count++;
//                        System.out.println("("+i+","+j+")="+image.getPixel(i, j));
                    byte[] pixel = Codes.int2Bin(image.getPixel(i, j),8);
                    System.arraycopy(pixel, 0, result, offset, 8); offset += 8;
                }
        }
        return result;
    }
    
    public static void reverseInfo() throws IOException{
        
        int offset = 0;
        
        byte[] widthA = new byte[8*2]; System.arraycopy(info, offset, widthA, 0, 8*2); offset += 8*2;
        byte[] heightA = new byte[8*2]; System.arraycopy(info, offset, heightA, 0, 8*2); offset += 8*2;
        int width = Codes.bin2Int(widthA); int height = Codes.bin2Int(heightA);
        
        byte[] paletteSize = new byte[8];
        System.arraycopy(info, offset, paletteSize, 0, 8); offset += 8;
        int size = Codes.bin2Int(paletteSize);
        
        System.out.println(size);
        
        byte[] reds = new byte[size];
        for(int i=0; i<size;i++){
            byte[] red   = new byte[8];
            System.arraycopy(info, offset, red, 0, 8); offset += 8;
            reds[i] = (byte) Codes.bin2Int(red);
        }
        
        byte[] greens = new byte[size];
        for(int i=0; i<size;i++){
            byte[] green   = new byte[8];
            System.arraycopy(info, offset, green, 0, 8); offset += 8;
            greens[i] = (byte) Codes.bin2Int(green);
        }
        
        byte[] blues = new byte[size];
        for(int i=0; i<size;i++){
            byte[] blue   = new byte[8];
            System.arraycopy(info, offset, blue, 0, 8); offset += 8;
            blues[i] = (byte) Codes.bin2Int(blue);
        }
        
        IndexColorModel colorModel = new IndexColorModel(8,size,reds,greens,blues);
        
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_BYTE_INDEXED,colorModel);
        WritableRaster raster = image.getRaster();
        for(int i=0;i<height;i++)
                for(int j=0; j<width;j++){
                    byte[] pixel = new byte[8];
                    System.arraycopy(info, offset, pixel, 0, 8); offset += 8;
                    int[] index = new int[1]; index[0]=Codes.bin2Int(pixel);
//                    System.out.println("("+i+","+j+")="+index[0]);
                    raster.setPixel(j, i, index);
                }
        
        FileDialog ChooseImage = new FileDialog(new Frame(), "Save Picture", FileDialog.SAVE);
        ChooseImage.setFile("result.gif");
        ChooseImage.setVisible(true);
        String saveTo=ChooseImage.getDirectory()+System.getProperty("file.separator")+ChooseImage.getFile();
        
        ImageIO.write(image,"gif",new File(saveTo));
    }
    
    public static void Tester() throws MatrixInputException{
        Random r = new Random();
        Vector inf = new Vector("00110");
        Vector p   = new Vector("010011010");
       
//        for(int j=0;j<1000;j++){
        
//            for(int i=0;i<inf.length();i++){
//                inf.set(i, (byte) r.nextInt(2));
//            } 

//            for(int i=0;i<p.length();i++){
//                p.set(i, (byte) r.nextInt(2));
//            } 
            
            System.out.println(inf.toString());
//            System.out.println(new Vector(Module3189.embed(p.tobyte(), inf.tobyte())).toString());
            System.out.println(new Vector(/*Module3189.extract(*/COV295.embedChanges(p.tobyte(), inf.tobyte())).toString());
            
            System.out.println("");
        
        
    }
    
    public static double PSNR() throws IOException, MatrixInputException{
        return new PaletteImage().PSNR(new PaletteImage());
    }
    
    
}
