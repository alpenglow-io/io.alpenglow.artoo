package oak.query.one;

import oak.func.$2.IntCons;
import oak.func.Cons;
import oak.func.Exec;
import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static oak.query.one.Projectable.as;
import static oak.query.one.Projectable.one;
import static oak.type.Nullability.nonNullable;

public interface Peekable<T> extends Sneakable<T> {
  default One<T> peek(final IntCons<? super T> peek) {
    return () -> new Peek<>(this, peek).iterator();
  }

  default One<T> peek(final Cons<? super T> peek) {
    nonNullable(peek, "peek");
    return peek((index, it) -> peek.accept(it));
  }

  default One<T> peek(final Exec exec) {
    nonNullable(exec, "exec");
    return peek((index, it) -> exec.execute());
  }

  public static void main(String[] args) {
    final var s = One.of(1)
      .peek(writeLn())
      .select(as(Object::toString))
      .select(one(it -> One.of(1)))
      .where(it -> it > 0)
      .or(123)
      .asString();
  }

  @NotNull
  static Cons<Integer> writeLn() {
    return System.out::println;
  }
}

final class Peek<T> implements Sneakable<T> {
  private final Queryable<T> queryable;
  private final IntCons<? super T> peek;

  Peek(Queryable<T> queryable, IntCons<? super T> peek) {
    this.queryable = queryable;
    this.peek = peek;
  }

  @Contract(pure = true)
  @Override
  public final IntCons<? super T> sneak() {
    return this.peek;
  }

  @NotNull
  @Override
  public final Iterator<T> iterator() {
    return null;
  }
}
