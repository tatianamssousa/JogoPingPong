public class Bola extends Thread {
    private int coordX;
    private int coordY;
    private double velocidadeX;
    private double velocidadeY;
    private int raio;
    private int altura;
    private int largura;

    @Override
    public void run(){
        while (true){
            move();
            try {
                sleep(10);
            } catch (InterruptedException e){
                e.printStackTrace(); //identifica o erro
            }
        }
    }

    public Bola(int coordX, int coordY, double velocidadeX, double velocidadeY, int raio, int largura, int altura){
        super();
        this.coordX = coordX;
        this.coordY = coordY;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
        this.raio = raio;
        this.largura = largura;
        this.altura = altura;
    }
    //calibrar a bola na tela
    public void move(){
        if (coordX + velocidadeX > (largura - raio)-7){
            coordX = (largura-raio)-7;
            velocidadeX *= -1;
        }

        if (coordX + velocidadeX < 9){
            coordX = 9;
            velocidadeX *= -1;
        }

        if (coordY + velocidadeY < (raio/2)+7){
            coordY = 29;
            velocidadeY *= -1;
        }

        if (coordY + velocidadeY > (altura-raio) - 6){
            coordY = (altura-raio) - 6;
            velocidadeY *= -1;
        }
        coordX += velocidadeX;
        coordY += velocidadeY;
    }

    public int getCoordX(){
       return this.coordX;
    }

    public void setCoordX(int coordX){
        this.coordX = coordX;
    }

    public int getCoordY() {
        return this.coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public double getVelocidadeX() {
        return this.velocidadeX;
    }

    public void setVelocidadeX(double velocidadeX) {
        this.velocidadeX = velocidadeX;
    }

    public double getVelocidadeY() {
        return this.velocidadeY;
    }

    public void setVelocidadeY(double velocidadeY) {
        this.velocidadeY = velocidadeY;
    }

    public int getRaio() {
        return this.raio;
    }

    public void setRaio(int raio) {
        this.raio = raio;
    }


}
