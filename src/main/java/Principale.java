import java.util.Scanner;

/**
 * Université du Québec à Montréal (UQAM)
 *
 * TP2: MISE EN PAGE DE CARACTÈRES JAPONAIS
 * Ce logiciel va accepter un texte (série de caractères occidentaux) écrit sur des lignes de gauche
 * à droite et de haut en bas. Le texte produit en sortie sera un fichier html affichant les caracteres Hiragana
 * et Katagana correspondant et sera lisible sur des colonnes de haut en bas et de droite à gauche.
 *
 * @author Alexandre Laurin (LAUA23108205)
 * @Cours : INF2120 - 020 - Automne 2021
 * @Évaluation: TP2
 * @version 2021-10-07
 */

public class Principale {

    /**
     * Porte d'entree du programme de réechantillonage.
     * Lit un fichier, génère un échantillonage et affiche les valeurs des y réechantillonées.
     *
     * @param args tableau de String
     */
    public static void main(String[] args) {
        Scanner clavier = new Scanner( System.in );
        LecteurFichier fichierEntree = lireFichier(clavier);
        EcritureFichier fichierSortie = new EcritureFichier(fichierEntree.getNomFichier());
        clavier.close();
        new Traduction(fichierEntree.getText(), fichierSortie);
    }

    /**
     * Lire le fichier de valeurs pour le reechantillonage.
     * L'utilisateur doit entrer le nom du fichier a lire.
     *
     * @return L'instance du fichier lu.
     */
    public static LecteurFichier lireFichier(Scanner clavier){
        System.out.println(Constantes.MSG_ENTRE_NOM_FICHIER);
        String nom  = clavier.nextLine();
        if(!nom.endsWith(Constantes.EXTENTION_TEXTE)){
            System.err.println(Constantes.MSG_ERR_NOM_FICHIER);
            System.exit(-1);
        }
         return new LecteurFichier(nom);
    }
}
