package uy.edu.fing.proyecto.appetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class VerPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Pedido);
        setTitle(title);

        setContentView(R.layout.activity_ver_pedido);
    }
}