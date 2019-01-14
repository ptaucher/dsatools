package net.pt.dsa.action;

import net.pt.dsa.DsaTools;
import net.pt.dsa.util.DsaUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class ConvertPortraitActionDSA1 extends AbstractDsaToolsAction {
  private static final String DSA1_IMG_DIR = "dsa1.dir.img";

  public ConvertPortraitActionDSA1(DsaTools tools) {
    super(tools);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    // First select PNG files
    File imgDirDefault = new File(tools.getPreference(DSA1_IMG_DIR, "."));
    final JFileChooser chooser = new JFileChooser(imgDirDefault);
    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    chooser.setMultiSelectionEnabled(true);
    chooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".png") || f.isDirectory();
      }
      @Override
      public String getDescription() {
        return "PNG Files";
      }
    });

    int returnValue = chooser.showOpenDialog(tools.getFrame());
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File[] pngFiles = chooser.getSelectedFiles();
      if (pngFiles != null && pngFiles.length > 0) {
        tools.setPreference(DSA1_IMG_DIR, pngFiles[0].getParent());
        tools.setDsa1ConvertPortraitsDir(pngFiles[0].getParentFile());

        int choice = JOptionPane.showConfirmDialog(tools.getFrame(), pngFiles.length + " " + tools.getTexts().getString("main.dsa2.convertportrait.makeitso"));
        if (choice == JOptionPane.OK_OPTION) {
          try {
            File portraitFile = new File(FileUtils.getTempDirectory(), "DUMMY.CHR");

            Path path = portraitFile.toPath();
            Files.copy(
                    ClassLoader.getSystemResourceAsStream(tools.getSetting("source.chr.file")),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);

            DsaUtil.convertPortraits(pngFiles, portraitFile);

          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      } else {
        JOptionPane.showMessageDialog(tools.getFrame(), tools.getTexts().getString("main.dsa2.convertportrait.nopngs"));
      }
    }
  }
}
