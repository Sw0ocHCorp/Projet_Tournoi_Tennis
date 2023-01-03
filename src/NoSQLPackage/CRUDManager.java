package NoSQLPackage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import static com.mongodb.client.model.Sorts.ascending;

// --> CLASSE: Gestion de la base de donnée MongoDB NoSQL
public class CRUDManager {

    private MongoClient mongoClient;
    private String hostName="localhost";
    private int port=27017;
    private MongoDatabase mainDB;
    private AggregateIterable<Document> outputCollection;
    JsonWriterSettings settings= JsonWriterSettings.builder().indent(true).outputMode(JsonMode.SHELL).build();
// ---->>   FONCTIONS DE GESTION DE LA DATABASE 
    // C -> Constructeur et Création de la BDD
    public CRUDManager(String dBName) {                                 
        mongoClient = new MongoClient();
        mainDB= mongoClient.getDatabase(dBName);
    }
    // C -> Création d'une collection VIDE
    public void addCollectionToDB(String colName) {                     
        mainDB.createCollection(colName);
        System.out.println("--> " + colName + " ajoutée");
        System.out.println("---------------------------");
        MongoIterable<String> colList = mainDB.listCollectionNames();
        for (String name : colList) {
            System.out.println(name);
        }
    }
    // C -> Création de plusieurs collections VIDES
    public void addCollectionToDB(ArrayList<String> colNamesList) {     
        for (String colName : colNamesList) {
            try {
                mainDB.createCollection(colName);
                System.out.println("--> " + colName + " ajoutée");
            } catch (Exception e) {
                System.err.println("!! " + colName + " Ne peut pas être ajoutée car elle existe déjà");
            }
        }
        System.out.println("---------------------------");
        System.out.println("Collections dans la base de donnée " + mainDB.getName() + "= ");
        MongoIterable<String> colList = mainDB.listCollectionNames();
        for (String name : colList) {
            System.out.println(name);
        }
        System.out.println("___________________________");
    }
    // D -> Suppression d'une collection
    public void dropCollectionToDB(String colName){                     
        boolean collectionExists = mainDB.listCollectionNames().into(new ArrayList<String>())
                        .contains(colName);
        if (collectionExists == true) {
            mainDB.getCollection(colName).drop();
            System.out.println("--> " + colName + " supprimée");
        } else {
            System.out.println("!! " + colName + " ne peut pas être supprimée, elle n'existe pas dans " + mainDB.getName());
        }
        System.out.println("---------------------------");
        System.out.println("Collections dans la base de donnée " + mainDB.getName() + "= ");
        MongoIterable<String> colList = mainDB.listCollectionNames();
        for (String name : colList) {
            System.out.println(name);
        }
        System.out.println("___________________________");
    }
    // D -> Suppression de plusieurs collections
    public void dropCollectionToDB(ArrayList<String> colNamesList) {    
        for (String colName : colNamesList) {
            mainDB.getCollection(colName).drop();
            System.out.println("--> " + colName + " supprimée");
        }
        System.out.println("---------------------------");
        System.out.println("Collections dans la base de donnée " + mainDB.getName() + "= ");
        MongoIterable<String> colList = mainDB.listCollectionNames();
        for (String name : colList) {
            System.out.println(name);
        }
        System.out.println("___________________________");
    }
    // D -> Suppression de la BDD sur laquelle on est connecté (créée par le constructeur)
    public void delDataBase() {                                         
        mainDB.drop();
        System.out.println("--> " + mainDB.getName() + " supprimée");
        System.out.println("___________________________");
    }
    // D -> Suppression d'une BDD
    public void delDataBase(String dBName) {                            
        boolean collectionExists = mongoClient.listDatabaseNames().into(new ArrayList<String>())
                        .contains(dBName);
        if (collectionExists) {
            MongoDatabase targetDb= mongoClient.getDatabase(dBName);
            targetDb.drop();
            System.out.println("--> " + dBName + " supprimée");   
        } else {
            System.out.println("!! " + dBName + " ne peut pas être supprimée, elle n'existe pas" );
        }
        System.out.println("---------------------------");
        System.out.println("Bases de données dans le MongoSH = ");
        MongoIterable<String> list = mongoClient.listDatabaseNames();
        for (String name : list) {
            System.out.println(name);
        }
        System.out.println("___________________________");
    }

// ---->>   FONCTIONS DE GESTION / MANIPULATION DES COLLECTIONS 
    // R -> Recherche d'un element ou plusieurs dans une collection
    public void searchElement(String colName, Document searchQuery) {           
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(searchQuery); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            System.out.println( ((Document) it.next()).toJson(settings) );
        }
        System.out.println("___________________________");
    }
    // R -> Recherche d'UN SEUL element dans une collection (si plusieurs, renvoie le dernier)
    public Document searchOneElement(String colName, Document searchQuery) {    
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(searchQuery); 
        Iterator it= searchedList.iterator();
        Document targetDocument= new Document();
        while (it.hasNext()) {
            targetDocument= (Document) it.next();
        }
        return targetDocument;
    }
    // C -> Ajout d'UN SEUL document / élément dans une collection
    public void addDocInCollection(Document doc, String colName) {              
        try {
            mainDB.getCollection(colName).insertOne(doc);
            System.out.println("-->> Document ajouté dans la Collection: " + colName);
        } catch (Exception e) {
            System.err.println("!! Interruption de l'ajout des Documents dans la Liste car un d'entre eux existe déjà");
            System.out.println("Documents dans la Collection " + colName + "= ");
            searchElement(colName, new Document());
        }
    }
    // C -> Ajout de plusieurs documents / éléments dans une collection
    public void addDocInCollection(ArrayList<Document> docList, String colName) {       
        try {
            mainDB.getCollection(colName).insertMany(docList);
        System.out.println("-->> Liste de Documents ajoutée dans la Collection: " + colName);
        } catch (Exception e) {
            System.err.println("!! Interruption de l'ajout des Documents dans la Liste car un d'entre eux existe déjà");
            System.out.println("Documents dans la Collection " + colName + "= ");
            searchElement(colName, new Document());
        }
    }
    // U -> Modification d'UN SEUL document / élément dans une collection
    public void updateDocument(String colName, Document filterQuery, Document newFieldDocument){    
        mainDB.getCollection(colName).updateOne(filterQuery, new Document("$set", newFieldDocument));
        System.out.println("-->> Document update dans la Collection: " + colName);
    }
    // U -> Modification de plusieurs documents / éléments dans une collection
    public void updateManyDocuments(String colName, Document filterQuery, Document newFieldDocument){   
        mainDB.getCollection(colName).updateMany(filterQuery, new Document("$set", newFieldDocument));
        System.out.println("-->> Liste de Documents update dans la Collection: " + colName);
    }
    // D -> Suppression d'UN SEUL document / élément dans une collection
    public void delDocInCollection(Document doc, String colName) {                      
        mainDB.getCollection(colName).deleteOne(doc);
        System.out.println("-->> Document supprimé dans la Collection: " + colName);
        System.out.println("---------------------------");
        System.out.println("Documents dans la Collection " + colName + "= ");
        searchElement(colName, new Document());
        System.out.println("___________________________");
    }
    // D -> Suppression de plusieurs documents / éléments dans une collection
    public void delDocInCollection(ArrayList<Document> docList, String colName) {       
        for (Document doc : docList) {
            mainDB.getCollection(colName).deleteOne(doc);
            doc= searchOneElement(colName, new Document("_id",String.valueOf(doc.get("_id"))));
            mainDB.getCollection(colName).deleteOne(doc);
        }
        System.out.println("-->> Liste de Documents supprimée dans la Collection: " + colName);
        System.out.println("---------------------------");
        System.out.println("Documents dans la Collection " + colName + "= ");
        searchElement(colName, new Document());
        System.out.println("___________________________");
    }
    
    // --> Fonction Avancées d'interactions avec la BDD
    // Jointure entre deux collections
    public void joinCollection(String firstColName, String secColName, Document whereQuery, String unwindField, String locField, String foreignFiels, String newColName) {
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        AggregateIterable<Document> outputCollection = firstCol.aggregate(Arrays.asList(
           Aggregates.match(whereQuery),
           Aggregates.unwind(unwindField),
           Aggregates.lookup(secColName, locField, foreignFiels, newColName)
        ));
        for (Document document : outputCollection) {
            System.out.println(document.toJson(settings));
        }
        System.out.println("___________________________");
    }
    // Tri des documents d'une collection
    public void sortDocuments(String colName, Document filterQuery, Document sortQuery){
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(filterQuery).sort(sortQuery); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            System.out.println( ((Document) it.next()).toJson(settings) );
        }
        System.out.println("___________________________");
    }
    // GroupBy des documents d'une collection
    public void groupByDocument(String colName, Document groupQuery) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        groupQuery= new Document("$group", groupQuery);
        AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(groupQuery));
        for (Document document : outputCollection) {
            System.out.println(document.toJson(settings));
        }
        System.out.println("___________________________");
    }
    // U -> Modification de TOUS LES documents / éléments dans une collection
    public void modifyAllDocuments(String colName, Document whereQuery, Document addQuery, Document rmQuery) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        Document matchQuery= new Document("$match", whereQuery);
        addQuery= new Document("$addFields", addQuery);
        rmQuery= new Document("$unset", rmQuery);
        AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(
            matchQuery,
            addQuery, 
            rmQuery));
            
        /*for (Document document : outputCollection) {
            System.out.println(document.toJson(settings));
        }*/
    }
    //Importation de données depuis un fichier CSV
    // Fait tout Bugger || Impossible d'afficher les Documents
    public void importDataFromCSV(String filePath, String colName) {
        int cpt= 0;
        String command = "mongoimport --host " + hostName + " --port " + port + " --db " + mainDB.getName() + " --collection " + colName + "  --headerline --type=csv --file "+ filePath;
        try {  
            Process process = Runtime.getRuntime().exec(command);  
            System.out.println("Imported csv into Database");  
        } catch (Exception e) {  
            System.out.println("Error executing " + command + e.toString());  
        } 
        System.out.println("---------------------------");
        System.out.println("Documents dans la Collection " + colName + "= ");
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(new Document()); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            cpt++;
        }
        System.out.println(cpt);
    }
    //Importation de données d'une collection depuis un fichier JSON
    //Marche Parfaitement
    public void importDataFromJSON(String filePath, String colName) {
        String command = "mongoimport " + filePath + " --db " + mainDB.getName() + " --collection " + colName + "  --jsonArray --drop";
			try {
				Process process= Runtime.getRuntime().exec(command);	//On passe la commande dans un terminal
				BufferedReader success= new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedReader error= new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String s;
				while ((s= success.readLine()) != null) {
					System.out.println(s);
				}
				while ((s= error.readLine()) != null) {
					System.out.println("ERROR: " + s);
				}
                System.out.println("Documents dans la Collection " + colName + "= ");
                searchElement(colName, new Document());
			} catch (Exception e) {
				e.printStackTrace();
			}

    }
    //Exportation de données d'une collection vers un fichier JSON
    public void exportDataToJSON(String filePath, String colName) {
        String command = "C:\\Program Files\\MongoDB\\Server\\5.0\\bin\\mongoexport.exe --db " + mainDB.getName() + " --collection " + colName + " --out " + filePath;
        try {
            Process process= Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------
    //FONCTIONS D'INTERACTION IHM ==> BDD --> BDD ==> IHM
    // Recherche d'un / des éléments dans une collection AVEC CONDITIONS DE RECHERCHE pour constituer les données de la Table de l'IHM
    public ArrayList<Document> searchUIDocument(String colName, Document searchQuery) {
        ArrayList<Document> docList= new ArrayList<>();
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(searchQuery); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            docList.add((Document) it.next());
        }
        return docList;
    }
    // Recherche d'un / des éléments dans une collection SANS CONDITIONS DE RECHERCHE pour constituer les données de la Table de l'IHM
    public ArrayList<Document> searchUIDocument(String colName) {
        ArrayList<Document> docList= new ArrayList<>();
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            docList.add((Document) it.next());
        }
        return docList;
    }
    // Récupération des clés d'une collection pour constituer les entêtes des colonnes de la Table de l'IHM
    public ArrayList<String> getUIKeys(String colName) {
        ArrayList<String> keysList= new ArrayList<>();
        for (Document document : searchUIDocument(colName)) {
            for (String key:document.keySet()) {
                if (!keysList.contains(key)) {
                    keysList.add(key);
                }
            }
        }
        return keysList;
    }


    // Fonction de contrôle de l'affichage des données dans la Table de l'IHM (Version Classique / Basique)
    public ArrayList<ArrayList<String>> getUICollectionElements(String colName) {
        ArrayList<ArrayList<String>> elementArray= new ArrayList<>();
        ArrayList<String> keyList= getUIKeys(colName);
        ArrayList<Document> docList= searchUIDocument(colName);
        String subDocBuffer = "<html>";
        for (Document document : docList) {
            ArrayList<String> valueList= new ArrayList<>();
            for (String key: keyList) {
                if (document.get(key) instanceof Integer) {
                    valueList.add(document.get(key).toString());
                
                } else if (document.get(key) instanceof Boolean) {
                    valueList.add(((Boolean)document.get(key)).toString());
                } else if (document.get(key) instanceof ArrayList) {
                    ArrayList<Document> subDocList= (ArrayList<Document>) document.get(key);
                    for (Document d: subDocList) {
                        for (String k : d.keySet()) {
                            String lastKString= k + ": " + d.get(k) + "<br>";
                            subDocBuffer= subDocBuffer.concat(lastKString); 
                        }
                        if (subDocList.size() > 1) {
                            subDocBuffer= subDocBuffer.concat("-----------<br>");   
                        }
                    }
                    subDocBuffer= subDocBuffer.concat("<html>");
                    subDocBuffer= subDocBuffer.replace("<br><html>", "<html>");
                    valueList.add(subDocBuffer);
                    subDocBuffer= "<html>";
                }

                else {
                    valueList.add((String)document.get(key));
                }
            }
            elementArray.add(valueList);
        }
        //uiMap.put(hostName, hostName)
        return elementArray;
    }
    // Récupération des clés des collections de la Jointure à conserver / afficher dans la Table de l'IHM
    public ArrayList<String> getUIJoinKeys(String firstColName, String secColName, Document whereQuery, String locField, String foreignField, String newColName) {
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        boolean isFirstInserted= false;
        outputCollection = firstCol.aggregate(Arrays.asList(
            Aggregates.match(whereQuery),
            Aggregates.lookup(secColName, locField, foreignField, newColName)
        ));   
        ArrayList<String> keyArrayList= new ArrayList<>();
        StringTokenizer st= new StringTokenizer(locField, ".");
        String baseField= st.nextToken();
        String subField= st.nextToken();
        for (Document mainDoc : outputCollection) {
            if (isFirstInserted == false) {
                isFirstInserted= true;
                for (String mainKeys : mainDoc.keySet()) {
                    if ((mainDoc.get(mainKeys) instanceof ArrayList) && (mainKeys.equals(baseField))) {
                        for (Document subDoc : ((ArrayList<Document>) mainDoc.get(mainKeys))) {
                            for (String key : subDoc.keySet()) {
                                if (key.equals(subField)) {
                                    keyArrayList.add(key);
                                } else if (!keyArrayList.contains((key))) {
                                    keyArrayList.add(key);
                                }
                            }
                        }
            
                    }else if (!keyArrayList.contains(mainKeys)) {
                        keyArrayList.add(mainKeys);
                    } 
                }
            }
        }
        return keyArrayList;
    }
    // Fonction de contrôle de l'affichage des données dans la Table de l'IHM (Version Jointure)
    public ArrayList<ArrayList<String>> getUIJoinElements(String firstColName, String secColName, Document whereQuery, String locField, String foreignField, String newColName, boolean justJoinElements) {
        var keySet= getUIJoinKeys(firstColName, secColName, whereQuery, locField, foreignField, newColName);
        String[] tokens= locField.split("\\.");
        int startIndex= keySet.indexOf(tokens[1]);
        boolean isFirstInserted= false;
        ArrayList<ArrayList<String>> joinElementArray= new ArrayList<>();
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        outputCollection = firstCol.aggregate(Arrays.asList(
            Aggregates.match(whereQuery),
            Aggregates.lookup(secColName, locField, foreignField, newColName)
        ));   
        boolean isPresent= false;
        String joinValue= "<html>";
        for (Document document : outputCollection) {
            startIndex= keySet.indexOf(tokens[1]);
            ArrayList<String> valueList= new ArrayList<>();
            for (String key: keySet) {
                isPresent= false;
                var mainValeur= document.get(key);
                if (document.get(key) instanceof Integer) {
                    valueList.add(document.get(key).toString());
                } else if (key.equals(newColName)) {
                    int firstIndex= startIndex;
                    for (Document subDoc : ((ArrayList<Document>) document.get(newColName))) {
                        isPresent= true;
                        for (String subKey : subDoc.keySet()) {
                            if ((subKey.equals("_id")) || (subDoc.get(subKey) instanceof ArrayList)) {
                                
                            } else {
                                String subVal= ((String)subDoc.get(subKey));
                                joinValue += subKey + "= " + subVal + "<br>";
                            } 
                        }
                        if (joinValue != "<html>") {
                            joinValue += "<html>";
                            valueList.set(startIndex, joinValue);
                            joinValue= "<html>";
                            startIndex +=1;
                        }
                    }
                    if (isPresent == false) {
                        valueList.add("");
                    }
                } else if (document.get(key) instanceof ArrayList) {
                    ArrayList<Document> subDocList= (ArrayList<Document>) document.get(key);
                    for (Document d: subDocList) {
                        for (String k : d.keySet()) {
                            String lastKString= k + ": " + d.get(k) + "<br>";
                            joinValue= joinValue.concat(lastKString); 
                        }
                        if (subDocList.size() > 1) {
                            joinValue= joinValue.concat("-----------<br>");   
                        }
                    }
                    joinValue= joinValue.concat("<html>");
                    joinValue= joinValue.replace("<br><html>", "<html>");
                    valueList.add(joinValue);
                    joinValue= "<html>";
                } else if (document.get(key) == null) {
                    valueList.add("");
                    
                } else {
                    valueList.add((String)document.get(key));
                }
            }
            joinElementArray.add(valueList); 
        }
        return joinElementArray;
    }

    // Fonction de contrôle de l'affichage des données dans la zone de Texte de l'IHM (Version GroupBy)
    public void getUIGroupByElements(String colName, Document groupQuery, JTextArea targetTextArea) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        if (groupQuery.get("_id") instanceof String) {
            String targetVal= groupQuery.getString("_id");
            String targetKey= targetVal.replace("$", "");
            groupQuery= new Document("$group", groupQuery);
            AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(groupQuery, Aggregates.sort(ascending(targetKey))));
            for (Document document : outputCollection) {
                int buffer= (int) document.get(targetKey);
                targetTextArea.append("Il y a " + buffer + " " + colName + " classés " + document.get("_id").toString() + "\n");
                targetTextArea.append("-----------------------------------------------------------------------------------------------------\n");
            }
        } if (groupQuery.get("_id") instanceof Document) {
            Document keysDoc= (Document) groupQuery.get("_id");
            Set<String> keys= keysDoc.keySet();
            groupQuery= new Document("$group", groupQuery);
            int idEvent= 0;
            String eventTag= "";
            String eventType= "";
            if (colName.equals("Staff")) {
                if (keys.contains("id_group_event")) {
                    idEvent= 20000;
                    eventTag= "id_group_event";
                } else if (keys.contains("id_final_event")) {
                    idEvent= 20101;
                    eventTag= "id_final_event";
                }
            }
            AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(groupQuery, Aggregates.sort(ascending("_id." + eventTag))));
            for (Document document : outputCollection) {
                Document subDoc= ((Document)document.get("_id"));
                int buffer= (int) document.get("job");
                if (eventTag.equals("id_group_event")) {
                    eventType= "Phase de Groupe";
                } else {
                    idEvent= (int) subDoc.get(eventTag);
                    if (idEvent < 20132) {
                        eventType= "Phase Pre-final";
                    } else if (idEvent < 20148) {
                        eventType= "Phase 16th-final";
                    } else if (idEvent < 20156) {
                        eventType= "Phase 8th-final";
                    } else if (idEvent < 20160) {
                        eventType= "Phase Quarter-final";
                    } else if (idEvent < 20162) {
                        eventType= "Phase Semi-final";
                    } else {
                        eventType= "Phase Finale";
                    }
                }
                if (idEvent < 20163) {
                    targetTextArea.append("Il y a " + buffer + " " + colName + " classés " + subDoc.get("job").toString() + " Pour l'événement " + subDoc.get(eventTag).toString() + " " + eventType + "\n");
                    targetTextArea.append("-----------------------------------------------------------------------------------------------------\n");
                }
                
            }

        }
        
    }

    // Fonction de contrôle de l'affichage des données dans la Table de l'IHM (Version Recherche d'éléments)
    public ArrayList<ArrayList<String>> getUISearchElements(String colName, Document searchQuery) {
        ArrayList<ArrayList<String>> elementArray= new ArrayList<>();
        ArrayList<String> keyList= getUIKeys(colName);
        ArrayList<Document> docList= searchUIDocument(colName, searchQuery);
        String subDocBuffer = "<html>";
        for (Document document : docList) {
            ArrayList<String> valueList= new ArrayList<>();
            for (String key: keyList) {
                if (document.get(key) instanceof Integer) {
                    valueList.add(document.get(key).toString());
                
                } else if (document.get(key) instanceof Boolean) {
                    valueList.add(((Boolean)document.get(key)).toString());
                } else if (document.get(key) instanceof ArrayList) {
                    ArrayList<Document> subDocList= (ArrayList<Document>) document.get(key);
                    for (Document d: subDocList) {
                        for (String k : d.keySet()) {
                            String lastKString= k + ": " + d.get(k) + "<br>";
                            subDocBuffer= subDocBuffer.concat(lastKString); 
                        }
                        if (subDocList.size() > 1) {
                            subDocBuffer= subDocBuffer.concat("-----------<br>");   
                        }
                    }
                    subDocBuffer= subDocBuffer.concat("<html>");
                    subDocBuffer= subDocBuffer.replace("<br><html>", "<html>");
                    valueList.add(subDocBuffer);
                    subDocBuffer= "<html>";
                }

                else {
                    valueList.add((String)document.get(key));
                }
            }
            elementArray.add(valueList);
        }
        //uiMap.put(hostName, hostName)
        return elementArray;
    }

}


