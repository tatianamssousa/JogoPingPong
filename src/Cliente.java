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
import java.net.Socket;

public class Cliente extends JFrame implements KeyListener, Runnable, WindowListener {

    private static final long serialVersionUID = 1L;

    private static final String titulo = "ping-pong::cliente";
    private static final int largura = 800; //da janela
    private static final int altura = 460; //da janela
    boolean isRunning = false;

    private JogoCliente jogadorC;
    private JogoServidor jogadorS;
    private int barraLargura = 30;
    private int barraAltura = 120;
    private int barraMovimento = 5;

    private static Socket socketCliente;
    private int portaAdd;
    private String ipAdd;
    private boolean reset = false;
    private int contS = 0;

    private Graphics g;
    private Font sFont = new Font("TimesRoman", Font.BOLD, 90);
    private Font mFont = new Font("TimesRoman", Font.BOLD, 50);
    private Font nFont = new Font("TimesRoman", Font.BOLD, 32);
    private Font rFont = new Font("TimesRoman", Font.BOLD, 18);
    private  String[] message; //mensagem separada em um array

    public Cliente(String nomeCliente, String portaAdd, String ipAdd){
        jogadorS = new JogoServidor();
        jogadorC = new JogoCliente(nomeCliente);
        jogadorS.setNome(nomeCliente);

        this.ipAdd = ipAdd;
        this.portaAdd = Integer.parseInt(portaAdd);
        this.isRunning = true;
        //janela
        this.setTitle(titulo);
        this.setSize(largura, altura);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        addKeyListener(this);

    }

    @Override
    public void run() {
        try {
            System.out.println("Procurando servidor...\nConectando a " + ipAdd + ":" + portaAdd);
            socketCliente = new Socket(ipAdd, portaAdd);
            System.out.println("Conectado ao servidor...");

            if (socketCliente.isConnected()){
                System.out.println("TEST");

                while (true){
                    ObjectOutputStream sendObj = new ObjectOutputStream(socketCliente.getOutputStream());
                    sendObj.writeObject(jogadorC);
                    sendObj = null;

                    ObjectInputStream getObj = new ObjectInputStream(socketCliente.getInputStream());
                    jogadorS = (JogoServidor) getObj.readObject();
                    getObj = null;

                    if (reset){
                        if (contS > 5){
                            jogadorC.restart = false;
                            reset = false;
                            contS = 0;
                        }
                    }
                    contS++;
                    repaint();
                }
            } else {
                System.out.println("Desconectando...");
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int chave = e.getKeyCode();
        if (chave == KeyEvent.VK_UP){
            moverCima();
            repaint();
        }
        if (chave == KeyEvent.VK_DOWN){
            moverBaixo();
            repaint();
        }
        if (jogadorS.isRestart()){
            jogadorC.restart = true;
            reset = true;
        }
        if (chave == KeyEvent.VK_ESCAPE || chave == KeyEvent.VK_N && jogadorS.isRestart()){
            try {
                this.setVisible(false);
                socketCliente.close();
                System.exit(EXIT_ON_CLOSE);
            } catch (IOException ex){
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
            socketCliente.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    private Image criarImagem(){
        BufferedImage imgBuffer = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        g = imgBuffer.createGraphics();
        //mesa
        g.setColor(new Color(15, 9, 9));
        g.fillRect(0, 0, largura, altura);
        //linhas
        g.setColor(Color.white);
        g.fillRect((largura/2) - 5, 0, 5, altura);
        g.fillRect((largura/2) + 5, 0, 5, altura);
        //score
        g.setColor(new Color(228, 38, 36));
        g.setFont(sFont);
        g.drawString("" + jogadorS.getScoreS(), (largura/2) - 60, 120);
        g.drawString("" + jogadorS.getScoreP(), (largura/2) + 15, 120);
        //nome jogadores
        g.setFont(nFont);
        g.setColor(Color.white);
        g.drawString(jogadorS.getNome(), largura/10, altura-20);
        g.drawString(jogadorC.getNome(), 600, altura-20);
        //barra
        g.setColor(new Color(57, 181, 74));
        g.fillRect(jogadorS.getCoordX(), jogadorS.getCoordY(), barraLargura, barraAltura);
        g.setColor(new Color(57, 181, 74));
        g.fillRect(jogadorC.getCoordX(), jogadorC.getCoordY(), barraLargura, barraAltura);
        //bola
        g.setColor(new Color(255, 255, 255));
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

    public void paint (Graphics g){
        g.drawImage(criarImagem(), 0, 0, this);
        jogadorC.ok = true;
    }

    public void moverCima(){
        if (jogadorC.getCoordY() - barraMovimento > (barraAltura/2) - 10){
            jogadorC.setCoordY(jogadorC.getCoordY() - barraMovimento);
        }
    }

    public void moverBaixo(){
        if (jogadorC.getCoordY() + barraMovimento < altura - barraAltura - 30){
            jogadorC.setCoordY(jogadorC.getCoordY() + barraMovimento);
        }
    }


}
