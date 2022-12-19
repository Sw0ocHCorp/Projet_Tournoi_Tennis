package UI_UXPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.parser.JSONParser;

import NoSQLPackage.CRUDManager;

public class CollectionTable extends AbstractTableModel{
    String colName;
    
    private ArrayList<String> playerBaseColumns= new ArrayList<>(Arrays.asList("_id", "first_name", 
        "last_name", "nationality", "ranking", "personnal_sponsor"));
    private ArrayList<String> publicBaseColumns= new ArrayList<>(Arrays.asList("_id", "first_name", 
        "last_name", "nationality"));
    private ArrayList<String> refBaseColumns= new ArrayList<>(Arrays.asList("_id", "first_name", 
        "last_name", "position"));
    private ArrayList<String> staffBaseColumns= new ArrayList<>(Arrays.asList("_id", "first_name", 
        "last_name", "job"));
    private ArrayList<String> recordsBaseColumns= new ArrayList<>(Arrays.asList("_id", "label", 
        "valRecord", "idJoueur"));
    private ArrayList<String> activeColumns, keySet;
    private ArrayList<ArrayList<String>> dataSet;
    public CollectionTable(String colName, CRUDManager crudManager) {
        keySet= crudManager.getUIKeys(colName);
        this.colName= colName;
        if (colName == "Arbitres") {
            if (keySet.isEmpty()) {
                activeColumns= refBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        } if (colName == "Calendrier") {
            if (crudManager.getUIKeys(colName).isEmpty()) {
                //activeColumns= refBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        } if (colName == "Records_Historiques") {
            if (crudManager.getUIKeys(colName).isEmpty()) {
                activeColumns= recordsBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        } if (colName == "Joueurs") {
            if (crudManager.getUIKeys(colName).isEmpty()) {
                activeColumns= playerBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        } if (colName == "Staff") {
            if (crudManager.getUIKeys(colName).isEmpty()) {
                activeColumns= staffBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        } if (colName == "Visiteurs") {
            if (crudManager.getUIKeys(colName).isEmpty()) {
                activeColumns= publicBaseColumns;
            } else {
                activeColumns= crudManager.getUIKeys(colName);
            }
        }
        dataSet= crudManager.getUICollectionElements(colName);
    
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return dataSet.size();
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return activeColumns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return dataSet.get(rowIndex).get(columnIndex);
    }

    public ArrayList<String> getUICol() {
        return activeColumns;
    }

    @Override
    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        return activeColumns.get(column);
    }
    
}
