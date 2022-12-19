package UI_UXPackage;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class VectorTableCellRenderer extends JList<String> implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
            if (value instanceof Vector) {
                setListData((Vector) value);
            }
        
            if (isSelected) {
                setBackground(UIManager.getColor("Table.selectionBackground"));
            } else {
                setBackground(UIManager.getColor("Table.background"));
            }
        
            return this;
    }


}
