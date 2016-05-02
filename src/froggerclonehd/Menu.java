package froggerclonehd;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */
public class Menu extends JFrame{
    
    private boolean startGame = false; //Gibt an, ob das Spiel gestartet werden soll.
    int playerScore; //Punktzahl des Spielers
    
    final private XMLReader xmlReader;
    
    int buttonWidth;
    int buttonHeight;
    
    Button start;
    Button exit;
    Button highscore;
    Button options;
    Button back;
    Button sendHighscore;
    Button submitOptions;
    
    JTextField email;
    JTextField name;
    JTextField resolution;
    
    Listener listener;
   
    //Bannerbild im Startmenü
    private Image image;
    
    /**
     * Gibt zurück ob auf Spiel starten geklickt wurde
     * @return Spiel soll gestartet werden? true oder false
     */
    public boolean getStartGame(){
        return this.startGame;
    }
    
    /**
     * Setzt den Wert einer bool Variable, die aussagt ob auf Spiel starten geklickt wurde.
     * @param b 
     */
    public void setStartGame(boolean b){
        this.startGame = b;
    }
    
    /**
     * Setz den Playerscore.
     * @param score Die erreichte Punktzahl des Spielers.
     */
    public void setPlayerScore(int score){
        this.playerScore = score;
    }
    
    /**
     * Gibt den Playerscore zurück.
     * @return Playerscore
     */
    private int getPlayerScore(){
        return this.playerScore;
    }

    /**
     * Konstruktor
     */
    public Menu(){
        xmlReader = XMLReader.getInstance();
        setSize(600, 800);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocationRelativeTo(null);
        setUndecorated(false);
        listener = new Listener();
        buttonWidth = 200;
        buttonHeight = 80;
        this.image = Toolkit.getDefaultToolkit().getImage(xmlReader.getBannerPicture());
        mainMenu(); //Lade das Hauptmenü
    }
    
