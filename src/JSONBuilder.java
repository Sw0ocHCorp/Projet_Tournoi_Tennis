import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bson.Document;

import NoSQLPackage.CRUDManager;

// --> CLASSE: Modification des données JSON des collections
public class JSONBuilder {
    
    public static void main(String[] args) {
        Document newRefRight= new Document();
        Document newRefLeft= new Document();
        Document newRefCenter= new Document();
        Random rand= new Random();
        String[] metiers= {"Hote / Hotesse d'Accueil", "Voiturier", "Restauration", "Securité", "Médecin", "Restauration", "Restauration", "Administrateur", "Restauration"};
        CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");
        crudManager.delDataBase("Tournoi_Tennis");
        crudManager.importDataFromJSON("data\\Staff_data.json", "Staff");
        ArrayList<Document> outputDocs= crudManager.searchUIDocument("Staff");
        Document document= new Document();
        for (int i = 0; i < 1000; i++) {
            int docId= outputDocs.get(i).getInteger("_id");
            int groupId= rand.nextInt(96) +20000;
            int finalId= rand.nextInt(63) +20101;
            if (i < 96) {
                document.append("job", "Administrateur");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            } else if (i < 192) {
                document.append("job", "Sécurité");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            } else if (i < 288) {
                document.append("job", "Médecin");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            } else if (i < 384) {
                document.append("job", "Voiturier");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            } else if (i < 480) {
                document.append("job", "Hote / Hotesse d'Accueil");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            } else {
                document.append("job", "Restauration");
                document.append("id_group_event", groupId);
                document.append("id_final_event", finalId);
            }
            crudManager.updateDocument("Staff", new Document("_id", docId), document);
        }
        crudManager.exportDataToJSON("data\\Staff_data.json", "Staff");
        //crudManager.addCollectionToDB(new ArrayList<>(Arrays.asList("Calendrier Phases Groupes", "Calendrier Phases Finales", "Arbitres")));
        /*crudManager.importDataFromJSON("data\\Ref_data.json", "Arbitres");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Groupes.json", "Calendrier_Phases_Groupes");
        crudManager.importDataFromJSON("data\\Calendrier_Phases_Avancees.json", "Calendrier_Phases_Finales");
        ArrayList<Document> outputDocs= crudManager.searchUIDocument("Arbitres");
        for (Document doc : outputDocs) {
            if (doc.get("position").equals("Right")) {
                documentsRefRight.add(doc);
            } else if (doc.get("position").equals("Left")) {
                documentsRefLeft.add(doc);
            } else if (doc.get("position").equals("Court")) {
                documentsRefCenter.add(doc);
            }       
        }
        outputDocs= crudManager.searchUIDocument("Calendrier_Phases_Groupes");
        for (Document doc : outputDocs) {
            for (Document docRef : documentsRefRight) {
                if (doc.get("_id").equals(docRef.getInteger("refereeing_group_event_id"))) {
                    newRefRight= new Document("id_ref", docRef.get("_id"));
                }
            }  
            for (Document docRef : documentsRefLeft) {
                if (doc.get("_id").equals(docRef.get("refereeing_group_event_id"))) {
                    newRefLeft= new Document("id_ref", docRef.get("_id"));
                }
            }     
            for (Document docRef : documentsRefCenter) {
                if (doc.get("_id").equals(docRef.get("refereeing_group_event_id"))) {
                    newRefCenter= new Document("id_ref", docRef.get("_id"));
                }
            }
            crudManager.updateDocument("Calendrier_Phases_Groupes", new Document("_id", doc.get("_id")), new Document("referees", Arrays.asList(newRefRight, newRefLeft, newRefCenter)));
        }
        outputDocs= crudManager.searchUIDocument("Calendrier_Phases_Finales");
        for (Document doc : outputDocs) {
            for (Document docRef : documentsRefRight) {
                if (doc.get("_id").equals(docRef.get("refereeing_final_event_id"))) {
                    newRefRight= new Document("id_ref", docRef.get("_id"));
                }
            }  
            for (Document docRef : documentsRefLeft) {
                if (doc.get("_id").equals(docRef.get("refereeing_final_event_id"))) {
                    newRefLeft= new Document("id_ref", docRef.get("_id"));
                }
            }     
            for (Document docRef : documentsRefCenter) {
                if (doc.get("_id").equals(docRef.get("refereeing_final_event_id"))) {
                    newRefCenter= new Document("id_ref", docRef.get("_id"));
                }
            }
            crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", doc.get("_id")), new Document("referees", Arrays.asList(newRefRight, newRefLeft, newRefCenter)));
        }

        crudManager.exportDataToJSON("data\\Calendrier_Phases_Groupes.json", "Calendrier_Phases_Groupes");
        crudManager.exportDataToJSON("data\\Calendrier_Phases_Avancees", "Calendrier_Phases_Finales");*/
    }
}
