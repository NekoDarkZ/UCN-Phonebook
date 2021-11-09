package cl.ucn.disc.dsm.smurquio.ucnphonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The Funcionario Adapter.
     */
    protected FuncionarioAdapter funcionarioAdapter;

    /**
     *
     * @param savedInstanceState the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the List(RecyclerView).
        final RecyclerView recyclerView = findViewById(R.id.am_rv_funcionarios);
        // The type of layout of RecyclerView.
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Build the Adapter.
        this.funcionarioAdapter = new FuncionarioAdapter();

        // Union of Adapter + RecyclerView
        recyclerView.setAdapter(this.funcionarioAdapter);

        // Build the ViewModel
        FuncionarioViewModel funcionarioViewModel = ViewModelProvider // Provider
            .AndroidViewModelFactory // The Factory
            .getInstance(this.getApplication()) // The Singleton instance of Factory
            .create(FuncionarioViewModel.class); // Call the Constructor of FuncionarioViewModel

        // Watch the List of Funcionario
        funcionarioViewModel.getFuncionarios().observe(this, funcionarios -> {
            // Set the funcionarios (from ViewModel)
            funcionarioAdapter.setFuncionarios(funcionarios);
            // Refresh the Recycler (ListView)
            funcionarioAdapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Run in the background.
        AsyncTask.execute(() -> {

            List<Funcionario> theFuncionarios;

            //Read the funcionarios.json
            try(final InputStream is = super.getApplication().getAssets().open("funcionarios.json")){

                //Get the type of List<Funcionario> with reflection
                final Type funcionarioListType = new TypeToken<List<Funcionario>>(){}.getType();

                //The json to object convert
                final Gson gson = new GsonBuilder().create();

                //The Reader
                final Reader reader = new InputStreamReader(is);

                //Google gson Black magic
                theFuncionarios = gson.fromJson(reader, funcionarioListType);

            }catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Sort by name
            theFuncionarios.sort(Comparator.comparing(Funcionario::getNombre));

            // Remove the Funcionarios without email
            theFuncionarios.removeIf(f -> f.getEmail() == null);

            // Populate the adapter.
            this.funcionarioAdapter.setFuncionarios(theFuncionarios);
            // Notify / Update the GUI.
            runOnUiThread(() ->  {
                // Run in UI Thread.
                this.funcionarioAdapter.notifyDataSetChanged();
            });
        });
    }
}