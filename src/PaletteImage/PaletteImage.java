/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PaletteImage;

import Codes.*;
import exception.MatrixInputException;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author tebaomang
 */
public class PaletteImage {
    
    /* Input constant to indicate the method used for steganography process */
    public static final int HAMMINGCODE = 0, CPTMETHOD = 1, GOLAYCODE = 2, COV295CODE=3, CPTEMETHOD = 4, MOD3189 = 5, MOD3158 = 6;
    
    /* Input constant to indicate no input */
    public static final int NONE = 0;
    
    private String imageName;
    private String imagePath;
    
    private BufferedImage image;
    private WritableRaster imageRaster;
    
    private int[] Next;
    private byte[] Value;
    
    private int width,height;
    
    private Palette palette;
    
    /* Contructor */
    public PaletteImage() throws IOException{
        /* Choose image */
        FileDialog ChooseImage = chooseImage("");
        
        /* Get name and path */
        imagePath = ChooseImage.getDirectory() + System.getProperty("file.separator");
        imageName = ChooseImage.getFile();
        
        /* Read image */
        image = ImageIO.read(new File(imagePath+imageName));
        imageRaster = image.getRaster();
        
        /* Read palette */
        palette = new Palette(image);
        
        /* Generate Rho true */
        Next  = palette.nearestMap();
        Value = palette.value(Next);
        
        /* Read image dimensions */
        width = image.getWidth();
        height = image.getHeight();
    }
    
    /* Constructor with dialog notify what image the user should choose */
    public PaletteImage(String name) throws IOException{
        /* Choose image */
        FileDialog ChooseImage = chooseImage(name);
        
        /* Get name and path */
        imagePath = ChooseImage.getDirectory() + System.getProperty("file.separator");
        imageName = ChooseImage.getFile();
        
        /* Read image */
        image = ImageIO.read(new File(imagePath+imageName));
        imageRaster = image.getRaster();
        
        /* Read palette */
        palette = new Palette(image);
        
        /* Generate Rho true */
        Next  = palette.nearestMap();
        Value = palette.value(Next);
        
        /* Read image dimensions */
        width = image.getWidth();
        height = image.getHeight();
    }
    
    /* Constructor that read a specified image */
    public PaletteImage(String path, String name) throws IOException{
        /* Get name and path */
        imagePath = path + System.getProperty("file.separator");
        imageName = name;
        
        /* Read image */
        image = ImageIO.read(new File(imagePath+imageName));
        imageRaster = image.getRaster();
        
        /* Read palette */
        palette = new Palette(image);
        
        /* Generate Rho true */
        Next  = palette.nearestMap();
        Value = palette.value(Next);
        
        /* Read image dimensions */
        width = image.getWidth();
        height = image.getHeight();
    }
    
    /* Calculate the PSNR value between two images */
    public double PSNR(PaletteImage that) throws MatrixInputException{
        
        if (width!=that.width&&height!=that.height) throw new MatrixInputException();
        
        double MSE=0;
        
        /* MSE = SUM(d^2)/(2*w*h) */
        
        for(int i=0;i<height;i++)
            for(int j=0; j<width;j++)
                if(getPixel(i, j)!=that.getPixel(i, j)) 
                    MSE = MSE + getIndex(getPixel(i, j)).distanceSquare(getIndex(that.getPixel(i, j)));
        
        MSE=MSE/(3*width*height);
        
        return 10*Math.log10(255*255/MSE);
    }
    
    public Palette getPalette(){
        return new Palette(palette);
    }

    public int Next(int i) {
        return Next[i];
    }

    public byte Value(int i) {
        return Value[i];
    }

    public WritableRaster getRaster() {
        return imageRaster;
    }
    
    /* Change the index of a pixel */
    public void setPixel(int row, int collumn, int index){
        int[] i = new int[1];
        i[0]=index;
        imageRaster.setPixel(collumn, row, i);
    }
    
    /* Return the index number of a pixel */
    public int getPixel(int row, int collumn){
        int[] index = new int[1];
        index = imageRaster.getPixel(collumn, row, index);
        return index[0];
    }
    
    /* Return the index of a pixel */
    public Index getIndex(int i){
        return palette.getIndex(i);
    }
    
    /* Flip a pixel: replace by the Next index */
    public void flipPixel(int row, int collumn){
        int index = getPixel(row, collumn);
        setPixel(row, collumn, Next[index]);
    }
    

    public BufferedImage getBuffered() {
        return image;
    }

    public String getFileName() {
        return imageName;
    }

