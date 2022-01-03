import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Université du Québec à Montréal (UQAM) Logiciel de réechantillonage.
 *
 * TP2 Réechantillonage : Cette classe analyse une chaine de caractere, extrait les syllabes japonaises et genere
 * le unicode correspondant. Le unicode est ecris dans le fichier a l'aide de l'instance d'ecriture du fichier
 * passe en argument dans le constructeur.
 *
 * @author Alexandre Laurin (LAUA23108205)
 * @Cours : INF2120 - 020 - Automne 2021
 * @Évaluation: TP2
 * @version 2021-10-07
 *
 * */

public class Traduction {

    private final ArrayList<String> lignes = new ArrayList<>();
    private final Map<String, String> hiragana = new HashMap<>();
    private final Map<String, String> katagana = new HashMap<>();

    /**
     * Constructeur prenant en argument le texte a analyser et l'instance d'ecriture du fichier.
     * Les hiragana et katagana sont stocke dans des Hashmap avec leur unicode correspondant.
     * Appel de la methode trouverLignes qui genere un arraylist de lignes.
     * Appel de la methode parcourirTexte qui trouve les syllabes et ecris les unicodes dans le fichier.
     * Fermeture du fichier html.
     *
     * @param texte le texte a analyser
     * @param fichier l'instance d'ecriture du fichier
     */
    public Traduction(String texte, EcritureFichier fichier ){
        loadHiragana();
        loadKatagana();
        trouverLignes(texte);
        parcourirTexte(lignes, fichier);
        fichier.fermerFichier();
    }

    /**
     * Cette methode trouve les syllabes et ecris les unicodes dans le fichier.
     * Appel de la methode finLecture pour determiner si la lecture du fichier est completer.
     * Appel de la methode longueurMax pour determiner la longueur de la plus longue ligne.
     *
     * @param texte Un arrayListe contenant les lignes du texte.
     * @param fichier l'instance permettant l'ecriture du fichier
     */
    private void parcourirTexte(ArrayList<String> texte, EcritureFichier fichier){
        int[] compteur = new int[texte.size()];
        int longueurMax = getLongueurMax(texte);
        while(!finLecture(compteur, texte)) {
            fichier.ajouterColonne();
            for (int i = texte.size() - 1; i >= 0; i--) {
                String syllabe = "";
                do {
                    if(texte.get(i).length() <= compteur[i]){
                        syllabe = Constantes.UNICODE_ESPACE;
                    }else if (texte.get(i).charAt(compteur[i]) != ' ') {
                        syllabe += texte.get(i).charAt(compteur[i]);
                    }
                    compteur[i]++;
                } while (!syllabe.equals(Constantes.UNICODE_ESPACE)  && !estVoyelle(texte.get(i).toLowerCase()
                        .charAt(compteur[i]-1)) && !syllabe.toLowerCase().endsWith("n'") && compteur[i] < longueurMax);

                if(!syllabe.equals("")){
                    fichier.ajouterSymbole(getUnicode(syllabe));
                }
            }
            fichier.fermerColonne();
        }
    }

    /**
     * Cette methode trouve la longueur de la ligne la plus longue.
     *
     * @param texte Un arrayListe contenant les lignes du texte.
     * @return la longueur du la ligne la plus longue.
     */
    private int getLongueurMax(ArrayList<String> texte){
        int longueur = 0;
        for (int i = 0; i < texte.size(); i++) {
            if(texte.get(i).length() > longueur){
                longueur = texte.get(i).length();
            }
        }
        return longueur;
    }

    /**
     * Cette methode determine si la lecture du texte est termine.
     *
     * @param compteur un tableau de compteur contenant le nombre d'unicode ecrit pour chaque lignes.
     * @param texte les lignes du texte
     * @return True si chaque ligne a generer autaut ou plus de unicode que la longueur de la ligne. false sinon.
     */
    private boolean finLecture(int[] compteur, ArrayList<String> texte){
        int finLigne = 0;
        for (int i = 0; i < texte.size(); i++) {
            if(compteur[i] >= texte.get(i).length()-1){
                finLigne++;
            }
        }
        return finLigne == texte.size();
    }

    /**
     * Determine si le caractere est une voyelle.
     *
     * @param lettre le caractere a analyser.
     * @return true si le caractere est une voyelle {a,i,u,e,o}, False sinon.
     */
    private boolean estVoyelle(char lettre){
        boolean estVoyelle = false;
        char[] voyelles = {'a', 'i', 'u', 'e', 'o'};
        for (int i = 0; i < voyelles.length; i++) {
            if(lettre == voyelles[i]){
                estVoyelle = true;
            }
        }
        return estVoyelle;
    }

