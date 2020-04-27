package artoo;

import static artoo.type.Str.$;

public final class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException(final String method) {
    super($("Method %s is not implemented yet.").format(method) + "");
  }
}
