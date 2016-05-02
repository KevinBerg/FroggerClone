package froggerclonehd;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

/**
 * 
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Audio extends Thread
{

    int length = 0; //Längenvariable für eine Sounddatei
    String fileName = ""; //Der Name der Sounddatei
    
    /**
     * Konstruktor für ein Soundfile.
     * @param fileName Dateipfad des Sounddfiles, welches abgespielt werden soll.
     * @param length Länge des Soundfiles in Sekunden.
     */
    public Audio(String fileName, int length)
    {
        this.length = length * 1000; //Umrechnung von s in ms.           
        this.length += 1000;
        
        this.fileName = fileName;
    }
    
    /**
     * Konstruktor für ein Soundfile (LOOP).
     * @param fileName Dateipfad des Sounddfiles, welches als Loop abgespielt werden soll.
     */
    public Audio(String fileName)
    {
        this.fileName = fileName;
    }
         
    /**
     * Spielt eine Audiodatei in einem neuem Thread ab.
     */
    public void run(){
        if(this.length == 0){ //Überprüft, ob eine Länge mitgeteilt wurde.
            this.playAudioLoop();
        } else {
            this.playAudio(); //Einmaliger Sound
        }
    }
    
    /**
     * Spielt ein Audiofile einmalig ab.
     */
    private void playAudio()
    {
        File f = new File(this.fileName);
        
        AudioClip sound = null;
        try
        {
            sound = Applet.newAudioClip(f.toURI().toURL());
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        sound.play();
        try
        {
            Thread.sleep(this.length);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
    }

    /**
     * Spielt ein Audiofile als Loop ab.
     */
    private void playAudioLoop()
    {
        
        File f = new File(this.fileName);

        AudioClip sound = null;
        try
        {
            sound = Applet.newAudioClip(f.toURI().toURL());
        }
        catch (MalformedURLException e1)
        {
            e1.printStackTrace();
        }
        sound.loop();
        try
        {  
            while(true)
            {
                Thread.sleep(100000);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
    }

}
