package oak.func.con;

import oak.func.Functional;
import oak.func.fun.Function1;

import java.util.function.Consumer;

@FunctionalInterface
public interface Consumer1<T> extends Consumer<T>, Function1<T, Void>, Functional.Con {
  @Override
  default Void apply(T t) {
    accept(t);
    return null;
  }
}
