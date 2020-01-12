import java.io.Serializable;

public class JogoCliente implements Serializable {
    private static final long serialVersionUID = 1L; //salvar o estado atual dos objetos no formato binário para o computador

    private String nome = "";
    private int coordX;
    private int coordY;
    boolean ok = false;
    boolean restart = false;

    public JogoCliente(String nome){
        this.nome = nome;
        coordX = 740;
        coordY = 210;
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

    @Override
    public String toString(){
        return "JogoCliente [nome= " + nome + ", coordenadaX= " + coordX + ", coordenadaY= " + coordY + "]";
    }
}
