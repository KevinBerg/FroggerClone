package froggerclonehd;

import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Figur extends Zeichenobjekt implements Observer {

    private int speed; //Geschwindigkeit mit der sich die Figur bewegt
    private int rightwards; //Gibt die Richtung der Figur an in die sie sich bewegt. Darf 1 oder -1 sein.
    private Rectangle bounding ; //Boundingbox

    int resolution = XMLReader.getInstance().getResolution(); //Auflösung aus der config.xml
    
    @Override
    public void update(Observable o, Object arg) {} //Updatemethode wird in den erbenden Klassen überschrieben.

    /**
     * Legt die Geschwindigkeit der Figur fest.
     * Standardgeschwindigkeit ist 5-10 (leicht).
     * @param speed Die Geschwindigkeit
     */
    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    /**
     * Gibt die Geschwindigkeit der Figur zurück.
     * @return 
     */
    public int getSpeed(){
        return this.speed;
    }

    /**
     * Setzt das Figurobjekt zurück auf das Spielfeld, falls es sich außerhalb befindet.
     */
    public void setBackToField()
    {
        if( getxPos() >= resolution ) { //Figur ist rechts rausgegangen
            this.setxPos(0);
        }
        else if(getxPos() <= - resolution / Spielfeld.getInstance().fields) { //Figur ist links rausgegangen.
            this.setxPos(resolution);
        }
    }
    
    /**
     * Berechnet die Boundingbox der Figur.
     */
    public void calculateBounding() {
        this.bounding = new Rectangle(getxPos(), getyPos(), getWidth(), getHeight());
    }
    
    /**
     * Gibt die Boundingbox zurück.
     * @return 
     */
    public Rectangle getBounding() {
        return this.bounding;
    }
    
    /**
     * Gibt zurück in welche Richtung sich die Figur bewegt.
     * -1 = links, 1 = rechts.
     * @return Richtung der Spielerfigur.
     */
    public int getRightwards() {
        return rightwards;
    }

    /**
     * Legt fest in welche Richtung sich die Spielerfigur bewegt.
     * -1 = links, 1 = rechts.
     * @param Bewegungsrichtung der Spielerfigur
     */
    public void setRightwards(int rightwards) {
        this.rightwards = rightwards;
    }   
} //class
