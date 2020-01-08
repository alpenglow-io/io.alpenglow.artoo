package dev.lug.oak.query;

import dev.lug.oak.query.many.Queryable;

public interface Splittable<T, S extends Structable<T>> extends Structable<T> {
  Queryable<S[]> splitAt(int... indexes);
}
