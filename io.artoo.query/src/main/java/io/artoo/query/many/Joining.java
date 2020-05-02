package io.artoo.query.many;

import io.artoo.query.Many;

import java.util.function.BiPredicate;

public interface Joining<T1, T2> {
  Many<Bag<T1, T2>> on(final BiPredicate<? super T1, ? super T2> on);

  interface Bag<T1, T2> extends Union<T1, T2> {
  }
}
