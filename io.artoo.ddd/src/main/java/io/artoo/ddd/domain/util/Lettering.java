package io.artoo.ddd.domain.util;

import io.artoo.lance.func.Func;

public interface Lettering {
  enum Default { $string }

  String REGEX = "([a-z0-9])([A-Z0-9]+)";
  String KEBAB = "$1-$2";

  default <T> String asKebabCase(Class<T> type) {
    return type.getSimpleName().replaceAll(REGEX, KEBAB).toLowerCase();
  }
}
