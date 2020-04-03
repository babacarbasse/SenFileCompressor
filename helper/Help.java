package helper;

/**
 *
 * @author babacarbasse
 */
public class Help {   
    public static void display() {
        System.out.println("======================================================================");
        System.out.println("                         SenFileCompressor                             ");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("Les options disponibles");
        System.out.println();
        System.out.println("-h: \n \t Afficher l'aide");
        System.out.println();
        System.out.println("-c <liste fichier à compresser>: \n \t fourni en sortie un fichier d’extension « .sfc » qui regroupe, \n sous forme compressé, les différents fichiers fournis en paramètre.");
        System.out.println();
        System.out.println("–d fichierADecompresser.sfc: \n \t fourni en sortie l’intégralité des fichiers contenus dans l’archive \n donné en paramètre.");
        System.out.println();
        System.out.println("[Option] –r: \n \t spécifier le chemin (absolu ou relatif) vers le répertoire où seront \n stockés les fichiers (ou le fichier) produits par le programme.");
        System.out.println();
        System.out.println("[Option] –v: \n \t Afficher la verbosité du programme.");
    }

    public static void displayVerbocityCompress() {
        System.out.println("======================================================================");
        System.out.println("                         SenFileCompressor                             ");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("Utilisation de l'archivage et compression de fichier");
        System.out.println();
        System.out.println("java SenFileCompressor -c <liste fichier à compresser> (1).");
        System.out.println();
        System.out.println();
        System.out.println("1: les noms des fichiers sont séparés par des espaces.");
        System.out.println();
        System.out.println();

    }

    public static void displayVerbocityUnCompress() {
        System.out.println("======================================================================");
        System.out.println("                         SenFileCompressor                             ");
        System.out.println("======================================================================");
        System.out.println();
        System.out.println("Utilisation du désarchivage et décompression de fichier");
        System.out.println();
        System.out.println("java SenFileCompressor -d fichierADecrompresser.sfc ");
        System.out.println();
        System.out.println();
    }
}