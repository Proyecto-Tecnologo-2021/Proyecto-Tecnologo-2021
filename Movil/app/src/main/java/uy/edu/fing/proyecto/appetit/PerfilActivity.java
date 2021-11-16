package uy.edu.fing.proyecto.appetit;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uy.edu.fing.proyecto.appetit.adapter.DireccionPerfilAdapter;
import uy.edu.fing.proyecto.appetit.constant.ConnConstants;
import uy.edu.fing.proyecto.appetit.obj.DtUsuario;

public class PerfilActivity extends AppCompatActivity {
    private static final String TAG = "PerfilActivity";
    final static Integer RC_SIGN_IN = 20213;
    private ConnectivityManager connMgr;
    private NetworkInfo networkInfo;
    private boolean isConnected;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    EditText textNombre;
    EditText textCorreo;
    EditText textTelefono;
    EditText textpass;
    EditText textpass2;
    ListView listView;
    ListAdapter listAdapter;
    ProgressBar progressBar;
    BottomNavigationView bottomNavigationView;


    private final DtUsuario dtUsuario = DtUsuario.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getString(R.string.title_Perfil);
        setTitle(title);

        setContentView(R.layout.activity_perfil);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        textNombre = findViewById(R.id.data_name);
        textCorreo = findViewById(R.id.data_correo);
        textTelefono = findViewById(R.id.data_tel);
        textpass = findViewById(R.id.data_pass);
        textpass2 = findViewById(R.id.data_pass2);

        progressBar = findViewById(R.id.pBarAddCliente);
        progressBar.setVisibility(View.INVISIBLE);

        textNombre.setText(dtUsuario.getNombre());
        textCorreo.setText(dtUsuario.getCorreo());
        textTelefono.setText(dtUsuario.getTelefono());
        textCorreo.setEnabled(false);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.menu_perfil);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.menu_menus:
                    Intent imenu = new Intent(PerfilActivity.this, MenuActivity.class);
                    startActivity(imenu);
                    return true;
                case R.id.menu_rest:
                    return true;
                case R.id.menu_pedido:
                    Intent ipedido = new Intent(PerfilActivity.this, VerPedidosActivity.class);
                    startActivity(ipedido);
                    return true;
                case R.id.menu_perfil:
                    return true;
            }
            return false;
        });

        listView = findViewById(R.id.dirListView);
        listAdapter = new DireccionPerfilAdapter(this, dtUsuario.getDirecciones());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((adapterView, view, position, id) -> {

            String mensaje = getString(R.string.datos_vdireccion) + "\n";


            AlertDialog dialog = new AlertDialog.Builder(PerfilActivity.this).create();
            dialog.setTitle(R.string.info_title);
            dialog.setMessage(mensaje);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.alert_btn_positive), (dialog1, which) -> {
                //dtPedido.remMenu(position);
                //addProductos(dtPedido.getMenus());

            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.alert_btn_negative), (dialog12, which) -> {
            });
            dialog.show();
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(ConnConstants.WEB_CLIENT_ID)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.perfil_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.perfil_cerrar:
                // Check if user is signed in (non-null) and update UI accordingly.
                //FirebaseUser currentUser = mAuth.getCurrentUser();
                signOut();
                return true;
            case R.id.perfil_modificar:
                modificarDatos();
                return true;
            case R.id.perfil_crear_dir:
                Intent inewdir = new Intent(PerfilActivity.this, AddDireccionActivity.class);
                startActivity(inewdir);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void modificarDatos() {
    }

    private void signOut() {
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        dtUsuario.setCorreo(null);
        dtUsuario.setTokenFirebase(null);
        dtUsuario.setToken(null);

        Intent imain = new Intent(PerfilActivity.this, MainActivity.class);
        startActivity(imain);

    }
}