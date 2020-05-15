package io.artoor.query.many;

import io.artoor.query.Many;

import java.util.function.BiPredicate;

public interface Joining<T1 extends Record, T2 extends Record> {
  Many<Bag<T1, T2>> on(final BiPredicate<? super T1, ? super T2> on);

  interface Bag<T1, T2> extends Union<T1, T2> {
  }
}
