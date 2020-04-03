import archivage.Archive;
import compression.Compressor;
import helper.Help;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 *
 * @author babacarbasse
 */
public class SenFileCompressor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Help.display();
            return;
        } else {
            if (args[0].equals("-h")) {
                Help.display();
                return;
            }
            if (args[0].equals("-c")) {
                if (args.length == 1 || (args[1].startsWith("-") && !args[1].equals("-v"))) {
                    System.out.println("ERROR");
                    System.out.println("\t\tVeillez donner le(s) fichier(s) à compresser");    
                    Help.display();
                    return;
                }

                if (args[1].equals("-v")) {
                    Help.displayVerbocityCompress();
                    return;
                }
                packCompresse(args);
                return;
            }
            if (args[0].equals("-d")) {
                System.out.println("Décompresser les fichiers");
                if (args.length == 1 || (args[1].startsWith("-") && !args[1].equals("-v"))) {
                    System.out.println("ERROR");
                    System.out.println("\t\tVeillez donner le fichier à décompresser");    
                    Help.display();
                    return;
                }

                if (args[1].equals("-v")) {
                    Help.displayVerbocityUnCompress();
                    return;
                }
                unCompressUnpack(args);
                return;
            }
            System.out.println("ERROR");
            System.out.println("Erreur de paramêtres");
            Help.display();
            return;
        }
    }

    static void packCompresse(String[] args) {
        int nbFiles = 0;
        for (int i = 1;i<args.length; i++) {
            if ((args[i].startsWith("-") && !args[i].equals("-r")) ||
                 (args[i].equals("-r") && i != (args.length - 2))
            ) {
                Help.display();
                return;
            }
            if (args[i].equals("-r")) {
                break;
            } else {
                nbFiles++;
            }
        }
        String output = "";
        if (args[args.length - 2].equals("-r")) {
            if (Files.isDirectory(Paths.get(args[args.length - 1]))) {
                output = Paths.get(args[args.length - 1]).toAbsolutePath().toString();
            } else {
                System.out.println("Le repertoire de sortie n'existe pas "+ args[args.length - 1]);
                return;
            }
        } else {
            output = System.getProperty("user.dir");
        }

        String [] files = new String[nbFiles];
        files = Arrays.copyOfRange(args, 1, nbFiles+1);
        File tmp = Archive.packFiles(files, output);
        if (tmp != null) {
            try {
                // On cree le nom du fichier final puis on compress le fichier temporaire
                String finalOuput = tmp.getParent()+"/"+tmp.getName().substring(0, tmp.getName().length() - 4) + ".sfc";
                System.out.println(finalOuput);
                if (Compressor.compressData(tmp, finalOuput)) {
                    //Fichier archivé et compress avec success.
                    //on supprime le fichier temporaire
                    System.out.println("Archiver avec succes: " + finalOuput);
                    tmp.delete();
                }
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
            return;
        } else {
            System.out.println("Une erreur est survenu lors de l'archivage");
            return;
        }
    }

    static void unCompressUnpack(String[] args) {
        int nbFiles = 0;
        for (int i = 1;i<args.length; i++) {
            if ((args[i].startsWith("-") && !args[i].equals("-r")) ||
                 (args[i].equals("-r") && i != (args.length - 2))
            ) {
                Help.display();
                return;
            }
            if (args[i].equals("-r")) {
                break;
            } else {
                nbFiles++;
            }
        }
        if (nbFiles != 1) {
            System.out.println("Vous devez donner un seul fichier à décompresser");
            Help.display();
            return;
        }
        if (!args[1].endsWith(".sfc")) {
            System.out.println("Veuillez donner un fichier .sfc à décompresser");
            Help.display();
            return;
        }

        File file = new File(args[1]);
        if (!file.exists()) {
            System.out.println("Erreur de décompression");
            System.out.println("Le fichier \"" + args[1] + "\" n'exitste pas.");
            return;
        }
        
        String output = "";
        if (args[args.length - 2].equals("-r")) {
            if (Files.isDirectory(Paths.get(args[args.length - 1]))) {
                output = Paths.get(args[args.length - 1]).toAbsolutePath().toString();
            } else {
                System.out.println("Le repertoire de sortie n'existe pas "+ args[args.length - 1]);
                return;
            }
        } else {
            output = System.getProperty("user.dir");
        }
        String tmpFile = "";

        if (output.endsWith("/")) {
            tmpFile = output+args[1].substring(0, args[1].length() - 4) + ".tmp";
        } else {
            tmpFile = output+"/"+args[1].substring(0, args[1].length() - 4) + ".tmp";
        }

        Boolean uncompress = false;
        try {
            uncompress = Compressor.decompressData(args[1], tmpFile);
        } catch(Exception e) {
            System.out.println("Une erreur est survenue: "+e.getMessage());
        }
        // on decompress d'abord le fichier ...
        if (uncompress) {
            File tmp = new File(tmpFile);
            Boolean out = Archive.unPackFile(tmp.getAbsolutePath(), tmp.getParent());
            if (out) {
                System.out.println("Décompression effectuer avec succès");
                //on supprime le fichier temporaire
                tmp.delete();
                return;
            } else {
                //on supprime le fichier temporaire
                tmp.delete();
                System.out.println("Une erreur est survenu lors du désarchivage");
                return;
            }
        } else {
            System.out.println("Une erreur est survenu lors du décompression");
            return;
        }
    }
    
}


