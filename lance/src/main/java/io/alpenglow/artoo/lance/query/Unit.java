package io.alpenglow.artoo.lance.query;

import io.alpenglow.artoo.lance.func.TrySupplier1;

import java.util.Objects;

import static io.alpenglow.artoo.lance.query.Unit.Undefined.*;
import static java.util.Objects.requireNonNull;

@SuppressWarnings("unchecked")
public sealed interface Unit<T> {
  static <T> Unit<T> value(T value) { return new Value<>(value); }
  static <T> Unit<T> nullable() { return (Unit<T>) Null; }
  static <T> Unit<T> nothing() { return (Unit<T>) Nothing; }
  default T value() { return null; }
  record Value<T>(T value) implements Unit<T> {
    public Value {
      requireNonNull(value, "Can't initialize a return-value with null");
    }
  }
  enum Undefined implements Unit<Object> {Null, Nothing}

  @SuppressWarnings("DataFlowIssue")
  static void main(String[] args) {
    switch (Unit.<Integer>nothing()) {
      case Value<Integer> it when Null.equals(it) -> System.out.println("Ehilà");
      case Value<Integer> it when Nothing.equals(it) -> System.out.println("Ohibò");
      default -> System.out.println("Bah");
    }
  }
}
