package net.pt.dsa.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ptaucher on 20.01.2019<br/>
 * Tool to extract charactor portraits from HEADS.NVF
 * C:\Users\Peter\OneDrive - PERFORM GROUP\NLT\Files\STAR.DAT\HEADS.NVF
 */
public class PortraitExtractor {

  public static void main(String... args) throws IOException {
    Properties settings = new Properties();
    settings.load(PortraitConverter.class.getResourceAsStream("/dsatools.properties"));

    /*
    File dir = new File(settings.getProperty("source.image.dir"));
    File portraitFile = new File(settings.getProperty("source.chr.file"));

    File[] pngFiles = dir.listFiles(f -> f.getName().toLowerCase().endsWith(".png"));

    DsaUtil.convertPortraits(pngFiles, portraitFile);
    */

    File headsFile = new File("C:\\Users\\Peter\\OneDrive - PERFORM GROUP\\NLT\\Files\\SCHWEIF\\STAR.DAT\\CHEADS2.NVF");

    BufferedImage image = DsaUtil.readDsaImageFromHeads(headsFile);

    DsaUtil.writeImage(new File("./headstest6.png"), image, "heads");

    // Try to read last 0x400 (32 x 32 = 1024) bytes
  }
}
