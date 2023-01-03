package re.artoo.lance.query.cursor.routine.sort;

import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.routine.Routine;
import re.artoo.lance.query.many.Ordering;

import static re.artoo.lance.query.many.Ordering.Arrange.asc;

public sealed interface Sort<T> extends Routine<T, Cursor<T>> permits Default, Arranged {
  record By<T, R>(TryFunction1<? super T, ? extends R> field, Ordering.Arrange arrange) {
    public By(TryFunction1<? super T, ? extends R> field) {
      this(field, asc);
    }

    public static <T, R> By<T, R> with(TryFunction1<? super T, ? extends R> field, Ordering.Arrange arrange) {
      return new By<>(field, arrange);
    }

    public int arranged(int compared) { return arrange.equals(asc) ? compared : ~compared; }
    public Object fieldOf(T entry) { return field.apply(entry); }
  }

  @SafeVarargs
  static <T> Sort<T> arranged(By<T, Object>... bys) {
    return new Arranged<>(bys);
  }

  static <T> Sort<T> byDefault() {
    return new Default<>();
  }
}
