package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.value.UInt32;

import java.util.function.Predicate;

import static java.util.function.Function.identity;

interface Countable<T extends Record> extends Queryable<T> {
  default One<UInt32> count() {
    return this.count(it -> true);
  }

  default One<UInt32> count(final Predicate<? super T> where) {
    return new Aggregate<>(this, it -> {}, UInt32.ZERO, where, identity(), (counted, item) -> counted.inc())::iterator;
  }
}
