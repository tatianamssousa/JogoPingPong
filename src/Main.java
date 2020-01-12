import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

public class Main extends JFrame implements KeyListener, Runnable{
    private static final long serialVersionUID = 1L;

    private static Image img;
    private Graphics g;
    private static final String titulo  = "ping-pong::network_game";
    private static final int    largura  = 800;
    private static final int    altura = 460;
    private String nomeServidor = "servername" , nomeCliente = "clientname";

    public Main(){

    }

    @Override
    public void run() {
        this.setVisible(true);
        this.setTitle(titulo);
        this.setSize(largura,altura);
        this.setResizable(false);
        this.addKeyListener(this);
    }

    public static void main(String[] args){
        Toolkit tk = Toolkit.getDefaultToolkit();
        img = tk.getImage("background_pong.jpg");
        Main newM = new Main();
        newM.run();

    }

    private Image criarImagem(){

        BufferedImage imgBuffer = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
        g = imgBuffer.createGraphics();
        g.fillRect(0, 0, largura, altura);
        g.drawImage(img,0, 0, this);
        return imgBuffer;

    }

    @Override
    public void paint(Graphics g){
        g.drawImage(criarImagem(), 0, 20, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int    keyCode = e.getKeyCode();
        String portAdd = null;
        String ipAdd   = null;
        if(keyCode == KeyEvent.VK_S) {
            portAdd = JOptionPane.showInputDialog(null, "ex. 1024", "Informe a porta do " +
                    "servidor:", 1);
            if (portAdd != null) {
                if (!isPort(portAdd)) {
                    JOptionPane.showMessageDialog(null, "Informe o número com formato " +
                            "correto!", "Error!", 1);
                } else {
                    nomeServidor = JOptionPane.showInputDialog(null, "Usuário:",
                            "Informe o nome do servidor:", 1);
                    nomeServidor += "";

                    if (nomeServidor.length() > 10 || nomeServidor.length() < 3 || nomeServidor.startsWith("null")) {
                        JOptionPane.showMessageDialog(null, "Informe o nome no formato " +
                                "correto!", "Error!", 1);
                    } else {
                        Servidor myServer = new Servidor(nomeServidor, portAdd);
                        Thread myServerT = new Thread(myServer);
                        myServerT.start();
                        this.setVisible(false);
                    }
                }
            }
        }
        if(keyCode == KeyEvent.VK_C){
            ipAdd = JOptionPane.showInputDialog(null, "ex. 127.0.0.1", "Informe o " +
                    "IP do servidor:", 1);
            if(ipAdd != null){
                if(!isIPAddress(ipAdd)){
                    JOptionPane.showMessageDialog(null, "Informe o número de " +
                            "IP no formato correto!", "Informe o IP do servidor:", 1);
                }
                else{
                    portAdd = JOptionPane.showInputDialog(null, "ex. 1024", "Informe " +
                            "a porta do servidor:", 1);
                    if(portAdd != null){
                        if(!isPort(portAdd)){
                            JOptionPane.showMessageDialog(null, "Informe o número da porta " +
                                    "no formato correto!", "Error!:", 1);
                        }
                        else{
                            nomeCliente = JOptionPane.showInputDialog(null, "Usuário:",
                                    "Informe o nome do usuário:", 1);
                            nomeCliente += "";
                            if(nomeCliente.length() > 10 || nomeCliente.length() < 3 || nomeCliente.startsWith("null")){
                                JOptionPane.showMessageDialog(null, "Informe o nome no" +
                                        " formato correto!", "Error!", 1);
                            }
                            else{
                                Cliente myClient = new Cliente(nomeCliente, portAdd, ipAdd);
                                Thread myClientT = new Thread(myClient);
                                myClientT.start();
                                this.setVisible(false);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private boolean isPort(String str) {
        Pattern pPattern = Pattern.compile("\\d{1,4}");
        return pPattern.matcher(str).matches();
    }

    private boolean isIPAddress(String str) {
        Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        return ipPattern.matcher(str).matches();
    }
}
