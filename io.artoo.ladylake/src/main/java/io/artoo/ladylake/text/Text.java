package io.artoo.ladylake.text;

public interface Text {
  enum Case { Kebab("$1-$2"), Camel("$1$2"), Pascal("$1$2"), Snake("$1_$2");
    private final String pattern;

    Case(final String pattern) {
      this.pattern = pattern;
    }
  }

  static Text $(String text) {
    return new Plain(text);
  }
  String REGEX = "([a-z0-9])([A-Z0-9]+)";

  default String to(Case case$) {
    return toString().replaceAll(REGEX, case$.pattern).toLowerCase();
  }
}

record Plain(String value) implements Text {
  @Override
  public String toString() {
    return value;
  }
}
