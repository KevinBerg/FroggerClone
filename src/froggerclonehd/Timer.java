package froggerclonehd;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Timer extends Thread{
    
    private boolean timerRunning = true;
    private int playedSeconds = 0; //Gespielte Sekunden
    
    /**
     * Erhöht playedSeconds jede Sekunde um 1.
     * Wird in einem neuen Thread gestartet.
     */
    public void run(){
        while(!isInterrupted()){
            try {
                Thread.sleep(1000); //1 Sekunde warten
                increasePlayedSeconds(); //Gespielte Zeit erhöhen
            } catch (InterruptedException ex) {
                Logger.getLogger(Timer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } //while
    } //run
    
    /**
     * Setzt die gespielten Sekunden auf 0 zurück.
     */
    public void resetPlayedSeconds(){
        this.playedSeconds = 0;
    }
    
    /**
     * Gibt zurück, ob der Timer gerade läuft.
     * @return true wenn der Timer läuft.
     */
    public boolean isTimerRunning(){
        return timerRunning;
    }
    
    /**
     * Legt fest, ob der Timer läuft.
     * @param b 
     */
    public void setTimerRunning(boolean b){
        this.timerRunning = b;
    }
    
    /**
     * Erhöht die gespielten Sekunden um 1.
     */
    public void increasePlayedSeconds(){
        this.playedSeconds++;
    }
    
    /**
     * Gibt die gespielten Sekunden zurück.
     * @return 
     */
    public int getPlayedSeconds(){
        return this.playedSeconds;
    }

}
