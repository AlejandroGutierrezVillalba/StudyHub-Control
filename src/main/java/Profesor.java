public class Profesor extends Usuario
{
   private String materia;
   
   public Profesor(String nombre, String apellido1, String apellido2, String correo, String estado, int ID, int DNI, String materia) 
   {
       super(nombre, apellido1, apellido2, correo, estado, ID, DNI);
       this.materia = materia;
   }

    public String getMateria() 
    {
        return materia;
    }

    public void setMateria(String materia) 
    {
        this.materia = materia;
    }

     @Override
    public String toString() 
    {
        return super.toString() + String.format(" | Materia=%s", materia);
    }
}