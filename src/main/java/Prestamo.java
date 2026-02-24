import java.time.LocalDate;

//Representa el préstamo de un dispositivo y la devolución//

public class Prestamo 
{
    private int ID;
    private Usuario usuario;
    private Dispositivo dispositivo;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    private String estado;

    public Prestamo(int ID, Usuario usuario, Dispositivo dispositivo,
                    LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista) 
    {
        this.ID = ID;
        this.usuario = usuario;
        this.dispositivo = dispositivo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = null;
        this.estado = "activo";
    }

    public Prestamo() {}

    //Getters y Setters//

    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Dispositivo getDispositivo() { return dispositivo; }
    public void setDispositivo(Dispositivo dispositivo) { this.dispositivo = dispositivo; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaDevolucionPrevista() { return fechaDevolucionPrevista; }
    public void setFechaDevolucionPrevista(LocalDate f) { this.fechaDevolucionPrevista = f; }

    public LocalDate getFechaDevolucionReal() { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(LocalDate f) { this.fechaDevolucionReal = f; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // --- Lógica de negocio ---

    //Registra la devolución del dispositivo en la fecha indicada.Marca el préstamo como devuelto y libera el dispositivo//

    public void registrarDevolucion(LocalDate fechaReal) 
    {
        this.fechaDevolucionReal = fechaReal;
        this.estado = "devuelto";
        if (this.dispositivo != null) {
            this.dispositivo.setEstado("disponible");
        }
    }

    //Calcula los días de retraso respecto a la fecha prevista. Usa la fecha real si ya fue devuelto, o la fecha de hoy si sigue activo//
     
    public long getDiasRetraso() {
        LocalDate referencia = (fechaDevolucionReal != null) ? fechaDevolucionReal : LocalDate.now();
        long dias = referencia.toEpochDay() - fechaDevolucionPrevista.toEpochDay();
        return Math.max(0, dias);
    }

    //Devuelve true si el préstamo tiene retraso//
    
    public boolean tieneRetraso() {
        return getDiasRetraso() > 0;
    }

    @Override
    public String toString() 
    {
        String devolucion = (fechaDevolucionReal != null) ? fechaDevolucionReal.toString() : "Pendiente";
        String retraso = tieneRetraso() ? "RETRASO" + getDiasRetraso() + " días" : "";
        return String.format("Préstamo ID=%d | Usuario=%s | Dispositivo=%s | Prestado=%s | Previsto=%s | Devuelto=%s | Estado=%s%s",
                ID,
                usuario != null ? usuario.getNombre() + " " + usuario.getApellido1() : "N/A",
                dispositivo != null ? dispositivo.toString() : "N/A",
                fechaPrestamo,
                fechaDevolucionPrevista,
                devolucion,
                estado,
                retraso);
    }
}