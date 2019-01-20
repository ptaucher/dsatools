package net.pt.dsa.util;

import com.mortennobel.imagescaling.ResampleFilter;
import com.mortennobel.imagescaling.ResampleFilters;
import com.mortennobel.imagescaling.ResampleOp;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class DsaUtil {

  /**
   * Source: https://www.compuphase.com/cmetric.htm
   *
   * @param c1
   * @param c2
   * @return
   */
  public static double colourDistance(Color c1, Color c2) {
    int red1 = c1.getRed();
    int red2 = c2.getRed();
    int rmean = (red1 + red2) >> 1;
    int r = red1 - red2;
    int g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
  }

  /**
   * Convert source image to 32 pixel DSA palette image
   *
   * @param sourceImage
   * @return converted image
   */
  public static BufferedImage convertImageTo32BitDsaPalette(File sourceImage) {
    BufferedImage result = null;
    try {
      // Read image file
      BufferedImage image = ImageIO.read(sourceImage);

      if (image != null) {
        transformToDsaPalette(image);

        // Write image as PNG (original size but new palette) for debug purposes
        writeImage(sourceImage, image, "large_palette");

        // Scale image to 32x32 .. testing different algorithms

        /*
        BufferedImage scaledImageBaseBilinearHigh = getScaledInstance(image, 32, 32, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
        // Write image as PNG (original size but new palette) for debug purposes
        writeImage(sourceImage, scaledImageBaseBilinearHigh, "scaled_base_bilinear_high");
        */

        BufferedImage scaledImageLanczos3 = getScaledInstance(image, 32, 32, ResampleFilters.getLanczos3Filter());
        // Write image as PNG (original size but new palette) for debug purposes
        writeImage(sourceImage, scaledImageLanczos3, "scaled_lanczos3");

        // For now use lanczos3 from mortennobel library
        BufferedImage scaledImage = scaledImageLanczos3;

        // Transform to DSA Palette (again, just to make sure scaling didn't lead to invalid colours)
        transformToDsaPalette(scaledImage);

        // Write image as PNG (original size but new palette) for debug purposes
        writeImage(sourceImage, scaledImage, "scaled_palette");

        result = scaledImage;
      }

    } catch (Exception ex) {
      try {
        Thread.sleep(200L);
      } catch (InterruptedException iex) { /* ignore */ }
      ex.printStackTrace();
    }
    return result;
  }

  /**
   * Transform image colour palette to DSA palette using distance algorithm in 'colourDistance'
   *
   * @param image
   */
  public static void transformToDsaPalette(BufferedImage image) {
    // Transform to DSA Palette
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        // Determine nearest RGB value from colour enum
        Dsa32BitColour colour = Dsa32BitColour.nearestRGBValue(image.getRGB(x, y));
        // Replace current pixel value with 'neares' palette value
        image.setRGB(x, y, colour.getRGB());
      }
    }
  }

  /**
   * Patches given image (needs to be 32x32 DSA Palette already!) into designated CHR file byte content
   *
   * @param portraitFile
   * @param image
   * @return CHR file with patched portrait bytes
   */
  public static byte[] patchPortraitIntoChrFileByteContent(File portraitFile, BufferedImage image) {
    byte[] fileContent = null;

    try {
      // Read existing CHR file
      fileContent = FileUtils.readFileToByteArray(portraitFile);

      // Create byte content (32 x 32 = 1024 bytes) from 'new' image content (assuming it is already in the correct palette and in 32 x 32 pixels)
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
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return fileContent;
  }

  /**
   * @param sourceImage
   * @param image
   * @param dirName
   * @throws IOException
   */
  protected static void writeImage(File sourceImage, BufferedImage image, String dirName) throws IOException {
    File targetDir = new File(sourceImage.getParentFile().getAbsolutePath() + File.separator + dirName);
    targetDir.mkdirs();

    File targetFile = new File(targetDir, sourceImage.getName());
    ImageIO.write(image, "png", targetFile);
  }

  /**
   * Convenience method that returns a scaled instance of the * provided {@code BufferedImage}.
   * Source: https://community.oracle.com/docs/DOC-983611
   * @param img           the original image to be scaled
   * @param targetWidth   the desired width of the scaled instance, in pixels
   * @param targetHeight  the desired height of the scaled instance, in pixels
   * @param hint          one of the rendering hints that corresponds to {@code RenderingHints.KEY_INTERPOLATION} (e.g.
   *                      {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
   *                      {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
   *                      {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
   * @param higherQuality if true, this method will use a multi-step scaling technique that provides higher quality than the usual one-step technique (only useful in downscaling cases, where {@code targetWidth} or {@code targetHeight} is smaller than the original dimensions, and generally only when the {@code BILINEAR} hint is specified)
   * @return a scaled version of the original {@code BufferedImage}
   */
  public static BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
    int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
    BufferedImage ret = (BufferedImage) img;
    int w, h;
    if (higherQuality) { // Use multi-step technique: start with original size, then scale down in multiple passes with drawImage() until the target size is reached
      w = img.getWidth();
      h = img.getHeight();
    } else { // Use one-step technique: scale directly from original size to target size with a single drawImage() call
      w = targetWidth;
      h = targetHeight;
    }
    do {
      if (higherQuality && w > targetWidth) {
        w /= 2;
        if (w < targetWidth) {
          w = targetWidth;
        }
      }
      if (higherQuality && h > targetHeight) {
        h /= 2;
        if (h < targetHeight) {
          h = targetHeight;
        }
      }
      BufferedImage tmp = new BufferedImage(w, h, type);
      Graphics2D g2 = tmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
      g2.drawImage(ret, 0, 0, w, h, null);
      g2.dispose();
      ret = tmp;
    } while (w != targetWidth || h != targetHeight);
    return ret;
  }

  /**
   * Scale image using filter of mortennobel image-scaling library
   * @param image
   * @param targetWidth
   * @param targetHeight
   * @param filter
   * @return
   */
  public static BufferedImage getScaledInstance(BufferedImage image, int targetWidth, int targetHeight, ResampleFilter filter) {
    ResampleOp resizeOp = new ResampleOp(targetWidth, targetHeight);
    resizeOp.setFilter(filter);
    return resizeOp.filter(image, null);
  }

  /**
   * Update character name in CHR file
   * @param fileContent
   * @param charName
   * @return
   */
  public static byte[] patchCharacterNameInChrFileContent(byte[] fileContent, String charName) {
    // First name starts at byte 0x00
    // Second name starts at byte 0x10
    byte[] nameByteArray = charName.getBytes();
    if (nameByteArray.length < 16) {
      for (int i = 0; i < nameByteArray.length; i++) {
        fileContent[i] = nameByteArray[i];
        fileContent[16+i] = nameByteArray[i];
      }
    }
    return fileContent;
  }

  public static void convertPortraits(File[] pngFiles, File portraitFile) {
    try {
      if (pngFiles != null) {
        int i = 0;
        int size = pngFiles.length;
        for (File pngFile : pngFiles) {
          System.out.println("Processing " + i + "/" + size + ": " + pngFile.getName());

          // Convert image to 32 bit DSA portrait
          BufferedImage converted32BitDsaPortrait = DsaUtil.convertImageTo32BitDsaPalette(pngFile);

          // Patch TEST.CHR file with new portrait and save as new file
          byte[] fileContent = DsaUtil.patchPortraitIntoChrFileByteContent(portraitFile, converted32BitDsaPortrait);
          String charName = pngFile.getName().substring(0, pngFile.getName().toLowerCase().indexOf(".png")).toUpperCase();
          DsaUtil.patchCharacterNameInChrFileContent(fileContent, charName);

          // Write new content with patched portrait to file
          File targetDir = new File(pngFile.getParentFile().getAbsolutePath() + File.separator + "chr");
          targetDir.mkdirs();

          File targetFile = new File(targetDir, charName + ".CHR");
          FileUtils.writeByteArrayToFile(targetFile, fileContent);

          i++;
        }
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static BufferedImage readDsaImageFromChrFile(File chrFile) throws IOException {
    BufferedImage image = null;

    // Read CHR file
    byte[] fileContent = FileUtils.readFileToByteArray(chrFile);
    if (fileContent != null) {
      // Start reading portrait data from offset 02DA until 06D9 (1024 [0x0400] bytes)
      // Create image according to palette conversion
      image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

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
    }

    return image;
  }

  public static BufferedImage readDsaImageFromHeads(File headsFile) throws IOException {
    BufferedImage image = null;

    // Read CHR file
    byte[] fileContent = FileUtils.readFileToByteArray(headsFile);
    if (fileContent != null) {
      // Try reading last 1024 [0x0400] bytes
      // TODO ???
      image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

      //for (int i = fileContent.length - 1024 - 1, x = 0, y = 0; y < 32; i++) {
      for (int i = 0, x = 0, y = 0; y < 32; i++) {
        Dsa32BitColour colour = Dsa32BitColour.fromByteValue(fileContent[i]);
          /* */
          System.out.println("i=" + i + " " +
              String.format("0x%04X", i) + ", x=" +
              String.format("%02d", x) + ", y=" +
              String.format("%02d", y) + ", " + colour);
          /* */
        image.setRGB(x, y, colour.getRGB());

        if (x == 31) {
          // Line complete
          x = 0;
          y++;
        } else {
          x++;
        }
      }
    }

    return image;
  }
}
