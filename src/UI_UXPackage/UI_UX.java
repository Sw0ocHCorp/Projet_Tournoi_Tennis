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
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.bson.Document;

import com.formdev.flatlaf.FlatIntelliJLaf;

import NoSQLPackage.CRUDManager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
// --> CLASSE: Définition Apparence / Comportement de la fenêtre du Software <--
public class UI_UX extends JFrame implements ActionListener, ItemListener, ListSelectionListener{
    private Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private Clock timerClock= Clock.systemDefaultZone();
    private JPanel panel, manageColPanel, textItemPanel, advFunctionPanel, advButtonPanel, keyFJPanel, refColPanel, tablePanel;
    private JButton addButton, removeButton, updateButton, joinButton, groupbyButton, vButton, cancelButton, searchButton, fLButton, addFieldButton;
    private JLabel testLabel, fColLab, lColLab, keyLabel, newFieldLab;
    private JTextField textField, gbRequestTextField, searchTextField, newFieldText;
    private JTextArea gbResultTextArea;
    private JComboBox comboBox, fColCmbx, lstColCmbx, fKeyQueryCmbx, sKeyQueryCmbx, gbColcbx, searchComboBox;
    private CollectionTable refCollectionTable;
    private JoinCollectionTable joinColTable;
    private searchCollectionTable searchColTable;
    private JoinCollectionTable complexColTable;
    private JTable tableFrame;
    private JCheckBox filterCheckBox;
    private int height= (int)dimension.getHeight();
    private int width= (int)dimension.getWidth();
    private Object elementSelected;
    private int targetIndex;
    private int functionMode= 1;
    private List<Object> collectionsNames= new ArrayList<>(Arrays.asList(
        "Joueurs", "Calendrier_Phases_Groupes", "Calendrier_Phases_Finales", "Staff", "Visiteurs"
    ));
    private ArrayList<Document> targetElementBuffer= new ArrayList<>();
    CRUDManager crudManager= new CRUDManager("Tournoi_Tennis");
    FantasyLeagueFrame frameTest;
    String targetField= "";

