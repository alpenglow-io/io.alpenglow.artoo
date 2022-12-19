package io.alpenglow.artoo.lance.query.closure;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.query.Closure;

public sealed interface Select<T, R> extends Closure<T, R> {
  static <T, R> Select<T, R> indexed(TryFunction2<? super Integer, ? super T, ? extends R> function) {
    return new Indexed<>(function);
  }
  static <T, R> Select<T, R> plain(TryFunction1<? super T, ? extends R> function) {
    return new Plain<>(function);
  }

  final class Indexed<T, R> implements Select<T, R> {
    private final TryFunction2<? super Integer, ? super T, ? extends R> function;
    private int index;
    Indexed(TryFunction2<? super Integer, ? super T, ? extends R> function) {
      this(function, 0);
    }
    private Indexed(TryFunction2<? super Integer, ? super T, ? extends R> function, int index) {
      this.function = function;
      this.index = index;
    }

    @Override
    public R invoke(T element) throws Throwable {
      return function.invoke(index++, element);
    }
  }

  final class Plain<T, R> implements Select<T, R> {
    private final TryFunction1<? super T, ? extends R> function;
    Plain(TryFunction1<? super T, ? extends R> function) {
      this.function = function;
    }
    @Override
    public R invoke(T element) throws Throwable {
      return function.invoke(element);
    }
  }
}
