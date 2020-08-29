package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

import java.util.ArrayList;
import java.util.Collection;

public interface Quantifiable<T> extends Projectable<T> {
  default <R> Cursor<Boolean> all(final Class<R> type) {
    return all(type::isInstance);
  }

  default Cursor<Boolean> all(final Pred.Uni<? super T> where) {
    return select(new All<>(where));
  }

  default Cursor<Boolean> any() { return this.any(t -> true); }

  default Cursor<Boolean> any(final Pred.Uni<? super T> where) {
    return select(new Any<>(where)).or(false);
  }

  default Cursor<Boolean> contains(final T element) {
    return select(new Contains<>(element));
  }

  default Cursor<Boolean> notContains(final T element) {
    return select(new Contains<>(element)).or(true);
  }
}

final class Contains<T> implements Func.Uni<T, Boolean> {
  private final T what;

  Contains(final T what) {
    assert what != null;
    this.what = what;
  }

  @Override
  public Boolean tryApply(final T item) {
    return what.equals(item) ? true : null;
  }
}

final class Any<T> implements Func.Uni<T, Boolean> {
  private final Collection<T> collected;
  private final Pred.Uni<? super T> where;

  Any(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
    this.collected = new ArrayList<>();
  }

  @Override
  public final Boolean tryApply(final T item) throws Throwable {
    if (where.tryTest(item)) {
      collected.add(item);
      return true;
    } else {
      return false;
    }
  }
}

final class All<T> implements Func.Uni<T, Boolean> {
  private final AllOfThem allOfThem = new AllOfThem();
  private final Pred.Uni<? super T> where;

  All(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public Boolean tryApply(final T element) throws Throwable {
    return (allOfThem.value &= where.tryApply(element));
  }

  private static final class AllOfThem {
    private boolean value = true;
  }
}
