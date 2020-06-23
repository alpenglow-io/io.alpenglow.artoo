package io.artoo.lance.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.type.Nullability.nullable;

@SuppressWarnings("unchecked")
public interface Cursor<R> extends Iterator<R> {
  @SafeVarargs
  @NotNull
  @Contract("_ -> new")
  static <R> Cursor<R> many(final R... values) {
    return new Linear<>(Arrays.copyOf(nonNullable(values, "values"), values.length));
  }

  @NotNull
  @Contract(" -> new")
  static Index index() {
    return Index.zero();
  }

  static <R> Cursor<R> of(final R value) {
    return nullable(value, Lone::new, Cursor::none);
  }

  static <R> Cursor<R> lone(final R value) {
    return new Lone<>(nonNullable(value, "element"));
  }

  @NotNull
  static <R> Cursor<R> none() {
    return (Cursor<R>) Default.None;
  }
}


