package froggerclonehd;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Controller implements KeyListener {

    Player player; //Playerfigur
    Spiel spiel; //Der Spiel-Frame
    
    /**
     * Konstruktor
     * @param spiel Instaz des Spiel-Frames.
     * @param fig Figurinstanz die beim KeyListener angemeldet werden soll (Player).
     */
    public Controller(Spiel spiel, Player fig){
        this.player = fig;
        this.spiel = spiel;
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        
        if(!Ticker.pause){ //Wenn das Spiel nicht pausiert wurde.

            if(ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_S){
                player.movePlayer(0, 1);
                
            }

            if(ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_W){
                player.movePlayer(0, -1);
            }

            if(ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D){
                player.movePlayer(1, 0);
            }

            if(ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A){
                 player.movePlayer(-1, 0);
            }

        }
        
        if(ke.getKeyCode() == KeyEvent.VK_ESCAPE || ke.getKeyCode() == KeyEvent.VK_P){ //Pausenmenü
            Ticker.togglePause();
            
            if(Ticker.pause){
                spiel.repaint();
            }
        }
        
       if(ke.getKeyCode() == KeyEvent.VK_F4){ //Spiel beenden
            System.exit(0);
        }
       
       if(ke.getKeyCode() == KeyEvent.VK_F2){ //Zurück ins Hauptmenü
           spiel.setModus("MODUS_BACK_TO_MENU");
       }
    }

    @Override
    public void keyReleased(KeyEvent ke) {}

    @Override
    public void keyTyped(KeyEvent ke) {}
    
} //class
