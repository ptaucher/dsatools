package net.pt.dsa.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Properties;

/**
 * Created by ptaucher on 14.01.2019
 */
public class FileUtilsTest {

  public static void main(String... args) throws Exception {

    // Create file for test(s) with range Byte (min -128 to max +127 values)
    /*
    try (FileOutputStream out = new FileOutputStream(new File(FileUtils.getTempDirectory(), "TEST.BIN"))) {
      for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
        out.write(i);
      }
      out.flush();
    }
    */

    /*
    // Copy binary test file via Path source using Files.copy
    Files.copy(
        Paths.get("./src/main/resources/TEST.BIN"),
        Paths.get("./TEST.BIN.1"),
        StandardCopyOption.REPLACE_EXISTING);

    // Copy binary test file via File source using FileUtils.copyFile
    FileUtils.copyFile(
        new File("./src/main/resources/TEST.BIN"),
        new File("./TEST.BIN.2"));

    // Copy binary test file from classpath using Files.copy
    Files.copy(
        Paths.get(FileUtilsTest.class.getClassLoader().getResource("TEST.BIN").toURI()),
        Paths.get("./TEST.BIN.3"),
        StandardCopyOption.REPLACE_EXISTING);

    // Copy binary test file from classpath using FileUtils.copyURLToFile
    FileUtils.copyURLToFile(
        ClassLoader.getSystemResource("TEST.BIN"),
        new File("./TEST.BIN.4"));
    */

    byte[] data_fs = Files.readAllBytes(Paths.get("./src/main/resources.bin/TEST.BIN"));

    System.out.println("FS: " + data_fs.length + "\n" + Arrays.toString(data_fs));

    byte[] data_cp = Files.readAllBytes(Paths.get(FileUtilsTest.class.getClassLoader().getResource("TEST.bin").toURI()));

    System.out.println("CP: " + data_cp.length + "\n" + Arrays.toString(data_cp));
  }
}
