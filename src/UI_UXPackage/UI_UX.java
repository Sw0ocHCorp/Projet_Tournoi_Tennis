package UI_UXPackage;
import javax.swing.*;
import java.awt.*;

import java.awt.CardLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.bson.Document;

import com.formdev.flatlaf.FlatIntelliJLaf;

import CustomJTableCells.TextAreaRenderer;
import NoSQLPackage.CRUDManager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class UI_UX extends JFrame implements ActionListener, ItemListener, ListSelectionListener{
    private Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private Clock timerClock= Clock.systemDefaultZone();
    private JPanel panel, manageColPanel, textItemPanel, advFunctionPanel, advButtonPanel, keyFJPanel, refColPanel;
    private JButton addButton, removeButton, updateButton, joinButton, groupbyButton, vButton, cancelButton, searchButton, fLButton;
    private JLabel testLabel, fColLab, lColLab, keyLabel;
    private JTextField textField, gbRequestTextField, searchTextField;
    private JTextArea gbResultTextArea;
    private JComboBox comboBox, fColCmbx, lstColCmbx, gbQueryCmbx, gbColcbx, searchComboBox;
    private CollectionTable refCollectionTable;
    private JoinCollectionTable joinColTable;
    private searchCollectionTable searchColTable;
    private ComplexCollectionTable complexColTable;
    private JTable tableFrame;
    private JCheckBox filterCheckBox;
    private int height= (int)dimension.getHeight();
    private int width= (int)dimension.getWidth();
    private Object elementSelected;
    private int targetIndex;
    private int functionMode= 1;
    private List<Object> collectionsNames= new ArrayList<>(Arrays.asList(
        "Arbitres", "Calendrier_Phases_Groupes", "Calendrier_Phases_Finales", "Records_Historiques", "Joueurs", "Staff", "Visiteurs"
    ));
    private ArrayList<Document> targetElementBuffer= new ArrayList<>();
    CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");
    FantasyLeagueFrame frameTest;
    private Document searchQuery, whereQuery, addQuery, rmQuery, groupQuery, sortQuery, filterQuery;
    public UI_UX() {
        super();
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        panel= new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        refCollectionTable= new CollectionTable("Arbitres", crudManager);
        tableFrame= new JTable(refCollectionTable);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(jspTable);
        
        advFunctionPanel= new JPanel();
        advFunctionPanel.setLayout(new BoxLayout(advFunctionPanel, BoxLayout.Y_AXIS));
        fColLab= new JLabel("1ère Collection");
        fColCmbx= new JComboBox(collectionsNames.toArray());
        fColCmbx.addItemListener(this);
        targetIndex= fColCmbx.getSelectedIndex();
        elementSelected= collectionsNames.remove(targetIndex);
        lColLab= new JLabel("2nde Collection");
        lstColCmbx= new JComboBox(collectionsNames.toArray());
        lstColCmbx.addItemListener(this);
        filterCheckBox= new JCheckBox("Garder uniquement \n les éléments de la Jointure", true);
        collectionsNames.add(targetIndex, elementSelected);
        advFunctionPanel.add(fColLab);
        advFunctionPanel.add(fColCmbx);
        advFunctionPanel.add(lColLab);
        advFunctionPanel.add(lstColCmbx);
        advFunctionPanel.add(filterCheckBox);
        
        manageColPanel= new JPanel();
        manageColPanel.setLayout(new BoxLayout(manageColPanel, BoxLayout.Y_AXIS));

        advButtonPanel= new JPanel();
        joinButton= new JButton("Effectuer une Jointure");
        joinButton.addActionListener(this);
        groupbyButton= new JButton("Effectuer un GroupBy");
        groupbyButton.addActionListener(this);
        searchButton= new JButton("Rechercher un / des éléments");
        searchButton.addActionListener(this);
        vButton= new JButton("Valider");
        vButton.addActionListener(this);
        cancelButton= new JButton("Retour");
        cancelButton.addActionListener(this);
        fLButton= new JButton("Jouer au mode Fantasy League");
        fLButton.addActionListener(this);
        advButtonPanel.setLayout(new BoxLayout(advButtonPanel, BoxLayout.X_AXIS));
        advButtonPanel.add(joinButton);
        advButtonPanel.add(groupbyButton);
        advButtonPanel.add(searchButton);
        advButtonPanel.add(vButton);
        advButtonPanel.add(cancelButton);
        advFunctionPanel.add(advButtonPanel);
        /*fLButton.setMinimumSize(new Dimension(200, 25));
        fLButton.setPreferredSize(new Dimension(200, 25));*/
        manageColPanel.add(fLButton);
        manageColPanel.add(advFunctionPanel);

        comboBox= new JComboBox(collectionsNames.toArray());
        comboBox.addItemListener(this);
        manageColPanel.add(comboBox);

        JPanel buttonsPanel= new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        addButton= new JButton("Ajouter un Element");
        addButton.addActionListener(this);
        removeButton= new JButton("Supprimer un Element");
        removeButton.addActionListener(this);
        updateButton= new JButton("Modifier un Element");
        updateButton.addActionListener(this);
        buttonsPanel.add(addButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(updateButton);
        manageColPanel.add(buttonsPanel);
        
        textItemPanel= new JPanel();
        textItemPanel.setLayout(new BoxLayout(textItemPanel, BoxLayout.Y_AXIS));
        for (String col : refCollectionTable.getUICol()) {
            textItemPanel.add(new JLabel(col));
            textItemPanel.add(new JTextField(10));
        }
        manageColPanel.add(textItemPanel);
        
        JScrollPane jsPane= new JScrollPane(manageColPanel ,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsPane.setMaximumSize(new Dimension(700, 1000));
        jsPane.setPreferredSize(new Dimension(700, 1000));
        panel.add(jsPane);
        this.pack();
        setContentPane(panel);              // Passe le Contenu dans la Fenetre
                                            // le contenu du Panel sera visible quand on appelera --> window.setVisible(true) 
        setTitle("MAIN_WINDOW");
        //setSize(width, height);
        setSize(1920, 1000);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    //FONCTION D'UPDATE DU CONTENU DU PANEL DE GAUCHE
    // -> Update contenu de la Table en fonction de la collection Sélectionnée
    private void updateTableGUI() {
        refCollectionTable= new CollectionTable(String.valueOf(comboBox.getSelectedItem()), crudManager);
        tableFrame= new JTable(refCollectionTable);
        tableFrame.setDefaultRenderer(getClass(), null);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        updateRowHeights(tableFrame);
        /*if (((String) comboBox.getSelectedItem()) == "Calendrier") {
            tableFrame.setRowHeight(200);
        }*/
        panel.remove(panel.getComponents()[0]);
        panel.revalidate();
        panel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(jspTable, 0);
        panel.revalidate();
        panel.repaint();
    }

    // -> Update contenu de la Table en fonction de la Jointure Effectuée
    private void updateJoinTableGUI(String fColName, String lColName, Document whereQuery) {
        if (fColName.equals("Joueurs") && lColName.equals("Records_Historiques")) {
            joinColTable= new JoinCollectionTable(fColName, lColName,
                                                    whereQuery,
                                                    "_id", "idJoueur", crudManager, null, filterCheckBox.isSelected());
            tableFrame= new JTable(joinColTable);
        } else if (fColName.equals("Calendrier_Phases_Groupes") && lColName.equals("Joueurs")){
            complexColTable= new ComplexCollectionTable(fColName, lColName,
                                                    whereQuery,
                                                    "Joueurs.id_joueur", "_id", crudManager, null, filterCheckBox.isSelected());
            tableFrame= new JTable(complexColTable);
        }
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        //tableFrame.setRowHeight(50);
        updateRowHeights(tableFrame);
        panel.remove(panel.getComponents()[0]);
        panel.revalidate();
        panel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(jspTable, 0);
        panel.revalidate();
        panel.repaint();
    }

    // -> Update contenu de la Table en fonction de la Recherche d'éléments Effectuée
    private void updateSearchTableGUI(String colName, Document searchQuery){
        searchColTable= new searchCollectionTable(colName, searchQuery, crudManager);
        tableFrame= new JTable(searchColTable);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        panel.remove(panel.getComponents()[0]);
        panel.revalidate();
        panel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(jspTable, 0);
        panel.revalidate();
        panel.repaint();
    }

    // -> Reset de la GUI
    private void updateSimpleGUI() {
        updateJoinPanel();
        updateTableGUI();
        updateColFieldsPanel();
    }

    // -> Update contenu du Panel de Gauche en Fonction de l'action Choisie: Join / GroupBy / Search
    private void updateComplexGUI(int functionMode) {
        if (functionMode == 1) {     //Si on fait une Jointure
            updateJoinPanel();
        } if (functionMode == 2) {        //Si on fait un GroupBy
            panel.remove(panel.getComponents()[0]);
            panel.revalidate();
            panel.repaint();
            gbResultTextArea= new JTextArea(100, 75);
            panel.add(new JScrollPane(gbResultTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), 0);
            panel.revalidate();
            panel.repaint();
        } if (functionMode == 3) {      //Si on veut Rechercher 1 / des éléments
            updateSearchPanel();
        }
    }

    //FONCTION D'UPDATE DU CONTENU DU PANEL DE DROITE
    // -> Fonction de refresh du contenu des combobox pour la Jointure
    private void updateMiniJoinPanel() {
        for (Component c : manageColPanel.getComponents()) {
            if (c == advFunctionPanel) {
                for (Component component : ((JPanel)advFunctionPanel).getComponents()) {
                    if ((component == lstColCmbx) || (component == lColLab)) {
                        advFunctionPanel.remove(component);
                    }

                }
                advFunctionPanel.revalidate();
                advFunctionPanel.repaint();
                targetIndex= fColCmbx.getSelectedIndex();
                elementSelected= collectionsNames.remove(targetIndex);
                lColLab= new JLabel("2nde Collection");
                lstColCmbx= new JComboBox(collectionsNames.toArray());
                lstColCmbx.addItemListener(this);
                collectionsNames.add(targetIndex, elementSelected);
                advFunctionPanel.add(lstColCmbx, 2);
                advFunctionPanel.add(lColLab, 2);
                
                advFunctionPanel.revalidate();
                advFunctionPanel.repaint();
            }
        }
    }

    // -> Fonction de chargement des composants graphiques pour ordonner la Jointure
    private void updateJoinPanel() {
        advFunctionPanel.removeAll();
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
        fColLab= new JLabel("1ère Collection");
        fColCmbx= new JComboBox(collectionsNames.toArray());
        fColCmbx.addItemListener(this);
        targetIndex= fColCmbx.getSelectedIndex();
        elementSelected= collectionsNames.remove(targetIndex);
        lColLab= new JLabel("2nde Collection");
        lstColCmbx= new JComboBox(collectionsNames.toArray());
        lstColCmbx.addItemListener(this);
        collectionsNames.add(targetIndex, elementSelected);
        advFunctionPanel.add(fColLab);
        advFunctionPanel.add(fColCmbx);
        advFunctionPanel.add(lColLab);
        advFunctionPanel.add(lstColCmbx);
        advFunctionPanel.add(filterCheckBox);
        advFunctionPanel.add(advButtonPanel);
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
    }

    // -> Fonction de chargement des composants graphiques pour ordonner le GroupBy
    private void updateGroupByPanel() {
        advFunctionPanel.removeAll();
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
        JPanel cbJPanel= new JPanel();
        keyFJPanel= new JPanel();
        refColPanel= new JPanel();
        cbJPanel.setLayout(new BoxLayout(cbJPanel, BoxLayout.X_AXIS));
        keyFJPanel.setLayout(new BoxLayout(keyFJPanel, BoxLayout.Y_AXIS));
        refColPanel.setLayout(new BoxLayout(refColPanel, BoxLayout.Y_AXIS));
        gbColcbx= new JComboBox(collectionsNames.toArray());
        gbColcbx.addItemListener(this);
        JLabel requestLab= new JLabel("Requête");
        gbQueryCmbx= new JComboBox(refCollectionTable.getUICol().toArray());
        gbQueryCmbx.addItemListener(this);
        gbRequestTextField= new JTextField();
        refColPanel.add(new JLabel("Sur quelle table effectuer un Groupement"));
        refColPanel.add(gbColcbx);
        keyFJPanel.add(new JLabel("Clé de groupement"));
        keyFJPanel.add(gbQueryCmbx);
        cbJPanel.add(refColPanel);
        cbJPanel.add(keyFJPanel);
        advFunctionPanel.add(cbJPanel);
        advFunctionPanel.add(requestLab);
        advFunctionPanel.add(gbRequestTextField);
        advFunctionPanel.add(advButtonPanel);
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
    }

    // -> Fonction de chargement des composants graphiques pour ordonner la Recherche
    private void updateSearchPanel() {
        advFunctionPanel.removeAll();
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
        JLabel searchLabel= new JLabel("Ordre de Recherche");
        JPanel searchOrderPanel= new JPanel();
        searchOrderPanel.setLayout(new BoxLayout(searchOrderPanel, BoxLayout.X_AXIS));
        searchComboBox= new JComboBox((crudManager.getUIKeys((String) comboBox.getSelectedItem())).toArray());
        searchTextField= new JTextField();
        searchOrderPanel.add(searchComboBox);
        searchOrderPanel.add(searchTextField);
        advFunctionPanel.add(searchLabel);
        advFunctionPanel.add(searchOrderPanel);
        advFunctionPanel.add(advButtonPanel);
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
    }

    // -> Fonction de chargement des composants graphiques pour Modifier les éléments | NE FONCTIONNE PAS POUR LE GROUP BY NI LA JOINTURE
    private void updateColFieldsPanel() {
        textItemPanel.removeAll();
        textItemPanel.revalidate();
        textItemPanel.repaint();
        for (String col : refCollectionTable.getUICol()) {
           if (col.equals("Joueurs")) {
                textItemPanel.add(new JLabel(col + "    (format= Aieme_joueur: x||Bieme_joueur: y||Cieme_joueur: z)"));
            } else {
                textItemPanel.add(new JLabel(col));
            }
            textItemPanel.add(new JTextField(10));
        }
        manageColPanel.revalidate();
        manageColPanel.repaint();
    }


    private void updateRowHeights(JTable table){
        for (int row = 0; row < table.getRowCount(); row++){
            int rowHeight = table.getRowHeight();

            for (int column = 0; column < table.getColumnCount(); column++)
            {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            table.setRowHeight(row, rowHeight);
        }
}

    //------------------------------------------------------------
    // FONCTIONS POUR LES LISTENERS
    // -> Fonction de paramétrage des actions des Boutons
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        Object targetObject= e.getSource();
        if (targetObject == fLButton) {
            frameTest= new FantasyLeagueFrame();
            new Thread() {
                public void run() {
                    frameTest.processFL(frameTest);
                }
            }.start();
        }
        if (targetObject == addButton) {
            //testLabel.setText(textField.getText());
            ArrayList<String> activeColList= refCollectionTable.getUICol();
            Component[] compoList= textItemPanel.getComponents();
            JTextField idTextField= (JTextField) textItemPanel.getComponents()[1];
            Document newElement= new Document();
            int indexCol= 0;
            if ((idTextField.getText() == "") || (idTextField.getText() == null) || (idTextField.getText() == " ")) {
                System.out.println("Vous ne pouvez pas Ajouter l'élément car il n'a pas d'ID");
            } else {
                try {
                    for (Component component : textItemPanel.getComponents()) {
                        if (component instanceof JTextField) {
                            if (indexCol == 0) {
                                int idVal= Integer.parseInt(((JTextField) component).getText());
                                newElement.append(activeColList.get(indexCol), idVal);
                            } else {
                                newElement.append(activeColList.get(indexCol), ((JTextField) component).getText());
                            }
                            indexCol += 1;
                        }
                    }
                    crudManager.addDocInCollection(newElement,(String) comboBox.getSelectedItem());
                    updateTableGUI();
                } catch (Exception exception) {
                    // TODO: handle exception
                    exception.printStackTrace();
                }
            }
            
        } if (targetObject == removeButton) {
            System.out.println("Appuie sur le bouton de Suppression");
            if (!targetElementBuffer.isEmpty()) {
                crudManager.delDocInCollection(targetElementBuffer,(String) comboBox.getSelectedItem());
                updateTableGUI();
                targetElementBuffer= new ArrayList<>();
            }
        } if (targetObject == updateButton) {
            System.out.println("Appuie sur le bouton de Modification");
            ArrayList<String> activeColList= refCollectionTable.getUICol();
            int indexTextComponent= 1;
            Document updateFieldDoc= new Document();
            Document lastWhereQuery= targetElementBuffer.get(targetElementBuffer.size()-1);
            Document lastDoc= crudManager.searchOneElement((String) comboBox.getSelectedItem(), lastWhereQuery);
            int idVal=(Integer) lastDoc.get("_id");
            for (String key : activeColList) {
                if (!key.equals("_id")) {
                    String targetVal= ((JTextField) textItemPanel.getComponents()[indexTextComponent]).getText();
                    if (lastDoc.get(key) != targetVal) {
                        JTextField targTextField= (JTextField) textItemPanel.getComponents()[indexTextComponent];
                        updateFieldDoc.append(key, targTextField.getText());
                    }
                }
                indexTextComponent += 2;
            }
            crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idVal), updateFieldDoc);
            updateTableGUI();
        } if (targetObject == joinButton) {
            functionMode= 1;
            updateComplexGUI(functionMode);
        } if (targetObject == groupbyButton) {
            functionMode= 2;
            updateComplexGUI(functionMode);
            updateGroupByPanel();
        } if (targetObject == searchButton) {
            functionMode= 3;
            updateComplexGUI(functionMode);
        } if (targetObject == vButton) {
            if (functionMode == 1) {           //Jointure
                whereQuery= new Document();
                String firstCol= (String) fColCmbx.getItemAt(fColCmbx.getSelectedIndex());
                String lastCol= (String) lstColCmbx.getItemAt(lstColCmbx.getSelectedIndex());
                updateJoinTableGUI(firstCol, lastCol, whereQuery);
            } else if (functionMode == 2) {                            //Group By
                String targetCol= ((String) gbColcbx.getSelectedItem());
                String keySelected= "$" + ((String) gbQueryCmbx.getSelectedItem());
                groupQuery= new Document("_id", keySelected);
                StringTokenizer requestAnalyzer= new StringTokenizer(gbRequestTextField.getText());
                while (requestAnalyzer.hasMoreTokens()) {
                    String token= requestAnalyzer.nextToken();
                    if ((token.equals( "compter")) || (token.equals("Compter")) || (token.equals("count")) || (token.equals("Count"))) {
                        Document countQuery= new Document();
                        groupQuery.append(((String) gbQueryCmbx.getSelectedItem()), new Document("$count", countQuery));
                    }
                }
                crudManager.getUIGroupByElements(targetCol, groupQuery, gbResultTextArea);
            } else {
                if ((searchTextField.getText()).matches("[0-9]+")) {
                    int val= Integer.valueOf(searchTextField.getText());
                    searchQuery= new Document((String) searchComboBox.getSelectedItem(), val);
                } else {
                    searchQuery= new Document((String) searchComboBox.getSelectedItem(), searchTextField.getText());
                }
                updateSearchTableGUI((String) comboBox.getSelectedItem(), searchQuery);
                //crudManager.searchElement((String) comboBox.getSelectedItem(), searchQuery);
                searchTextField.setText("");
            }
        } if (targetObject == cancelButton) {
            updateSimpleGUI();
        }
        if ((targetObject == addButton) || (targetObject == removeButton) || (targetObject == updateButton) || (targetObject == joinButton) || (targetObject == groupbyButton) || (targetObject == searchButton) || (targetObject == cancelButton)) {
            for (Component component : textItemPanel.getComponents()) {
                if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                }
            }
        }

    }

    // -> Fonction de paramétrage des actions des ComboBox
    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == comboBox) {
            updateSimpleGUI();
        }
        if (e.getSource() == fColCmbx) {
            updateMiniJoinPanel();
        }
        if (e.getSource() == gbQueryCmbx) {
            
        }
        if (e.getSource() == gbColcbx) {
            keyFJPanel.removeAll();
            manageColPanel.revalidate();
            manageColPanel.repaint();
            String colSelected= (String) gbColcbx.getSelectedItem();
            gbQueryCmbx= new JComboBox(crudManager.getUIKeys(colSelected).toArray());
            gbQueryCmbx.addItemListener(this);
            keyFJPanel.add(new JLabel("Clé de groupement"));
            keyFJPanel.add(gbQueryCmbx);
            manageColPanel.revalidate();
            manageColPanel.repaint();
        }
    }

    // -> Fonction de Link entre Table et champs de texte (Modification d'un élément)
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // TODO Auto-generated method stub
        int[] rowSelected;
        int index= 0;
        rowSelected= tableFrame.getSelectedRows();
        if (rowSelected.length > 0) {
            for (int i : rowSelected) {
                int val= Integer.valueOf((String) tableFrame.getValueAt(rowSelected[rowSelected.length-1], 0));
                Document targetDocument= new Document("_id", val);
                if (!targetElementBuffer.contains((Document) targetDocument)) {
                    targetElementBuffer.add(targetDocument);
                }

            }
            for (var component : textItemPanel.getComponents()) {
                if (component instanceof JTextField) {
                    JTextField targetTextField= (JTextField) component;
                    targetTextField.setText("");
                    String strValue= (String) tableFrame.getValueAt(rowSelected[rowSelected.length-1], index);
                    if (strValue.contains("<html>")) {
                        strValue= strValue.replace("<html>", "");
                        strValue= strValue.replace("<br>", "||");
                    }
                    targetTextField.setText(strValue);
                    index += 1;
                }
            }
        }

    }

}
