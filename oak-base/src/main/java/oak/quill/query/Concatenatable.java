package oak.quill.query;

import oak.quill.Structable;

public interface Concatenatable<T> extends Structable<T> {
  @SuppressWarnings("unchecked")
  Queryable<T> concat(final T... values);
  @SuppressWarnings("unchecked")
  Queryable<T> merge(final T... values);
}
