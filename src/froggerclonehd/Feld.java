package froggerclonehd;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Feld extends Zeichenobjekt {

    //Anhand dieser Variable wird entschieden, ob das Feld begehbar ist oder nicht.
    //Außerdem werden auch die Grafiken anhand des Typs zugeteilt.
    String typ; 
    
    final private XMLReader xmlReader = XMLReader.getInstance();
    
    /**
     * Konstruktor
     * @param width Breite eines Felds.
     * @param height Höhe eines Felds.
     * @param typ Grafikentyp und Entscheidungsvariable für die Begehbarkeit des Felds.
     */
    public Feld(int width, int height, String typ) {
        this.setWidth(width);
        this.setHeight(height);
        this.setTyp(typ);
        
        if( typ.equals("h") ||
            typ.equals("k") ||
            typ.equals("r") ||
            typ.equals("o") ||
            typ.equals("y") ||
            typ.equals("x") ||
            typ.equals("c") ||
            typ.equals("v")) {
            this.setStatus("fix"); //Nicht begehbares Feld
        } else {
            this.setStatus("unfix"); //Begehbares Feld
        }
        setImage(xmlReader.getPicturePath(typ)); //Zuteilung des Bilds
    }
    
    /**
     * Setzt den Typ des Felds fest.
     * @param typ String Feldtyp
     */
    public void setTyp(String typ)
    {
        this.typ = typ;
    }
    
    /**
     * Gibt den Typ des Felds zurück.
     * @return String Feldtyp
     */
    public String getTyp()
    {
        return this.typ;
    }
    
    /**
     * Setzt ein Targetfeld auf den Status fix und ändert den typ sowie das Bild.
     * Diese Methode muss verwendet werden, sobald der Player ein Targetfeld erreicht hat.
     */
    public void setTargetReached()
    {
        this.setStatus("fix"); //Feld wird auf fix gesetzt, damit dieses nicht mehr erreichbar ist.
        this.setTyp("z"); //target typ ändern damit der Targetcounter heruntergeht
        this.setImage(xmlReader.getPlayerImage()); //Feld bekommt nun das Playerbild zugewiesen
    }
}//class
