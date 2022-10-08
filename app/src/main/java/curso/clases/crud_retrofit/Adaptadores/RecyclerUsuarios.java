package curso.clases.crud_retrofit.Adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import curso.clases.crud_retrofit.Models.Usuarios;
import curso.clases.crud_retrofit.R;

public class RecyclerUsuarios extends RecyclerView.Adapter<RecyclerUsuarios.ViewHolder> {
    ArrayList<Usuarios> lUsuarios;
    private LayoutInflater mInflater;
    private Context context;

    public RecyclerUsuarios(ArrayList<Usuarios> lUsuarios,LayoutInflater mInflater,Context context) {
        this.lUsuarios = lUsuarios;
        this.mInflater = mInflater;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerUsuarios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.recycler_usuario,parent,false);
        return new RecyclerUsuarios.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerUsuarios.ViewHolder holder, int position) {
        holder.binData(lUsuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return lUsuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewId,textViewNombre,textViewSexo,textViewEdad,textViewPassword;
        LinearLayout linearLayout;
        String [] colors = new String [] {"#4C9900",
                "#99CCFF",
                "#CC0000",
                "#CCFFCC",
                "#663300",
                "#CC99FF",
                "#660033",
                "#660033",
                "##006666",
                "#E5CCFF",
                "#FFFFCC"};

        public ViewHolder(View view){
            super(view);
            textViewId = view.findViewById(R.id.textViewId);
            textViewNombre = view.findViewById(R.id.textViewNombre);
            textViewSexo = view.findViewById(R.id.textViewSexo);
            textViewEdad = view.findViewById(R.id.textViewEdad);
            textViewPassword = view.findViewById(R.id.textViewPassword);

            linearLayout = view.findViewById(R.id.linearLayout);
        }
        public void binData(Usuarios usuarios) {
            textViewId.setText(usuarios.getUsuario_id());
            textViewNombre.setText(usuarios.getUsuario_nombre());
            textViewSexo.setText(usuarios.getUsuario_sexo());
            textViewEdad.setText(usuarios.getUsuario_edad());
            textViewPassword.setText(usuarios.getUsuario_password());

            int min = 0;
            int max = 10;

            //Generate random int value from 50 to 100
            System.out.println("Random value in int from "+min+" to "+max+ ":");
            int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);


            linearLayout.setBackgroundColor(Color.parseColor(colors[random_int]));
        }
    }
}
