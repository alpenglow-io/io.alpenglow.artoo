package io.artoo.lance.query;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.one.Filterable;
import io.artoo.lance.query.one.Otherwise;
import io.artoo.lance.query.one.Peekable;
import io.artoo.lance.query.one.Projectable;
import io.artoo.lance.value.Char;
import io.artoo.lance.value.Single32;
import io.artoo.lance.value.Single64;
import io.artoo.lance.value.Int16;
import io.artoo.lance.value.Int32;
import io.artoo.lance.value.Int64;
import io.artoo.lance.value.Int8;
import io.artoo.lance.value.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface One<R extends Record> extends Projectable<R>, Peekable<R>, Filterable<R>, Otherwise<R> {
  static <L extends  Record> One<L> of(final L record) {
    return record != null ? One.just(record) : One.none();
  }

  @NotNull
  @Contract("_ -> new")
  static <L extends Record> One<L> just(final L record) {
    return new Just<>(record);
  }

  static One<Text> from(final String value) {
    try {
      return One.just(Text.let(value));
    } catch (AssertionError error) {
      return One.none();
    }
  }

  static @NotNull One<Char> from(final char value) {
    return One.just(new Char(value));
  }

  static @NotNull One<Int8> from(final byte value) {
    return One.just(new Int8(value));
  }

  static @NotNull One<Int16> from(final short value) {
    return One.just(new Int16(value));
  }

  static @NotNull One<Int32> from(final int value) {
    return One.just(new Int32(value));
  }

  static @NotNull One<Int64> from(final long value) {
    return One.just(new Int64(value));
  }

  static @NotNull One<Single32> from(final float value) {
    return One.just(new Single32(value));
  }

  static @NotNull One<Single64> from(final double value) {
    return One.just(new Single64(value));
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  @SuppressWarnings("unchecked")
  static <L extends Record> One<L> none() {
    return (One<L>) OneRecord.None;
  }
}

final class Just<T extends Record> implements One<T> {
  private final T value;

  public Just(T value) {this.value = value;}

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return Cursor.of(value);
  }
}

enum OneRecord implements One<Record> {
  None;

  @NotNull
  @Override
  public Iterator<Record> iterator() {
    return Cursor.none();
  }
}
