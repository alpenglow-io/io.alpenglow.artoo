package oak.quill;

import oak.quill.query.Queryable;
import oak.quill.query.array.QueryableArray;
import org.jetbrains.annotations.NotNull;

public enum Quill {
  ;

  @NotNull
  @SafeVarargs
  public static <T> Queryable<T> from(final T... elements) {
    return QueryableArray.from(elements);
  }
}
