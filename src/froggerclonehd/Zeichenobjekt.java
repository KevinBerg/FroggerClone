package froggerclonehd;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Zeichenobjekt{
    private int width;
    private int height;
    private int xPos;
    private int yPos;
    private String status; //Wenn diese Variable "fix" ist, so ist dieses Feld nicht begehbar.
    private Image image;
    
    /**
     * Setzt den Status des Objekt.
     * Der Status dient dazu Objekte zu erkennen,
     * die sich nicht bewegen und Hindernisse darstellen sollen.
     * @param status 
     */
    public void setStatus(String status){
        this.status = status;
    }
    
    /**
     * Gibt das Bild des Objekts zurück.
     * @return 
     */
    public Image getImage(){
        return this.image;
    }
    
    /**
     * Legt das Bild für das Objekt fest.
     * @param imagePath Bildpfad als String
     */
    public void setImage(String imagePath){
        this.image = Toolkit.getDefaultToolkit().getImage(imagePath); // Bild einlesen, auf diesem Wege ist es zum zeichnen bereits bereit
    }
    
    /**
     * Setzt das Bild für das Feld fest.
     * Nimmt als zweiten Parameter die Richtung entgegen.
     * Diese Methode wird genutzt, um ein anderes Bild zu laden, sobald
     * ein Objekt seine Richtung wechselt.
     * @param imagePath Bildpfad als String
     * @param rightwards Richtung des Objekts, 1 = rechts, -1 = links
     */
    public void setImage(String imagePath, int rightwards){
        if(rightwards == 1){
            int l  = imagePath.length();
            String rotatedPicPath = imagePath.substring(0, l-4) + "-r" + imagePath.substring(l-4);
            setImage(rotatedPicPath);
        } else {
            setImage(imagePath);
        }
    }
    
    /**
     * @return Gibt die Breite des Objekts zurück
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setzt die Breite des Objekts.
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return Gibt die Höhe des Objekts zurück
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setzt die Höhe des Objekts.
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return the xPos
     */
    public int getxPos() {
        return xPos;
    }

    /**
     * Setzt die X-Position des Objekts.
     * @param xPos
     */
    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the yPos
     */
    public int getyPos() {
        return yPos;
    }

    /**
     * Setzt die Y-Position des Objekts.
     * @param yPos the yPos to set
     */
    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
     
}
