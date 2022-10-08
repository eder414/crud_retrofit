package curso.clases.crud_retrofit.Models;

public class PostUsuario {
    String usuario_nombre, usuario_password,action;
    int usuario_edad,usuario_sexo;
    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public String getUsuario_password() {
        return usuario_password;
    }

    public void setUsuario_password(String usuario_password) {
        this.usuario_password = usuario_password;
    }

    public int getUsuario_sexo() {
        return usuario_sexo;
    }

    public void setUsuario_sexo(int usuario_sexo) {
        this.usuario_sexo = usuario_sexo;
    }

    public int getUsuario_edad() {
        return usuario_edad;
    }

    public void setUsuario_edad(int usuario_edad) {
        this.usuario_edad = usuario_edad;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
