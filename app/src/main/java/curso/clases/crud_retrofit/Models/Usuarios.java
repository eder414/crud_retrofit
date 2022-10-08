package curso.clases.crud_retrofit.Models;

public class Usuarios {
    String usuario_id,
            usuario_nombre,
            usuario_sexo,
            usuario_edad,
            usuario_password;

    public String getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(String usuario_id) {
        this.usuario_id = usuario_id;
    }

    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public String getUsuario_sexo() {
        return usuario_sexo;
    }

    public void setUsuario_sexo(String usuario_sexo) {
        this.usuario_sexo = usuario_sexo;
    }

    public String getUsuario_edad() {
        return usuario_edad;
    }

    public void setUsuario_edad(String usuario_edad) {
        this.usuario_edad = usuario_edad;
    }

    public String getUsuario_password() {
        return usuario_password;
    }

    public void setUsuario_password(String usuario_password) {
        this.usuario_password = usuario_password;
    }
}
