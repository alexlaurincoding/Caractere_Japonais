import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Locale;
import java.util.Scanner;

/**
 * Université du Québec à Montréal (UQAM) Logiciel de réechantillonage.
 *
 * TP2: MISE EN PAGE DE CARACTÈRES JAPONAIS Cette classe lit le fichier passe en argument et sauvegarde le texte dans
 * une chaine de caractere.
 *
 * @author Alexandre Laurin (LAUA23108205)
 * @Cours : INF2120 - 020 - Automne 2021
 * @Évaluation: TP2
 * @version 2021-10-07
 *
 * */

public class LecteurFichier {

    private final String nomFichier;
    private String text;

    /**
     * Constructeur prenant en argument le nom du fichier a lire
     * Appelle la methode lire.
     *
     * @param nomFichier Le nom du fichier a analyser
     */
    public LecteurFichier(String nomFichier){
        this.nomFichier = nomFichier;
        lire();
    }

    /**
     * Cette methode lit le fichier passe en argument dans le constructeur.
     * Appel des methodes pour extraire les donnees et les mettre dans les variables appropriees.
     * Si une exception est generer par les differentes methodes appelée, un message d'erreur est afficher et le
     * programme se termine avec le statut -1.
     */
    public void lire(){
        try {
            FileReader fr = new FileReader(nomFichier);
            BufferedReader fichier = new BufferedReader(fr);
            Scanner sc = new Scanner(fichier);

            sc.useLocale( Locale.CANADA );

            sc.useDelimiter("\\Z");
            text = sc.next();
            sc.close();
        } catch ( FileNotFoundException e ) {
            System.err.println(Constantes.MSG_FICHIER_INTROUVABLE);
            System.exit( -1 );
        }
    }

    public String getText() {
        return text;
    }

    public String getNomFichier() {
        return nomFichier;
    }
}
