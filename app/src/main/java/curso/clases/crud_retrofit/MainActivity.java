package curso.clases.crud_retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import curso.clases.crud_retrofit.Adaptadores.RecyclerUsuarios;
import curso.clases.crud_retrofit.Interfaces.ServicesRetrofit;
import curso.clases.crud_retrofit.Models.InsertResponse;
import curso.clases.crud_retrofit.Models.PostUsuario;
import curso.clases.crud_retrofit.Models.Usuarios;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View .OnClickListener{
    EditText editTextNombre,editTextEdad,editPassword,editTextSexo,editTextId;
    Button btnInsertar, btnActualizar,btnBorrar ,btnObtener;

    LinearLayout linearInsertar;
    RecyclerView rvUsuarios;

    private LayoutInflater mInflater;

    RecyclerUsuarios recyclerUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEdad = findViewById(R.id.editTextEdad);
        editPassword = findViewById(R.id.editPassword);
        editTextSexo = findViewById(R.id.editTextSexo);
        editTextId = findViewById(R.id.editTextId);

        btnInsertar = findViewById(R.id.btnInsertar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnObtener = findViewById(R.id.btnObtener);

        btnInsertar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnObtener.setOnClickListener(this);

        linearInsertar = findViewById(R.id.linearInsertar);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        mInflater = LayoutInflater.from(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInsertar:
                Insertar();
                break;
            case R.id.btnActualizar:
                Actualizar();
                break;
            case R.id.btnBorrar:
                Borrar();
                break;
            case R.id.btnObtener:
                ObtenerUsuarios();
                break;
        }
    }

    private void ObtenerUsuarios() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://pirschdev.com/WsBackend/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ServicesRetrofit service = retrofit.create(ServicesRetrofit.class);
        RequestBody _action = RequestBody.create(MediaType.parse("multipart/form-data"),"P_ObtenerUsuario");

        Call<ArrayList<Usuarios>> call = service.ObtenerUsuarios(_action);

        call.enqueue(new Callback<ArrayList<Usuarios>>() {
            @Override
            public void onResponse(Call<ArrayList<Usuarios>> call, Response<ArrayList<Usuarios>> response) {
                ArrayList<Usuarios> lUsuarios ;
                lUsuarios = response.body();
                LlenarRecyclerView(lUsuarios);
            }

            @Override
            public void onFailure(Call<ArrayList<Usuarios>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"ERROR AL LLAMAR WEBSERVICE: "+t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LlenarRecyclerView(ArrayList<Usuarios> lUsuarios) {
        recyclerUsuarios = new RecyclerUsuarios(lUsuarios,mInflater,this);
        rvUsuarios.setHasFixedSize(true);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        rvUsuarios.setItemAnimator(new DefaultItemAnimator());
        rvUsuarios.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        rvUsuarios.setAdapter(recyclerUsuarios);
    }

    private void Borrar() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://pirschdev.com/WsBackend/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ServicesRetrofit service = retrofit.create(ServicesRetrofit.class);

        RequestBody _id = RequestBody.create(MediaType.parse("multipart/form-data"),editTextId.getText().toString());
        RequestBody _action = RequestBody.create(MediaType.parse("multipart/form-data"),"P_DeleteUsuario");

        Call<ArrayList<InsertResponse>> call = service.BorrarUsuario(_id,_action);

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

    private void Actualizar() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://pirschdev.com/WsBackend/").
                addConverterFactory(GsonConverterFactory.create()).
                build();
        ServicesRetrofit service = retrofit.create(ServicesRetrofit.class);

        RequestBody _id = RequestBody.create(MediaType.parse("multipart/form-data"),editTextId.getText().toString());
        RequestBody _usuario_nombre = RequestBody.create(MediaType.parse("multipart/form-data"),editTextNombre.getText().toString());
        RequestBody _usuario_password = RequestBody.create(MediaType.parse("multipart/form-data"),editPassword.getText().toString());
        RequestBody _usuario_sexo = RequestBody.create(MediaType.parse("multipart/form-data"),editTextSexo.getText().toString());
        RequestBody _usuario_edad = RequestBody.create(MediaType.parse("multipart/form-data"),editTextEdad.getText().toString());
        RequestBody _action = RequestBody.create(MediaType.parse("multipart/form-data"),"P_Actualizar");

        Call<ArrayList<InsertResponse>> call = service.ActualizaUsuario(_id,_usuario_nombre,_usuario_password,_usuario_sexo,_usuario_edad,_action);

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