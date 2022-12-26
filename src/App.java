import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bson.Document;

import NoSQLPackage.CRUDManager;


public class App {
    public static void main(String[] args) throws Exception {
        //CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");

        
        CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");
        crudManager.delDataBase("Tournoi_Tennis");
        crudManager.addCollectionToDB(new ArrayList<>(Arrays.asList("Joueurs", "Calendrier Phases Groupes", "Calendrier Phases Finales",
                                                                    "Visiteurs", "Records_Historiques",
                                                                    "Staff", "Arbitres",
                                                                    "Joueurs_FL", "Calendrier Phases Groupes FL", "Calendrier Phases Finales FL")));
        /*ArrayList<Document> poolJoueurs= new ArrayList<>(Arrays.asList(
            new Document("_id", 7).append("Prenom", "Casper").append("Nom", "Ruud").append("ok", "TEST"),
            new Document("_id", 6).append("Prenom", "Andy").append("Nom", "Murray").append("ok", "TEST"),
            new Document("_id", 8).append("Prenom", "Matteo").append("Nom", "Berrettini"),
            new Document("_id", 9).append("Prenom", "Carlos").append("Nom", "Alcaraz"),
            new Document("_id", 5).append("Prenom", "Gaël").append("Nom", "Monfils") 
        ));
        crudManager.addDocInCollection(poolJoueurs, "Joueurs");
        crudManager.updateManyDocuments("Joueurs", new Document("ok", "TEST"), new Document("ok", "DEBUG"));
        crudManager.updateDocument("Joueurs", new Document("_id", 7), new Document("Prenom", "TA MERE"));
        crudManager.searchElement("Joueurs", new Document("_id", 7));
        crudManager.delDocInCollection(new Document("_id", 7), "Joueurs");*/
        crudManager.importDataFromJSON("data\\Joueurs_Data_V2.json", "Joueurs");
        crudManager.importDataFromJSON("data\\Joueurs_Data_V2_FL.json", "Joueurs_FL");
        crudManager.importDataFromJSON("data\\Staff_data.json", "Staff");
        crudManager.importDataFromJSON("data\\Ref_data.json", "Arbitres");
        crudManager.importDataFromJSON("data\\Public_data.json", "Visiteurs");
        //crudManager.importDataFromJSON("data\\Calendrier_backup_data.json", "Calendrier");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Groupes.json", "Calendrier_Phases_Groupes");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Groupes.json", "Calendrier_Phases_Groupes_FL");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Avancees.json", "Calendrier_Phases_Finales");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Avancees.json", "Calendrier_Phases_Finales_FL");
        crudManager.importDataFromJSON("data\\Records_historiques.json", "Records_Historiques");
        System.out.println("---------------------->>>>>>>>>>>");
        //Document groupByQuery= new Document("_id", "$position").append("position", new Document("$count", new   Document()));
        //crudManager.groupByDocument("Arbitres", groupByQuery); 
        //crudManager.updateManyDocuments("Joueurs_FL", new Document(), new Document("isBetPlayer", false).append("statePlayer", "Poules"));
        //crudManager.exportDataToJSON("data\\Joueurs_Data_V2_FL.json", "Joueurs_FL");
        Document whereQuery= new Document("_id", 180);
        //crudManager.groupByDocument(, whereQuery);
        crudManager.joinCollection("Calendrier", "Joueurs", whereQuery, "$Joueurs", "id_joueur", "_id", "TEST");
        //crudManager.searchElement("Visiteurs",new Document("_id", 5000));
        //crudManager.importDataFromCSV("C:\\Users\\nclsr\\OneDrive\\Bureau\\Cours_L3IA\\Base_de_Donnees_NoSQL\\Projet_Tournoi_Tennis\\data\\Joueurs_data.csv", "Joueurs");
        //crudManager.searchElement("Joueurs",new Document("_id", 9));
        //crudManager.searchElement("Joueurs",new Document("first_name", "Kirby"));
    }
}