    private Document searchQuery, whereQuery, addQuery, rmQuery, groupQuery, sortQuery, filterQuery;
    public UI_UX() {                                                                                    //CONSTRUCTEUR : Définition du contenu de la fenêtre
        super();
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        panel= new JPanel();                                                            //Partie Visualisation BDD de la Fenêtre 
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        refCollectionTable= new CollectionTable("Joueurs", crudManager);
        tableFrame= new JTable(refCollectionTable);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        updateRowHeights(tableFrame);
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths(tableFrame, 200);
        tableFrame.setShowGrid(true);
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(jspTable, BorderLayout.CENTER);
        panel.add(tablePanel);
        
        advFunctionPanel= new JPanel();                                                 //RESTE= Partie Interactions avec la BDD de la Fenêtre
        advFunctionPanel.setLayout(new BoxLayout(advFunctionPanel, BoxLayout.Y_AXIS));          //Fonctions Interactions Avancées avec la BDD (Jointure, GroupBy, etc.)
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
        manageColPanel.add(fLButton);
        manageColPanel.add(advFunctionPanel);

        comboBox= new JComboBox(collectionsNames.toArray());                                    //Fonctions Interactions de Base avec la BDD (Ajout, Suppression, Modification)
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
        addFieldButton= new JButton("Ajouter une Nouveau Champs");
        addFieldButton.addActionListener(this);
        newFieldText= new JTextField(10);
        newFieldLab= new JLabel();
        textItemPanel.add(addFieldButton);
        textItemPanel.add(newFieldText);
        manageColPanel.add(textItemPanel);
        
        JScrollPane jsPane= new JScrollPane(manageColPanel ,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jsPane.setMaximumSize(new Dimension(700, 1000));
        jsPane.setPreferredSize(new Dimension(700, 1000));
        panel.add(jsPane);
        setContentPane(panel);
        setTitle("MAIN_WINDOW");
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
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths(tableFrame, 200);
        updateRowHeights(tableFrame);
        tableFrame.setShowGrid(true);
        tableFrame.setAutoscrolls(true);
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(jspTable);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // -> Update contenu de la Table en fonction de la Jointure Effectuée
    private void updateJoinTableGUI(String fColName, String lColName, Document whereQuery) {
        if (fColName.equals("Calendrier_Phases_Groupes") && lColName.equals("Joueurs")){
            joinColTable= new JoinCollectionTable(fColName, lColName,
                                                    whereQuery,
                                                    "Joueurs.id_joueur", "_id", crudManager, filterCheckBox.isSelected());
            tableFrame= new JTable(joinColTable);
        } else if ((fColName.equals("Calendrier_Phases_Groupes") || fColName.equals("Calendrier_Phases_Finales")) && lColName.equals("Arbitres")) {
            joinColTable= new JoinCollectionTable(fColName, lColName,
                                                    whereQuery,
                                                    "referees.id_ref", "_id", crudManager, filterCheckBox.isSelected());
            tableFrame= new JTable(joinColTable);
        }
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths(tableFrame, 200);
        updateRowHeights(tableFrame);
        tableFrame.setShowGrid(true);
        tableFrame.setAutoscrolls(true);
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(jspTable);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // -> Update contenu de la Table en fonction de la Recherche d'éléments Effectuée
    private void updateSearchTableGUI(String colName, Document searchQuery){
        searchColTable= new searchCollectionTable(colName, searchQuery, crudManager);
        tableFrame= new JTable(searchColTable);
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableFrame.setAutoCreateRowSorter(true);
        tableFrame.getSelectionModel().addListSelectionListener(this);
        tableFrame.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths(tableFrame, 200);
        updateRowHeights(tableFrame);
        tableFrame.setShowGrid(true);
        tableFrame.setAutoscrolls(true);
        tablePanel.removeAll();
        tablePanel.revalidate();
        tablePanel.repaint();
        JScrollPane jspTable= new JScrollPane(tableFrame, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        tablePanel.add(jspTable);
        tablePanel.revalidate();
        tablePanel.repaint();
    }

    // -> Reset de la GUI
    private void updateSimpleGUI() {
        updateJoinPanel();
        updateTableGUI();
        updateColFieldsPanel(false);
    }

    // -> Update contenu du Panel de Gauche en Fonction de l'action Choisie: Join / GroupBy / Search
    private void updateComplexGUI(int functionMode) {
        if (functionMode == 1) {     //Si on fait une Jointure
            updateJoinPanel();
        } if (functionMode == 2) {        //Si on fait un GroupBy
            tablePanel.removeAll();
            tablePanel.revalidate();
            tablePanel.repaint();
            gbResultTextArea= new JTextArea(100, 75);
            tablePanel.add(new JScrollPane(gbResultTextArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS), 0);
            tablePanel.revalidate();
            tablePanel.repaint();
        } if (functionMode == 3) {      //Si on veut Rechercher 1 / des éléments
            updateSearchPanel();
        }
    }

    //FONCTION D'UPDATE DU CONTENU DU PANEL DE DROITE
    // -> Fonction de refresh du contenu des combobox pour la Jointure
    private void updateMiniJoinPanel(int cbNumber) {
        for (Component c : manageColPanel.getComponents()) {
            if (c == advFunctionPanel) {
                for (Component component : ((JPanel)advFunctionPanel).getComponents()) {
                    if (cbNumber == 2) {
                        if ((component == fColLab) || (component == fColCmbx)) {
                            advFunctionPanel.remove(component);
                        }
                    } else if (cbNumber == 1) {
                        if ((component == lstColCmbx) || (component == lColLab)) {
                            advFunctionPanel.remove(component);
                        }
                    }
                }
            }
        }
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
        if (cbNumber == 1) {
            String targetField= fColCmbx.getSelectedItem().toString();
            targetIndex= fColCmbx.getSelectedIndex();
            collectionsNames.remove(targetField);
            lColLab= new JLabel("2nde Collection");
            lstColCmbx= new JComboBox(collectionsNames.toArray());
            lstColCmbx.addItemListener(this);
            collectionsNames.add(targetIndex, targetField);
            advFunctionPanel.add(lstColCmbx, 2);
            advFunctionPanel.add(lColLab, 2);
        } else if (cbNumber == 2) {
            String targetField= lstColCmbx.getSelectedItem().toString();
            targetIndex= lstColCmbx.getSelectedIndex();
            collectionsNames.remove(targetField);
            fColLab= new JLabel("1ère Collection");
            fColCmbx= new JComboBox(collectionsNames.toArray());
            fColCmbx.addItemListener(this);
            collectionsNames.add(targetIndex, targetField);
            advFunctionPanel.add(fColCmbx, 0);
            advFunctionPanel.add(fColLab, 0);
        }
        
        advFunctionPanel.revalidate();
        advFunctionPanel.repaint();
    }

    private void updateMiniGroupByPanel(int cbNumber) {
        String colSelected= (String) gbColcbx.getSelectedItem();
        ArrayList<String> keysList= crudManager.getUIKeys(colSelected);
        String sTargetField= sKeyQueryCmbx.getSelectedItem().toString();
        targetField= fKeyQueryCmbx.getSelectedItem().toString();
        if (cbNumber == 0) {
            keysList.add(" ");
            fKeyQueryCmbx= new JComboBox(keysList.toArray());
            fKeyQueryCmbx.addItemListener(this);
            keysList.remove(" ");
            targetField= keysList.get(0);
            keysList.remove(0);
            sKeyQueryCmbx= new JComboBox(keysList.toArray());
            sKeyQueryCmbx.addItemListener(this);
            keyFJPanel.removeAll();
            keyFJPanel.removeAll();
            keyFJPanel.revalidate();
            keyFJPanel.repaint();
            keyFJPanel.add(fKeyQueryCmbx, 0);
            keyFJPanel.add(new JLabel("1ère Clé de Groupement"), 0);
        } else if (cbNumber == 1) {
            if (targetField.equals(" ")) {
                sKeyQueryCmbx= new JComboBox(keysList.toArray());
                sKeyQueryCmbx.addItemListener(this);
                sKeyQueryCmbx.setSelectedItem(sTargetField);
            } else {
                targetIndex= fKeyQueryCmbx.getSelectedIndex();
                keysList.remove(targetField);
                sKeyQueryCmbx= new JComboBox(keysList.toArray());
                sKeyQueryCmbx.addItemListener(this);
                sKeyQueryCmbx.setSelectedItem(sTargetField);
                keysList.add(targetIndex, targetField);
            }
            keyFJPanel.remove(2);
            keyFJPanel.remove(2);
        } 
        keyFJPanel.revalidate();
        keyFJPanel.repaint();
        keyFJPanel.add(sKeyQueryCmbx, 2);
        keyFJPanel.add(new JLabel("2nde Clé de Groupement"), 2);
        keyFJPanel.revalidate();
        keyFJPanel.repaint();
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
        ArrayList<String> keysList= refCollectionTable.getUICol();
        cbJPanel.setLayout(new BoxLayout(cbJPanel, BoxLayout.X_AXIS));
        keyFJPanel.setLayout(new BoxLayout(keyFJPanel, BoxLayout.Y_AXIS));
        refColPanel.setLayout(new BoxLayout(refColPanel, BoxLayout.Y_AXIS));
        gbColcbx= new JComboBox(collectionsNames.toArray());
        gbColcbx.addItemListener(this);
        String field= keysList.get(1);
        keysList.remove(1);
        keysList.add(" ");
        fKeyQueryCmbx= new JComboBox(keysList.toArray());
        keysList.remove(" ");
        keysList.add(1, field);
        fKeyQueryCmbx.addItemListener(this);
        String targetField= fKeyQueryCmbx.getSelectedItem().toString();
        targetIndex= fKeyQueryCmbx.getSelectedIndex();
        keysList.remove(targetField);
        sKeyQueryCmbx= new JComboBox(keysList.toArray());
        sKeyQueryCmbx.addItemListener(this);
        keysList.add(targetIndex, targetField);
        gbRequestTextField= new JTextField();
        refColPanel.add(new JLabel("Sur quelle table effectuer un Groupement"));
        refColPanel.add(gbColcbx);
        keyFJPanel.add(new JLabel("1ère Clé de groupement"));
        keyFJPanel.add(fKeyQueryCmbx);
        keyFJPanel.add(new JLabel("2nde Clé de groupement"));
        keyFJPanel.add(sKeyQueryCmbx);
        cbJPanel.add(refColPanel);
        cbJPanel.add(keyFJPanel);
        advFunctionPanel.add(cbJPanel);
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
    private void updateColFieldsPanel(boolean isNewField) {
        String newField= newFieldText.getText();
        textItemPanel.removeAll();
        textItemPanel.revalidate();
        textItemPanel.repaint();
        for (String col : refCollectionTable.getUICol()) {
            textItemPanel.add(new JLabel(col));
            textItemPanel.add(new JTextField(10));
        } 
        if (isNewField) {
            newFieldLab.setText(newField);
            textItemPanel.add(newFieldLab);
            textItemPanel.add(new JTextField(10));
        }
        textItemPanel.add(addFieldButton);
        textItemPanel.add(newFieldText);
        manageColPanel.revalidate();
        manageColPanel.repaint();
    }

    // -> Fonction de Modification de la hauteur des lignes de la Table
    private void updateRowHeights(JTable table){
        int prevHeight=table.getRowHeight();
        boolean isMaxHeight= false;
        for (int row = 0; row < table.getRowCount(); row++){
            int rowHeight = table.getRowHeight();
            if (prevHeight < rowHeight) {
                isMaxHeight= true;
            }
            for (int column = 0; column < table.getColumnCount(); column++)
            {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getMaximumSize().height);
                if (prevHeight < rowHeight) {
                    isMaxHeight= true;
                }
            }
            if (isMaxHeight) {
                isMaxHeight= false;
                table.setRowHeight(row, rowHeight+30);
            } else {
                table.setRowHeight(row, rowHeight);
            }
            
        }
    }

    // -> Fonction de Modification de la largeur des colonnes de la Table
    public static void setColumnWidths(JTable table, int width) {
        TableColumnModel columnModel = table.getColumnModel();
        var enumColumn= columnModel.getColumns();
        while(enumColumn.hasMoreElements()) {
            enumColumn.nextElement().setPreferredWidth(width);
        }
    }

    public void addDoc(Component[] components) {
        Document newElement= new Document();
        int indexCol= 0;
        StringTokenizer docTokenizer;
        StringTokenizer fieldTokenizer;
        StringTokenizer keyValTokenizer;
        ArrayList<Document> addDocList= new ArrayList<>();
        Document doc= new Document();
        int idVal= 0;
        String keyString= null;
        try {
            for (Component component : components) {
                if (component instanceof JLabel) {
                    keyString= ((JLabel) component).getText();
                } else if (component instanceof JTextField) {
                    String text= ((JTextField) component).getText();
                    if (text.contains("||")) {
                        System.out.println(text);
                        String targetString= text.replace(" || ", "||");
                        targetString= targetString.replace(" , ", ",");
                        targetString= targetString.replace(", ", ",");
                        System.out.println(targetString);
                        docTokenizer= new StringTokenizer(text, "||");
                        while (docTokenizer.hasMoreTokens()) {
                            fieldTokenizer= new StringTokenizer(docTokenizer.nextToken(), ",");
                            while (fieldTokenizer.hasMoreTokens()) {
                                keyValTokenizer= new StringTokenizer(fieldTokenizer.nextToken(), ": ");
                                if (keyValTokenizer.countTokens() != 2) {
                                    System.out.println(keyValTokenizer.countTokens());
                                    System.out.println("Erreur de formatage de la chaine de caractère");
                                }
                                String keyVal= keyValTokenizer.nextToken();
                                String stringVal= keyValTokenizer.nextToken();
                                if (stringVal.matches("[0-9]+")) {
                                    int intVal= Integer.valueOf(stringVal);
                                    doc.append(keyVal, intVal);
                                } else {
                                    doc.append(keyVal, stringVal);
                                }
                            }
                            addDocList.add(doc);
                            doc= new Document();
                        }
                        newElement.append(keyString, addDocList);
                        addDocList= new ArrayList<>();
                    } else if (text.contains(",")) {
                        String targetString= text.replace(" , ", ",");
                        targetString= targetString.replace(", ", ",");
                        fieldTokenizer= new StringTokenizer(targetString, ",");
                        while (fieldTokenizer.hasMoreTokens()) {
                            String token= fieldTokenizer.nextToken();
                            keyValTokenizer= new StringTokenizer(token, ": ");
                            String keyVal= keyValTokenizer.nextToken();
                            if (keyValTokenizer.countTokens() > 2) {
                                StringTokenizer altTokenizer= new StringTokenizer(token, ":");
                                keyVal= altTokenizer.nextToken();
                                String stringVal= altTokenizer.nextToken();
                                if (stringVal.matches("[0-9]+")) {
                                    int intVal= Integer.parseInt(stringVal);
                                    doc.append(keyVal, intVal);
                                } else {
                                    doc.append(keyVal, stringVal);
                                }
                            } else {
                                String stringVal= keyValTokenizer.nextToken();
                                if (stringVal.matches("[0-9]+")) {
                                    int intVal= Integer.parseInt(stringVal);
                                    doc.append(keyVal, intVal);
                                } else {
                                    doc.append(keyVal, stringVal);
                                }
                            }
                        }
                        newElement.append(keyString, Arrays.asList(doc));
                        doc= new Document();
                    } else if (text.contains(":")) {
                        keyValTokenizer= new StringTokenizer(text, ": ");
                        if (keyValTokenizer.countTokens() > 2) {
                            StringTokenizer altTokenizer= new StringTokenizer(text, ":");
                            String keyVal= altTokenizer.nextToken();
                            String stringVal= altTokenizer.nextToken();
                            if (stringVal.matches("[0-9]+")) {
                                int intVal= Integer.parseInt(stringVal);
                                doc.append(keyVal, intVal);
                            } else {
                                doc.append(keyVal, stringVal);
                            }
                        } else {
                            String keyVal= keyValTokenizer.nextToken();
                            String stringVal= keyValTokenizer.nextToken();
                            if (stringVal.matches("[0-9]+")) {
                                int intVal= Integer.parseInt(stringVal);
                                doc.append(keyVal, intVal);
                            } else {
                                doc.append(keyVal, stringVal);
                            }
                        }
                        newElement.append(keyString, Arrays.asList(doc));
                        doc= new Document();
                    } else {
                        if (text.matches("[0-9]+")) {
                            int intVal= Integer.parseInt(text);
                            newElement.append(keyString, intVal);
                        }
                        
                        newElement.append(keyString, text);
                    }
                    indexCol += 1;
                }
                if (component instanceof JButton) {
                    break;
                }
            }
            crudManager.addDocInCollection(newElement,(String) comboBox.getSelectedItem());
            updateTableGUI();
        } catch (Exception exception) {
            // TODO: handle exception
            exception.printStackTrace();
        }
    }

    // -> Fonction de Modification d'un Document
    public void updateDoc(String targetVal, int indexTextComponent, String key, int idDoc, String idString) {
        StringTokenizer docTokenizer;
        StringTokenizer fieldTokenizer;
        StringTokenizer keyValTokenizer;
        ArrayList<Document> addDocList= new ArrayList<>();
        Document doc= new Document();
        Document updateFieldDoc= new Document();
        if (targetVal.contains("||")) {       // --* Si le champs / key contient plusieurs sous documents
            System.out.println(targetVal);
            String targetString= targetVal.replace(" || ", "||");
            System.out.println(targetString);
            docTokenizer= new StringTokenizer(targetVal, "||");
            while (docTokenizer.hasMoreTokens()) {
                fieldTokenizer= new StringTokenizer(docTokenizer.nextToken(), ",");
                while (fieldTokenizer.hasMoreTokens()) {
                    keyValTokenizer= new StringTokenizer(fieldTokenizer.nextToken(), ": ");
                    if (keyValTokenizer.countTokens() != 2) {
                        System.out.println(keyValTokenizer.countTokens());
                        System.out.println("Erreur de formatage de la chaine de caractère");
                    }
                    String keyVal= keyValTokenizer.nextToken();
                    String stringVal= keyValTokenizer.nextToken();
                    if (stringVal.matches("[0-9]+")) {
                        int intVal= Integer.parseInt(stringVal);
                        doc.append(keyVal, intVal);
                    } else {
                        doc.append(keyVal, stringVal);
                    }
                }
                addDocList.add(doc);
                doc= new Document();
            }
            updateFieldDoc.append(key, addDocList);
            if (idString != null) {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idString), updateFieldDoc);
            } else {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idDoc), updateFieldDoc);
            }
            updateFieldDoc.clear();
            addDocList.clear();
        } else if (targetVal.contains(",")) {  // --* Si le champs / key contient UN SEUL sous document
            fieldTokenizer= new StringTokenizer(targetVal, ",");
            while (fieldTokenizer.hasMoreTokens()) {
                String token= fieldTokenizer.nextToken();
                keyValTokenizer= new StringTokenizer(token, ": ");
                String keyVal= keyValTokenizer.nextToken();
                if (keyValTokenizer.countTokens() > 2) {
                    StringTokenizer altTokenizer= new StringTokenizer(token, ":");
                    keyVal= altTokenizer.nextToken();
                    String stringVal= altTokenizer.nextToken();
                    if (stringVal.matches("[0-9]+")) {
                        int intVal= Integer.parseInt(stringVal);
                        updateFieldDoc.append(keyVal, intVal);
                    } else {
                        updateFieldDoc.append(keyVal, stringVal);
                    }
                } else {
                    String stringVal= keyValTokenizer.nextToken();
                    if (stringVal.matches("[0-9]+")) {
                        int intVal= Integer.parseInt(stringVal);
                        updateFieldDoc.append(keyVal, intVal);
                    } else {
                        updateFieldDoc.append(keyVal, stringVal);
                    }
                }
            }
            if (idString != null) {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idString), new Document(key, Arrays.asList(updateFieldDoc)));
            } else {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idDoc), new Document(key, Arrays.asList(updateFieldDoc)));
            }
            updateFieldDoc.clear();
        } else {                                        // --* Si le champs / key contient une valeur simple
            updateFieldDoc.append(key, targetVal);
            if (idString != null) {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idString), updateFieldDoc);
            } else {
                crudManager.updateDocument((String) comboBox.getSelectedItem(), new Document("_id", idDoc), updateFieldDoc);
            }
            updateFieldDoc.clear();
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
        if (targetObject == addButton) {                // --* Ajout d'un élément suivant le contenu des JTextFields
                                                        // ,= séparations des champs du sous document / || séparation des sous documents d'un champs du document "parent"
            ArrayList<String> activeColList= refCollectionTable.getUICol();
            Component[] compoList= textItemPanel.getComponents();
            JTextField idTextField= (JTextField) textItemPanel.getComponents()[1];
            
            if ((idTextField.getText() == "") || (idTextField.getText() == null) || (idTextField.getText() == " ")) {
                System.out.println("Vous ne pouvez pas Ajouter l'élément car il n'a pas d'ID");
            } else {                                    
                addDoc(compoList);
            }
            
        } if (targetObject == removeButton) {           // --* Suppression d'un ou plusieurs éléments sélectionné dans la JTable
            System.out.println("Appuie sur le bouton de Suppression");
            if (!targetElementBuffer.isEmpty()) {
                crudManager.delDocInCollection(targetElementBuffer,(String) comboBox.getSelectedItem());
                updateTableGUI();
                targetElementBuffer= new ArrayList<>();
            }
        } if (targetObject == updateButton) {           // --* Modification d'un ou plusieurs éléments sélectionné dans la JTable
                                                        // ,= séparations des champs du sous document / || séparation des sous documents d'un champs du document "parent"
            System.out.println("Appuie sur le bouton de Modification");
            ArrayList<String> activeColList= refCollectionTable.getUICol();
            int indexBaseKey= 1;
            Document lastWhereQuery= targetElementBuffer.get(targetElementBuffer.size()-1);
            String collection= (String) comboBox.getSelectedItem();
            Document lastDoc= crudManager.searchOneElement(collection, lastWhereQuery);
            int idVal= 0;
            String idString= null;
            if (lastDoc.size() == 0) {
                if (lastWhereQuery.get("_id") instanceof Integer) {
                    idString= String.valueOf(lastWhereQuery.get("_id"));
                } else if (lastWhereQuery.get("_id") instanceof String) {
                    idString= (String) lastWhereQuery.get("_id");
                }
                lastWhereQuery= new Document("_id", idString);
                lastDoc= crudManager.searchOneElement(collection, lastWhereQuery);
            }
            int docCounter= 0;
            String activeKey= "";
            for (Component component : textItemPanel.getComponents()) {
                if (component instanceof JLabel) {
                    activeKey= ((JLabel) component).getText();
                }
                else if (component instanceof JTextField) {
                    if ((lastDoc.get(activeKey) == null) && (activeColList.contains(activeKey))) {
                        
                    }
                    else if (!activeKey.equals("_id")) {
                        String targetVal= ((JTextField) component).getText();
                        if ((!activeColList.contains(activeKey)) || (!lastDoc.get(activeKey).equals(targetVal))) {
                            updateDoc(targetVal, indexBaseKey, activeKey, idVal, idString);
                        }
                    }
                }
                if (component instanceof JButton) {
                    break;
                }
                    
            }
            updateTableGUI();
        } if (targetObject == joinButton) {             // --* Sélection du Mode Jointure
            functionMode= 1;
            updateComplexGUI(functionMode);
        } if (targetObject == groupbyButton) {          // --* Sélection du Mode Group By
            functionMode= 2;
            updateComplexGUI(functionMode);
            updateGroupByPanel();
        } if (targetObject == searchButton) {           // --* Sélection du Mode Recherche d'un élément
            functionMode= 3;
            updateComplexGUI(functionMode);
        } if (targetObject == vButton) {                // --* Valider l'opération souhaitée
            if (functionMode == 1) {                            //Mode Jointure de deux Collections sélectionné
                whereQuery= new Document();
                String firstCol= (String) fColCmbx.getItemAt(fColCmbx.getSelectedIndex());
                String lastCol= (String) lstColCmbx.getItemAt(lstColCmbx.getSelectedIndex());
                updateJoinTableGUI(firstCol, lastCol, whereQuery);
            } else if (functionMode == 2) {                     //Mode Group By sélectionné
                String targetCol= ((String) gbColcbx.getSelectedItem());
                String fKeySelected= "$" + ((String) fKeyQueryCmbx.getSelectedItem());
                String sKeySelected= "$" + ((String) sKeyQueryCmbx.getSelectedItem());
                Document keysDoc= new Document();
                Document countQuery= new Document();
                Document sortQuery= new Document();
                if (((String) fKeyQueryCmbx.getSelectedItem()).equals(" ")) {
                    groupQuery= new Document("_id", sKeySelected);
                    groupQuery.append(((String) sKeyQueryCmbx.getSelectedItem()), new Document("$count", countQuery));
                    sortQuery.append(((String) sKeyQueryCmbx.getSelectedItem()), -1);
                } else {
                    keysDoc.append(((String) fKeyQueryCmbx.getSelectedItem()), fKeySelected).append(((String) sKeyQueryCmbx.getSelectedItem()), sKeySelected);
                    groupQuery= new Document("_id", keysDoc);

                    groupQuery.append(((String) fKeyQueryCmbx.getSelectedItem()), new Document("$count", countQuery));
                }
                crudManager.getUIGroupByElements(targetCol, groupQuery, gbResultTextArea);
            } else {                                            //Mode Recherche d'un élément sélectionné
                if ((searchTextField.getText()).matches("[0-9]+")) {
                    int val= Integer.valueOf(searchTextField.getText());
                    searchQuery= new Document((String) searchComboBox.getSelectedItem(), val);
                } else {
                    searchQuery= new Document((String) searchComboBox.getSelectedItem(), searchTextField.getText());
                }
                updateSearchTableGUI((String) comboBox.getSelectedItem(), searchQuery);
                searchTextField.setText("");
            }
        } if (targetObject == cancelButton) {
            updateSimpleGUI();
        }
        if ((targetObject == addButton) || (targetObject == removeButton) || (targetObject == updateButton) || (targetObject == joinButton) || (targetObject == groupbyButton) || (targetObject == searchButton) || (targetObject == cancelButton)) {
            for (Component component : textItemPanel.getComponents()) { // Reset des champs de saisie
                if (component instanceof JTextField) {
                    ((JTextField) component).setText("");
                }
            }
        }
        if (targetObject == addFieldButton) {
            updateColFieldsPanel(true);
            newFieldText.setText("");
        }

    }

    // -> Fonction de paramétrage des actions des ComboBox (Changement de Collection)
    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == comboBox) {
            updateSimpleGUI();              // --* Affichage de la table de la Collection sélectionnée sur l'Interface
        } else if (e.getSource() == fColCmbx) {    // --* Update du Contenu de la 2nde ComboBox en fonction de la collection sélectionnée pour la 1ère
            updateMiniJoinPanel(1);
        } else if (e.getSource() == lstColCmbx) {    // --* Update du Contenu de la 1ère ComboBox en fonction de la collection sélectionnée pour la 1ère
            updateMiniJoinPanel(2);
        } else if (e.getSource() == fKeyQueryCmbx) {    // --* Update du Contenu de la 2nde ComboBox en fonction de la collection sélectionnée pour la 1ère
            updateMiniGroupByPanel(1);
        } else if (e.getSource() == gbColcbx) {    // --* Update du Contenu de la 2nde ComboBox en fonction de la collection sélectionnée pour la 1ère
            updateMiniGroupByPanel(0);
        }
    }

    // -> Fonction de Link entre Table et champs de texte (Sélection d'une ou plusieurs lignes)
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // TODO Auto-generated method stub
        int[] rowSelected;
        int index= 0;
        rowSelected= tableFrame.getSelectedRows();
        if (rowSelected.length > 0) {
            for (int i : rowSelected) {             // --* Récupération des ID de tous les éléments sélectionnés et chargement dans une liste
                int val= Integer.valueOf((String) tableFrame.getValueAt(rowSelected[rowSelected.length-1], 0));
                Document targetDocument= new Document("_id", val);
                if (!targetElementBuffer.contains((Document) targetDocument)) {
                    targetElementBuffer.add(targetDocument);
                } else {
                    targetElementBuffer.remove(targetDocument);
                    targetElementBuffer.add(targetDocument);
                }

            }
            for (var component : textItemPanel.getComponents()) {   // --* Chargement des champs de texte avec les valeurs de la dernière ligne sélectionnée
                if (component instanceof JTextField) {
                    String strValue= null;
                    JTextField targetTextField= (JTextField) component;
                    targetTextField.setText("");
                    try {
                        strValue= (String) tableFrame.getValueAt(rowSelected[rowSelected.length-1], index);
                    } catch (Exception exeption) {
                        // TODO: handle exception
                    }
                    if (strValue == null) {
                        strValue= "";
                    } else if (strValue.contains("<html>")) {
                        strValue= strValue.replace("<html>", "");
                        strValue= strValue.replace("<br>-----------<br>", " || ");
                        strValue= strValue.replace("<br>-----------", "");
                        strValue= strValue.replace("<br>", ",");
                    }
                    targetTextField.setText(strValue);
                    index += 1;
                }
            }
        }

    }

}
