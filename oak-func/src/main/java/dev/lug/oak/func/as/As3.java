package dev.lug.oak.func.as;

import dev.lug.oak.func.fun.Function3;

@FunctionalInterface
public
interface As3<V1, V2, V3> {
  <T> T as(Function3<V1, V2, V3, T> as);
}
