package archivage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author babacar
 * @version 1.0.0
 * @date 02/02/2018 01:24
 */
public class Archive {

    public static File packFiles(String[] listeStrFiles, String outputPath) {
        List<File> files = new ArrayList<>();
        for (String strfile : listeStrFiles) {
            File file = new File(strfile);
            if (!file.exists()) {
                System.out.println("Erreur de compression");
                System.out.println("Le fichier \"" + strfile + "\" n'exitste pas.");
                return null;
            }
            files.add(file);
        }
        //On crée un nouveau fichier de sortie
        //On recupere le timestamp courant qui sera le nom du fichier de sorte a eviter les fichiers qui existe deja
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (outputPath.endsWith("/")) {
            outputPath +="new"+timestamp.getTime()+".tmp";
        } else {
            outputPath +="/new"+timestamp.getTime()+".tmp";
        }
        File out = new File(outputPath);
        if(!out.exists()) {
            try {
                out.createNewFile(); // On essaye de créer le fichier de sortie s'il n'existe pas.
            } catch (IOException e) {
                System.out.println("Impossible de créer le fichier de sortie !");
            } 
        }

        //On initialise notre Writer et notre Reader à null
        DataOutputStream writer = null;
        DataInputStream reader = null;
        try {
            writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(out)));

            //on ecrit le nombre d'element à archiver
            writer.writeInt(files.size()); // on l'ecrit au début du fichier de sortie.
            //Ensuite on parcours chaque element(fichier) à ecrire
            for (File file : files) {
                //On recupere le nom + la longueur du fichier
                String name = file.getName();
                long length = file.length();
                
                //On ecrit alors ces informations
                writer.writeUTF(name);
                writer.writeLong(length);

                //Puis on copie le contenu du fichier à la suite
                reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                final byte[] buffer = new byte[1024]; //tampon 
                int dataCount;
                while((dataCount = reader.read(buffer, 0, buffer.length)) != -1 )
                {
                    writer.write(buffer, 0, dataCount);
                    System.out.println("Archivage du fichier " + name);
                }
                //On ferme le flux de lecture.
                reader.close();
            }

        } catch(IOException e){
            System.out.println("Erreur lors de l'archivage du fichier : ");
            e.printStackTrace();
        } finally {
            try {
                if(writer != null) {
                    writer.flush();
                    writer.close();
                }
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Impossible de fermer correctement les flux ");
            }
          
        }
        return out;
    }

    public static Boolean unPackFile(String input, String outputPath) {
        System.out.println(input);
        //On crée le repertoire de sortie
        // On utilise encore le timeStamp pour eviter les erreurs de repertoire qui existe deja
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        outputPath +="/Output "+timestamp.getTime();
        Boolean success = (new File(outputPath )).mkdirs();
        if (!success) {
            System.out.println("Erreur lors de la création du repertoire de sortie");
            return false;
        }
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        try {
            dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(input)));
            int fileCount = dataInputStream.readInt();
            System.out.println("L'archive contient "+  fileCount + " fichiers ");
            for(int i = 0 ; i < fileCount ; i++) {
                String fileName = dataInputStream.readUTF();
                long bytes = dataInputStream.readLong();
                System.out.println(" Unpacking : " + fileName +" - " + bytes/1024 + " kb");
                String filePath = outputPath+"/"+fileName;
                File outFile = new File(filePath);
                if(!outFile.exists())
                {
                    outFile.createNewFile();
                }
                //on initialise le buffer d'ecriture dans le fichier out
                dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
                //ecriture du fichier
                final byte[] buffer = new byte[(int)bytes];
                int dataCount;
                dataCount = dataInputStream.read(buffer,0,(int)bytes);
                dataOutputStream.write(buffer, 0, dataCount);
                
                //On ferme le flux d'ecriture
                dataOutputStream.flush();
                dataOutputStream.close();
            }
            return true;
		} catch (FileNotFoundException e1) {
			System.out.println("Impossible de lire dans le fichier source ! ");
			return false;
        } catch (IOException e2) {
            System.out.println("Erreur lors de la lecture du fichier");
            return false;
		}
    }
}