package dev.lug.oak.func.as;

import dev.lug.oak.func.fun.Function2;

@FunctionalInterface
public
interface As2<V1, V2> {
  <T> T as(Function2<V1, V2, T> as);
}
