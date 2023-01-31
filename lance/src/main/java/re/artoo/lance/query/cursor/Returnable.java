package re.artoo.lance.query.cursor;

import re.artoo.lance.func.TryIntFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Returnable<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default <RETURN> RETURN yield(TryIntFunction1<? super ELEMENT, ? extends RETURN> returns) {
    try {
      return tick(returns);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  default ELEMENT yield() {
    return yield((index, it) -> it);
  }
}
