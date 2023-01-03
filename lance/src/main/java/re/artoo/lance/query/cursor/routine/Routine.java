package re.artoo.lance.query.cursor.routine;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.cursor.Fetcher;
import re.artoo.lance.query.cursor.routine.concat.Concat;
import re.artoo.lance.query.cursor.routine.convert.Convert;
import re.artoo.lance.query.cursor.routine.join.Join;
import re.artoo.lance.query.cursor.routine.sort.Sort;

import java.util.Iterator;
import java.util.List;

public sealed interface Routine<T, R> permits Join, Concat, Convert, Sort {

  static <T> Convert<T, List<T>> list() { return new Convert.Listable<>(); }

  static <T> Convert<T, T[]> array(final Class<T> type) {
    return new Convert.Arrayable<>(type);
  }

  TryFunction1<T[], R> onArray();
  TryFunction1<Fetcher<T>, R> onSource();
  TryFunction1<Iterator<T>, R> onIterator();

  @SuppressWarnings("unchecked")
  default TryFunction1<T, R> onSelf() {
    return it -> (R) it;
  }
}

