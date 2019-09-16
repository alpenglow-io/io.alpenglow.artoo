package oak.quill.single;

import oak.func.pre.Predicate1;
import org.jetbrains.annotations.Contract;

import static oak.type.Nullability.nonNullable;

public interface FilterableSingle<T> extends StructableSingle<T> {
  default Nullable<T> where(Predicate1<? super T> filter) { return new Where<>(this, nonNullable(filter, "filter")); }

  default <R> Nullable<R> ofType(Class<? extends R> type) { return new OfType<>(this, nonNullable(type, "type")); }
}

final class Where<T> implements Nullable<T> {
  private final StructableSingle<T> structable;
  private final Predicate1<? super T> filter;

  @Contract(pure = true)
  Where(final StructableSingle<T> structable, final Predicate1<? super T> filter) {
    this.structable = structable;
    this.filter = filter;
  }

  @Override
  @org.jetbrains.annotations.Nullable
  public final T get() {
    final var value = structable.get();
    return value != null && filter.test(value) ? value : null;
  }
}

final class OfType<T, R> implements Nullable<R> {
  private final StructableSingle<T> structable;
  private final Class<? extends R> type;

  @Contract(pure = true)
  OfType(final StructableSingle<T> structable, final Class<? extends R> type) {
    this.structable = structable;
    this.type = type;
  }

  @Override
  @Contract(pure = true)
  @org.jetbrains.annotations.Nullable
  public final R get() {
    final var value = structable.get();
    return value != null && value.getClass().equals(type) ? type.cast(value) : null;
  }
}
