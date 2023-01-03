package UI_UXPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.json.simple.parser.JSONParser;

import NoSQLPackage.CRUDManager;

// --> CLASSE: Table de la collection sélectionnée (Table Basique)
public class CollectionTable extends AbstractTableModel{
    String colName;
    
    
    private ArrayList<String> activeColumns;
    private ArrayList<ArrayList<String>> dataSet;
    public CollectionTable(String colName, CRUDManager crudManager) {
        this.colName= colName;
        activeColumns= crudManager.getUIKeys(this.colName);     // Entêtes des colonnes de la Table
        dataSet= crudManager.getUICollectionElements(colName);  // Données de la Table
    
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
