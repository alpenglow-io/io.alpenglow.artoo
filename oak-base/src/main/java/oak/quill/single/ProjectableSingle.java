package oak.quill.single;

import oak.func.con.Consumer1;
import oak.func.fun.Function1;
import org.jetbrains.annotations.Contract;

import java.util.List;

import static oak.type.Nullability.nonNullable;

public interface ProjectableSingle<T> extends StructableSingle<T> {
  default <R> Nullable<R> select(final Function1<? super T, ? extends R> map) {
    return new Select<>(this, nonNullable(map, "map"));
  }

  default <R, M extends Nullable<? extends R>> Nullable<R> selection(final Function1<? super T, M> flatMap) {
    return new Selection<>(new Select<>(this, nonNullable(flatMap, "flatMap")));
  }

  default Nullable<T> peek(final Consumer1<? super T> peek) { return new Peek<>(this, nonNullable(peek, "peek")); }
}

final class Select<S, R> implements Nullable<R> {
  private final StructableSingle<S> structable;
  private final Function1<? super S, ? extends R> map;

  @Contract(pure = true)
  Select(final StructableSingle<S> structable, Function1<? super S, ? extends R> map) {
    this.structable = structable;
    this.map = map;
  }

  @Override
  public final R get() {
    return map.apply(structable.get());
  }
}

final class Selection<R, S extends StructableSingle<? extends R>> implements Nullable<R> {
  private final StructableSingle<S> structable;

  @Contract(pure = true)
  Selection(StructableSingle<S> structable) {
    this.structable = structable;
  }

  @Override
  public final R get() {
    return structable.get().get();
  }
}

final class Peek<T> implements Nullable<T> {
  private final StructableSingle<T> structable;
  private final Consumer1<? super T> peek;

  @Contract(pure = true)
  Peek(final StructableSingle<T> structable, final Consumer1<? super T> peek) {
    this.structable = structable;
    this.peek = peek;
  }

  @Override
  public final T get() {
    final var value = structable.get();
    peek.accept(value);
    return value;
  }
}
