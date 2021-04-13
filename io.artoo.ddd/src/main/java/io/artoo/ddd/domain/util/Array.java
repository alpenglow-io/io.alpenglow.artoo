package io.artoo.ddd.domain.util;

import java.util.Arrays;

import static java.lang.System.arraycopy;

@SuppressWarnings("unchecked")
public interface Array {
  default <T> T[] push(T[] source, T... elements) {
    final var copied = Arrays.copyOf(source, source.length + elements.length);
    if (copied.length - source.length >= 0) arraycopy(elements, 0, copied, source.length, copied.length - source.length);
    return copied;
  }

  default <T> T[] copy(T[] source) {
    return Arrays.copyOf(source, source.length);
  }
}
