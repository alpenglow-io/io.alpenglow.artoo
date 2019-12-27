package dev.lug.oak.quill.single;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.func.sup.Supplier1;
import dev.lug.oak.type.Nullability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

import static dev.lug.oak.type.Nullability.*;
import static java.util.Objects.isNull;

public interface Nullable<T> extends Projectable<T>, Filterable<T>, Casing<T> {
  static <L> Nullable<L> of(final L value) {
    return isNull(value) ? Nullable.none() : Single.of(value);
  }

  static <L> Nullable<L> of(final Supplier1<L> supplier) {
    Stream.of(1, 2, 3).map(it -> null).findAny();
    return nonNullable(supplier, "supplier").get();
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  static <L> Nullable<L> none() {
    return new None<>();
  }
}

final class None<T> implements Nullable<T> {
  @NotNull
  @Contract(pure = true)
  @Override
  public final Iterator<T> iterator() {
    return Cursor.none();
  }
}
