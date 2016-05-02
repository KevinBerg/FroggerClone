package froggerclonehd;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Ticker extends Observable {

    long lastFrame; //Berechnungsgeschwindigkeit des letzten Frames
    float timeSinceLastFrame; //Aktuelle Systemzeit wird hier gespeichert
    
    static boolean pause = false; //Variable die aussagt, ob der Ticker pausiert werden soll.
    
    /**
     * Gibt zurück, ob der Ticker pausiert ist.
     * @return 
     */
    static public boolean getPause(){
        return pause;
    }
    
    /**
     * Manipuliert die pausenvariable
     */
    static public void togglePause(){
        pause = !pause;
    }
    
    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
    } // addObserver

    /**
     * Berechnung der Systemzeit anhand der Systemgeschwindigkeit.
     * Der berechnete Wert sorgt dafür, dass Animationen auf verschiedenen
     * System gleich schnell ablaufen.
     */
    private void calculateFrameTime(){
        long thisFrame = System.currentTimeMillis();
        this.timeSinceLastFrame = (float)(thisFrame - this.lastFrame) / 1000f;
        this.lastFrame = thisFrame;
    } 
    
    /**
     * Gibt die aktuelle Systemzeit zurück.
     * @return 
     */
    public float getTimeSinceLastFrame(){
        return this.timeSinceLastFrame; //Dieser Wert muss mit allen Koordinatenänderungen von beweglichen Figuren multipliziert werden!
    } 
    
    public void tick() {
            
        try {
            Thread.sleep(15); //Ticker kurz anhalten, da das Spiel sonst viel zu schnell läuft.
            calculateFrameTime(); //Berechnung der Systemzeit
            if(!getPause()){ //Wenn der Ticker nicht pausiert ist.
                setChanged();
                notifyObservers(getTimeSinceLastFrame()); //TimeSinceLastFrame an Beobachter übergeben
            } 

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } //tick
} //class
