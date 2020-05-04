package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;
import io.artoo.value.Int32;

import java.util.function.Predicate;

import static java.util.function.Function.identity;

interface Countable<T extends Record> extends Queryable<T> {
  default One<Int32> count() {
    return this.count(it -> true);
  }

  default One<Int32> count(final Predicate<? super T> where) {
    return new Aggregate<>(this, it -> {}, Int32.ZERO, where, identity(), (counted, item) -> new Int32(counted.eval() + 1))::iterator;
  }
}
