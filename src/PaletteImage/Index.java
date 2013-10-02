/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PaletteImage;

/**
 *
 * @author tebaomang
 */
public class Index {
    private int R, G, B;
    
    /* Constructor */
    public Index(int red, int green, int blue){
        R = red;
        G = green;
        B = blue;
    }

    /* Getter */
    public int getB() {
        return B;
    }

    public int getG() {
        return G;
    }

    public int getR() {
        return R;
    }
    
    /* Distances */
    public double distance(Index that){ // Khoang cach - Euclid
        return Math.sqrt(distanceSquare(that));
    }
    
    public double distanceSquare(Index that){// Binh phuong khoang cach - Euclid
        return (Math.pow(R-that.R,2)+Math.pow(G-that.G,2)+Math.pow(B-that.B,2));
    }
}
