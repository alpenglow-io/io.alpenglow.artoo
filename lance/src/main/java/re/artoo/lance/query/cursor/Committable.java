package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;

public sealed interface Committable<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <RETURN> RETURN commit(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return returns.invoke(0, fetch());
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default ELEMENT commit() {
    return commit((index, it) -> it);
  }
}
