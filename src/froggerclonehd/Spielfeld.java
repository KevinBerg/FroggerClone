package froggerclonehd;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Spielfeld {

    static int width;
    static int height;
    static int fieldSize; //Größe eines Felds.
    static int fields = 20; //Anzahl der Spielfelder für X- und Y-Koordinaten.
    static Feld felder[][] = new Feld[20][20]; //Feldarray
    static int targets; //Anzahl der Zielfelder die der Player erreichen muss.

    static Spielfeld spielfeldInstanz;
    private final XMLReader xmlReader = XMLReader.getInstance();
    
    /**
     * Konstruktor
     * @throws IOException 
     */
    private Spielfeld(){
        width = xmlReader.getResolution();
        height = width; //quadratisches Fenster
        fieldSize = width / fields; //Berechnung der 
    }

    /**
     * Erzeugt die Felder anhand der Leveldaten vom XMLReader.
     * @param lvl 
     */
    static public void initMap(int lvl){
        for (int i = 0; i < fields; i++) {
            
            String[] mapString = XMLReader.getInstance().getMap(lvl);
            StringReader stringReader = new StringReader(mapString[i]);
            
            for (int j = 0; j < fields; j++) {
                
                try {
                    
                    String typ = Character.toString((char) stringReader.read());
                    felder[j][i] = new Feld(fieldSize, fieldSize, typ); //Feld erzeugen
                    if (typ.equals("t")) increaseTargets(); //Für jedes erzeugte Targetfeld wird die Targetanzahl erhöht
                        
                } catch (IOException ex) {
                    
                    Logger.getLogger(Spielfeld.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            stringReader.close();
        }
    } //initMap
    
    /**
     * Gibt die Feldgröße zurück.
     * @return int Feldgröße
     */
    static public int getFieldSize() {
        return fieldSize;
    }
    
    /**
     * Liefert die (einzige) Instanz der Klasse Spielfeld.
     * @return Referenz auf die Instanz der Klasse Spielfeld.
     */
    static public Spielfeld getInstance(){
        if (spielfeldInstanz == null) spielfeldInstanz = new Spielfeld();
        return spielfeldInstanz;
    }
    
    /**
     * Lädt die Spielfeldinstanz neu.
     */
    public void reloadSpielfeld(){
        spielfeldInstanz = new Spielfeld();
    }

    /**
     * Erhöht die Anzahl der Targetfelder um 1.
     */
    static void increaseTargets() {
        targets += 1;
    }

    /**
     * Verringert die Anzahl der Targetfelder um 1.
     */
    static void decreaseTargets() {
        targets -= 1;
    }

    /**
     * Gibt die Anzahl der Targetfelder zurück.
     * @return 
     */
    static int getTargets() {
        return targets;
    }
    
    /**
     * Setzt die Anzahl der Targetfelder auf 0.
     */
    static void resetTargets(){
        targets = 0;
    }
}
