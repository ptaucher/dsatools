package net.pt.dsa;

import net.pt.dsa.action.*;
import net.pt.dsa.util.Dsa32BitColour;
import net.pt.dsa.util.DsaUtil;
import net.pt.dsa.util.PortraitConverter;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by ptaucher on 10.01.2019<br/>
 * Main class for DSA Tools (will display a simple starting window)
 */
public class DsaTools {
  final ResourceBundle texts;
  final Properties settings;
  final JFrame frame;
  final Preferences preferences;

  JLabel dsa1SwitchPortraitLabel;
  JButton dsa1SwitchPortraitSelectCharFile;
  JLabel dsa1SwitchPortraitImage;
  JButton dsa1SwitchPortraitSelectImageFile;
  JLabel dsa1SwitchPortraitImage2;
  JButton dsa1SwitchPortraitApplyImage;
  File dsa1SwitchPortraitChrFile;
  File dsa1SwitchPortraitImgFile;

  JLabel dsa1ConvertPortraitsLabel;
  JButton dsa1ConvertPortraitsSelectDir;
  File dsa1ConvertPortraitsDir;

  JLabel dsa2SwitchPortraitLabel;
  JButton dsa2SwitchPortraitSelectCharFile;

  /**
   * Main method will initialize resources and display GUI
   *
   * @param args
   */
  public static void main(String... args) {
    DsaTools tools = new DsaTools();

    // Create and show application's GUI via EDT
    SwingUtilities.invokeLater(() -> {
      try {
        tools.createAndShowGUI();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
  }

  /**
   * Private constructor (should only be called from main method!)
   */
  private DsaTools() {
    // Initialize resource bundle for label/button texts
    texts = ResourceBundle.getBundle("texts");

    // Create main window instance
    frame = new JFrame(texts.getString("main.title"));

    // Initialize user preferences
    preferences = Preferences.userNodeForPackage(DsaTools.class);

    // Initialize settings
    settings = new Properties();
    try {
      settings.load(DsaTools.class.getResourceAsStream("/dsatools.properties"));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Create the GUI and show it.<br/>
   * For thread safety, this method should be invoked from the event-dispatching-thread (EDT).
   */
  private void createAndShowGUI() throws MalformedURLException {
    // Create and set up the window
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setMinimumSize(new Dimension(500, 300));

    // Create grid of labels and buttons (using GridBagLayout)
    GridBagLayout layout = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    JPanel pane = new JPanel();
    frame.getContentPane().add(pane, BorderLayout.NORTH);
    pane.setLayout(layout);
    constraints.ipadx = 10;
    constraints.ipady = 5;

    addLabel(pane, constraints, 5, 5, 1, 1, GridBagConstraints.WEST, "main.blank");

    // 1. row -> DSA 1 character portraits switcher
    dsa1SwitchPortraitLabel = addLabel(pane, constraints, 10, 10, 1, 1, GridBagConstraints.WEST,
        "main.dsa1.portraits.label");
    dsa1SwitchPortraitSelectCharFile = addButton(pane, constraints, 12, 10, 1, 1, GridBagConstraints.CENTER,
        "main.dsa1.portraits.chr.button", getClass().getClassLoader().getResource("open1.png"),
        new SelectPortraitActionDSA1(this));
    dsa1SwitchPortraitImage = addLabel(pane, constraints, 10, 12, 1, 1, GridBagConstraints.WEST,
        "main.dsa1.portraits.image", getClass().getClassLoader().getResource("empty.png"));
    dsa1SwitchPortraitSelectImageFile = addButton(pane, constraints, 12, 12, 1, 1, GridBagConstraints.CENTER,
        "main.dsa1.portraits.img.button", getClass().getClassLoader().getResource("graphix.png"),
        new SelectNewPortraitActionDSA1(this));
    dsa1SwitchPortraitSelectImageFile.setEnabled(false);
    dsa1SwitchPortraitImage2 = addLabel(pane, constraints, 10, 14, 1, 1, GridBagConstraints.WEST,
        "main.dsa1.portraits.image", getClass().getClassLoader().getResource("empty.png"));
    dsa1SwitchPortraitApplyImage = addButton(pane, constraints, 12, 14, 1, 1, GridBagConstraints.CENTER,
        "main.dsa1.portraits.apply.button", getClass().getClassLoader().getResource("check.png"),
        new SwitchPortraitActionDSA1(this));
    dsa1SwitchPortraitApplyImage.setEnabled(false);

    addLabel(pane, constraints, 15, 15, 1, 1, GridBagConstraints.WEST, "main.blank");

    // 2. row -> DSA 1 character portraits converter
    dsa1ConvertPortraitsLabel = addLabel(pane, constraints, 10, 20, 1, 1, GridBagConstraints.WEST,
        "main.dsa1.convertportrait.label");
    dsa1ConvertPortraitsSelectDir = addButton(pane, constraints, 12, 20, 1, 1, GridBagConstraints.CENTER,
        "main.dsa2.convertportrait.button", getClass().getClassLoader().getResource("open1.png"),
        new ConvertPortraitActionDSA1(this));

    addLabel(pane, constraints, 25, 25, 1, 1, GridBagConstraints.WEST, "main.blank");

    // 3. row -> DSA 2 character portraits switcher
    dsa2SwitchPortraitLabel = addLabel(pane, constraints, 10, 30, 1, 1, GridBagConstraints.WEST,
        "main.dsa2.portraits.label");
    dsa2SwitchPortraitSelectCharFile = addButton(pane, constraints, 12, 30, 1, 1, GridBagConstraints.CENTER,
        "main.dsa2.portraits.button", getClass().getClassLoader().getResource("open1.png"),
        new SelectPortraitActionDSA2(this));
    dsa2SwitchPortraitSelectCharFile.setEnabled(false);

    // Display the window
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Add label with layout constraints
   *
   * @param pane
   * @param constraints
   * @param gridx
   * @param gridy
   * @param spanx
   * @param spany
   * @param anchor
   * @param text
   * @return
   */
  private JLabel addLabel(Container pane,
                          GridBagConstraints constraints,
                          int gridx,
                          int gridy,
                          int spanx,
                          int spany,
                          int anchor,
                          String text) {
    return addLabel(pane, constraints, gridx, gridy, spanx, spany, anchor, text, null);
  }

  /**
   * Add label with layout constraints
   *
   * @param pane
   * @param constraints
   * @param gridx
   * @param gridy
   * @param spanx
   * @param spany
   * @param anchor
   * @param text
   * @param icon
   * @return
   */
  private JLabel addLabel(Container pane,
                          GridBagConstraints constraints,
                          int gridx,
                          int gridy,
                          int spanx,
                          int spany,
                          int anchor,
                          String text,
                          URL icon) {
    JLabel label;
    if (icon != null) {
      label = new JLabel(texts.getString(text), new ImageIcon(icon), JLabel.CENTER);
    } else {
      label = new JLabel(texts.getString(text));
    }
    addComponent(pane, constraints, gridx, gridy, spanx, spany, anchor, label);
    return label;
  }

  /**
   * Add button with layout constraints
   *
   * @param pane
   * @param constraints
   * @param gridx
   * @param gridy
   * @param spanx
   * @param spany
   * @param anchor
   * @param text
   * @param icon
   * @param action
   * @return
   */
  private JButton addButton(Container pane,
                            GridBagConstraints constraints,
                            int gridx,
                            int gridy,
                            int spanx,
                            int spany,
                            int anchor,
                            String text,
                            URL icon,
                            Action action) {
    JButton button;
    if (icon != null) {
      button = new JButton();
      JLabel label = new JLabel(texts.getString(text), new ImageIcon(icon), JLabel.CENTER);
      button.add(label);
      button.setPreferredSize(new Dimension(250, 32));
      button.revalidate();
    } else {
      button = new JButton(texts.getString(text));
    }
    addComponent(pane, constraints, gridx, gridy, spanx, spany, anchor, button);
    button.setAction(action);
    return button;
  }

  /**
   * Add component with layout constraints
   *
   * @param pane
   * @param constraints
   * @param gridx
   * @param gridy
   * @param spanx
   * @param spany
   * @param anchor
   * @param component
   */
  private void addComponent(Container pane,
                            GridBagConstraints constraints,
                            int gridx,
                            int gridy,
                            int spanx,
                            int spany,
                            int anchor,
                            JComponent component) {
    constraints.gridx = gridx;
    constraints.gridy = gridy;
    constraints.gridwidth = spanx;
    constraints.gridheight = spany;
    constraints.anchor = anchor;

    pane.add(component, constraints);
  }

  public ResourceBundle getTexts() {
    return texts;
  }

  public JFrame getFrame() {
    return frame;
  }

  public String getPreference(String name, String defaultValue) {
    return preferences.get(name, defaultValue);
  }

  public void setPreference(String name, String value) {
    preferences.put(name, value);
  }

  public JLabel getDsa1SwitchPortraitLabel() {
    return dsa1SwitchPortraitLabel;
  }

  public JButton getDsa1SwitchPortraitSelectCharFile() {
    return dsa1SwitchPortraitSelectCharFile;
  }

  public JLabel getDsa1SwitchPortraitImage() {
    return dsa1SwitchPortraitImage;
  }

  public JButton getDsa1SwitchPortraitSelectImageFile() {
    return dsa1SwitchPortraitSelectImageFile;
  }

  public File getDsa1SwitchPortraitChrFile() {
    return dsa1SwitchPortraitChrFile;
  }

  public void setDsa1SwitchPortraitChrFile(File dsa1SwitchPortraitChrFile) {
    this.dsa1SwitchPortraitChrFile = dsa1SwitchPortraitChrFile;

    dsa1SwitchPortraitImage.setText(dsa1SwitchPortraitChrFile.getName());
    dsa1SwitchPortraitImage.setIcon(getIconFromChr(dsa1SwitchPortraitChrFile));
    dsa1SwitchPortraitSelectImageFile.setEnabled(true);
  }

  public File getDsa1SwitchPortraitImgFile() {
    return dsa1SwitchPortraitImgFile;
  }

  public void setDsa1SwitchPortraitImgFile(File dsa1SwitchPortraitImgFile) {
    this.dsa1SwitchPortraitImgFile = dsa1SwitchPortraitImgFile;

    dsa1SwitchPortraitImage2.setText(dsa1SwitchPortraitImgFile.getName());
    dsa1SwitchPortraitImage2.setIcon(getIconFromNewImage(dsa1SwitchPortraitImgFile));
    dsa1SwitchPortraitApplyImage.setEnabled(true);
  }

  public JLabel getDsa1SwitchPortraitImage2() {
    return dsa1SwitchPortraitImage2;
  }

  public JButton getDsa1SwitchPortraitApplyImage() {
    return dsa1SwitchPortraitApplyImage;
  }

  public JLabel getDsa2SwitchPortraitLabel() {
    return dsa2SwitchPortraitLabel;
  }

  public JButton getDsa2SwitchPortraitSelectCharFile() {
    return dsa2SwitchPortraitSelectCharFile;
  }

  public JLabel getDsa1ConvertPortraitsLabel() {
    return dsa1ConvertPortraitsLabel;
  }

  public JButton getDsa1ConvertPortraitsSelectDir() {
    return dsa1ConvertPortraitsSelectDir;
  }

  public File getDsa1ConvertPortraitsDir() {
    return dsa1ConvertPortraitsDir;
  }

  public void setDsa1ConvertPortraitsDir(File dsa1ConvertPortraitsDir) {
    this.dsa1ConvertPortraitsDir = dsa1ConvertPortraitsDir;
  }

  private ImageIcon getIconFromChr(File chrFile) {
    ImageIcon icon = new ImageIcon();
    try {
      // Read CHR file
      byte[] fileContent = FileUtils.readFileToByteArray(chrFile);
      if (fileContent != null) {
        // Start reading portrait data from offset 02DA until 06D9 (1024 [0x0400] bytes)
        // Create image according to palette conversion
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        for (int i = 0x02DA, x = 0, y = 0; i <= 0x06D9; i++) {
          Dsa32BitColour colour = Dsa32BitColour.fromByteValue(fileContent[i]);
          /*
          System.out.println("i=" + i + " " +
              String.format("0x%04X", i) + ", x=" +
              String.format("%02d", x) + ", y=" +
              String.format("%02d", y) + ", " + colour);
          */
          image.setRGB(x, y, colour.getRGB());

          if (x == 31) {
            // Line complete
            x = 0;
            y++;
          } else {
            x++;
          }
        }

        icon = new ImageIcon(image);
        // ImageIO.write(image, "png", new File("char.png"));
      }
    } catch (Exception ex) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException iex) { /* ignore */ }
      ex.printStackTrace();
    }
    return icon;
  }

  private ImageIcon getIconFromNewImage(File imgFile) {
    ImageIcon icon = new ImageIcon();
    try {
      // Convert image via util method
      BufferedImage image = DsaUtil.convertImageTo32BitDsaPalette(imgFile);

      if (image != null) {
        icon = new ImageIcon(image);
      }
    } catch (Exception ex) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException iex) { /* ignore */ }
      ex.printStackTrace();
    }
    return icon;
  }

  public String getSetting(String key) {
    return settings.getProperty(key);
  }
}
