public abstract class Usuario 
{
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String estado; //baja o alta//
    private int ID;
    private int DNI;


    public Usuario(String nombre, String apellido1, String apellido2, String correo, String estado, int ID, int DNI) 
    {
        this.nombre = nombre;
        this.correo = correo;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.estado = estado;
        this.ID = ID;
        this.DNI = DNI;
    }
    public Usuario(){}

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDNI() {
        return DNI;
    }

    public void setDNI(int DNI) {
        this.DNI = DNI;
    }
    //devuelve si el usuario está activo//
    public boolean isActivo() {
        return "alta".equalsIgnoreCase(this.estado);
    }

    @Override
    public String toString() {
        return String.format("[%s] ID=%d | %s %s %s | DNI=%d | Email=%s | Estado=%s",
                this.getClass().getSimpleName(), ID, nombre, apellido1, apellido2, DNI, correo, estado);
    }
}