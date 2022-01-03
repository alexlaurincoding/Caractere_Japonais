import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Université du Québec à Montréal (UQAM) Logiciel de réechantillonage.
 *
 * TP2: MISE EN PAGE DE CARACTÈRES JAPONAIS: Cette classe cree le ficher html en utilisant le meme nom que le fichier
 *  en entree et en modifiant l'extention .txt en .html. A la creation, le texte d'entete est generer. Par la suite,
 *  cette classe permet d'ajouter les symbole ascii des caracteres dans le tableau, d'ajouter et de fermer
 * des colonnes pour chaque lignes lu et de fermer le fichier html.
 *
 * @author Alexandre Laurin (LAUA23108205)
 * @Cours : INF2120 - 020 - Automne 2021
 * @Évaluation: TP2
 * @version 2021-10-07
 */

public class EcritureFichier {

    private final String nomFichier;
    private FileWriter monWriter;

    /**
     * Constructeur prenant en argument le nom du fichier lu.
     * Modification de l'extention en .html et appel de la methode de creation du fichier.
     *
     * @param nomFichier Le nom du fichier a analyser
     */
    public EcritureFichier(String nomFichier) {
        this.nomFichier = nomFichier.replaceAll("txt$", "html");
        creeFichier();
    }

    /**
     * Cette methode cree un nouveau fichier html. si le fichier existe deja il sera ecraser.
     * L'entete de la page html est ecrite. Creation du FileWriter.
     * Une IOException exception est lancee et attrappee si une erreur survient dans la creation du fichier
     */
    private void creeFichier(){
        try {
            File fichierSortie = new File(nomFichier);
            if (fichierSortie.createNewFile()) {
                System.out.println("Fichier " + fichierSortie.getName() + " créé avec succès.");
            }
            this.monWriter = new FileWriter(nomFichier);
            monWriter.write("<!DOCTYPE html> \n" +
                    "<html> \n" +
                    "    <head> \n" +
                    "        <title>TP 2</title> \n" +
                    "    </head> \n" +
                    "    <body> \n" +
                    "        <hr> \n" +
                    "        <table>\n");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Cette methode ajoute une colonne dans le tableau du fichier html
     * Une IOException exception est lancee et attrappee si une erreur survient l'ors de l'ecriture.
     */
    public void ajouterColonne(){
        try {
            monWriter.write("           <tr>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode ferme une colonne dans le tableau du fichier html
     * Une IOException exception est lancee et attrappee si une erreur survient l'ors de l'ecriture.
     */
    public void fermerColonne(){
        try {
            monWriter.write("           </tr>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode ajoute le unicode du caractere passe en argument dans le tableau.
     * Une IOException exception est lancee et attrappee si une erreur survient l'ors de l'ecriture.
     *
     * @param symbole le Unicode a ajouter
     */
    public void ajouterSymbole(String symbole){
        if(symbole != null){
            if(symbole.length() == 10){
                ajouterSymbole(symbole.substring(0,5), symbole.substring(5));
            }else{
                try {
                    monWriter.write("               <td>&#" + symbole + ";</td>\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Cette methode ajoute les unicodes des caracteres passe en argument dans le tableau.
     * Une IOException exception est lancee et attrappee si une erreur survient l'ors de l'ecriture.
     *
     * @param symbole1 premier unicode a ajouter
     * @param symbole2 deuxieme unicode a ajouter
     */
    public void ajouterSymbole(String symbole1, String symbole2){
        try {
            monWriter.write("               <td>&#" + symbole1 + ";&#"
                    + symbole2 +";</td>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fermeture du fichier en fermant le tableau, le body et la balise html.
     * Fermeture du filewriter.
     * Une IOException exception est lancee et attrappee si une erreur survient l'ors de l'ecriture.
     */
    public void fermerFichier(){
        try {
            monWriter.write("        </table> \n" +
                    "        <hr> \n" +
                    "    </body> \n" +
                    "</html>");
            monWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
