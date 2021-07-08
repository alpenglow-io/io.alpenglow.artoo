package io.artoo.lance.query.internal;

import io.artoo.lance.func.Func;

import java.util.concurrent.atomic.AtomicReference;

public interface Select<T, R> extends Func.Uni<T, Tail<T, R, Select<T, R>>> {
  static <T, R> Atomic<Select<T, R>> indexed(final Func.Uni<? super T, ? extends R> select) {
    return indexed((i, it) -> select.tryApply(it));
  }

  static <T, R> Atomic<Select<T, R>> indexed(final Func.Bi<? super Integer, ? super T, ? extends R> select) {
    return new Atomic<>(new Indexed<>(select));
  }

  final class Atomic<T> {
    private final AtomicReference<T> reference;

    Atomic(final T value) { this(new AtomicReference<>(value)); }
    private Atomic(final AtomicReference<T> reference) {this.reference = reference;}

    public final <R> R let(final Func.Uni<? super T, ? extends R> apply, final Func.Uni<? super R, ? extends T> update) {
      T prev = reference.get(), next = null;
      R applied = null;
      for (var haveNext = false;;) {
        if (!haveNext) {
          applied = apply.apply(prev);
          next = update.apply(applied);
        }
        if (reference.weakCompareAndSetVolatile(prev, next))
          return applied;
        haveNext = (prev == (prev = reference.get()));
      }
    }
  }

  final class Indexed<T, R> implements Select<T, R> {
    private final Func.Bi<? super Integer, ? super T, ? extends R> select;

    private final int index;

    Indexed(final Func.Bi<? super Integer, ? super T, ? extends R> select) {
      this(0, select);
    }
    Indexed(final int index, final Func.Bi<? super Integer, ? super T, ? extends R> select) {
      assert select != null;
      this.index = index;
      this.select = select;
    }

    @Override
    public Tail<T, R, Select<T, R>> tryApply(final T t) throws Throwable {
      return
        new Tail<>(
          new Indexed<>(index + 1, select),
          select.tryApply(index, t)
        );
    }
  }
}
