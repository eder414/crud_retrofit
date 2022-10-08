package curso.clases.crud_retrofit.Interfaces;

import java.util.ArrayList;
import java.util.List;

import curso.clases.crud_retrofit.Models.InsertResponse;
import curso.clases.crud_retrofit.Models.PostUsuario;
import curso.clases.crud_retrofit.Models.Usuarios;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServicesRetrofit {
    @Multipart
    @POST("WsAndroid.php")
    Call<ArrayList<InsertResponse>> InsertaUsuario(@Part("usuario_nombre") RequestBody usuario_nombre,
                                                   @Part("usuario_password") RequestBody usuario_password,
                                                   @Part("usuario_sexo") RequestBody usuario_sexo,
                                                   @Part("usuario_edad") RequestBody usuario_edad,
                                                   @Part("action") RequestBody action);

    @Multipart
    @POST("WsAndroid.php")
    Call<ArrayList<InsertResponse>> ActualizaUsuario(@Part("id") RequestBody id,
                                                    @Part("usuario_nombre") RequestBody usuario_nombre,
                                                   @Part("usuario_password") RequestBody usuario_password,
                                                   @Part("usuario_sexo") RequestBody usuario_sexo,
                                                   @Part("usuario_edad") RequestBody usuario_edad,
                                                   @Part("action") RequestBody action);
    @Multipart
    @POST("WsAndroid.php")
    Call<ArrayList<InsertResponse>> BorrarUsuario(@Part("id") RequestBody id,
                                                     @Part("action") RequestBody action);

    @Multipart
    @POST("WsAndroid.php")
    Call<ArrayList<Usuarios>> ObtenerUsuarios(@Part("action") RequestBody action);


    @Multipart
    @POST("WsAndroid.php")
    Call<List<InsertResponse>> uploadImagen(
            @Part List<MultipartBody.Part> Files,
            @Part("action") RequestBody action);
}
