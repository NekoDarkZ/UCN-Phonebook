package cl.ucn.disc.dsm.smurquio.ucnphonebook;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

public class FuncionarioViewModel extends AndroidViewModel {

  /**
   * The List of {@link Funcionario}.
   */
  private MutableLiveData<List<Funcionario>> funcionarios;

  /**
   * The Constructor.
   *
   * @param application to use.
   */
  public FuncionarioViewModel(@NonNull Application application) {
    super(application);
  }

  public LiveData<List<Funcionario>> getFuncionarios(){
    if(this.funcionarios == null){
      this.funcionarios = new MutableLiveData<>();
      this.loadFuncionarios();
    }
    return this.funcionarios;
  }

  /**
   * Read the funcionarios from funcionarios.json.
   */
  private void loadFuncionarios(){

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

        this.funcionarios.postValue(theFuncionarios);

    });
  }
}
