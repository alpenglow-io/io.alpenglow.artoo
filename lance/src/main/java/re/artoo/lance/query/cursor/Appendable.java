package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Append;

public sealed interface Appendable<ELEMENT> extends Probe<ELEMENT> permits Cursor {
  default Cursor<ELEMENT> append(Cursor<ELEMENT> cursor) {
    return new Append<>(this, cursor).coalesce();
  }
}

