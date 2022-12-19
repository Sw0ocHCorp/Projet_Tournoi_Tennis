package CustomJTableCells;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class TextAreaRenderer extends JTextArea implements TableCellRenderer {
  
    public TextAreaRenderer() {
      setLineWrap(true);
      setWrapStyleWord(true);
    }
  
    public Component getTableCellRendererComponent(JTable jTable,
        Object obj, boolean isSelected, boolean hasFocus, int row,
        int column) {
        setText((String)obj);
        int height_wanted = (int)getPreferredSize().getHeight();
        if (height_wanted != jTable.getRowHeight(row))
        jTable.setRowHeight(row, height_wanted);
        return this;
    }
  }