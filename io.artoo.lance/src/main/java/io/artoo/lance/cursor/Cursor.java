package io.artoo.lance.cursor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

import static io.artoo.lance.type.Nullability.nonNullable;
import static io.artoo.lance.type.Nullability.nullable;

@SuppressWarnings("unchecked")
public interface Cursor<R extends Record> extends Iterator<R> {
  @SafeVarargs
  @NotNull
  @Contract("_ -> new")
  static <R extends Record> Cursor<R> many(final R... values) {
    return new Linear<>(Arrays.copyOf(nonNullable(values, "values"), values.length));
  }

  @NotNull
  @Contract(" -> new")
  static Index index() {
    return new Index(0);
  }

  @NotNull
  @Contract("_ -> new")
  static Index index(final int start) {
    return new Index(start);
  }

  static <R extends Record> Cursor<R> of(final R value) {
    return nullable(value, Just::new, Cursor::none);
  }

  static <R extends Record> Cursor<R> just(final R value) {
    return new Just<>(nonNullable(value, "value"));
  }

  @NotNull
  static <R extends Record> Cursor<R> none() {
    return (Cursor<R>) Default.None;
  }

  Cursor<R> resume();
}


