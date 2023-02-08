package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public sealed interface Returnable<ELEMENT> extends Head<ELEMENT>, Tail<ELEMENT> permits Cursor {
  default <RETURN> RETURN submit(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return scroll(returns);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default ELEMENT submit() {
    return submit((index, it) -> it);
  }
}
