package re.artoo.fxcalibur.ui;

import re.artoo.fxcalibur.Asset;

public enum Theme implements atlantafx.base.theme.Theme {
  FxcaliburLight(Constants.FXCALIBUR_LIGHT, Constants.FXCALIBUR_LIGHT_CSS, "/asset/css/fxcalibur-light.bss");

  private final String name;
  private final Asset css;
  private final String bss;
  private final boolean isDark;

  Theme(String name, String css, String bss) {
    this(name, css, bss, false);
  }

  Theme(String name, String css, String bss, boolean isDark) {
    this.name = name;
    this.css = Asset.css(css);
    this.bss = bss;
    this.isDark = isDark;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getUserAgentStylesheet() {
    return css.location().toExternalForm();
  }

  @Override
  public String getUserAgentStylesheetBSS() {
    return bss;
  }

  @Override
  public boolean isDarkMode() {
    return isDark;
  }

  private static class Constants {
    public static final String FXCALIBUR_LIGHT_CSS = "fxcalibur-light";
    public static final String FXCALIBUR_LIGHT = "Fxcalibur Light";
  }
}
