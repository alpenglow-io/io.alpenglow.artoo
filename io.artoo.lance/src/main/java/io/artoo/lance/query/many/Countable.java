package io.artoo.lance.query.many;

import io.artoo.lance.query.One;
import io.artoo.lance.query.Queryable;
import io.artoo.lance.value.UInt32;

import java.util.function.Predicate;

import static java.util.function.Function.identity;

interface Countable<T extends Record> extends Queryable<T> {
  default One<UInt32> count() {
    return this.count(it -> true);
  }

  default One<UInt32> count(final Predicate<? super T> where) {
    return new Aggregate<>(this, it -> {}, UInt32.ZERO, where, identity(), (counted, item) -> counted.inc());
  }
}
