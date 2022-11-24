package lance.literator.cursor.routine.sort;

import lance.func.Func;
import lance.literator.Cursor;
import lance.literator.cursor.routine.Routine;
import lance.query.many.Ordering;

import static lance.query.many.Ordering.Arrange.asc;

public sealed interface Sort<T> extends Routine<T, Cursor<T>> permits Default, Arranged {
  record By<T, R>(Func.MaybeFunction<? super T, ? extends R> field, Ordering.Arrange arrange) {
    public By(Func.MaybeFunction<? super T, ? extends R> field) {
      this(field, asc);
    }

    public static <T, R> By<T, R> with(Func.MaybeFunction<? super T, ? extends R> field, Ordering.Arrange arrange) {
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
