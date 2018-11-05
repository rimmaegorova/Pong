package com.egorova;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import net.miginfocom.swing.*;
import net.sevecek.util.swing.*;

public class HlavniOkno extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    JLabel labMicek;
    JLabel labLevyHrac;
    JLabel labPravyHrac;
    JLabel labLevyHracScore;
    JLabel labPravyHracScore;
    JLabel labStartHry;
    JLabel labVyhra;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    JPanel contentPane;
    MigLayout migLayoutManager;

    Timer casovac;

    JKeyboard klavesnice = new JKeyboard();

    public static final int DELTA_X =5;
    public static final int DELTA_Y =5;
    public static final int SCORE = 5;

    int smerX = -1;
    int smerY = -1;

    int levyHracScore = 0;
    int pravyHracScore = 0;
    Timer pauzovac;
    boolean hrajeSe;

    public HlavniOkno() {
        initComponents();
    }


    private void whenWindowIsOpened(WindowEvent event) {
        labLevyHracScore.setLocation(contentPane.getWidth()/5,15);
        labPravyHracScore.setLocation(contentPane.getWidth() - contentPane.getWidth()/3, 15);
        labLevyHracScore.setText("Left Player: " + levyHracScore);
        labPravyHracScore.setText("Right Player: " + pravyHracScore);
        gameStart();
        casovac = new Timer(50, e -> moveBall());
        casovac.start();

        moveBall();

        /*if (pressEnterToContinue()){
        casovac.start();
        moveBall();
        }*/

    }



    private void whenWindowIsClosed(WindowEvent e) {
        casovac.stop();
    }

    private void gameStart(){
         int y = contentPane.getHeight()/2- labLevyHrac.getHeight()/2;
         labMicek.setLocation(contentPane.getWidth()/2, contentPane.getHeight()/2);
         labLevyHrac.setLocation(0, ((20-y%20) +y));
         labPravyHrac.setLocation(contentPane.getWidth()-labPravyHrac.getWidth(), ((20-y%20) +y));
    }

    private boolean pressEnterToContinue(){
        if (klavesnice.isKeyDown(KeyEvent.VK_ENTER)){
            hrajeSe = true;
            labStartHry.setVisible(false);
            return hrajeSe;
        }
            hrajeSe=false;
            return hrajeSe;
    }

    public void playersMove(){
        if (klavesnice.isKeyDown(KeyEvent.VK_UP)){ //sipka nahoru
            moveRobot(-1, labPravyHrac);
        } else if (klavesnice.isKeyDown(KeyEvent.VK_DOWN)){//sipka dolu
            moveRobot(1, labPravyHrac);
        }


        if (klavesnice.isKeyDown(KeyEvent.VK_W)){ //W nahoru
            moveRobot(-1, labLevyHrac);
        } else if (klavesnice.isKeyDown(KeyEvent.VK_S)){//S dolu
            moveRobot(1, labLevyHrac);
        }
    }

    private void moveBall(){

            playersMove();
            checkScore();

            int x = labMicek.getX();
            int y = labMicek.getY();
            x = x + smerX * DELTA_X;
            y = y + smerY * DELTA_Y;
            labMicek.setLocation(x, y);


            if (y < 0 || (y + labMicek.getHeight() > contentPane.getHeight())) {
                smerY = -smerY;
            }

            if (detekujiKolizi(labLevyHrac,labMicek)){
                if (smerX<0) {
                    smerX = -smerX;
                }
            }

            if (detekujiKolizi(labPravyHrac, labMicek)){
                if (smerX>0) {
                    smerX = -smerX;                                    
                }
            }

            if (x + labMicek.getWidth() >= contentPane.getWidth()) {
                gameStart();
                levyHracScore = levyHracScore + 1;
                labLevyHracScore.setText("Left Player: " + Integer.toString(levyHracScore));
                casovac.stop();

                pauzovac = new Timer(1_000_000, e1 -> {
                    casovac.start();
                    pauzovac.stop();
                });
                pauzovac.setInitialDelay(3000);
                pauzovac.start();
                return;
            }

            if (x < 0) {
                gameStart();
                pravyHracScore = pravyHracScore + 1;
                labPravyHracScore.setText("Right Player: " + Integer.toString(pravyHracScore));

                //pauza();
                return;
            }
    }


    public void moveRobot(int smerY, JLabel komponenta) {
        int x = komponenta.getX();
        int y = komponenta.getY();
      
        if ((smerY == -1) && ((y + smerY * DELTA_Y) < 0)){
            return;
        } else if ((smerY ==1) && ((y + smerY * DELTA_Y) > (contentPane.getHeight() - komponenta.getHeight()))){
            return;
        }
        komponenta.setLocation(x, y + smerY * DELTA_Y);
    }



    public boolean detekujiKolizi(JComponent komponenta1, JComponent komponenta2){

        int k1x1 = komponenta1.getX();
        int k1x2 = komponenta1.getX() + komponenta1.getWidth();
        int k1y1 = komponenta1.getY();
        int k1y2 = komponenta1.getY() + komponenta1.getHeight();

        int k2x1 = komponenta2.getX();
        int k2x2 = komponenta2.getX() + komponenta2.getWidth();
        int k2y1 = komponenta2.getY();
        int k2y2 = komponenta2.getY() + komponenta2.getHeight();

        return k1x1 < k2x2 && k2x1 < k1x2 && k1y1 < k2y2 && k2y1 < k1y2;

    }


    /*public void pauza(){
        pauzovac = new Timer(1_000_000, e1 -> {
            casovac.start();
            pauzovac.stop();
        });
        pauzovac.setInitialDelay(3000);
        pauzovac.start();
    }*/

    public void checkScore(){
        if (pravyHracScore >= SCORE || levyHracScore >= SCORE){
            casovac.stop();

            if (pravyHracScore > levyHracScore){
                labVyhra.setText("Game Over. Pravy hrac vyhral");
            } else {
                labVyhra.setText("Game Over. Levy hrac vyhral");
            }

            labVyhra.setVisible(true);
        }
    }








    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        labMicek = new JLabel();
        labLevyHrac = new JLabel();
        labPravyHrac = new JLabel();
        labLevyHracScore = new JLabel();
        labPravyHracScore = new JLabel();
        labStartHry = new JLabel();
        labVyhra = new JLabel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pong");
        setMinimumSize(new Dimension(500, 500));
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                whenWindowIsClosed(e);
            }
            @Override
            public void windowOpened(WindowEvent e) {
                whenWindowIsOpened(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(null);
        this.contentPane = (JPanel) this.getContentPane();
        this.contentPane.setBackground(this.getBackground());
        LayoutManager layout = this.contentPane.getLayout();
        if (layout instanceof MigLayout) {
            this.migLayoutManager = (MigLayout) layout;
        }

        //---- labMicek ----
        labMicek.setIcon(new ImageIcon(getClass().getResource("/micek.png")));
        contentPane.add(labMicek);
        labMicek.setBounds(235, 200, 40, 42);

        //---- labLevyHrac ----
        labLevyHrac.setIcon(new ImageIcon(getClass().getResource("/levy-hrac.png")));
        contentPane.add(labLevyHrac);
        labLevyHrac.setBounds(new Rectangle(new Point(0, 150), labLevyHrac.getPreferredSize()));

        //---- labPravyHrac ----
        labPravyHrac.setIcon(new ImageIcon(getClass().getResource("/pravy-hrac.png")));
        contentPane.add(labPravyHrac);
        labPravyHrac.setBounds(new Rectangle(new Point(470, 155), labPravyHrac.getPreferredSize()));

        //---- labLevyHracScore ----
        labLevyHracScore.setText("0");
        contentPane.add(labLevyHracScore);
        labLevyHracScore.setBounds(55, 45, 100, labLevyHracScore.getPreferredSize().height);

        //---- labPravyHracScore ----
        labPravyHracScore.setText("0");
        contentPane.add(labPravyHracScore);
        labPravyHracScore.setBounds(300, 45, 120, labPravyHracScore.getPreferredSize().height);

        //---- labStartHry ----
        labStartHry.setText("Press Enter key to continue");
        contentPane.add(labStartHry);
        labStartHry.setBounds(new Rectangle(new Point(160, 95), labStartHry.getPreferredSize()));

        //---- labVyhra ----
        labVyhra.setText("Game Over");
        labVyhra.setVisible(false);
        contentPane.add(labVyhra);
        labVyhra.setBounds(115, 145, 300, labVyhra.getPreferredSize().height);

        contentPane.setPreferredSize(new Dimension(340, 341));
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
}