    /**
     * Cette methode determine si la syllabe est un Hiragana, un Katagane ou un espace.
     * Si la syllabe commence par une minuscule, la methode getHiragana est appeler.
     * Si la syllabe commence par une majuscule, la methode getKatagana est appeler.
     * Si la syllabe est le unicode de l'espace, ce unicode est retournee.
     *
     * @param syllabe la syllabe a traduire.
     * @return Le unicode de la syllabe.
     */
    private String getUnicode(String syllabe){
        String unicode;
        if(syllabe.equals(Constantes.UNICODE_ESPACE)){
            unicode = Constantes.UNICODE_ESPACE;
        }else if(syllabe.charAt(0) >= Constantes.UNICODE_MAJUSCULE_MIN &&
                syllabe.charAt(0) <= Constantes.UNICODE_MAJUSCULE_MAX ){
            unicode = getKatagana(syllabe.toUpperCase());
        }else{
            unicode = getHiragana(syllabe.toLowerCase());
        }
        return unicode;
    }

    /**
     * Cette methode retourne le unicode du Katagana passe en argument.
     *
     * @param syllabe la syllabe a traduire.
     * @return Le unicode de la syllabe.
     */
    private String getKatagana(String syllabe){
        String unicode = null;
        if(syllabe.length() == 3){
            String yayuyo = syllabe.substring(1);
            if(yayuyo.equals("YA") || yayuyo.equals("YU") || yayuyo.equals("YO")){
                unicode = katagana.get(syllabe.charAt(0) +  "I") +
                        (Integer.parseInt(katagana.get(yayuyo)) - 1);
            }
        }
        if(unicode == null){
            unicode = katagana.get(syllabe);
        }
        return unicode;
    }

    /**
     * Cette methode retourne le unicode du Hiragana passe en argument.
     *
     * @param syllabe la syllabe a traduire.
     * @return Le unicode de la syllabe.
     */

    private String getHiragana(String syllabe){
        String unicode = null;
        if(syllabe.length() == 3){
            String yayuyo = syllabe.substring(1);
            if(yayuyo.equals("ya") || yayuyo.equals("yu") || yayuyo.equals("yo")){
                unicode = hiragana.get(syllabe.charAt(0) + "i") +
                        (Integer.parseInt(hiragana.get(yayuyo)) - 1);
            }
        }
        if(unicode == null){
            unicode = hiragana.get(syllabe);
        }
        return unicode;
    }

    /**
     * Divise le texte passe en argument en lignes et les sauvegarde dans le arrayListe <code>lignes</code>.
     */
    private void trouverLignes(String texte){
        int compteur = 0;
        for (int i=0; i<texte.length(); i++){
            if (texte.charAt(i) == '\n') {
                lignes.add(texte.substring(compteur, i));
                compteur = i + 1;
            }
        }
        if(texte.substring(compteur).length() > 0){
            lignes.add(texte.substring(compteur));
        }
    }

