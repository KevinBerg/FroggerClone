package froggerclonehd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter; 

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class XMLReader {

    Element root; //Rootelement 
    Document doc; 
    SAXBuilder builder;
    File f;

    static XMLReader XMLReaderInstanz;

    /**
     * Konstruktor (private, Singeleton Entwurfsmuster)
     * @throws IOException 
     */
    private XMLReader() {
        try {
            this.f = new File("config.xml");
            this.builder = new SAXBuilder();
            this.doc = (Document) builder.build(this.f); //cast
            this.setRootElement();
        } catch (JDOMException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Schreibt die Auflösung resolution in die XML-Datei.
     * @param resolution Auflösung
     */
    public void setResolution(int resolution) {
        String res = Integer.toString(resolution);
        this.root.getChild("daten").getChild("resolution").getAttribute("value").setValue(res); //Auflösung schreiben
        saveDoc(); //Dokument speichern
    }
    
    /**
     * List die Auflösung aus der XML-Datei.
     * @return 
     */
    public int getResolution() {
        return Integer.parseInt(this.root.getChild("daten").getChild("resolution").getAttributeValue("value"));
    }

    /**
     * Liest den Pfad der Hintergrundmusik aus der XML-Datei.
     * @return 
     */
    public String getBackgroundMusic() {
        return this.root.getChild("daten").getChild("backgroundmusic").getAttributeValue("value");
    }
    
    public String getBannerPicture(){
        return this.root.getChild("daten").getChild("banner").getAttributeValue("value");
    }
    
    /**
     * Liest den Pfad der Kollisionssound aus der XML-Datei.
     * @return 
     */
    public String getCollisionSound() {
        return this.root.getChild("daten").getChild("collisionsound").getAttributeValue("value");
    }
    
    /**
     * Liest den Pfad des Levelchangesounds aus der XML-Datei.
     * @return 
     */
    public String getLevelChangeSound() {
        return this.root.getChild("daten").getChild("levelchangesound").getAttributeValue("value");
    }

    /**
     * Liest aus der XML-Datei, wie viele Level es insgesamt gibt.
     * @return 
     */
    public int getLevels() {  //Anzahl der Level auslesen
        return this.root.getChild("levels").getChildren().size();
    }

    /**
     * Gibt die Map als Array zurück.
     * @param levelNumber
     * @return 
     */
    public String[] getMap(int levelNumber) {

        String lvl = "level_" + levelNumber;
        int mapSize = this.root.getChild("levels").getChild(lvl).getChild("map").getChildren().size();

        String[] map = new String[mapSize];
        for (int i = 0; i < 20; i++) {
            map[i] = this.getLine(levelNumber, i);
        }
        
        return map;
    }

    /**
     * Gibt eine Levelline zurück.
     * @param levelNumber
     * @param lineNumber
     * @return 
     */
    public String getLine(int levelNumber, int lineNumber) {
        String lvl = "level_" + levelNumber;
        String line;
        if (lineNumber == 0) {
            line = "line_0";
        } else {
            line = "line_" + lineNumber;
        }
        return this.root.getChild("levels").getChild(lvl).getChild("map").getChild(line).getAttributeValue("value");
    }
    
    /**
     * Gibt die Anzahl der Levels zurück.
     * @return 
     */
    public int countLevels(){
        return this.root.getChild("levels").getChildren().size() + 1; // + 1, da es lvl 0 nicht gibt.
    }
    
    /**
     * Liest die Startposition des Spielers für ein bestimmtes Level aus.
     * Gibt ein int-Array zurück. 
     * @param lvl Index 0 enthält X-Position, Index 1 enthält Y-Position.
     * @return 
     */
    public int[] getStartPosition(String lvl){
        lvl = "level_" + lvl;
        Element e = this.root.getChild("levels").getChild(lvl).getChild("map").getChild("startposition");
        int[] startPos = {Integer.parseInt(e.getChild("xPos").getAttributeValue("value")),
                          Integer.parseInt(e.getChild("yPos").getAttributeValue("value"))};
        
        return startPos;
    }

    /**
     * Liest die Walker eines Levels aus und gibt diese in einem Walker-Array zurück.
     * @param lvl
     * @return 
     */
    public Walker[] getWalkers(String lvl) {
        lvl = "level_" + lvl;

        Element walker = this.root.getChild("levels").getChild(lvl).getChild("walkers");   //Selektieren der Walker Line
        int walkerSize = walker.getChildren().size();
        Walker[] walkers = new Walker[walkerSize]; //aray der Länge Walkersize

        for (int i = 0; i < walkerSize; i++) {
            String walkerNumber = "walker_" + (i + 1);
            Element walkerLine = this.root.getChild("levels").getChild(lvl).getChild("walkers").getChild(walkerNumber);
            
            //Walker erstellen und die einzelnen Parameter aus der XML-Datei auslesen.
            
            walkers[i] = new Walker(Integer.parseInt(walkerLine.getChild("xPos").getAttributeValue("value")), 
                    Integer.parseInt(walkerLine.getChild("yPos").getAttributeValue("value")),
                    Integer.parseInt(walkerLine.getChild("speed").getAttributeValue("value")), 
                    Integer.parseInt(walkerLine.getChild("rightwards").getAttributeValue("value")),
                    getWalkerPicture(walkerLine.getChild("type").getAttributeValue("value")));         
        }

        return walkers;
    }
    
    /**
     * Liest die Vehicles eines Levels aus und gibt diese in einem Vehicle-Array zurück.
     * @param lvl
     * @return 
     */
    public Vehicle[] getVehicles(String lvl) {
        lvl = "level_" + lvl;

        Element vehicle = this.root.getChild("levels").getChild(lvl).getChild("vehicles");   //Selektieren der Walker Line
        int vehicleSize = vehicle.getChildren().size();
        Vehicle[] vehicles = new Vehicle[vehicleSize]; //aray der Länge Walkersize

        for (int i = 0; i < vehicleSize; i++) { 
            String vehicleNumber = "vehicle_" + (i + 1);
            Element vehicleLine = this.root.getChild("levels").getChild(lvl).getChild("vehicles").getChild(vehicleNumber);

            //Vehicle erstellen und die einzelnen Parameter aus der XML-Datei auslesen.
            
            vehicles[i] = new Vehicle(Integer.parseInt(vehicleLine.getChild("xPos").getAttributeValue("value")),
            Integer.parseInt(vehicleLine.getChild("yPos").getAttributeValue("value")), 
            Integer.parseInt(vehicleLine.getChild("speed").getAttributeValue("value")), 
            Integer.parseInt(vehicleLine.getChild("rightwards").getAttributeValue("value")),
            getVehiclePicture(vehicleLine.getChild("type").getAttributeValue("value")));
        }

        return vehicles;
    }

    /**
     * Liest den Bildpfad eines bestimmten Texturtyps aus der XML-Datei.
     * @param typ
     * @return 
     */
    public String getPicturePath(String typ) {
        return this.root.getChild("textures").getChild("backgrounds").getChild(typ).getAttributeValue("value");
    }
    
    /**
     * Liest den Bildpfad für ein Leben aus der XML-Datei.
     * @return 
     */
    public String getHealthImage(){
        return this.root.getChild("textures").getChild("healthimage").getAttributeValue("value");
    }
    
    /**
     * Liest den Bildpfad für ein abgezogenes Leben aus der XML-Datei.
     * @return 
     */
    public String getHealthLessImage(){
        return this.root.getChild("textures").getChild("healthlessimage").getAttributeValue("value");
    }
    
    /**
     * Liest den Bildpfad für die Playertextur aus der XML-Datei.
     * @return 
     */
    public String getPlayerImage(){
        return this.root.getChild("textures").getChild("player").getAttributeValue("value");
    }

    /**
     * Liest den Bildpfad für ein Vehicle aus der XML-Datei.
     * @param type
     * @return 
     */
    public String getVehiclePicture(String type){
        return this.root.getChild("textures").getChild("vehicles").getChild(type).getAttributeValue("value");
    }

    /**
     * Liest den Bildpfad für ein Walker aus der XML-Datei.
     * @param type
     * @return 
     */
    public String getWalkerPicture(String type){
        return this.root.getChild("textures").getChild("walkers").getChild(type).getAttributeValue("value");
    }

    /**
     * Legt das Rootelement der XML-Datei in einer Variable fest.
     */
    public void setRootElement() {
        this.root = doc.getRootElement();
    }
    
    /**
     * Speichert das Dokument ab.
     */
    private void saveDoc() {
        try {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(f);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(highscoreXMLReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(doc, fos);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(highscoreXMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gibt die einzig existierende Instanz dieser Klasse zurück.
     * @return 
     */
    static public XMLReader getInstance() {
        if (XMLReaderInstanz == null) {
            XMLReaderInstanz = new XMLReader();
        }
        return XMLReaderInstanz;
    }
}