    /**
     * Zeigt das Hauptmenü an.
     */
    private void mainMenu(){
        
        setTitle("Froggerclone HD");
        
        
        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        setLayout(boxLayout);
        
        JPanel panel =  new JPanel();
        BoxLayout boxLayoutPanel = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        panel.setLayout(boxLayoutPanel);

        start = new Button("Spiel starten");
        start.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        start.addActionListener(listener);
        
        exit = new Button("Spiel beenden");
        exit.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        exit.addActionListener(listener);
        
        highscore = new Button("Highscore");
        highscore.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        highscore.addActionListener(listener);
        
        options = new Button("Options");
        options.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        options.addActionListener(listener);
        
        JLabel jlabel = new JLabel();
        jlabel.setMaximumSize(new Dimension(450, 300));
        jlabel.setAlignmentX(CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon(image);
        jlabel.setIcon(icon );
        
        Box box = Box.createHorizontalBox();
        box.add(jlabel);
        
        Box box_2 = Box.createVerticalBox();
        box_2.setAlignmentY(CENTER_ALIGNMENT);
        box_2.add(start);
        box_2.add(options);
        box_2.add(highscore);
        box_2.add(exit);

        add(box);
        add(box_2);
    }
    
    /**
     * Räumt den Frame auf.
     */
    private void clearFrame(){
        this.getContentPane().removeAll();
    }
    
    /**
     * Zeigt das Optionsmenü an.
     */
    private void optionsMenu(){ 
        
        setTitle("Froggerclone HD - Optionen");
        
        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        setLayout(boxLayout);
        
        JPanel panel =  new JPanel();
        BoxLayout boxLayoutPanel = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        panel.setLayout(boxLayoutPanel);
        
        back = new Button("Zurück");
        back.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        back.addActionListener(listener);
        
        submitOptions = new Button("Übernehmen");
        submitOptions.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        submitOptions.addActionListener(listener);
        
        JLabel jlabel = new JLabel();
        jlabel.setMaximumSize(new Dimension(450, 300));
        jlabel.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setMaximumSize(new Dimension(300,200));
        
        JLabel jtext = new JLabel("Auflösung: ");
        
        resolution = new JTextField(Integer.toString(xmlReader.getResolution()));
        resolution.setPreferredSize(new Dimension(40, 20));

        p.add(jtext);
        p.add(resolution);
        
        ImageIcon icon = new ImageIcon(image);
        jlabel.setIcon(icon );
        
        Box box = Box.createHorizontalBox();
        box.add(jlabel);
        
        Box box_2 = Box.createVerticalBox();
        box_2.setAlignmentY(CENTER_ALIGNMENT);
        //box_2.add(jtext);
        box_2.add(p);
        box_2.add(submitOptions);
        box_2.add(back);

        add(box);
        add(box_2);
        
        setVisible(true);
    }
    
    /**
     * Zeigt das Highscoremenü an.
     */
    private void highscoreMenu(){
        
        setTitle("Froggerclone HD - Highscore");
        
        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        setLayout(boxLayout);
        
        JPanel panel =  new JPanel();
        BoxLayout boxLayoutPanel = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        panel.setLayout(boxLayoutPanel);
        
        back = new Button("Zurück");
        back.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        back.addActionListener(listener);
        
        JLabel jlabel = new JLabel();
        jlabel.setMaximumSize(new Dimension(450, 300));
        jlabel.setAlignmentX(CENTER_ALIGNMENT);

        Integer highscore[] = highscoreXMLReader.getInstance().getHighscore(); //Drei Highscores aus der highscore.xml lesen
        
        JLabel highscoreLabel = new JLabel();
        highscoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        highscoreLabel.setText
        
            ("<html><center>" +
                "<h1>Highscore</h1><br>" + 
                "1)  " + highscore[2] + " Punkte <br>" +
                "2)  " + highscore[1] + " Punkte <br>" +
                "3)  " + highscore[0] + " Punkte" +
             "</center></html>"
            );
                
        highscoreLabel .setMaximumSize(new Dimension(450, 200));
        highscoreLabel .setAlignmentX(CENTER_ALIGNMENT);

        ImageIcon icon = new ImageIcon(image);
        jlabel.setIcon(icon );
        
        Box box = Box.createHorizontalBox();
        box.add(jlabel);
        
        Box box_2 = Box.createVerticalBox();
        box_2.setAlignmentY(CENTER_ALIGNMENT);
        box_2.add(highscoreLabel);
        box_2.add(back);

        add(box);
        add(box_2);
        
        setVisible(true);
    }
    
    /**
     * Zeigt das Menü um den Highscore per Mail zu verschicken.
     */
    public void submitHighscoreMenu(){
        
        setTitle("Froggerclone HD - Game Over");
        
        BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        setLayout(boxLayout);
        
        JPanel panel =  new JPanel();
        BoxLayout boxLayoutPanel = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS); // top to bottom
        panel.setLayout(boxLayoutPanel);
        
        sendHighscore = new Button("Einem Freund senden");
        sendHighscore.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        sendHighscore.addActionListener(listener);
        
        back = new Button("Hauptmenü");
        back.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
        back.addActionListener(listener);
        
        name = new JTextField("Dein Name");
        name.setPreferredSize(new Dimension(150, 20));
        email = new JTextField("E-Mailadresse");
        email.setPreferredSize(new Dimension(150, 20));
        
        JLabel jlabel = new JLabel();
        jlabel.setMaximumSize(new Dimension(450, 300));
        jlabel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txt = new JLabel("Sie haben " + Integer.toString(getPlayerScore()) + " Punkte erreicht.");
        txt.setMaximumSize(new Dimension(450, 300));
        txt.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.setMaximumSize(new Dimension(200,200));
        
        ImageIcon icon = new ImageIcon(image);
        jlabel.setIcon(icon );
        
        Box box = Box.createHorizontalBox();
        box.add(jlabel);
        
        p.add(txt);
        p.add(name);
        p.add(email);
        
        
        Box box_2 = Box.createVerticalBox();
        box_2.setAlignmentY(CENTER_ALIGNMENT);
        box_2.add(p);
        box_2.add(sendHighscore);
        box_2.add(back);

        add(box);
        add(box_2);
        
        setVisible(true);
    }
    
    /**
     * Sorgt für die richtige Darstellung des Menüs,
     * welches zum verschicken (Mail) des Highscores dient.
     * @param score 
     */
    public void setGameEndMenu(int score){
        clearFrame();
        setPlayerScore(score);
        submitHighscoreMenu();
    }
    
    /**
     * Actionlistener Klasse für alle Buttons des Menüs.
     */
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == start){
                setStartGame(true); //Spiel soll gestartet werden
            } else if(e.getSource() == exit){
                System.exit(0);
            } else if(e.getSource() == options){
                clearFrame();
                optionsMenu();
            } else if(e.getSource() == highscore){
                clearFrame();
                highscoreMenu(); 
            } else if(e.getSource() == back){
                clearFrame();
                mainMenu();
                setVisible(true);
            } else if(e.getSource() == sendHighscore){
                new Mail(name.getText(), email.getText(), getPlayerScore()); //Hier wird die Mail verschickt.
                clearFrame();
                mainMenu();
                setVisible(true);
            } else if(e.getSource() == submitOptions){
                xmlReader.setResolution(Integer.parseInt(resolution.getText()));
            }
            
        } //actionPerformed
    
    } //class Listener
    
} //class
