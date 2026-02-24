public class Alumno extends Usuario
{
    private String curso;
    private String tutor;
    private int edad;

    public Alumno(String nombre, String apellido1, String apellido2, String correo, String estado, int ID, int DNI, String curso, String tutor, int edad) 
    {
        super(nombre, apellido1, apellido2, correo, estado, ID, DNI);
        this.curso = curso;
        this.tutor = tutor;
        this.edad = edad;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | Curso=%s | Tutor=%s | Edad=%d", curso, tutor, edad);
    }
}