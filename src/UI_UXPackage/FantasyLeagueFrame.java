package UI_UXPackage;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.bson.Document;

import com.formdev.flatlaf.FlatIntelliJLaf;

import FantasyLeagueGame.PongEngine;
import NoSQLPackage.CRUDManager;
import ai.djl.nn.BlockList;

import java.awt.Component;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FantasyLeagueFrame extends JFrame implements ActionListener{
    Image img;
    int bgWidth= 500, bgHeight= 500;
    PongEngine env;
    JPanel mainPanel, pongPanel, betPanel, schedulePanel;
    JButton startPongButton, betButton,validBetButton, cancelBetButton, groupButton, finalButton;
    JTable tableFL, betTable;
    JLabel winnerLabel;
    CollectionTable tableCollection, betTableCollection;
    int betId= 0;
    JScrollPane jspBetTable, jspScheduleTable;
    JFrame betFrame;
    CRUDManager crudManager;
    boolean isFLFinished= false;
    boolean isBetValidated= false;
    boolean isPongStarted= false;
    boolean isPongFinished= false;
    public FantasyLeagueFrame() {
        super("Fantasy League");
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainPanel= new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
        pongPanel= new JPanel();
        pongPanel.setLayout(new BoxLayout(pongPanel, BoxLayout.Y_AXIS));
        this.env= new PongEngine();
        startPongButton= new JButton("Start Pong");
        startPongButton.addActionListener(this);
        try {
            img= ImageIO.read(new File("C:\\Users\\nclsr\\OneDrive\\Bureau\\Cours_L3IA\\Base_de_Donnees_NoSQL\\Projet_Tournoi_Tennis\\src\\FantasyLeagueGame\\Tennis_Background.png"));
            env.initEnv(img);
            bgWidth= img.getWidth(null);
            bgHeight= img.getHeight(null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        crudManager= new CRUDManager("Tournoi_Tennis");

        schedulePanel= new JPanel();
        schedulePanel.setLayout(new BoxLayout(schedulePanel, BoxLayout.X_AXIS));
        JPanel buttonPanel= new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        groupButton= new JButton("Calendrier des phases de groupes");
        groupButton.addActionListener(this);
        groupButton.setEnabled(false);
        finalButton= new JButton("Calendrier des phases finales");
        finalButton.addActionListener(this);
        tableCollection= new CollectionTable("Calendrier_Phases_Groupes", crudManager);
        betButton= new JButton("Parier sur un joueur");
        betButton.addActionListener(this);
        tableFL= new JTable(tableCollection);
        tableFL.setAutoCreateRowSorter(true);
        updateRowHeights(tableFL);
        jspScheduleTable= new JScrollPane(tableFL, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        winnerLabel= new JLabel("");
        //betPanel.add(jspBetTable);
        //betPanel.add(validBetButton);
        pongPanel.add(env);
        pongPanel.add(startPongButton);
        schedulePanel.add(groupButton);
        schedulePanel.add(finalButton);
        buttonPanel.add(schedulePanel);
        buttonPanel.add(betButton);
        buttonPanel.add(winnerLabel);

        mainPanel.add(pongPanel);
        mainPanel.add(jspScheduleTable);
        mainPanel.add(buttonPanel);
        
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1800, 1000);

        betFrame= new JFrame("Parie");
        betPanel= new JPanel();
        betPanel.setLayout(new BoxLayout(betPanel, BoxLayout.X_AXIS));
        betTableCollection= new CollectionTable("Joueurs_FL", crudManager);
        betTable= new JTable(betTableCollection);
        updateRowHeights(betTable);
        jspBetTable= new JScrollPane(betTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        betTable.setAutoCreateRowSorter(true);
        validBetButton= new JButton("Valider le parie");
        validBetButton.addActionListener(this);
        cancelBetButton= new JButton("Retour");
        cancelBetButton.addActionListener(this);
        JPanel betButtonPanel= new JPanel();
        betButtonPanel.setLayout(new BoxLayout(betButtonPanel, BoxLayout.Y_AXIS));
        betButtonPanel.add(validBetButton);
        betButtonPanel.add(cancelBetButton);
        betPanel.add(jspBetTable);
        betPanel.add(betButtonPanel);
        betFrame.setContentPane(betPanel);
        betFrame.setSize(1000, 600);


        this.setVisible(true);
    }

    public void playPong() {
        this.env.playGame();
        isPongFinished= true;
    }

    private void updateRowHeights(JTable table){
        for (int row = 0; row < table.getRowCount(); row++){
            int rowHeight = table.getRowHeight();

            for (int column = 0; column < table.getColumnCount(); column++)
            {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            table.setRowHeight(rowHeight);
        }
    }

    public void updateColGUI(String colName, JPanel panel) {
        int indexWidget= -1;
        int targetIndex= 0;
        for (var widget : panel.getComponents()) {
            indexWidget++;
            if (widget instanceof JScrollPane) {
                targetIndex= indexWidget;
                panel.remove(widget);
            }
                
            }
        panel.revalidate();
        if (colName.equals("Joueurs_FL")) {
            betTableCollection= new CollectionTable("Joueurs_FL", crudManager);
            betTable= new JTable(betTableCollection);
            betTable.setAutoCreateRowSorter(true);
            updateRowHeights(betTable);
            jspBetTable= new JScrollPane(betTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            panel.add(jspBetTable, targetIndex);
        } else {
            tableCollection= new CollectionTable(colName, crudManager);
            tableFL= new JTable(tableCollection);
            updateRowHeights(tableFL);
            tableFL.setAutoCreateRowSorter(true);
            jspScheduleTable= new JScrollPane(tableFL, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
                                                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            panel.add(jspScheduleTable, targetIndex);
        }
        panel.revalidate();
    }

    public CRUDManager getCRUDManager() {
        return crudManager;
    }
    public void processFL() {
        Random rand= new Random();
        ArrayList<Integer> idList= new ArrayList<>();
        int compteurUpdateDoc= 0;
        int prevId= 0;
        int nbPlayer= 0;
        int firstPreFinalPhase= 20100, firstGeneralPhase= 20100;
        int firstFinalPhase= 20132;
        int firstEigthPhase= 20148;
        int firstQuarterPhase= 20156;
        int firstSemiPhase= 20160;
        int Final= 20162;
        while (isFLFinished == false) {
            System.out.println("isBetValidated: "+ isBetValidated);      
            if (isBetValidated == true) {
                /*try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                //Pre-Final OK
                for (int i = 0; i < 96; i++) {
                    compteurUpdateDoc++;;
                    int idFinalist= 10001 + rand.nextInt(961);
                    while (prevId == idFinalist) {
                        idFinalist= 10001 + rand.nextInt(961);
                        
                    }
                    prevId= idFinalist;
                    idList.add(idFinalist);
                    nbPlayer++;
                    System.out.println("nbPlayer: "+ nbPlayer);
                    Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                    if (player != null) {
                        crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Pre-final"));    
                        if (compteurUpdateDoc >= 3) {
                            crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstGeneralPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)), new Document("id_joueur", idList.get(2)))));
                            idList.removeAll(idList);
                            firstGeneralPhase++;
                            compteurUpdateDoc= 0;
                        }
                    }
                }
                /*try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                //16th-Final OK
                for (int i = 0; i < 16; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstPreFinalPhase));
                    int idJ1= rand.nextInt(3);
                    int idJ2= rand.nextInt(3);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstPreFinalPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstPreFinalPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "16th-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstFinalPhase++;
                    firstPreFinalPhase++;
                    
                }
                //8th-Final OK
                firstFinalPhase= 20132;
                for (int i = 0; i < 8; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstFinalPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "8th-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstEigthPhase++;
                    firstFinalPhase++;
                    
                }
                //Quarter-Final OK
                firstEigthPhase= 20148;
                for (int i = 0; i < 4; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstEigthPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Quarter-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstEigthPhase++;
                    firstQuarterPhase++;
                    
                }
                //Semi-Final OK
                firstQuarterPhase= 20156;
                for (int i = 0; i < 2; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstQuarterPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Semi-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstSemiPhase++;
                    firstQuarterPhase++;
                    
                }
                //Final OK
                firstSemiPhase= 20160;
                Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase));
                int idJ1= rand.nextInt(2);
                int idJ2= rand.nextInt(2);
                for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                    if (compteurUpdateDoc == idJ1) {
                        idList.add((Integer) playerDoc.get("id_joueur"));
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                firstSemiPhase++;
                Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase));
                for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                    if (compteurUpdateDoc == idJ2) {
                        idList.add((Integer) playerDoc.get("id_joueur"));
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", Final), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                for (Integer idFinalist : idList) {
                    Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                    if (player != null) {
                        crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Final"));    
                    }
                }
                idList.removeAll(idList);
                    
                //Winner OK
                Document finalEvent= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", Final));    
                int player1Index= rand.nextInt(2);
                int player1Id= 0, player2Id= 0;
                for (Document playerDoc : (ArrayList<Document>)finalEvent.get("Joueurs")) {
                    if (compteurUpdateDoc == player1Index) {
                        player1Id= (Integer) playerDoc.get("id_joueur");
                    } else {
                        player2Id= (Integer) playerDoc.get("id_joueur");
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                while (isPongStarted == false) {
                    winnerLabel.setText("Appuyer sur Start Pong pour simuler la finale");
                }
                Thread pongThread= new Thread() {
                    public void run() {
                        playPong();
                    }
                };
                pongThread.start();
                try {
                    pongThread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Finale Terminée");
                int winnerId= 0;
                if (env.getScoreJ1() >= 3.0) {
                    winnerId= player1Id;
                } else if (env.getScoreJ2() >= 3.0){
                    winnerId= player2Id;
                }
                Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", winnerId));
                if (player != null) {
                    crudManager.updateDocument("Joueurs_FL", new Document("_id", winnerId), new Document("tournament_phase", "Winner"));    
                    winnerLabel.setText("Le joueur présenti pour remporter le Tournoi est: " + (String)player.get("last_name") + " " + (String)player.get("first_name"));
                }
                isFLFinished= true;
                
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        Object buttonPressed= e.getSource();
        if (buttonPressed == finalButton) {
            updateColGUI("Calendrier_Phases_Finales", mainPanel);
            finalButton.setEnabled(false);
            groupButton.setEnabled(true);
        } if (buttonPressed == groupButton) {
            updateColGUI("Calendrier_Phases_Groupes", mainPanel);
            groupButton.setEnabled(false);
            finalButton.setEnabled(true);
        } else if (buttonPressed == startPongButton) {
            isPongStarted= true;
        } else if (buttonPressed == betButton) {
            updateColGUI("Joueurs_FL", betPanel);
            betFrame.setVisible(true);
        } else if (buttonPressed == cancelBetButton) {
            betFrame.setVisible(false);
        } else if (buttonPressed == validBetButton) {
            Boolean isBetPlayer= true;
            int rowSelected= betTable.getSelectedRow();
            betId= Integer.valueOf((String) betTable.getValueAt(rowSelected, 0));
            ArrayList<Document> joueurList= crudManager.searchUIDocument("Joueurs_FL", new Document("_id", betId));
            Document targetPlayer= joueurList.get(joueurList.size()-1);
            System.out.println("Bet on player: "+betId);
            if (betId != 0) {
                validBetButton.setEnabled(false);
                isBetPlayer= (Boolean) targetPlayer.get("is_bet_player");
            } if (isBetPlayer == false) {
                crudManager.updateDocument("Joueurs_FL", new Document("_id", betId), new Document("is_bet_player", true));
            }
            isBetValidated= true;
            updateColGUI("Joueurs_FL", betPanel);
            betFrame.setVisible(false);
            
        } 
    }

    public static void main(String[] args) {
        FantasyLeagueFrame frameTest= new FantasyLeagueFrame();
        new Thread() {
            public void run() {
                frameTest.processFL(frameTest);
            }
        }.start();
    }

    public static void processFL(FantasyLeagueFrame frameTest) {
        Random rand= new Random();
        CRUDManager crudManager= frameTest.getCRUDManager();
        ArrayList<Integer> idList= new ArrayList<>();
        int compteurUpdateDoc= 0;
        int prevId= 0;
        int nbPlayer= 0;
        int firstPreFinalPhase= 20100, firstGeneralPhase= 20100;
        int firstFinalPhase= 20132;
        int firstEigthPhase= 20148;
        int firstQuarterPhase= 20156;
        int firstSemiPhase= 20160;
        int Final= 20162;
        while (frameTest.isFLFinished == false) {
            System.out.println("isBetValidated: "+frameTest.isBetValidated);      
            if (frameTest.isBetValidated == true) {
                /*try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                //Pre-Final OK
                for (int i = 0; i < 96; i++) {
                    compteurUpdateDoc++;;
                    int idFinalist= 10001 + rand.nextInt(961);
                    while (prevId == idFinalist) {
                        idFinalist= 10001 + rand.nextInt(961);
                        
                    }
                    prevId= idFinalist;
                    idList.add(idFinalist);
                    nbPlayer++;
                    System.out.println("nbPlayer: "+ nbPlayer);
                    Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                    if (player != null) {
                        crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Pre-final"));    
                        if (compteurUpdateDoc >= 3) {
                            crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstGeneralPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)), new Document("id_joueur", idList.get(2)))));
                            idList.removeAll(idList);
                            firstGeneralPhase++;
                            compteurUpdateDoc= 0;
                        }
                    }
                }
                /*try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }*/
                //16th-Final OK
                for (int i = 0; i < 16; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstPreFinalPhase));
                    int idJ1= rand.nextInt(3);
                    int idJ2= rand.nextInt(3);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstPreFinalPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstPreFinalPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "16th-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstFinalPhase++;
                    firstPreFinalPhase++;
                    
                }
                //8th-Final OK
                firstFinalPhase= 20132;
                for (int i = 0; i < 8; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstFinalPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstFinalPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "8th-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstEigthPhase++;
                    firstFinalPhase++;
                    
                }
                //Quarter-Final OK
                firstEigthPhase= 20148;
                for (int i = 0; i < 4; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstEigthPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstEigthPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Quarter-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstEigthPhase++;
                    firstQuarterPhase++;
                    
                }
                //Semi-Final OK
                firstQuarterPhase= 20156;
                for (int i = 0; i < 2; i++) {
                    Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase));
                    int idJ1= rand.nextInt(2);
                    int idJ2= rand.nextInt(2);
                    for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ1) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    firstQuarterPhase++;
                    Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstQuarterPhase));
                    for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                        if (compteurUpdateDoc == idJ2) {
                            idList.add((Integer) playerDoc.get("id_joueur"));
                        }
                        compteurUpdateDoc++;
                    }
                    compteurUpdateDoc= 0;
                    crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                    for (Integer idFinalist : idList) {
                        Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                        if (player != null) {
                            crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Semi-final"));    
                        }
                    }
                    idList.removeAll(idList);
                    firstSemiPhase++;
                    firstQuarterPhase++;
                    
                }
                //Final OK
                firstSemiPhase= 20160;
                Document player1Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase));
                int idJ1= rand.nextInt(2);
                int idJ2= rand.nextInt(2);
                for (Document playerDoc : (ArrayList<Document>)player1Event.get("Joueurs")) {
                    if (compteurUpdateDoc == idJ1) {
                        idList.add((Integer) playerDoc.get("id_joueur"));
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                firstSemiPhase++;
                Document player2Event= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", firstSemiPhase));
                for (Document playerDoc : (ArrayList<Document>)player2Event.get("Joueurs")) {
                    if (compteurUpdateDoc == idJ2) {
                        idList.add((Integer) playerDoc.get("id_joueur"));
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                crudManager.updateDocument("Calendrier_Phases_Finales", new Document("_id", Final), new Document("Joueurs", Arrays.asList(new Document("id_joueur", idList.get(0)), new Document("id_joueur", idList.get(1)))));
                for (Integer idFinalist : idList) {
                    Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", idFinalist));
                    if (player != null) {
                        crudManager.updateDocument("Joueurs_FL", new Document("_id", idFinalist), new Document("tournament_phase", "Final"));    
                    }
                }
                idList.removeAll(idList);
                    
                //Winner OK
                Document finalEvent= crudManager.searchOneElement("Calendrier_Phases_Finales", new Document("_id", Final));    
                int player1Index= rand.nextInt(2);
                int player1Id= 0, player2Id= 0;
                for (Document playerDoc : (ArrayList<Document>)finalEvent.get("Joueurs")) {
                    if (compteurUpdateDoc == player1Index) {
                        player1Id= (Integer) playerDoc.get("id_joueur");
                    } else {
                        player2Id= (Integer) playerDoc.get("id_joueur");
                    }
                    compteurUpdateDoc++;
                }
                compteurUpdateDoc= 0;
                while (frameTest.isPongStarted == false) {
                    frameTest.winnerLabel.setText("Appuyer sur Start Pong pour simuler la finale");
                }
                Thread pongThread= new Thread() {
                    public void run() {
                        frameTest.playPong();
                    }
                };
                pongThread.start();
                try {
                    pongThread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Finale Terminée");
                int winnerId= 0;
                if (frameTest.env.getScoreJ1() >= 3.0) {
                    winnerId= player1Id;
                } else if (frameTest.env.getScoreJ2() >= 3.0){
                    winnerId= player2Id;
                }
                Document player= crudManager.searchOneElement("Joueurs_FL", new Document("_id", winnerId));
                if (player != null) {
                    crudManager.updateDocument("Joueurs_FL", new Document("_id", winnerId), new Document("tournament_phase", "Winner"));    
                    frameTest.winnerLabel.setText("Le joueur présenti pour remporter le Tournoi est: " + (String)player.get("last_name") + " " + (String)player.get("first_name"));
                }
                frameTest.isFLFinished= true;
                
            }
        }
    }

}
