package net.pt.dsa.util;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by ptaucher on 10.01.2019<br/>
 */
public enum Dsa32BitColour {
  COLOR_000000(0x20, 0x00, 0x00, 0x00),
  COLOR_0000FC(0x21, 0x00, 0x00, 0xFC),
  COLOR_000090(0x22, 0x00, 0x00, 0x90),
  COLOR_F0C0A0(0x23, 0xF0, 0xC0, 0xA0),
  COLOR_F0B090(0x24, 0xF0, 0xB0, 0x90),
  COLOR_F0A070(0x25, 0xF0, 0xA0, 0x70),
  COLOR_E09060(0x26, 0xE0, 0x90, 0x60),
  COLOR_D08050(0x27, 0xD0, 0x80, 0x50),
  COLOR_C07050(0x28, 0xC0, 0x70, 0x50),
  COLOR_A06040(0x29, 0xA0, 0x60, 0x40),
  COLOR_905030(0x2A, 0x90, 0x50, 0x30),
  COLOR_704030(0x2B, 0x70, 0x40, 0x30),
  COLOR_603020(0x2C, 0x60, 0x30, 0x20),
  COLOR_503020(0x2D, 0x50, 0x30, 0x20),
  COLOR_E0E0E0(0x2E, 0xE0, 0xE0, 0xE0),
  COLOR_C0C0C0(0x2F, 0xC0, 0xC0, 0xC0),
  COLOR_B0B0B0(0x30, 0xB0, 0xB0, 0xB0),
  COLOR_A0A0A0(0x31, 0xA0, 0xA0, 0xA0),
  COLOR_808080(0x32, 0x80, 0x80, 0x80),
  COLOR_707070(0x33, 0x70, 0x70, 0x70),
  COLOR_505050(0x34, 0x50, 0x50, 0x50),
  COLOR_404040(0x35, 0x40, 0x40, 0x40),
  COLOR_00FC00(0x36, 0x00, 0xFC, 0x00),
  COLOR_009000(0x37, 0x00, 0x90, 0x00),
  COLOR_F0E000(0x38, 0xF0, 0xE0, 0x00),
  COLOR_D0B000(0x39, 0xD0, 0xB0, 0x00),
  COLOR_A08000(0x3A, 0xA0, 0x80, 0x00),
  COLOR_F06040(0x3B, 0xF0, 0x60, 0x40),
  COLOR_C04020(0x3C, 0xC0, 0x40, 0x20),
  COLOR_903010(0x3D, 0x90, 0x30, 0x10),
  COLOR_402010(0x3E, 0x40, 0x20, 0x10),
  COLOR_F0F0F0(0x3F, 0xF0, 0xF0, 0xF0);

  final int byteValue;
  final Color color;

  Dsa32BitColour(int byteValue, int r, int g, int b) {
    this.byteValue = byteValue;
    this.color = new Color(r, g, b);
  }

  public Color getColor() {
    return color;
  }

  public byte getByteValue() {
    return (byte) byteValue;
  }

  public int getRGB() {
    return getColor().getRGB();
  }

  public static Dsa32BitColour fromByteValue(byte b) {
    Optional<Dsa32BitColour> colour = Arrays.stream(values())
        .filter(c -> c.byteValue == (int) b)
        .findFirst();
    if (colour.isPresent()) {
      return colour.get();
    } else {
      return COLOR_000000;
    }
  }

  public static Dsa32BitColour fromRGBValue(int rgb) {
    Optional<Dsa32BitColour> colour = Arrays.stream(values())
        .filter(c -> c.getRGB() == rgb)
        .findFirst();
    if (colour.isPresent()) {
      return colour.get();
    } else {
      return COLOR_000000;
    }
  }

  public static Dsa32BitColour nearestRGBValue(int rgb) {
    Dsa32BitColour colour = null;
    Color original = new Color(rgb);
    double minDistance = Double.MAX_VALUE;
    for (Dsa32BitColour itm : values()) {
      double distance = DsaUtil.colourDistance(original, itm.getColor());
      if (distance < minDistance) {
        minDistance = distance;
        colour = itm;
      }
    }
    return colour;
  }

  @Override
  public String toString() {
    return name() + "{" +
        String.format("0x%02X", byteValue) +
        ", #" + String.format("0x%02X", color.getRed()) + String.format("0x%02X", color.getGreen()) + String.format("0x%02X", color.getBlue()) +
        '}';
  }
}
