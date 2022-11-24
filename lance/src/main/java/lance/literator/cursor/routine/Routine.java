package lance.literator.cursor.routine;

import lance.func.Func;
import lance.literator.Literator;
import lance.literator.cursor.routine.concat.Concat;
import lance.literator.cursor.routine.convert.Convert;
import lance.literator.cursor.routine.join.Join;
import lance.literator.cursor.routine.sort.Sort;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Join, Concat, Convert, Sort {

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  Func.MaybeFunction<T[], R> onArray();
  Func.MaybeFunction<Literator<T>, R> onLiterator();
  Func.MaybeFunction<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default Func.MaybeFunction<T, R> onSelf() {
    return it -> (R) it;
  }
}

