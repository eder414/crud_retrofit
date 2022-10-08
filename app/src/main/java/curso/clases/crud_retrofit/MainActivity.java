package curso.clases.crud_retrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import curso.clases.crud_retrofit.Adaptadores.RecyclerUsuarios;
import curso.clases.crud_retrofit.Interfaces.ServicesRetrofit;
import curso.clases.crud_retrofit.Models.InsertResponse;
import curso.clases.crud_retrofit.Models.PostUsuario;
import curso.clases.crud_retrofit.Models.Usuarios;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View .OnClickListener{
    EditText editTextNombre,editTextEdad,editPassword,editTextSexo,editTextId;
    Button btnInsertar, btnActualizar,btnBorrar ,btnObtener,btnFoto,btnSalvarImagen;

    ImageView imageViewPhoto;

    LinearLayout linearInsertar;
    RecyclerView rvUsuarios;

    private LayoutInflater mInflater;

    RecyclerUsuarios recyclerUsuarios;

    private static final int IMAG_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int RESUL_OK = -1;
    public static final int CAMERA_PIC_REQUEST = 1002;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    /*direccion de la imagen*/
    String currentPhotoPath;

    Bitmap imageUrls;

    ArrayList<File> imagenes = new ArrayList<>();
    Uri photoURI;

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
        btnFoto = findViewById(R.id.btnFoto);
        btnSalvarImagen = findViewById(R.id.btnSalvarImagen);

        btnInsertar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnObtener.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        btnSalvarImagen.setOnClickListener(this);

        linearInsertar = findViewById(R.id.linearInsertar);

        rvUsuarios = findViewById(R.id.rvUsuarios);
        mInflater = LayoutInflater.from(this);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
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
            case R.id.btnFoto:
                PermisosFoto();
                break;
            case R.id.btnSalvarImagen:
                SubirImagen();
                break;
        }
    }

    private void SubirImagen() {
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://pirschdev.com/WsBackend/")
                //        baseUrl("http://192.168.8.251:8082/WsApi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        List<MultipartBody.Part> _body = new ArrayList<>();

        for(int i = 0 ; i < imagenes.size() ; i++){
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),imagenes.get(i));
            _body.add(MultipartBody.Part.createFormData("files",imagenes.get(i).getName()+"_"+i+".jpg",requestFile));
        }

        RequestBody _action = RequestBody.create(MediaType.parse("multipart/form-data"),"uploadFile");

        ServicesRetrofit service = retrofit.create(ServicesRetrofit.class);
        Call<List<InsertResponse>> call = service.uploadImagen(_body,_action);
        call.enqueue(new Callback<List<InsertResponse>>() {
            @Override
            public void onResponse(Call<List<InsertResponse>> call, Response<List<InsertResponse>> response) {
                List<InsertResponse> lTiendasResponse;
                if(response.isSuccessful()){
                    lTiendasResponse = response.body();
                    //tiendasResponse.setResponse(response.toString());
                    Toast.makeText(getApplicationContext(),"response: "+lTiendasResponse.get(0).getResponse(),Toast.LENGTH_SHORT).show();
                }else{
                    lTiendasResponse = response.body();
                    //tiendasResponse.setResponse(response.toString());

                    Toast.makeText(getApplicationContext(),"Error al retornar webservices: ",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<InsertResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error al llamar webService: "+t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PermisosFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                System.out.println("entra");
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider2",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String serviceName = "photo";
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = timeStamp.substring(0,6);
        //String tDir = System.getProperty("java.io.tmpdir");
        //System.out.println("tDir: "+tDir);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        File dest = new File(image.getParentFile().getAbsolutePath()+"/"+imageFileName);

        if(image.renameTo(dest)){
            currentPhotoPath = dest.getAbsolutePath();
            return dest;
        }else{
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }

        // Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = image.getAbsolutePath();
        //currentPhotoPath = image.getAbsolutePath();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String[] permB = {Manifest.permission.CAMERA};
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Arrays.equals(permissions, permB)) {
                        takeImage();
                    }
                } else {
                    Toast.makeText(this, "Permiso denegado...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void takeImage() {
        // intent to pick image
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,CAMERA_PIC_REQUEST);
        //startActivityForResult(intent,IMAG_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = new File(currentPhotoPath);


        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        if(file_size > 0){
            /*Picasso
                    .get()
                    .load("file://"+currentPhotoPath)
                    .into(target);*/
            //Picasso.get().load("file://"+currentPhotoPath).into(imgPicture);
            //imageUrls.add("file://"+currentPhotoPath);

            String filePath = file.getPath();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);

            imageUrls = bitmap;
            imageViewPhoto.setImageBitmap(bitmap);

            galleryAddPic();
            notifyMediaStoreScanner(file);



            /*viewPager = findViewById(R.id.view_pager);
            ViewPagerAdapter adapter = new ViewPagerAdapter(MainActivity.this,imageUrls);
            viewPager.setAdapter(adapter);*/
        }else{
            boolean deleted = file.delete();
        }

    }
    public final void notifyMediaStoreScanner(File file) {

        try {
            MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            MainActivity.this.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        int file_size = Integer.parseInt(String.valueOf(f.length()/1024));
        Drawable d = Drawable.createFromPath(currentPhotoPath);

        System.out.println("path: "+f.getAbsolutePath());

        imagenes.clear();

        imagenes.add(f);
        //imageSlider.setImageList(imageList);
        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
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