    public String getFilePath() {
        return imagePath;
    }
    
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    /* Choose an image bmp or gif */
    private FileDialog chooseImage(String name){
        FileDialog ChooseImage = new FileDialog(new Frame(), "Choose Picture: "+name, FileDialog.LOAD);
        ChooseImage.setFilenameFilter(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name){
                return (name.endsWith(".bmp") || name.endsWith(".gif"));
            }
        });
        ChooseImage.setVisible(true);
        return ChooseImage;
    }
    
    /* Save the image */
    public String saveTo() throws IOException{
        FileDialog ChooseImage = new FileDialog(new Frame(), "Save Picture", FileDialog.SAVE);
        ChooseImage.setFile(imageName);
        ChooseImage.setVisible(true);
        String saveTo=ChooseImage.getDirectory()+System.getProperty("file.separator")+ChooseImage.getFile();
        ImageIO.write(image,"gif",new File(saveTo));
        return saveTo;
    }
    
    /* Embe the $changes in to the $offset-th pixel block length $range of the image*/
    public void embed(int offset, int range, byte[] changes){
        for(int i=0;i<changes.length;i++)
            if (changes[i]==1){
                int[] pixel = byteToPixel(offset, range, i);
                flipPixel(pixel[0],pixel[1]);
            }
    }
    
    /* Get coordinates of $position-th point of $offset-th block pixel length $range */
    public int[] byteToPixel(int offset, int range, int position){
        
        int[] result = new int[2];
        offset++;
        position++;
        
        /* Row order */
        int offsetPixel=((offset-1)*range+position);
        result[0]=(int)Math.ceil(offsetPixel/(double)width)-1;
        /* Collumn order */
        if (offsetPixel%width==0) result[1]=width-1;
        else result[1]=offsetPixel-width*(int)Math.floor(offsetPixel/width)-1;
        
        return result;
    }
    
    /* Get array of value of all pixels in $offset-th block pixel length $range */
    public byte[] getBlock(int offset, int range){
        byte[] result = new byte[range];
        for (int i=0; i<range; i++){
            int[] pixel = byteToPixel(offset, range, i);
            result[i]=Value[getPixel(pixel[0],pixel[1])];
        }
        return result;
    }
    
    /* Number of blocks will be used for hiding process */
    public int ammountOfBlocks(int range){
        return width*height/range;
    }
    
    public static int[] methodProp(int blockSize, int method){
        int[] result = {0,0};
        switch (method){
            case HAMMINGCODE:
                result[0] = blockSize;
                result[1] = Hamming.plainSize(blockSize);
                break;
            case CPTMETHOD:
                result[0] = CPT.embedBitLength(blockSize);
                result[1] = blockSize;
                break;
            case GOLAYCODE:
                result[0] = 11;
                result[1] = 23;
                break;
            case COV295CODE:
                result[0] = 5;
                result[1] = 9;
                break;
            case CPTEMETHOD:
                result[0] = CPTE.embedBitLength(blockSize);
                result[1] = blockSize;
                break;
            case MOD3189:
                result[0] = 9;
                result[1] = 18;
                break;
            case MOD3158:
                result[0] = 8;
                result[1] = 15;
                break;
        }
        return result;
    }
    
    public static byte[] methodChanges(byte[] block, byte[] inf, int blockSize, int method) throws MatrixInputException{
        int plainBlockSize = methodProp(blockSize, method)[1];
        byte[] changes = new byte[plainBlockSize];
        switch (method){
                case HAMMINGCODE:
                    changes = Hamming.embedChangesU(block, inf);
                    break;
                case CPTMETHOD:
                    changes = CPT.embedChanges(block, inf);
                    break;
                case GOLAYCODE:
                    changes = Golay.embedChanges(block, inf);
                    break;
                case COV295CODE:
                    changes = COV295.embedChanges(block, inf);
                    break;
                case CPTEMETHOD:
                    changes = CPTE.embedChanges(block, inf);
                    break;
                case MOD3189:
                    changes = Module3189.embedChanges(block, inf);
                    break;
                case MOD3158:
                    changes = Module3158.embedChanges(block, inf);
                    break;
        }
        return changes;
    }
    
    public static byte[] methodExtract(byte[] block, int blockSize, int method) throws MatrixInputException{
        
        /* Get the size of block of information and pixels*/
        int[] methodProp = methodProp(blockSize, method);
        int plainBlockSize = methodProp[1];
        
        byte[] result = new byte[plainBlockSize];
        switch(method){
                case HAMMINGCODE:
                    result = Hamming.extractU(block);
                    break;
                case CPTMETHOD:
                    result = CPT.extract(block);
                    break;
                case GOLAYCODE:
                    result = Golay.extract(block);
                    break;
                case COV295CODE:
                    result = COV295.extract(block);
                    break;
                case CPTEMETHOD:
                    result = CPTE.extract(block);
                    break;
                case MOD3189:
                    result = Module3189.extract(block);
                    break;
                case MOD3158:
                    result = Module3158.extract(block);
                    break;
            }
        return result;
    }
    
    /* Embeding method */
    public void Embed(byte[] info, int blockSize, int method) throws MatrixInputException{
        
        /* Get the size of block of information and pixels*/
        int[] methodProp = methodProp(blockSize, method);
        int infoBlockSize = methodProp[0];
        int plainBlockSize = methodProp[1];
        
        /* Extend original information for data hiding */
        info = Codes.extendByte(info, infoBlockSize, plainBlockSize);
        
        /* Divide into blocks and handle each block */
        for(int i=0; i<info.length/infoBlockSize;i++){
            
            byte[] inf = Codes.stripByte(i,infoBlockSize,info);
            byte[] block = getBlock(i,plainBlockSize);
            
            /* Find the changes needed to made */
            byte[] changes = methodChanges(block, inf, blockSize, method);
            
            /* Apply the changes */
            embed(i,plainBlockSize,changes);
        }
    }
    
    /* Embeding with specified PSNR */
    public int PSNREmbed(byte[] info, int blockSize, int method, double PSNR) throws IOException, MatrixInputException{
            
        /* Get the size of block of information and pixels */
        int[] methodProp = methodProp(blockSize, method);
        int infoBlockSize = methodProp[0];
        int plainBlockSize = methodProp[1];
        
        double MSE=0;
        int offset = 0;
        
        /* Variable used for controlling the point where calculated PSNR
         * passes the inputted PSNR. The point right before that is
         * the result.
         */
        double nextPSNR;
        
        /* First changes, initailize the MSE and the next PSNR */
        byte[] changes = BlockEmbed(info, blockSize, method, offset);
        for(int i=0;i<changes.length;i++)
            if (changes[i]==1){
                int[] pixel = byteToPixel(offset, plainBlockSize, i);
                int orderIndex = getPixel(pixel[0], pixel[1]);
                MSE = MSE + getIndex(orderIndex).distanceSquare(getIndex(Next[orderIndex]));
            }
        nextPSNR = 10*Math.log10(255*255/(MSE/(3*width*height)));
        offset++;
        
        /* Keep calculating until no place left to hide or
         * found the MSE needed (PSNR): where nextPSNR passes
         * the inputted PSNR.
         */
        while(nextPSNR>=PSNR && offset<ammountOfBlocks(plainBlockSize) && offset<info.length/infoBlockSize){
            
            this.embed(offset-1, plainBlockSize, changes);
            
            changes = BlockEmbed(info, blockSize, method, offset);
            for(int i=0;i<changes.length;i++)
                if (changes[i]==1){
                    int[] pixel = byteToPixel(offset, plainBlockSize, i);
                    int orderIndex = getPixel(pixel[0], pixel[1]);
                    MSE = MSE + getIndex(orderIndex).distanceSquare(getIndex(Next[orderIndex]));
                }
            
            nextPSNR = 10*Math.log10(255*255/(MSE/(3*width*height)));
            offset++;
        }
        
        if (offset>=ammountOfBlocks(plainBlockSize) || offset>=info.length/infoBlockSize) return -1;
        
        return (offset-1)*infoBlockSize;
    }
    
    /* Handling embeding block by block*/
    public byte[] BlockEmbed(byte[] info, int blockSize, int method, int offset) throws MatrixInputException{
        
        /* Get the size of block of information and pixels */
        int[] methodProp = methodProp(blockSize, method);
        int infoBlockSize = methodProp[0];
        int plainBlockSize = methodProp[1];
        
        /* Cut information block*/
        byte[] inf = Codes.stripByte(offset, infoBlockSize, info);
        
        /* Cut pixels block*/
        byte[] block = getBlock(offset,plainBlockSize);
        
        /* Find the changes needed to be made */
        byte[] changes = methodChanges(block, inf, blockSize, method);
        return changes;
    }
    
    /* Extracting method */
    public byte[] Extract(int blockSize, int method) throws MatrixInputException{
        
        /* Get the size of block of information and pixels */
        int[] methodProp = methodProp(blockSize, method);
        int infoBlockSize = methodProp[0];
        int plainBlockSize = methodProp[1];
        
        byte[] result = new byte[(width*height/plainBlockSize)*infoBlockSize];
        for(int i=0; i<width*height/plainBlockSize;i++){
            byte[] block = getBlock(i,plainBlockSize);
            byte[] temp = methodExtract(block, blockSize, method);
            System.arraycopy(temp, 0, result, i*infoBlockSize, infoBlockSize);
        }
        return result;
    }
    
}
