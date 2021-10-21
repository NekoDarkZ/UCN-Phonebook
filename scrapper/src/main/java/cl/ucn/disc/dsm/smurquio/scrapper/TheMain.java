package cl.ucn.disc.dsm.smurquio.scrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class to scrappe the Directorio Telefonico of UCN.
 * @author Sebastian Murquio Castillo
 */
@Slf4j
public class TheMain {

    /**
     * The JSON parser.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     *The starting point.
     *@param args to use.
     **/
    public static void main(String[] args) throws IOException, InterruptedException {

        log.debug("Loading the Funcionarios from funcionarios.json ...");

        //Load the file into the data
        String data = FileUtils.readFileToString(new File("funcionarios.json"), StandardCharsets.UTF_8);

        //Define the Type
        Type type = new TypeToken<List<Funcionario>>(){
        }.getType();

        //The List of Funcionario (string -> List<Funcionario>)
        List<Funcionario> funcionarios = GSON.fromJson(data, type);

        //The latest id loaded
        int start = funcionarios.get(funcionarios.size() -1).getId();
        int end = 30000;

        Random r = new Random();
        log.debug("Starting the Scrapping from {} to {} ...", start, end);
        for(int id = start; id <= end; id++) {

            //Wait for...
            Thread.sleep(50 + r.nextInt(350));

            log.debug("Retriving Funcionario id: {}.", id);

            //Connect and get the Document
            Document doc = Jsoup
                .connect("https://admision01.ucn.cl/directoriotelefonicoemail/fichaGenerica/?cod=" + id)
                .get();

            String nombre = doc.getElementById("lblNombre").text();
            String cargo = doc.getElementById("lblCargo").text();
            String unidad = doc.getElementById("lblUnidad").text();
            String email = doc.getElementById("lblEmail").text();
            String telefono = doc.getElementById("lblTelefono").text();
            String oficina = doc.getElementById("lblOficina").text();
            String direccion = doc.getElementById("lblDireccion").text();

            //Skip if data not found
            if(nombre.length() <= 1){
                log.warn("No data found for id: {}.", id);
                continue;
            }

            log.info("Funcionario id: {} founded: {}.", id, nombre);

            //Call the constructor
            Funcionario f = Funcionario.builder()
                .id(id)
                .nombre(nombre)
                .cargo(cargo)
                .unidad(unidad)
                .email(email)
                .telefono(telefono)
                .oficina(oficina)
                .direccion(direccion)
                .build();

            //Add the Funcionario into the list
            funcionarios.add(f);

            //Save by 25
            if(funcionarios.size() % 3 == 0){

                log.debug("Writting {} Funcionarios to file...", funcionarios.size());

                //Write the List of Funcionario in JSON Format
                FileUtils.writeStringToFile(
                    new File("funcionarios.json"),
                    GSON.toJson(funcionarios),
                    StandardCharsets.UTF_8);
            }
        }
        log.debug("Done.");
    }

}