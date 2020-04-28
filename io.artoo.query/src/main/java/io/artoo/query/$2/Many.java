package io.artoo.query.$2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import io.artoo.cursor.Cursor;
import io.artoo.query.$2.many.Filterable;
import io.artoo.query.$2.many.Projectable;
import io.artoo.union.$2.Union;

import java.util.ArrayList;

import static io.artoo.type.Nullability.nonNullable;

@FunctionalInterface
public interface Many<V1, V2> extends Projectable<V1, V2>, Filterable<V1, V2> {
  @NotNull
  @Contract("_ -> new")
  @SafeVarargs
  static <T1, T2> Many<T1, T2> of(final Union<T1, T2>... unions) {
    nonNullable(unions, "unions");
    return from(() -> Cursor.many(unions));
  }

  @NotNull
  @Contract("_ -> new")
  static <T1, T2> Many<T1, T2> from(final Iterable<Union<T1, T2>> unions) {
    nonNullable(unions, "unions");
    return () -> {
      final var array = new ArrayList<Union<T1, T2>>();
      for (final var union : unions)
        array.add(union);
      return array.iterator();
    };
  }
}
