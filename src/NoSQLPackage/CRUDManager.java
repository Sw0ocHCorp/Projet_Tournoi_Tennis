package NoSQLPackage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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

public class CRUDManager {

    private MongoClient mongoClient;
    private String hostName="localhost";
    private int port=27017;
    private MongoDatabase mainDB;
    private AggregateIterable<Document> outputCollection;
    JsonWriterSettings settings= JsonWriterSettings.builder().indent(true).outputMode(JsonMode.SHELL).build();
// ---->>   FONCTIONS DE GESTION DE LA DATABASE 
    public CRUDManager(String dBName) {                                 //C
        mongoClient = new MongoClient();
        mainDB= mongoClient.getDatabase(dBName);
    }

    public void addCollectionToDB(String colName) {                     //U
        mainDB.createCollection(colName);
        System.out.println("--> " + colName + " ajoutée");
        System.out.println("---------------------------");
        MongoIterable<String> colList = mainDB.listCollectionNames();
        for (String name : colList) {
            System.out.println(name);
        }
    }

    public void addCollectionToDB(ArrayList<String> colNamesList) {     //U
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
    public void dropCollectionToDB(String colName){                     //U
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

    public void dropCollectionToDB(ArrayList<String> colNamesList) {    //U
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

    public void delDataBase() {                                         //D
        mainDB.drop();
        System.out.println("--> " + mainDB.getName() + " supprimée");
        System.out.println("___________________________");
    }

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
                                                                        //C --> Deja Code pour le U de DataBase
                                                                        //D --> Deja Code pour le U de DataBase 
    public void searchElement(String colName, Document searchQuery) {   //Getter
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(searchQuery); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            System.out.println( ((Document) it.next()).toJson(settings) );
        }
        System.out.println("___________________________");
    }

    public Document searchOneElement(String colName, Document searchQuery) {   //Getter
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(searchQuery); 
        Iterator it= searchedList.iterator();
        Document targetDocument= new Document();
        while (it.hasNext()) {
            targetDocument= (Document) it.next();
        }
        return targetDocument;
    }

    public void addDocInCollection(Document doc, String colName) {                      //U
        try {
            mainDB.getCollection(colName).insertOne(doc);
            System.out.println("-->> Document ajouté dans la Collection: " + colName);
        } catch (Exception e) {
            System.err.println("!! Interruption de l'ajout des Documents dans la Liste car un d'entre eux existe déjà");
            System.out.println("Documents dans la Collection " + colName + "= ");
            searchElement(colName, new Document());
        }
    }

    public void addDocInCollection(ArrayList<Document> docList, String colName) {       //U
        try {
            mainDB.getCollection(colName).insertMany(docList);
        System.out.println("-->> Liste de Documents ajoutée dans la Collection: " + colName);
        } catch (Exception e) {
            System.err.println("!! Interruption de l'ajout des Documents dans la Liste car un d'entre eux existe déjà");
            System.out.println("Documents dans la Collection " + colName + "= ");
            searchElement(colName, new Document());
        }
    }

    public void updateDocument(String colName, Document filterQuery, Document newFieldDocument){
        mainDB.getCollection(colName).updateOne(filterQuery, new Document("$set", newFieldDocument));
        System.out.println("-->> Document update dans la Collection: " + colName);
    }
    public void updateManyDocuments(String colName, Document filterQuery, Document newFieldDocument){ 
        mainDB.getCollection(colName).updateMany(filterQuery, new Document("$set", newFieldDocument));
        System.out.println("-->> Liste de Documents update dans la Collection: " + colName);
    }

    public void delDocInCollection(Document doc, String colName) {                      //U
        mainDB.getCollection(colName).deleteOne(doc);
        System.out.println("-->> Document supprimé dans la Collection: " + colName);
        System.out.println("---------------------------");
        System.out.println("Documents dans la Collection " + colName + "= ");
        searchElement(colName, new Document());
        System.out.println("___________________________");
    }

    public void delDocInCollection(ArrayList<Document> docList, String colName) {       //U
        for (Document doc : docList) {
            mainDB.getCollection(colName).deleteOne(doc);
        }
        System.out.println("-->> Liste de Documents supprimée dans la Collection: " + colName);
        System.out.println("---------------------------");
        System.out.println("Documents dans la Collection " + colName + "= ");
        searchElement(colName, new Document());
        System.out.println("___________________________");
    }

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

    public void sortDocuments(String colName, Document filterQuery, Document sortQuery){
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        FindIterable<Document> searchedList= targetCol.find(filterQuery).sort(sortQuery); 
        Iterator it= searchedList.iterator();
        while (it.hasNext()) {
            System.out.println( ((Document) it.next()).toJson(settings) );
        }
        System.out.println("___________________________");
    }

    public void groupByDocument(String colName, Document groupQuery) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        groupQuery= new Document("$group", groupQuery);
        AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(groupQuery));
        for (Document document : outputCollection) {
            System.out.println(document.toJson(settings));
        }
        System.out.println("___________________________");
    }

