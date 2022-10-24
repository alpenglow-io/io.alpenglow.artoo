package io.artoo.anacleto;

import static java.lang.System.out;

@SuppressWarnings("RedundantStringFormatCall")
public interface Log {
  static <T, R> Return<T, R> info(String template, T any) {
    out.println(template.formatted(any));
    return Return.of(any);
  }
}
