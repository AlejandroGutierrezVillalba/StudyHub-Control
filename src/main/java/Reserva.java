import java.time.LocalDate;
import java.time.LocalTime;

//Reserva de un puesto por parte de un usuario en una fecha y franja horaria concreta//
public class Reserva 
{
    private int ID;
    private Usuario usuario;      
    private Puesto puesto;
    private LocalDate fecha;       
    private LocalTime horaInicio;  
    private LocalTime horaFin;     
    private String estado;

    public Reserva(int ID, Usuario usuario, Puesto puesto,LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) 
    {
        this.ID = ID;
        this.usuario = usuario;
        this.puesto = puesto;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = "activa";
    }

    public Reserva() {}

    //Getters y Setters//

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Puesto getPuesto() { return puesto; }
    public void setPuesto(Puesto puesto) { this.puesto = puesto; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    //Cancela esta reserva y libera el puesto asociado//
    public void cancelar() 
    {
        this.estado = "cancelada";
        if (this.puesto != null) {
            this.puesto.setEstaReservado(false);
        }
    }

    @Override
    public String toString() 
    {
        return String.format("Reserva ID=%d | Usuario=%s | %s | Fecha=%s | %s-%s | Estado=%s",
                ID,
                usuario != null ? usuario.getNombre() + " " + usuario.getApellido1() : "N/A",
                puesto != null ? puesto.toString() : "N/A",
                fecha,
                horaInicio,
                horaFin,
                estado);
    }
}