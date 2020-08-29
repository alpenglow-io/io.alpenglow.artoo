package io.artoo.lance.next.cursor;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Pred;
import io.artoo.lance.next.Cursor;

public interface Uniquable<T> extends Projectable<T> {
  default Cursor<T> at(final int index) {
    return select(new At<>(index));
  }

  default Cursor<T> first() {
    return first(it -> true);
  }

  default Cursor<T> first(final Pred.Uni<? super T> where) {
    return select(new First<>(where));
  }

  default Cursor<T> last() {
    return last(it -> true);
  }

  default Cursor<T> last(final Pred.Uni<? super T> where) {
    return select(new Last<>(where));
  }

  default Cursor<T> single() {
    return single(it -> true);
  }

  default Cursor<T> single(final Pred.Uni<? super T> where) {
    return select(new Single<>(where))
      .select(new Last<>(it -> true))
      .selectNext(it -> select(new At<>(it)));
  }
}

final class Single<T> implements Func.Uni<T, Integer> {
  private static final int NOT_FOUND = -1;
  private static final int NON_SINGLE = -2;

  private final Singleton singleton = new Singleton();
  private final Pred.Uni<? super T> where;

  Single(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public final Integer tryApply(final T element) throws Throwable {
    ++singleton.index;

    if (where.tryTest(element) && singleton.position == NOT_FOUND) {
      singleton.position = singleton.index; // found

    } else if (where.tryTest(element) && singleton.position >= 0) {
      singleton.position = NON_SINGLE; // non-single
    }

    return singleton.position;
  }

  private static final class Singleton {
    private int index = -1;
    private int position = NOT_FOUND;
  }
}

final class First<T> implements Func.Uni<T, T> {
  private final Found found = new Found();
  private final Pred.Uni<? super T> where;

  First(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element) && found.value == null) {
      found.value = element;
    }

    return found.value;
  }

  private final class Found {
    private T value;
  }
}

final class Last<T> implements Func.Uni<T, T> {
  private final Found found = new Found();
  private final Pred.Uni<? super T> where;

  Last(final Pred.Uni<? super T> where) {
    assert where != null;
    this.where = where;
  }

  @Override
  public T tryApply(final T element) throws Throwable {
    if (where.tryTest(element)) {
      found.value = element;
    }
    return found.value;
  }

  private final class Found {
    private T value;
  }
}

final class At<T> implements Func.Uni<T, T> {
  private final Pointed pointed = new Pointed();
  private final int at;

  At(final int at) {
    this.at = at;
  }

  @Override
  public final T tryApply(final T element) {
    return pointed.index++ == at ? element : null;
  }

  private static final class Pointed {
    private int index = 0;
  }
}
