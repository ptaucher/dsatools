package net.pt.dsa.action;

import net.pt.dsa.DsaTools;
import net.pt.dsa.util.Dsa32BitColour;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

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

      // Read existing CHR file
      byte[] fileContent = FileUtils.readFileToByteArray(portraitFile);

      // Create byte content (32 x 32 = 1024 bytes) from 'new' image content (assuming it is already in the correct palette and in 32 x 32 pixels)
      BufferedImage image = (BufferedImage) newImageIcon.getImage();
      for (int i = 0x02DA, x = 0, y = 0; y < 32; i++) {
        Dsa32BitColour colour = Dsa32BitColour.fromRGBValue(image.getRGB(x, y));
        fileContent[i] = colour.getByteValue();

        if (x == 31) {
          // Line complete
          x = 0;
          y++;
        } else {
          x++;
        }
      }

      // Make backup copy before overwriting file
      FileUtils.copyFile(portraitFile, new File(portraitFile.getParentFile(), portraitFile.getName() + "." + System.currentTimeMillis()));
      // Write new content to file
      FileUtils.writeByteArrayToFile(portraitFile, fileContent);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
