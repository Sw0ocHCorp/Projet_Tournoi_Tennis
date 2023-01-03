package UI_UXPackage;
import NoSQLPackage.CRUDManager;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.bson.Document;
// --> CLASSE: Table des données des éléments Recherchés dans la collection sélectionnée
public class searchCollectionTable extends AbstractTableModel {
    private ArrayList<String> keySet;
    private ArrayList<ArrayList<String>> dataSet;

    public searchCollectionTable(String colName, Document searchQuery, CRUDManager crudManager) {
        keySet= crudManager.getUIKeys(colName);                         // Entêtes des colonnes de la Table
        dataSet= crudManager.getUISearchElements(colName, searchQuery); // Données de la Table
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return dataSet.size();
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return keySet.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return dataSet.get(rowIndex).get(columnIndex);
    }
    @Override
    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        return keySet.get(column);
    }
}