    public void modifyAllDocuments(String colName, Document whereQuery, Document addQuery, Document rmQuery) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        Document matchQuery= new Document("$match", whereQuery);
        addQuery= new Document("$addFields", addQuery);
        rmQuery= new Document("$unset", rmQuery);
        AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(
            matchQuery,
            addQuery, 
            rmQuery));
        for (Document document : outputCollection) {
            System.out.println(document.toJson(settings));
        }
    }

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

    //------------------------------------------------------------------------------------
    //FONCTIONS MANIPULATION DE L'IHM

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
                } else if (document.get(key) instanceof ArrayList) {
                    ArrayList<Document> subDocList= (ArrayList<Document>) document.get(key);
                    for (Document d: subDocList) {
                        for (String k : d.keySet()) {
                            String lastKString= k + ": " + d.get(k) + "<br>";
                            subDocBuffer= subDocBuffer.concat(lastKString); 
                        }
                    }
                    subDocBuffer= subDocBuffer.concat("<html>");
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

    public ArrayList<String> getUIComplexJoinKeys(String firstColName, String secColName, Document whereQuery, String locField, String foreignField, String newColName) {
        String[] tokens= locField.split("\\.");
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        boolean isFirstInserted= false;
        outputCollection = firstCol.aggregate(Arrays.asList(
            Aggregates.match(whereQuery),
            Aggregates.lookup(secColName, locField, foreignField, newColName)
        ));   
        ArrayList<String> keyArrayList= new ArrayList<>();
        for (Document mainDoc : outputCollection) {
            if (isFirstInserted == false) {
                isFirstInserted= true;
                for (String mainKeys : mainDoc.keySet()) {
                    if (mainKeys.equals(newColName) && !keyArrayList.contains(mainKeys)) {
                        keyArrayList.add(mainKeys);
                    }
                    else if (mainDoc.get(mainKeys) instanceof ArrayList) {
                        for (Document subDoc : ((ArrayList<Document>) mainDoc.get(mainKeys))) {
                            for (String key : subDoc.keySet()) {
                                if (key.equals(tokens[1])) {
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

    public ArrayList<ArrayList<String>> getUIJoinComplexElements(String firstColName, String secColName, Document whereQuery, String unwindField, String locField, String foreignField, String newColName, boolean justJoinElements) {
        var keySet= getUIComplexJoinKeys(firstColName, secColName, whereQuery, locField, foreignField, newColName);
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
                    for (Document subDoc : ((ArrayList<Document>) document.get(newColName))) {
                        isPresent= true;
                        for (String subKey : subDoc.keySet()) {
                            if (subKey.equals("_id")) {
                                if(isFirstInserted == false) {
                                    isFirstInserted= true;
                                } else {
                                    joinValue += "<html>";
                                    valueList.set(startIndex, joinValue);
                                    startIndex +=1;
                                    joinValue= "<html>";
                                }
                            } else {
                                String subVal= ((String)subDoc.get(subKey));
                                joinValue += subKey + "= " + subVal + "<br>";
                            } 
                        }
                        if (joinValue != "<html>") {
                            valueList.add(joinValue);
                        }
                    }
                    if (isPresent == false) {
                        valueList.add("");
                    }
                } else if (document.get(key) == null) {
                    valueList.add("");
                    
                }
                else {
                    valueList.add((String)document.get(key));
                }
            }
            if (justJoinElements == true) {
                if (isPresent == true) {
                    joinElementArray.add(valueList);   
                }
            } else {
                joinElementArray.add(valueList); 
            }
        }
        return joinElementArray;
    }

    public ArrayList<String> getUIJoinKeys(String firstColName, String secColName, Document whereQuery, String locField, String foreignFiels, String newColName) {
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        AggregateIterable<Document> outputCollection = firstCol.aggregate(Arrays.asList(
           Aggregates.match(whereQuery),
           Aggregates.lookup(secColName, locField, foreignFiels, newColName)
        ));
        ArrayList<String> keyArrayList= new ArrayList<>();
        for (Document mainDoc : outputCollection) {
            for (String mainKeys : mainDoc.keySet()) {
                if (mainDoc.get(mainKeys) instanceof ArrayList) {
                    for (Document subDoc : ((ArrayList<Document>) mainDoc.get(mainKeys))) {
                        for (String key : subDoc.keySet()) {
                            if (key.equals(foreignFiels)) {
                
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
        return keyArrayList;
    }

    public ArrayList<ArrayList<String>> getUIJoinElements(String firstColName, String secColName, Document whereQuery, String locField, String foreignFiels, String newColName, boolean justJoinElements) {
        var keySet= getUIJoinKeys(firstColName, secColName, whereQuery, locField, foreignFiels, newColName);
        ArrayList<ArrayList<String>> joinElementArray= new ArrayList<>();
        MongoCollection<Document> firstCol= mainDB.getCollection(firstColName);
        AggregateIterable<Document> outputCollection = firstCol.aggregate(Arrays.asList(
           Aggregates.match(whereQuery),
           Aggregates.lookup(secColName, locField, foreignFiels, newColName)
        ));
        boolean isPresent= false;
        for (Document document : outputCollection) {
            ArrayList<String> valueList= new ArrayList<>();
            for (String key: keySet) {
                isPresent= false;
                var mainValeur= document.get(key);
                if (document.get(key) instanceof Integer) {
                    valueList.add(document.get(key).toString());
                } else if (document.get(key) == null) {
                    for (Document subDoc : ((ArrayList<Document>) document.get(newColName))) {
                        isPresent= true;
                        if (subDoc.get(key) instanceof Integer) {
                            valueList.add(subDoc.get(key).toString());
                        } else if(subDoc.get(key) == null) {
                            valueList.add("");
                        } else {
                            valueList.add((String)subDoc.get(key));
                        }
                    }
                    if (isPresent == false) {
                        valueList.add("");
                    }
                }
                else {
                    valueList.add((String)document.get(key));
                }
            }
            if (justJoinElements == true) {
                if (isPresent == true) {
                    joinElementArray.add(valueList);   
                }
            } else {
                joinElementArray.add(valueList); 
            }
        }
        return joinElementArray;
    }

    public void getUIGroupByElements(String colName, Document groupQuery, JTextArea targetTextArea) {
        MongoCollection<Document> targetCol= mainDB.getCollection(colName);
        String targetVal= groupQuery.getString("_id");
        String targetKey= targetVal.replace("$", "");
        groupQuery= new Document("$group", groupQuery);
        AggregateIterable<Document> outputCollection = targetCol.aggregate(Arrays.asList(groupQuery));
        for (Document document : outputCollection) {
            targetTextArea.append("Il y a " + Integer.toString((int) document.get(targetKey)) + " " + colName + " classés " + document.getString("_id") + "\n");
        }
        targetTextArea.append("-----------------------------------------------------------------------------------------------------\n");
    }

    public ArrayList<ArrayList<String>> getUISearchElements(String colName, Document searchQuery) {
        ArrayList<ArrayList<String>> elementArray= new ArrayList<>();
        ArrayList<String> keyList= getUIKeys(colName);
        ArrayList<Document> docList= searchUIDocument(colName, searchQuery);
        for (Document document : docList) {
            ArrayList<String> valueList= new ArrayList<>();
            for (String key: keyList) {
                if (document.get(key) instanceof Integer) {
                    valueList.add(String.valueOf(document.get(key)));
                } else if (document.get(key) instanceof ArrayList) {
                    ArrayList<Document> subDocList= (ArrayList<Document>) document.get(key);
                    for (Document d: subDocList) {
                        String fullValue= "";
                        for (String k : d.keySet()) {
                            if (d.get(k) instanceof Integer) {
                                fullValue += (String.valueOf(d.get(k))) + "\n";
                            }
                            else {
                                fullValue += ((String) d.get(k)) + "\n";
                            }
                        }
                        valueList.add(fullValue);
                    }
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


