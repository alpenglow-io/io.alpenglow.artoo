package io.artoo.lance;

import static io.artoo.lance.type.Str.$;

public final class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException(final String method) {
    super($("Method %s is not implemented yet.").format(method) + "");
  }
}
