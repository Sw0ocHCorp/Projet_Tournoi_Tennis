import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bson.Document;

import NoSQLPackage.CRUDManager;

// --> PROGRAMME de chargement des données <--
public class DataLoader {
    public static void main(String[] args) throws Exception {               
        CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");
        crudManager.delDataBase("Tournoi_Tennis");
        crudManager.importDataFromJSON("data\\Joueurs_data.json", "Joueurs");
        crudManager.importDataFromJSON("data\\Joueurs_data_FL.json", "Joueurs_FL");
        crudManager.importDataFromJSON("data\\Staff_data.json", "Staff");
        crudManager.importDataFromJSON("data\\Public_data.json", "Visiteurs");
        crudManager.importDataFromJSON("data\\Calendrier_group_data.json", "Calendrier_Phases_Groupes");
        crudManager.importDataFromJSON("data\\Calendrier_group_data.json", "Calendrier_Phases_Groupes_FL");
        crudManager.importDataFromJSON("data\\Calendrier_final_data.json", "Calendrier_Phases_Finales");
        crudManager.importDataFromJSON("data\\Calendrier_final_data.json", "Calendrier_Phases_Finales_FL");
        System.out.println("---------------------->>>>>>>>>>>");
    }
}
