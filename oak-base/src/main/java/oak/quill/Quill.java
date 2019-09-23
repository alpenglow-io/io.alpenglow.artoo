package oak.quill;

import oak.quill.query.Queryable;
import org.jetbrains.annotations.NotNull;

public enum Quill {
  ;

  @NotNull
  @SafeVarargs
  public static <T> Queryable<T> from(final T... elements) {
    return Queryable.from(elements);
  }
}
