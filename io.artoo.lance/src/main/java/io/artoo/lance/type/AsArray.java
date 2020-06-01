package io.artoo.lance.type;

@FunctionalInterface
public interface AsArray<T> {
  T[] eval();
}
