package net.pt.dsa.util;

import java.awt.*;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public class DsaUtil {

  /**
   * Source: https://www.compuphase.com/cmetric.htm
   * @param c1
   * @param c2
   * @return
   */
  public static double colourDistance(Color c1, Color c2)
  {
    int red1 = c1.getRed();
    int red2 = c2.getRed();
    int rmean = (red1 + red2) >> 1;
    int r = red1 - red2;
    int g = c1.getGreen() - c2.getGreen();
    int b = c1.getBlue() - c2.getBlue();
    return Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
  }
}
