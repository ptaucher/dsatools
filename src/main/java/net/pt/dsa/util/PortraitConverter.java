package net.pt.dsa.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ptaucher on 11.01.2019<br/>
 * Tool to convert all PNG files in a directory (and patching TEST.chr)
 */
public class PortraitConverter {

  public static void main(String... args) throws IOException {
    Properties settings = new Properties();
    settings.load(PortraitConverter.class.getResourceAsStream("/dsatools.properties"));

    File dir = new File(settings.getProperty("source.image.dir"));
    File portraitFile = new File(settings.getProperty("source.chr.file"));

    File[] pngFiles = dir.listFiles(f -> f.getName().toLowerCase().endsWith(".png"));

    DsaUtil.convertPortraits(pngFiles, portraitFile);
  }
}
