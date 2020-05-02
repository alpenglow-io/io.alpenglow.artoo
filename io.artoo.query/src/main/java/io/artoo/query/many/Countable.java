package io.artoo.query.many;

import io.artoo.query.One;
import io.artoo.query.Queryable;
import io.artoo.query.many.impl.Aggregate;

import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.function.Function.identity;

interface Countable<T extends Record> extends Queryable<T> {
  record Counted(int value) {}

  default One<Counted> count() {
    return this.count(it -> true);
  }

  default One<Counted> count(final Predicate<? super T> where) {
    return new Aggregate<>(this, it -> {}, new Counted(0), where, identity(), (counted, item) -> new Counted(counted.value + 1))::iterator;
  }
}
