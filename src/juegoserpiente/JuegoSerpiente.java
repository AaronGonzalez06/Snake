/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package juegoserpiente;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Aaron
 */
public class JuegoSerpiente extends JFrame {

    JPanel[][] matriz = new JPanel[10][10];
    int[] coordenada = new int[2];
    Negra[] corrdenaPrueba = new Negra[5];
    int[] coordenadaManzana = new int[2];
    boolean derecha = false;
    boolean izquierda = false;
    boolean arriba = false;
    boolean abajo = false;
    boolean manzana = true;
    int contador = 0;
    ArrayList serpiente = new ArrayList();

    //imagenes
    //prueba de movimiento
    Negra PosicionAux = null;

    public Clip clip;
    Color pisarColor;
    Color colorActual;

    String[] botones = {"Salir."};         

    Timer timer = new Timer(250, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // coordenadasNegro();
            detectarManzanas();
            seguirMovimiento();
            manzanas();
        }
    });

    public JuegoSerpiente() {
        crearVentana();
        matriz();
        coordenadasNegro();
        detectarManzanas();

        timer.start();
        mover();
        seguirMovimiento();
        manzanas();
        detectarManzanas();

    }

    //crear la ventana
    public void crearVentana() {
        this.setSize(1000, 1000);
        //pone titulo a la ventana
        setTitle("Snake");
        //nos hace el cierre de la ventana 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(700, 700));
        this.setResizable(false);
        this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true);                

    }

    //mostrar matriz
    public void matriz() {
        //this.setLayout(new GridLayout(10, 10, 2, 2));
        this.setLayout(new GridLayout(10, 10));
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                matriz[x][y] = new JPanel();
                if ((x == 4 && y == 4) || (x == 4 && y == 5) || (x == 4 && y == 6)) {
                    matriz[x][y].setBackground(Color.black);
                } else {
                    matriz[x][y].setBackground(Color.gray);
                }

                this.add(matriz[x][y]);

            }
        }

    }

    public void sonidoIntro(String nombreSonido) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(nombreSonido).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(0);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Error al reproducir el sonido.");
        }
    }

    //cuenta fichas negras que hay
    public void coordenadasNegro() {
        Color color;

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                color = matriz[x][y].getBackground();

                if (color.getRed() == 0 && color.getBlue() == 0 && color.getGreen() == 0) {

                    if (serpiente.size() > 2) {
                        for (int z = 0; z < serpiente.size(); z++) {
                            Negra ejes = (Negra) serpiente.get(z);
                            if (!(ejes.getPosicionX() == x) && !(ejes.getPosicionY() == y)) {
                                serpiente.add(new Negra(x, y, 0, 0));
                                //System.out.println("va");
                            } else {

                                //System.out.println("nada ");
                            }
                        }
                    } else {
                        //System.out.println("entro");
                        // contador++;

                        serpiente.add(new Negra(x, y, 0, 0));

                    }

                    //corrdenaPrueba[contador] = new Negra(x, y);
                    //serpiente.add(new Negra(x, y));
                }
            }
        }
        //System.out.println("x: " + coordenada[0] + " y: " + coordenada[1]);
        //System.out.println("negros en pantalla " + contador);
        //Negra prueba = (Negra) serpiente.get(1);
        //System.out.println("x: " + prueba.getPosicionX() + " y: " + prueba.getPosicionY());
        //System.out.println(serpiente.size());
        // System.out.println(contador);

    }

    //movimiento principal
    public void mover() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //cuando soltamos la tecla

                if (e.getKeyChar() == 'd') {
                    //derecha
                    for (int x = 0; x < serpiente.size(); x++) {
                        Negra cuerpo = (Negra) serpiente.get(x);
                        int cuerpoX = cuerpo.getPosicionX();
                        int cuerpoY = cuerpo.getPosicionY();

                        cuerpo.setPosicionAuxX(cuerpoX);
                        cuerpo.setPosicionAuxY(cuerpoY);
                        int cuerpoXaux = cuerpo.getPosicionAuxX();
                        int cuerpoYaux = cuerpo.getPosicionAuxY();
                        //obtengo ya las coordenadas de la serpiente                                                
                        System.out.println(cuerpoX + " " + cuerpoY);
                        //PosicionAux = (Negra) serpiente.get(x);                        

                        if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                            String audio = "audio/puntos.wav";
                            sonidoIntro(audio);

                            manzana = true;

                            //borrar foto                            
                            matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                            int ultimo = serpiente.size() - 1;
                            Negra cola = (Negra) serpiente.get(ultimo);
                            int ejeX = cola.getPosicionX();
                            int ejeY = cola.getPosicionY();
                            System.out.println(ejeX + " " + ejeY);

                            if (ejeY + 1 == 10) {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            } else if (ejeY - 1 == -1) {
                                matriz[ejeX][ejeY + 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                            } else if (ejeX + 1 == 10) {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            } else if (ejeX - 1 == -1) {
                                matriz[ejeX + 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                            } else {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            }

                        }

                        if ((cuerpoY == 9) && (x == 0)) {

                            JOptionPane.showMessageDialog(null, "Game Over");
                        } else if (x == 0) {

                            colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                            pisarColor = matriz[cuerpoX][cuerpoY + 1].getBackground();
                            if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                                mensaje();
                            }

                        }

                        if (x == 0) {
                            //la cabeza
                            //guarda la posicion actual
                            //PosicionAux = (Negra) serpiente.get(x);
                            PosicionAux = (Negra) serpiente.get(x);
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[cuerpoX][cuerpoY + 1].setBackground(Color.black);
                            serpiente.set(x, new Negra(cuerpoX, cuerpoY + 1, cuerpoXaux, cuerpoYaux));

                            derecha = true;
                            izquierda = false;
                            arriba = false;
                            abajo = false;

                        } else {
                            //PosicionAux = (Negra) serpiente.get(x);
                            int ejeX = PosicionAux.getPosicionAuxX();
                            int ejeY = PosicionAux.getPosicionAuxY();
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[ejeX][ejeY].setBackground(Color.black);
                            serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                            PosicionAux = (Negra) serpiente.get(x);

                            derecha = true;
                            izquierda = false;
                            arriba = false;
                            abajo = false;

                        }

                    }

                } else if (e.getKeyChar() == 'a') {

                    //izquierda funciona
                    //Negra PosicionAux = null;
                    for (int x = 0; x < serpiente.size(); x++) {
                        Negra cuerpo = (Negra) serpiente.get(x);
                        int cuerpoX = cuerpo.getPosicionX();
                        int cuerpoY = cuerpo.getPosicionY();

                        cuerpo.setPosicionAuxX(cuerpoX);
                        cuerpo.setPosicionAuxY(cuerpoY);
                        int cuerpoXaux = cuerpo.getPosicionAuxX();
                        int cuerpoYaux = cuerpo.getPosicionAuxY();
                        //obtengo ya las coordenadas de la serpiente                                                
                        System.out.println(cuerpoX + " " + cuerpoY);
                        //PosicionAux = (Negra) serpiente.get(x);

                        if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                            String audio = "audio/puntos.wav";
                            sonidoIntro(audio);

                            manzana = true;

                            matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                            int ultimo = serpiente.size() - 1;
                            Negra cola = (Negra) serpiente.get(ultimo);
                            int ejeX = cola.getPosicionX();
                            int ejeY = cola.getPosicionY();
                            System.out.println(ejeX + " " + ejeY);

                            if (ejeY + 1 == 10) {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            } else if (ejeY - 1 == -1) {
                                matriz[ejeX][ejeY + 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                            } else if (ejeX + 1 == 10) {
                                matriz[ejeX - 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                            } else if (ejeX - 1 == -1) {
                                matriz[ejeX + 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                            } else {
                                matriz[ejeX][ejeY + 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                            }

                        }

                        if ((cuerpoY == 0) && (x == 0)) {

                            mensaje();
                        } else if (x == 0) {

                            colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                            pisarColor = matriz[cuerpoX][cuerpoY - 1].getBackground();
                            if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                                mensaje();
                            }

                        }

                        if (x == 0) {
                            //la cabeza
                            //guarda la posicion actual
                            //PosicionAux = (Negra) serpiente.get(x);
                            PosicionAux = (Negra) serpiente.get(x);
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[cuerpoX][cuerpoY - 1].setBackground(Color.black);
                            serpiente.set(x, new Negra(cuerpoX, cuerpoY - 1, cuerpoXaux, cuerpoYaux));

                            derecha = false;
                            izquierda = true;
                            arriba = false;
                            abajo = false;

                        } else {
                            //PosicionAux = (Negra) serpiente.get(x);
                            int ejeX = PosicionAux.getPosicionAuxX();
                            int ejeY = PosicionAux.getPosicionAuxY();
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[ejeX][ejeY].setBackground(Color.black);
                            serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                            PosicionAux = (Negra) serpiente.get(x);

                            derecha = false;
                            izquierda = true;
                            arriba = false;
                            abajo = false;

                        }

                    }

                } else if (e.getKeyChar() == 'w') {
                    //para arriba funciona

                    for (int x = 0; x < serpiente.size(); x++) {
                        Negra cuerpo = (Negra) serpiente.get(x);
                        int cuerpoX = cuerpo.getPosicionX();
                        int cuerpoY = cuerpo.getPosicionY();

                        cuerpo.setPosicionAuxX(cuerpoX);
                        cuerpo.setPosicionAuxY(cuerpoY);
                        int cuerpoXaux = cuerpo.getPosicionAuxX();
                        int cuerpoYaux = cuerpo.getPosicionAuxY();
                        //obtengo ya las coordenadas de la serpiente                                                
                        System.out.println(cuerpoX + " " + cuerpoY);
                        //PosicionAux = (Negra) serpiente.get(x);                      

                        if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                            String audio = "audio/puntos.wav";
                            sonidoIntro(audio);

                            manzana = true;

                            matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                            int ultimo = serpiente.size() - 1;
                            Negra cola = (Negra) serpiente.get(ultimo);
                            int ejeX = cola.getPosicionX();
                            int ejeY = cola.getPosicionY();
                            System.out.println(ejeX + " " + ejeY);

                            if (ejeY + 1 == 10) {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            } else if (ejeY - 1 == -1) {
                                matriz[ejeX][ejeY + 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                            } else if (ejeX + 1 == 10) {
                                matriz[ejeX - 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                            } else if (ejeX - 1 == -1) {
                                matriz[ejeX + 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                            } else {
                                matriz[ejeX - 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                            }

                        }

                        if (cuerpoX == 0 && (x == 0)) {

                            mensaje();
                        } else if (x == 0) {

                            colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                            pisarColor = matriz[cuerpoX - 1][cuerpoY].getBackground();
                            if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                                mensaje();
                            }

                        }

                        if (x == 0) {
                            //la cabeza
                            //guarda la posicion actual
                            //PosicionAux = (Negra) serpiente.get(x);
                            PosicionAux = (Negra) serpiente.get(x);
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[cuerpoX - 1][cuerpoY].setBackground(Color.black);
                            serpiente.set(x, new Negra(cuerpoX - 1, cuerpoY, cuerpoXaux, cuerpoYaux));

                            derecha = false;
                            izquierda = false;
                            arriba = true;
                            abajo = false;

                        } else {
                            //PosicionAux = (Negra) serpiente.get(x);
                            int ejeX = PosicionAux.getPosicionAuxX();
                            int ejeY = PosicionAux.getPosicionAuxY();
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[ejeX][ejeY].setBackground(Color.black);
                            serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                            PosicionAux = (Negra) serpiente.get(x);

                            derecha = false;
                            izquierda = false;
                            arriba = true;
                            abajo = false;

                        }

                    }

                } else if (e.getKeyChar() == 's') {
                    //abajo funciona

                    for (int x = 0; x < serpiente.size(); x++) {
                        Negra cuerpo = (Negra) serpiente.get(x);
                        int cuerpoX = cuerpo.getPosicionX();
                        int cuerpoY = cuerpo.getPosicionY();

                        cuerpo.setPosicionAuxX(cuerpoX);
                        cuerpo.setPosicionAuxY(cuerpoY);
                        int cuerpoXaux = cuerpo.getPosicionAuxX();
                        int cuerpoYaux = cuerpo.getPosicionAuxY();
                        //obtengo ya las coordenadas de la serpiente                                                
                        System.out.println(cuerpoX + " " + cuerpoY);
                        //PosicionAux = (Negra) serpiente.get(x);

                        if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                            String audio = "audio/puntos.wav";
                            sonidoIntro(audio);

                            manzana = true;

                            matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                            int ultimo = serpiente.size() - 1;
                            Negra cola = (Negra) serpiente.get(ultimo);
                            int ejeX = cola.getPosicionX();
                            int ejeY = cola.getPosicionY();
                            System.out.println(ejeX + " " + ejeY);

                            if (ejeY + 1 == 10) {
                                matriz[ejeX][ejeY - 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                            } else if (ejeY - 1 == -1) {
                                matriz[ejeX][ejeY + 1].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                            } else if (ejeX + 1 == 10) {
                                matriz[ejeX - 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                            } else if (ejeX - 1 == -1) {
                                matriz[ejeX + 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                            } else {
                                matriz[ejeX + 1][ejeY].setBackground(Color.black);
                                serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                            }

                        }

                        if (cuerpoX == 9 && (x == 0)) {

                            mensaje();
                        } else if (x == 0) {

                            colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                            pisarColor = matriz[cuerpoX + 1][cuerpoY].getBackground();
                            if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                                mensaje();
                            }

                        }

                        if (x == 0) {
                            //la cabeza
                            //guarda la posicion actual
                            //PosicionAux = (Negra) serpiente.get(x);
                            PosicionAux = (Negra) serpiente.get(x);
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[cuerpoX + 1][cuerpoY].setBackground(Color.black);
                            serpiente.set(x, new Negra(cuerpoX + 1, cuerpoY, cuerpoXaux, cuerpoYaux));

                            derecha = false;
                            izquierda = false;
                            arriba = false;
                            abajo = true;

                        } else {
                            //PosicionAux = (Negra) serpiente.get(x);
                            int ejeX = PosicionAux.getPosicionAuxX();
                            int ejeY = PosicionAux.getPosicionAuxY();
                            matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                            matriz[ejeX][ejeY].setBackground(Color.black);
                            serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                            PosicionAux = (Negra) serpiente.get(x);

                            derecha = false;
                            izquierda = false;
                            arriba = false;
                            abajo = true;

                        }

                    }

                }

            }

        }
        );

    }

    //seguir movimiento al darle al boton
    public void seguirMovimiento() {
        if (derecha) {

            for (int x = 0; x < serpiente.size(); x++) {
                Negra cuerpo = (Negra) serpiente.get(x);
                int cuerpoX = cuerpo.getPosicionX();
                int cuerpoY = cuerpo.getPosicionY();

                cuerpo.setPosicionAuxX(cuerpoX);
                cuerpo.setPosicionAuxY(cuerpoY);
                int cuerpoXaux = cuerpo.getPosicionAuxX();
                int cuerpoYaux = cuerpo.getPosicionAuxY();
                //obtengo ya las coordenadas de la serpiente                                                
                System.out.println(cuerpoX + " " + cuerpoY);
                //PosicionAux = (Negra) serpiente.get(x);                        

                if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                    String audio = "audio/puntos.wav";
                    sonidoIntro(audio);

                    manzana = true;

                    //borrar foto                            
                    matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                    int ultimo = serpiente.size() - 1;
                    Negra cola = (Negra) serpiente.get(ultimo);
                    int ejeX = cola.getPosicionX();
                    int ejeY = cola.getPosicionY();
                    System.out.println(ejeX + " " + ejeY);

                    if (ejeY + 1 == 10) {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    } else if (ejeY - 1 == -1) {
                        matriz[ejeX][ejeY + 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                    } else if (ejeX + 1 == 10) {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    } else if (ejeX - 1 == -1) {
                        matriz[ejeX + 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                    } else {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    }

                }

                if ((cuerpoY == 9) && (x == 0)) {

                    mensaje();
                } else if (x == 0) {

                    colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                    pisarColor = matriz[cuerpoX][cuerpoY + 1].getBackground();
                    if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                        mensaje();
                    }

                }

                if (x == 0) {
                    //la cabeza
                    //guarda la posicion actual
                    //PosicionAux = (Negra) serpiente.get(x);
                    PosicionAux = (Negra) serpiente.get(x);
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[cuerpoX][cuerpoY + 1].setBackground(Color.black);
                    serpiente.set(x, new Negra(cuerpoX, cuerpoY + 1, cuerpoXaux, cuerpoYaux));

                    derecha = true;
                    izquierda = false;
                    arriba = false;
                    abajo = false;

                } else {
                    //PosicionAux = (Negra) serpiente.get(x);
                    int ejeX = PosicionAux.getPosicionAuxX();
                    int ejeY = PosicionAux.getPosicionAuxY();
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[ejeX][ejeY].setBackground(Color.black);
                    serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                    PosicionAux = (Negra) serpiente.get(x);

                    derecha = true;
                    izquierda = false;
                    arriba = false;
                    abajo = false;

                }

            }

        } else if (izquierda) {

            for (int x = 0; x < serpiente.size(); x++) {
                Negra cuerpo = (Negra) serpiente.get(x);
                int cuerpoX = cuerpo.getPosicionX();
                int cuerpoY = cuerpo.getPosicionY();

                cuerpo.setPosicionAuxX(cuerpoX);
                cuerpo.setPosicionAuxY(cuerpoY);
                int cuerpoXaux = cuerpo.getPosicionAuxX();
                int cuerpoYaux = cuerpo.getPosicionAuxY();
                //obtengo ya las coordenadas de la serpiente                                                
                System.out.println(cuerpoX + " " + cuerpoY);
                //PosicionAux = (Negra) serpiente.get(x);

                if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                    String audio = "audio/puntos.wav";
                    sonidoIntro(audio);

                    manzana = true;

                    matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                    int ultimo = serpiente.size() - 1;
                    Negra cola = (Negra) serpiente.get(ultimo);
                    int ejeX = cola.getPosicionX();
                    int ejeY = cola.getPosicionY();
                    System.out.println(ejeX + " " + ejeY);

                    if (ejeY + 1 == 10) {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    } else if (ejeY - 1 == -1) {
                        matriz[ejeX][ejeY + 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                    } else if (ejeX + 1 == 10) {
                        matriz[ejeX - 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                    } else if (ejeX - 1 == -1) {
                        matriz[ejeX + 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                    } else {
                        matriz[ejeX][ejeY + 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                    }

                }

                if ((cuerpoY == 0) && (x == 0)) {

                    mensaje();
                } else if (x == 0) {

                    colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                    pisarColor = matriz[cuerpoX][cuerpoY - 1].getBackground();
                    if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                        mensaje();
                    }

                }

                if (x == 0) {
                    //la cabeza
                    //guarda la posicion actual
                    //PosicionAux = (Negra) serpiente.get(x);
                    PosicionAux = (Negra) serpiente.get(x);
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[cuerpoX][cuerpoY - 1].setBackground(Color.black);
                    serpiente.set(x, new Negra(cuerpoX, cuerpoY - 1, cuerpoXaux, cuerpoYaux));

                    derecha = false;
                    izquierda = true;
                    arriba = false;
                    abajo = false;

                } else {
                    //PosicionAux = (Negra) serpiente.get(x);
                    int ejeX = PosicionAux.getPosicionAuxX();
                    int ejeY = PosicionAux.getPosicionAuxY();
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[ejeX][ejeY].setBackground(Color.black);
                    serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                    PosicionAux = (Negra) serpiente.get(x);

                    derecha = false;
                    izquierda = true;
                    arriba = false;
                    abajo = false;

                }

            }

        } else if (arriba) {

            for (int x = 0; x < serpiente.size(); x++) {
                Negra cuerpo = (Negra) serpiente.get(x);
                int cuerpoX = cuerpo.getPosicionX();
                int cuerpoY = cuerpo.getPosicionY();

                cuerpo.setPosicionAuxX(cuerpoX);
                cuerpo.setPosicionAuxY(cuerpoY);
                int cuerpoXaux = cuerpo.getPosicionAuxX();
                int cuerpoYaux = cuerpo.getPosicionAuxY();
                //obtengo ya las coordenadas de la serpiente                                                
                System.out.println(cuerpoX + " " + cuerpoY);
                //PosicionAux = (Negra) serpiente.get(x);                      

                if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                    String audio = "audio/puntos.wav";
                    sonidoIntro(audio);

                    manzana = true;

                    matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                    int ultimo = serpiente.size() - 1;
                    Negra cola = (Negra) serpiente.get(ultimo);
                    int ejeX = cola.getPosicionX();
                    int ejeY = cola.getPosicionY();
                    System.out.println(ejeX + " " + ejeY);

                    if (ejeY + 1 == 10) {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    } else if (ejeY - 1 == -1) {
                        matriz[ejeX][ejeY + 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                    } else if (ejeX + 1 == 10) {
                        matriz[ejeX - 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                    } else if (ejeX - 1 == -1) {
                        matriz[ejeX + 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                    } else {
                        matriz[ejeX - 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                    }

                }

                if (cuerpoX == 0 && (x == 0)) {

                    mensaje();
                } else if (x == 0) {

                    colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                    pisarColor = matriz[cuerpoX - 1][cuerpoY].getBackground();
                    if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                        mensaje();
                    }

                }

                if (x == 0) {
                    //la cabeza
                    //guarda la posicion actual
                    //PosicionAux = (Negra) serpiente.get(x);
                    PosicionAux = (Negra) serpiente.get(x);
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[cuerpoX - 1][cuerpoY].setBackground(Color.black);
                    serpiente.set(x, new Negra(cuerpoX - 1, cuerpoY, cuerpoXaux, cuerpoYaux));

                    derecha = false;
                    izquierda = false;
                    arriba = true;
                    abajo = false;

                } else {
                    //PosicionAux = (Negra) serpiente.get(x);
                    int ejeX = PosicionAux.getPosicionAuxX();
                    int ejeY = PosicionAux.getPosicionAuxY();
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[ejeX][ejeY].setBackground(Color.black);
                    serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                    PosicionAux = (Negra) serpiente.get(x);

                    derecha = false;
                    izquierda = false;
                    arriba = true;
                    abajo = false;

                }

            }

        } else if (abajo) {

            for (int x = 0; x < serpiente.size(); x++) {
                Negra cuerpo = (Negra) serpiente.get(x);
                int cuerpoX = cuerpo.getPosicionX();
                int cuerpoY = cuerpo.getPosicionY();

                cuerpo.setPosicionAuxX(cuerpoX);
                cuerpo.setPosicionAuxY(cuerpoY);
                int cuerpoXaux = cuerpo.getPosicionAuxX();
                int cuerpoYaux = cuerpo.getPosicionAuxY();
                //obtengo ya las coordenadas de la serpiente                                                
                System.out.println(cuerpoX + " " + cuerpoY);
                //PosicionAux = (Negra) serpiente.get(x);

                if ((coordenadaManzana[0] == cuerpoX) && (coordenadaManzana[1] == cuerpoY)) {

                    String audio = "audio/puntos.wav";
                    sonidoIntro(audio);

                    manzana = true;

                    matriz[coordenadaManzana[0]][coordenadaManzana[1]].removeAll();

                    int ultimo = serpiente.size() - 1;
                    Negra cola = (Negra) serpiente.get(ultimo);
                    int ejeX = cola.getPosicionX();
                    int ejeY = cola.getPosicionY();
                    System.out.println(ejeX + " " + ejeY);

                    if (ejeY + 1 == 10) {
                        matriz[ejeX][ejeY - 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY - 1, 0, 0));
                    } else if (ejeY - 1 == -1) {
                        matriz[ejeX][ejeY + 1].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX, ejeY + 1, 0, 0));
                    } else if (ejeX + 1 == 10) {
                        matriz[ejeX - 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX - 1, ejeY, 0, 0));
                    } else if (ejeX - 1 == -1) {
                        matriz[ejeX + 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                    } else {
                        matriz[ejeX + 1][ejeY].setBackground(Color.black);
                        serpiente.add(new Negra(ejeX + 1, ejeY, 0, 0));
                    }

                }

                if (cuerpoX == 9 && (x == 0)) {

                    mensaje();
                } else if (x == 0) {

                    colorActual = matriz[cuerpoX][cuerpoY].getBackground();
                    pisarColor = matriz[cuerpoX + 1][cuerpoY].getBackground();
                    if ((pisarColor.getRed() == colorActual.getRed() && pisarColor.getBlue() == colorActual.getBlue() && pisarColor.getGreen() == colorActual.getGreen()) && (x == 0)) {
                        mensaje();
                    }

                }

                if (x == 0) {
                    //la cabeza
                    //guarda la posicion actual
                    //PosicionAux = (Negra) serpiente.get(x);
                    PosicionAux = (Negra) serpiente.get(x);
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[cuerpoX + 1][cuerpoY].setBackground(Color.black);
                    serpiente.set(x, new Negra(cuerpoX + 1, cuerpoY, cuerpoXaux, cuerpoYaux));

                    derecha = false;
                    izquierda = false;
                    arriba = false;
                    abajo = true;

                } else {
                    //PosicionAux = (Negra) serpiente.get(x);
                    int ejeX = PosicionAux.getPosicionAuxX();
                    int ejeY = PosicionAux.getPosicionAuxY();
                    matriz[cuerpoX][cuerpoY].setBackground(Color.gray);
                    matriz[ejeX][ejeY].setBackground(Color.black);
                    serpiente.set(x, new Negra(ejeX, ejeY, cuerpoXaux, cuerpoYaux));
                    PosicionAux = (Negra) serpiente.get(x);

                    derecha = false;
                    izquierda = false;
                    arriba = false;
                    abajo = true;

                }

            }

        }

    }

    //pone las manzajas en el tablero
    public void manzanas() {
        if (manzana) {
            Color color;
            int x;
            int y;

            do {

                x = (int) Math.floor(Math.random() * 8 + 1);
                y = (int) Math.floor(Math.random() * 8 + 1);

                color = matriz[x][y].getBackground();
                coordenadaManzana[0] = x;
                coordenadaManzana[1] = y;

            } while ((color.getRed() == 0) && (color.getBlue() == 0) && (color.getGreen() == 0));

            JLabel imagen = new JLabel();
            //añadimos la imagen
            String nombre = "img/manzana.png";
            ImageIcon imageicon = new ImageIcon(nombre);
            //se crea la imagen y se escala a lo que le hemos dicho con Image.SCALE_DEFAULT
            Icon icon = new ImageIcon(imageicon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
            //se añade la imagen a la etiqueta
            imagen.setIcon(icon);
            imagen.setBounds(0, 0, 100, 100);
            //se añade la etiqueta al panel
            //panelItro.add(imagen, BorderLayout.EAST);
            matriz[x][y].add(imagen);

            matriz[x][y].setBackground(Color.GREEN);
            manzana = false;

        }

    }

    //detecta las manzanas
    public void detectarManzanas() {
        Color color;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {

                color = matriz[x][y].getBackground();

                if (color.getRed() == 0 && color.getBlue() == 0 && color.getGreen() == 255) {
                    coordenadaManzana[0] = x;
                    coordenadaManzana[1] = y;
                }

            }
        }
        // System.out.println(coordenadaManzana[0] + " " + coordenadaManzana[1]);
    }

    public void mensaje() {
        int ventana = JOptionPane.showOptionDialog(null,
                "Game Over",
                "Snake",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                botones, botones[0]);
        if (ventana == 0) {
            System.exit(0);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JuegoSerpiente juego = new JuegoSerpiente();
    }

}
