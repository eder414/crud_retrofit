package curso.clases.crud_retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import curso.clases.crud_retrofit.Interfaces.ServicesRetrofit;
import curso.clases.crud_retrofit.Models.InsertResponse;
import curso.clases.crud_retrofit.Models.PostUsuario;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View .OnClickListener{
    EditText editTextNombre,editTextEdad,editPassword,editTextSexo;
    Button btnInsertar, btnActualizar,btnBorrar ,btnObtener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEdad = findViewById(R.id.editTextEdad);
        editPassword = findViewById(R.id.editPassword);
        editTextSexo = findViewById(R.id.editTextSexo);

        btnInsertar = findViewById(R.id.btnInsertar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnObtener = findViewById(R.id.btnObtener);

        btnInsertar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnObtener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInsertar:
                Insertar();
                break;
            case R.id.btnActualizar:
                break;
            case R.id.btnBorrar:
                break;
            case R.id.btnObtener:
                break;
        }
    }

    private void Insertar() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://pirschdev.com/WsBackend/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ServicesRetrofit service = retrofit.create(ServicesRetrofit.class);

        PostUsuario postUsuario = new PostUsuario();

        RequestBody _usuario_nombre = RequestBody.create(MediaType.parse("multipart/form-data"),editTextNombre.getText().toString());
        RequestBody _usuario_password = RequestBody.create(MediaType.parse("multipart/form-data"),editPassword.getText().toString());
        RequestBody _usuario_sexo = RequestBody.create(MediaType.parse("multipart/form-data"),editTextSexo.getText().toString());
        RequestBody _usuario_edad = RequestBody.create(MediaType.parse("multipart/form-data"),editTextEdad.getText().toString());
        RequestBody _action = RequestBody.create(MediaType.parse("multipart/form-data"),"insertUsuario");

        Call<ArrayList<InsertResponse>> call = service.InsertaUsuario(_usuario_nombre,_usuario_password,_usuario_sexo,_usuario_edad,_action);

        call.enqueue(new Callback<ArrayList<InsertResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<InsertResponse>> call, Response<ArrayList<InsertResponse>> response) {
                ArrayList<InsertResponse> lResponse ;
                lResponse = response.body();
                for(int i= 0 ; i < lResponse.size(); i++){
                    Toast.makeText(getApplicationContext(),"Respuesta: "+lResponse.get(i).getResponse(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<InsertResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"ERROR AL LLAMAR WEBSERVICE: "+t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}