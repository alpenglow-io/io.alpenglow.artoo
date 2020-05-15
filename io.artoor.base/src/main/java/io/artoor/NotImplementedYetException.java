package io.artoor;

import static io.artoor.type.Str.$;

public final class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException(final String method) {
    super($("Method %s is not implemented yet.").format(method) + "");
  }
}
