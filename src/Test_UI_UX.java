
import javax.swing.SwingUtilities;

import UI_UXPackage.UI_UX;

// --> CLASSE: Programme Principal d'utilisation de l'IHM
public class Test_UI_UX {
    public static void main(String[] args) {
        SwingUtilities.invokeLater((new Runnable() {
            public void run() {
                UI_UX frame= new UI_UX();
                frame.setVisible(true);
            }
        }));
    }
}
