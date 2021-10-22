package cl.ucn.disc.dsm.smurquio.ucnphonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /**
     *
     * @param savedInstanceState the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se genero un error
        //throw new IllegalArgumentException("Error en la aplicacion");
    }
}