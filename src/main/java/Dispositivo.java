public abstract class Dispositivo 
{
    private String marca;
    private String modelo;
    private String tipo;
    private int ID;
    private String estado;

    public Dispositivo(String marca, String modelo, String tipo, int ID, String estado) 
    {
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ID = ID;
        this.estado = estado;
    }

    public Dispositivo() {}

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //Devuelve true si el dispositivo está disponible para préstamo//
   
    public boolean isDisponible() 
    {
        return "disponible".equalsIgnoreCase(this.estado);
    }

    @Override
    public String toString() 
    {
        return String.format("[%s] ID=%d | %s %s | Estado=%s",
                this.getClass().getSimpleName(), ID, marca, modelo, estado);
    }
}