    /**
     * Enregistre les Hiraganas et leurs unicode correspondant dans un HashMap.
     */
    private void loadHiragana(){
        hiragana.put("a", "12354");
        hiragana.put("i", "12356");
        hiragana.put("u", "12358");
        hiragana.put("e", "12360");
        hiragana.put("o", "12362");
        hiragana.put("ka", "12363");
        hiragana.put("ki", "12365");
        hiragana.put("ku", "12367");
        hiragana.put("ke", "12369");
        hiragana.put("ko", "12371");
        hiragana.put("sa", "12373");
        hiragana.put("shi", "12375");
        hiragana.put("su", "12377");
        hiragana.put("se", "12379");
        hiragana.put("so", "12381");
        hiragana.put("ta", "12383");
        hiragana.put("chi", "12385");
        hiragana.put("tsu", "12388");
        hiragana.put("te", "12390");
        hiragana.put("to", "12392");
        hiragana.put("na", "12394");
        hiragana.put("ni", "12395");
        hiragana.put("nu", "12396");
        hiragana.put("ne", "12397");
        hiragana.put("no", "12398");
        hiragana.put("ha", "12399");
        hiragana.put("hi", "12402");
        hiragana.put("fu", "12405");
        hiragana.put("he", "12408");
        hiragana.put("ho", "12411");
        hiragana.put("ma", "12414");
        hiragana.put("mi", "12415");
        hiragana.put("mu", "12416");
        hiragana.put("me", "12417");
        hiragana.put("mo", "12418");
        hiragana.put("ya", "12420");
        hiragana.put("yu", "12422");
        hiragana.put("yo", "12424");
        hiragana.put("ra", "12425");
        hiragana.put("ri", "12426");
        hiragana.put("ru", "12427");
        hiragana.put("re", "12428");
        hiragana.put("ro", "12429");
        hiragana.put("wa", "12431");
        hiragana.put("wi", "12432");
        hiragana.put("we", "12433");
        hiragana.put("wo", "12434");
        hiragana.put("ga", "12364");
        hiragana.put("gi", "12366");
        hiragana.put("gu", "12368");
        hiragana.put("ge", "12370");
        hiragana.put("go", "12372");
        hiragana.put("za", "12374");
        hiragana.put("ji", "12376");
        hiragana.put("zu", "12378");
        hiragana.put("ze", "12380");
        hiragana.put("zo", "12382");
        hiragana.put("da", "12384");
        hiragana.put("dji", "12386");
        hiragana.put("dzu", "12389");
        hiragana.put("de", "12391");
        hiragana.put("do", "12393");
        hiragana.put("ba", "12400");
        hiragana.put("bi", "12403");
        hiragana.put("bu", "12406");
        hiragana.put("be", "12409");
        hiragana.put("bo", "12412");
        hiragana.put("pa", "12401");
        hiragana.put("pi", "12404");
        hiragana.put("pu", "12407");
        hiragana.put("pe", "12410");
        hiragana.put("po", "12413");
        hiragana.put("n'", "12435");
    }

    /**
     * Enregistre les Kataganas et leurs unicode correspondant dans un HashMap.
     */
    private void loadKatagana(){
        katagana.put("A", "12450");
        katagana.put("I", "12452");
        katagana.put("U", "12454");
        katagana.put("E", "12456");
        katagana.put("O", "12458");
        katagana.put("KA", "12459");
        katagana.put("KI", "12461");
        katagana.put("KU", "12463");
        katagana.put("KE", "12465");
        katagana.put("KO", "12467");
        katagana.put("SA", "12469");
        katagana.put("SHI", "12471");
        katagana.put("SU", "12473");
        katagana.put("SE", "12475");
        katagana.put("SO", "12477");
        katagana.put("TA", "12479");
        katagana.put("CHI", "12481");
        katagana.put("TSU", "12484");
        katagana.put("TE", "12486");
        katagana.put("TO", "12488");
        katagana.put("NA", "12490");
        katagana.put("NI", "12491");
        katagana.put("NU", "12492");
        katagana.put("NE", "12493");
        katagana.put("NO", "12494");
        katagana.put("HA", "12495");
        katagana.put("HI", "12498");
        katagana.put("FU", "12501");
        katagana.put("HE", "12504");
        katagana.put("HO", "12507");
        katagana.put("MA", "12510");
        katagana.put("MI", "12511");
        katagana.put("MU", "12512");
        katagana.put("ME", "12513");
        katagana.put("MO", "12514");
        katagana.put("YA", "12516");
        katagana.put("YU", "12518");
        katagana.put("YO", "12520");
        katagana.put("RA", "12521");
        katagana.put("RI", "12522");
        katagana.put("RU", "12523");
        katagana.put("RE", "12524");
        katagana.put("RO", "12525");
        katagana.put("WA", "12527");
        katagana.put("WI", "12528");
        katagana.put("WE", "12529");
        katagana.put("WO", "12530");
        katagana.put("GA", "12460");
        katagana.put("GI", "12462");
        katagana.put("GU", "12464");
        katagana.put("GE", "12466");
        katagana.put("GO", "12468");
        katagana.put("ZA", "12470");
        katagana.put("JI", "12472");
        katagana.put("ZU", "12474");
        katagana.put("ZE", "12476");
        katagana.put("ZO", "12478");
        katagana.put("DA", "12480");
        katagana.put("DJI", "12482");
        katagana.put("DZU", "12485");
        katagana.put("DE", "12487");
        katagana.put("DO", "12489");
        katagana.put("BA", "12496");
        katagana.put("BI", "12499");
        katagana.put("BU", "12502");
        katagana.put("BE", "12505");
        katagana.put("BO", "12508");
        katagana.put("PA", "12497");
        katagana.put("PI", "12500");
        katagana.put("PU", "12503");
        katagana.put("PE", "12506");
        katagana.put("PO", "12509");
        katagana.put("N'", "12531");
    }

}
