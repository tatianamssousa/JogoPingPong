import java.io.Serializable;

public class JogoServidor implements Serializable{
    private static final long serialVersionUID = 1L;

    private String nome;
    private int coordX;
    private int coordY;
    private int bolaX;
    private int bolaY;
    private int scoreS = 0;
    private int scoreP = 0;
    private String imessage = "";
    private String omessage = "";
    private boolean restart = false;

    public boolean isRestart(){
        return this.restart;
    }

    public void setRestart(boolean restart){
        this.restart = restart;
    }

    public JogoServidor(){
        coordX = 50;
        coordY = 200;
        bolaX = 380;
        bolaY = 230;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCoordX() {
        return this.coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return this.coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public int getBolaX() {
        return this.bolaX;
    }

    public void setBolaX(int bolaX) {
        this.bolaX = bolaX;
    }

    public int getBolaY() {
        return this.bolaY;
    }

    public void setBolaY(int bolaY) {
        this.bolaY = bolaY;
    }

    public int getScoreP() {
        return this.scoreP;
    }

    public void setScoreP(int scoreP) {
        this.scoreP = scoreP;
    }

    public int getScoreS() {
        return this.scoreS;
    }

    public void setScoreS(int scoreS) {
        this.scoreS = scoreS;
    }

    public String getImessage() {
        return this.imessage;
    }

    public void setImessage(String imessage) {
        this.imessage = imessage;
    }

    public String getOmessage() {
        return this.omessage;
    }

    public void setOmessage(String omessage) {
        this.omessage = omessage;
    }
}
