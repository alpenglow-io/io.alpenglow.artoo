package re.artoo.lance.query.cursor;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Collectable<FROM> extends Probe<FROM> permits Cursor, As {
  default <TO, CURSOR extends Cursor<TO>> CURSOR to(Routine<FROM, CURSOR> routine) {
    return this.as(routine);
  }
  <TO> TO as(Routine<FROM, TO> routine);
}

