package compression;

import java.io.*;
import java.util.zip.*;
/**
 * Cette classe fournit une compression et une décompression utilisant l'algorithme Deflate.
 *
 */
public class Compressor {
    
    public static Boolean compressData(File input, String output) throws IOException, Exception {
        HuffmanCompress.beginHuffmanCompress(input.getAbsolutePath());
        return true;
    }

    public static Boolean decompressData(String input, String output) throws IOException, Exception  {
        HuffmanDecompress.beginHuffmanDecompress(input, output);
        return true;
    }
}
