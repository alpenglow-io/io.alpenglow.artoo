package lance.literator.cursor.routine.concat;

import lance.literator.Cursor;
import lance.literator.Literator;
import lance.literator.cursor.routine.Routine;

public sealed interface Concat<T> extends Routine<T, Cursor<T>> permits Array, Liter {
  @SafeVarargs
  static <T> Concat<T> array(T... elements) {
    return new Array<>(elements);
  }

  static <T> Concat<T> liter(Literator<T> literator) {
    return new Liter<>(literator);
  }

}
