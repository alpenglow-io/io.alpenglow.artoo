package re.artoo.fxcalibur.element;

import re.artoo.fxcalibur.Asset;



public interface Theme {
  enum Dragon implements atlantafx.base.theme.Theme {
    Blue("Blue Dragon", "dragon", "/asset/css/dragon.bss");

    private final String name;
    private final Asset css;
    private final String bss;
    private final boolean isDark;

    Dragon(String name, String css, String bss) {
      this(name, css, bss, false);
    }

    Dragon(String name, String css, String bss, boolean isDark) {
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
  }
}
