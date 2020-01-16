package dev.lug.oak.query;

import dev.lug.oak.collect.Many;

public interface Splittable<T, S extends Structable<T>> extends Structable<T> {
  Many<S[]> splitAt(int... indexes);
}
