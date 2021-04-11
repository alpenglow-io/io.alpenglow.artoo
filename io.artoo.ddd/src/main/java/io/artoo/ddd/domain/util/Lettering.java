package io.artoo.ddd.domain.util;

public interface Lettering {
  String REGEX = "([a-z0-9])([A-Z0-9]+)";
  String KEBAB = "$1-$2";

  default <T> String asKebabCase(Class<T> type) {
    return type.getSimpleName().replaceAll(REGEX, KEBAB).toLowerCase();
  }
}
