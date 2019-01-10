package net.pt.dsa.action;

import net.pt.dsa.DsaTools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class SelectNewPortraitActionDSA1 extends AbstractDsaToolsAction {
  private static final String DSA1_IMG_FILE = "dsa1.file.img";

  public SelectNewPortraitActionDSA1(DsaTools tools) {
    super(tools);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    // First select CHR file
    File chrFileDefault = new File(tools.getPreference(DSA1_IMG_FILE, "."));
    JFileChooser chooser = new JFileChooser(chrFileDefault);

    int returnValue = chooser.showOpenDialog(tools.getFrame());
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = chooser.getSelectedFile();
      tools.setPreference(DSA1_IMG_FILE, selectedFile.getAbsolutePath());

      // Now display new portrait in DSA palette from selected image file ...
      System.out.println(selectedFile.getAbsolutePath());

      tools.setDsa1SwitchPortraitImgFile(selectedFile);
    }
  }
}
