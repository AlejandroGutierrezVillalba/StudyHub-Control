public class Puesto 
{
    private int ID;
    private int numeroDePuesto;
    private String aula;
    private String tipoDePuesto;
    private boolean estaReservado;

    public Puesto(int ID, int numeroDePuesto, String aula, String tipoDePuesto, boolean estaReservado) 
    {
        this.ID = ID;
        this.numeroDePuesto = numeroDePuesto;
        this.aula = aula;
        this.tipoDePuesto = tipoDePuesto;
        this.estaReservado = estaReservado;
    }

    public Puesto() {}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getNumeroDePuesto() {
        return numeroDePuesto;
    }

    public void setNumeroDePuesto(int numeroDePuesto) {
        this.numeroDePuesto = numeroDePuesto;
    }
    public String getAula() { 
        return aula; 
    }

    public void setAula(String aula) { 
        this.aula = aula; 
    }

    public String getTipoDePuesto() {
        return tipoDePuesto;
    }

    public void setTipoDePuesto(String tipoDePuesto) {
        this.tipoDePuesto = tipoDePuesto;
    }

    public boolean isEstaReservado() {
        return estaReservado;
    }

    public void setEstaReservado(boolean estaReservado) {
        this.estaReservado = estaReservado;
    } 
    
    @Override
    public String toString() {
        String disponibilidad = estaReservado ? "OCUPADO" : "LIBRE";
        return String.format("Puesto #%d | Aula=%s | Tipo=%s | %s",
                numeroDePuesto, aula, tipoDePuesto, disponibilidad);
    }
}