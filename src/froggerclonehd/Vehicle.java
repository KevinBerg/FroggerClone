package froggerclonehd;

import java.util.Observable;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Vehicle extends Figur {
    
    String picPath;
    Spielfeld spielfeld;
    
    public void update(Observable o, Object arg) {
        calculateBounding();
        setBackToField();
        float systemTime = (Float)arg;
        if(systemTime > 1) return; //Wenn Systemzeit zu hoch, keine Berechnung vornehmen!
        int nextPos =  this.getxPos() + (this.getRightwards() * (int)( (this.getSpeed()* systemTime) * (XMLReader.getInstance().getResolution() / (float) 80)));  //Nächste Position mit Frametime errechnen
        this.setxPos(nextPos); //Koordinatenänderung
        //try catch notwendig, da die systemzeit anfangs höher ist und somit eine arrayindexoutofbounds ex geworfen werden kann
        try{
            if(vehicleOnFixField()) toggleRightwards();
        } catch (ArrayIndexOutOfBoundsException e){}
    } // update
    
    /**
     * Konstruktor
     * @param xPos
     * @param yPos
     * @param speed
     * @param rightwards
     * @param type 
     */
    public Vehicle(int xPos, int yPos, int speed, int rightwards, String type){
        spielfeld = Spielfeld.getInstance();
        float x = (float)xPos;
        float y = (float)yPos;
        this.setxPos((int)(x/spielfeld.fields * XMLReader.getInstance().getResolution()));
        this.setyPos((int)(y/spielfeld.fields * XMLReader.getInstance().getResolution()));
        this.setSpeed((int)(speed*2));
        this.setWidth(spielfeld.getFieldSize());
        this.setHeight(spielfeld.getFieldSize());
        this.setRightwards(rightwards);//direction
        this.setImage(type, rightwards);
        picPath = type;
    }  

    /**
     * Ändert die Richtung und das Bild des Vehicles.
     */
    private void toggleRightwards(){
        setRightwards(getRightwards() * -1);
        setImage(picPath, getRightwards());
    }
    
    /**
     * Gibt den Feldtyp zurück, auf welchem das Vehicle gerade steht.
     * @return 
     */
     public Feld getVehicleField()
    {
        return spielfeld.felder[getxCoord()][getyCoord()];
         
    }

    /**
     * Gibt true zurück, wenn das Vehicle auf einem Fled vom Typ fix steht.
     * @return 
     */
    private boolean vehicleOnFixField()
    {
        return getVehicleField().getStatus().equals("fix");
           
    }
    
    /**
     * Gibt die X-Spielfeldkoordinate zurück.
     * @return 
     */
    private int getxCoord()
    {
        return (getxPos() / spielfeld.fieldSize);
    }
    
    /**
     * Gibt die Y-Spielfeldkoordinate zurück.
     * @return 
     */
    private int getyCoord()
    {
        return (getyPos() / spielfeld.fieldSize);
    }
}
