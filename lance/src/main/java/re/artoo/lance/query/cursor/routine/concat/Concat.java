package re.artoo.lance.query.cursor.routine.concat;

import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Inquiry;
import re.artoo.lance.query.cursor.routine.Routine;

public sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Array, Liter {
  @SafeVarargs
  static <T> Concat<T> array(T... elements) {
    return new Array<>(elements);
  }

  static <T> Concat<T> liter(Inquiry<T> inquiry) {
    return new Liter<>(inquiry);
  }

}
