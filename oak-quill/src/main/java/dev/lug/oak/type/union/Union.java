package dev.lug.oak.type.union;

import dev.lug.oak.func.fun.Function2;

public interface Union {

}

enum Default {;
  static Union2<?, ?> None2 = new Union2<>() {
    @Override
    public <T> T as(Function2<Object, Object, T> as) {
      return null;
    }
  };
}
