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
public class highscoreXMLReader {

    Element root;
    Document doc;
    SAXBuilder builder;
    File f;
    XMLOutputter fmt;
    
    static highscoreXMLReader highscoreXMLReaderInstanz;

    /**
     * Privater Konstruktor (Singleton Entwurfsmuster)
     */
    private highscoreXMLReader() {
        try {
            this.f = new File("highscore.xml");
            this.builder = new SAXBuilder();
            this.doc = (Document) builder.build(this.f); //cast
            this.fmt = new XMLOutputter();
            this.setRootElement();
        } catch (JDOMException ex) {
            Logger.getLogger(XMLReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(highscoreXMLReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    /**
     * Liest den Highscore aus der Highscore-XML-Datei.
     * @return Integer Array der Länge 3
     */
    public Integer[] getHighscore()
    {
        Integer highscoreArray[] = new Integer[3];
        highscoreArray[2] = Integer.parseInt(this.root.getChild("h_2").getAttributeValue("value"));
        highscoreArray[1] = Integer.parseInt(this.root.getChild("h_1").getAttributeValue("value"));
        highscoreArray[0] = Integer.parseInt(this.root.getChild("h_0").getAttributeValue("value"));
        
        return highscoreArray;
    }
    
    /**
     * Nimmt einen Highscore als int entgegen und prüft,
     * ob dieser Wert in die Highscoreliste eingetragen wird.
     * @param score 
     */
    public void submitHighscore(int score){
        Integer highscoreArray[] = getHighscore();
        
        if(score > highscoreArray[0]){
            
            if(score > highscoreArray[1]){
                
                if(score > highscoreArray[2]){
                    replaceScore(0, highscoreArray[1]); //2. Platz = 3.Platz
                    replaceScore(1, highscoreArray[2]); //1.Platz = 2. Platz 
                    replaceScore(2, score); //1. Platz wird ersetzt
                    return;
                }

                replaceScore(0,  highscoreArray[1]); // 2. Platz = 3. Platz
                replaceScore(1, score); //Neuer 2. Platz
                return;
            }  
          
            replaceScore(0, score);
        } 
    }
    
    /**
     * Ersetzt einen Highscoreeintrag.
     * @param index Highscoreplatz (1, 2 oder 3)
     * @param score
     */
    public void replaceScore(int index, int score){ //private
        this.root.getChild("h_"+Integer.toString(index)).getAttribute("value").setValue(Integer.toString(score));
        saveDoc();
    }

    /**
     * Legt das Rootelement des XML-Dokuments fest.
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
     * Liefert die einzig existierende Instanz dieser Klasse zurück.
     * @return 
     */
    static public highscoreXMLReader getInstance() {
        if (highscoreXMLReaderInstanz == null) {
            highscoreXMLReaderInstanz = new highscoreXMLReader();
        }
        return highscoreXMLReaderInstanz;
    }


}
