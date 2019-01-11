package net.pt.dsa.action;

import net.pt.dsa.DsaTools;
import net.pt.dsa.util.Dsa32BitColour;
import net.pt.dsa.util.DsaUtil;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class SwitchPortraitActionDSA1 extends AbstractDsaToolsAction {
  private static final String DSA1_IMG_FILE = "dsa1.file.img";

  public SwitchPortraitActionDSA1(DsaTools tools) {
    super(tools);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    // Switch new image with previous image
    try {
      File portraitFile = tools.getDsa1SwitchPortraitChrFile();
      ImageIcon newImageIcon = (ImageIcon) tools.getDsa1SwitchPortraitImage2().getIcon();
      BufferedImage image = (BufferedImage) newImageIcon.getImage();

      // Make backup copy before overwriting file
      FileUtils.copyFile(portraitFile, new File(portraitFile.getParentFile(), portraitFile.getName() + "." + System.currentTimeMillis()));

      // Switch portrait via util method
      byte[] fileContent = DsaUtil.patchPortraitIntoChrFileByteContent(portraitFile, image);

      if (fileContent != null) {
        // Write new content to file
        FileUtils.writeByteArrayToFile(portraitFile, fileContent);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
