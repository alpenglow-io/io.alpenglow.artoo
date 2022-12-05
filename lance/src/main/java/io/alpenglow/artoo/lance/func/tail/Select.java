package io.alpenglow.artoo.lance.func.tail;

import io.alpenglow.artoo.lance.func.Recursive.Tailrec;
import io.alpenglow.artoo.lance.func.TryFunction2;
import io.alpenglow.artoo.lance.func.TryFunction1;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Select<T, R> extends Tailrec<T, R, Select<T, R>> {
  private final TryFunction2<? super Integer, ? super T, ? extends R> select;
  private final int index;

  private Select(final int index, final TryFunction2<? super Integer, ? super T, ? extends R> select) {
    assert select != null;
    this.index = index;
    this.select = select;
  }

  @Override
  public Return<T, R, Select<T, R>> tryApply(final T t) throws Throwable {
    return Return.with(select.tryApply(index, t), new Select<>(index + 1, select));
  }

  public static <T, R> Select<T, R> with(TryFunction1<? super T, ? extends R> select) {
    return new Select<>(0, (index, it) -> select.tryApply(it));
  }

  public static <T, R> Select<T, R> with(TryFunction2<? super Integer, ? super T, ? extends R> select) {
    return new Select<>(0, select);
  }

  public static void main(String[] args) {
    interface Int32 extends Supplier<Integer> {
    }

    enum Operator implements BinaryOperator<Int32> {
      Plus {
        @Override
        public Int32 apply(Int32 num1, Int32 num2) {
          return () -> num1.get() + num2.get();
        }
      },
      Minus {
        @Override
        public Int32 apply(Int32 num1, Int32 num2) {
          return () -> num1.get() - num2.get();
        }
      }
    }

    enum Fibonacci implements Function<Long, Long> {
      Fun;
      @Override
      public Long apply(Long value) {
        if (value == 0L) return 0L;
        if (value == 1L) return 1L;
        return Fibonacci.Fun.apply(value - 1) + Fibonacci.Fun.apply(value - 2);
      }
    }

    var fibonacci = Fibonacci.Fun;
    for (long value = 0; value < 300; value++) {
      System.out.println(fibonacci.apply(value));
    }
  }
}
