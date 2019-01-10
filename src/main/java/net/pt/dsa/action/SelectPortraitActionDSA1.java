package net.pt.dsa.action;

import net.pt.dsa.DsaTools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class SelectPortraitActionDSA1 extends AbstractDsaToolsAction {
  private static final String DSA1_CHR_FILE = "dsa1.file.chr";

  public SelectPortraitActionDSA1(DsaTools tools) {
    super(tools);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    // First select CHR file
    File chrFileDefault = new File(tools.getPreference(DSA1_CHR_FILE, "."));
    JFileChooser chooser = new JFileChooser(chrFileDefault);

    int returnValue = chooser.showOpenDialog(tools.getFrame());
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      File selectedFile = chooser.getSelectedFile();
      tools.setPreference(DSA1_CHR_FILE, selectedFile.getAbsolutePath());

      // Now display current portrait from selected CHR file
      System.out.println(selectedFile.getAbsolutePath());

      tools.setDsa1SwitchPortraitChrFile(selectedFile);
    }
  }
}
