package UI_UXPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.json.simple.parser.JSONParser;

import NoSQLPackage.CRUDManager;
import org.bson.Document;

public class JoinCollectionTable extends AbstractTableModel{
    String colName;
    private ArrayList<String> keySet;
    private ArrayList<ArrayList<String>> joinDataSet;
    public JoinCollectionTable(String firstColName, String secColName, Document whereQuery, String locField, String foreignFiels, CRUDManager crudManager, String unwindField, boolean justJoinElements) {
        keySet= crudManager.getUIJoinKeys(firstColName, secColName, whereQuery, locField, foreignFiels, "newCol");
        joinDataSet= crudManager.getUIJoinElements(firstColName, secColName, whereQuery, locField, foreignFiels, "newCol", justJoinElements);
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return joinDataSet.size();
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return keySet.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return joinDataSet.get(rowIndex).get(columnIndex);
    }

    public ArrayList<String> getgetUICol() {
        return keySet;
    }

    @Override
    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        return keySet.get(column);
    }
    
}
