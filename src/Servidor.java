import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends JFrame implements KeyListener, Runnable, WindowListener {
    private static final long serialVersionUID = 1L;

    private static  final  String titulo = "ping-pong::server";
    private static final int largura = 800;
    private static final int altura = 460;

    boolean isRunning = false;
    boolean check = true;
    boolean initgame = false;

    Bola movBola;
    private JogoServidor jogadorS;
    private JogoCliente jogadorC;

    private int velocidadeBola = 4;
    private int larguraBarra = 30;
    private int alturaBarra = 120;
    private int maxScore = 5;
    private int barraMovimento = 5;
    private boolean restart = false;
    private boolean restartOn = false;

    private static Socket socketCliente = null;
    private static ServerSocket socketServidor = null;
    private int portaAdd;

    private Graphics g;
    private Font sFont = new Font("TimesRoman", Font.BOLD, 90);
    private Font mFont = new Font("TimesRoman", Font.BOLD, 50);
    private Font nFont = new Font("TimesRoman", Font.BOLD, 32);
    private Font rFont = new Font("TimesRoman", Font.BOLD, 18);
    private String[] message;
    private Thread movB;

    public Servidor(String nomeServidor, String portaAdd){
        jogadorS = new JogoServidor();
        jogadorC = new JogoCliente("");
        jogadorS.setNome(nomeServidor);

        this.portaAdd = Integer.parseInt(portaAdd);
        this.isRunning = true;
        this.setTitle(titulo + "::porta número[" + portaAdd + "]");
        this.setSize(largura, altura);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        movBola = new Bola(jogadorS.getBolaX(), jogadorS.getBolaY(), velocidadeBola, velocidadeBola,
                45, largura, altura);

        addKeyListener(this);

        }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if(keycode == KeyEvent.VK_UP){
            moverCima();
            repaint();
        }
        if(keycode == KeyEvent.VK_DOWN){
            moverBaixo();
            repaint();
        }
        if(restart == true){
            restartOn = true;
            jogadorS.setRestart(true);
        }
        if(keycode == KeyEvent.VK_N || keycode == KeyEvent.VK_ESCAPE && restart == true){
            try {
                this.setVisible(false);
                socketServidor.close();
                System.exit(EXIT_ON_CLOSE);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void windowClosing(WindowEvent e) {
        Thread.currentThread().stop();
        this.setVisible(false);
        try {
            socketServidor.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.exit(1);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        try {
            socketServidor = new ServerSocket(portaAdd);
            System.out.println("Servidor na porta " + portaAdd + " port.\nEsperando por um jogador...");
            System.out.println("Esperando por conexão...");
            jogadorS.setImessage("Esperando por um jogador");
            socketCliente = socketServidor.accept();

            System.out.println("Conectado a um jogador...");

            if (socketCliente.isConnected()){
                boolean notchecked = true;
                movB = new Thread(movBola);
                while (true){
                    if (jogadorS.getScoreP() >= maxScore || jogadorS.getScoreS() >= maxScore && restart == false){
                        if (jogadorS.getScoreS() > jogadorS.getScoreP()){
                            jogadorS.setOmessage("Won            Loss - Sair: Esc|N");
                            jogadorS.setImessage("Won            Loss - Sair: ESC");
                            restart = true;
                        } else {
                            jogadorS.setImessage("Loss            Won -  Sair: Esc|N");
                            jogadorS.setOmessage("Loss            Won - Sair: Esc|N");
                            restart = true;
                        }
                        movB.suspend();
                    }

                    if (jogadorC.ok && notchecked){
                        jogadorS.setImessage("");
                        movB.start();
                        notchecked = false;
                    }
                    updateBola();
                    ObjectInputStream getObj = new ObjectInputStream(socketCliente.getInputStream());
                    jogadorC = (JogoCliente) getObj.readObject();
                    getObj = null;
                    ObjectOutputStream sendObj = new ObjectOutputStream(socketCliente.getOutputStream());
                    sendObj.writeObject(jogadorS);
                    sendObj = null;

                    if (restartOn){
                        if (jogadorC.restart){
                            jogadorS.setScoreP(0);
                            jogadorS.setScoreS(0);
                            jogadorS.setOmessage("");
                            jogadorS.setImessage("");
                            restart = false;
                            jogadorS.setRestart(false);
                            jogadorS.setBolaX(380);
                            jogadorS.setBolaY(230);
                            movBola.setCoordX(380);
                            movBola.setCoordY(230);
                            movB.resume();
                            restartOn = false;
                        }
                    }
                    repaint();
                }
            } else {
                System.out.println("Desconectando...");
            }
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    private Image criarImagem(){
        BufferedImage imgBuffer = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        g = imgBuffer.createGraphics();
        //mesa
        g.setColor(new Color(15, 9, 9));
        g.fillRect(0, 0, largura, altura);
        //linha
        g.setColor(Color.white);
        g.fillRect((largura/2)-5, 0, 5, altura);
        g.fillRect((largura/2)+5, 0, 5, altura);
        //score
        g.setFont(sFont);
        g.setColor(new Color(228,38, 36));
        g.drawString("" + jogadorS.getScoreS(), (largura/2)-60, 120);
        g.drawString("" + jogadorS.getScoreP(), (largura/2)+15, 120);
        //nomes
        g.setFont(nFont);
        g.setColor(Color.white);
        g.drawString(jogadorS.getNome(), largura/10, altura-20);
        g.drawString(jogadorC.getNome(), 600, altura-20);
        //barra
        g.setColor(new Color(57, 181, 74));
        g.fillRect(jogadorS.getCoordX(), jogadorS.getCoordY(), larguraBarra, alturaBarra);
        g.setColor(new Color(57, 181, 74));
        g.fillRect(jogadorC.getCoordX(), jogadorC.getCoordY(), larguraBarra, alturaBarra);
        //bola
        g.setColor(new Color(255,255, 255));
        g.fillOval(jogadorS.getBolaX(), jogadorS.getBolaY(), 45, 45);
        g.setColor(new Color(228, 38, 36));
        g.fillOval(jogadorS.getBolaX()+5, jogadorS.getBolaY()+5, 35, 35);
        //mensagem
        message = jogadorS.getImessage().split("-");
        g.setFont(mFont);
        g.setColor(Color.white);
        if (message.length != 0){
            g.drawString(message[0], (largura/4)-31, (altura/2)+38);
            if (message.length > 1){
                if (message[1].length() > 6){
                    g.setFont(rFont);
                    g.setColor(new Color(228, 38, 36));
                    g.drawString(message[1], (largura/4)-31, (altura/2)+100);
                }
            }
        }
        return imgBuffer;
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(criarImagem(),0, 0, this);
    }

    public void updateBola(){
        checkCol();
        jogadorS.setBolaX(movBola.getCoordX());
        jogadorS.setBolaY(movBola.getCoordY());
    }

    public void moverCima(){
        if (jogadorS.getCoordY() - barraMovimento > (alturaBarra/2)-10){
            jogadorS.setCoordY(jogadorS.getCoordY()-barraMovimento);
        }
    }

    public void moverBaixo(){
        if (jogadorS.getCoordY() + barraMovimento < altura - alturaBarra - 30){
            jogadorS.setCoordY(jogadorS.getCoordY() + barraMovimento);
        }
    }
    //colisões
    public void checkCol(){
        if (jogadorS.getBolaX() < jogadorC.getCoordX() && jogadorS.getBolaX() > jogadorS.getCoordX()){
            check = true;
        }
        if(jogadorS.getBolaX() > jogadorC.getCoordX() && check){

            jogadorS.setScoreS(jogadorS.getScoreS()+1);

            check = false;
        }
        else if (jogadorS.getBolaX() <= jogadorS.getCoordX() && check){

            jogadorS.setScoreP(jogadorS.getScoreP()+1);

            check = false;

        }
        if((movBola.getCoordX() <= (jogadorS.getCoordX() + larguraBarra)) && ((movBola.getCoordY() +
                movBola.getRaio()) >= jogadorS.getCoordY()) && (movBola.getCoordY() <= (jogadorS.getCoordY() +
                alturaBarra))){
            movBola.setCoordX(jogadorS.getCoordX() + larguraBarra);
            jogadorS.setBolaX(jogadorS.getCoordX() + larguraBarra);
            movBola.setVelocidadeX(movBola.getVelocidadeX()*-1);
        }
        if(((movBola.getCoordX() + movBola.getRaio()) >= jogadorC.getCoordX()) && ((movBola.getCoordY() +
                movBola.getRaio()) >= jogadorC.getCoordY()) && (movBola.getCoordY() <= (jogadorC.getCoordY() +
                alturaBarra))){
            movBola.setCoordX(jogadorC.getCoordX() - movBola.getRaio());
            jogadorS.setBolaX(jogadorC.getCoordX() - movBola.getRaio());
            movBola.setVelocidadeX(movBola.getVelocidadeX()*-1);
        }
    }


}
