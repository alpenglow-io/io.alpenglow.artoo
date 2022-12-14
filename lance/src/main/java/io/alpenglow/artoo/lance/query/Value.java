package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TrySupplier1;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public sealed interface Value<T> extends TrySupplier1<T> {
  static <T> Value<T> set(T value) { return new Set<>(value); }
  static <T> Value<T> nil() { return (Value<T>) Nil.Default; }
  static <T> Value<T> nop() { return (Value<T>) Nop.Default; }
  default T tryGet() { return null; }
  record Set<T>(T tryGet) implements Value<T> {
    public Set {
      requireNonNull(tryGet, "Can't initialize a set-value with null");
    }
  }
  enum Nil implements Value<Object> { Default }
  enum Nop implements Value<Object> { Default }
}
