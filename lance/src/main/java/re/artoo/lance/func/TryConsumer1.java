package re.artoo.lance.func;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryConsumer1<A> extends Consumer<A>, Invocable {
  void invoke(A a) throws Throwable;

  @Override
  default void accept(A a) {
    attempt(() -> invoke(a));
  }

  default A autoAccept(A a) {
    accept(a);
    return a;
  }

  default A autoInvoke(A a) throws Throwable {
    invoke(a);
    return a;
  }

  default TryConsumer1<A> then(TryConsumer1<A> next) {
    return it -> { invoke(it); next.invoke(it); };
  }
  default TryConsumer1<A> before(TryConsumer1<A> next) {
    return it -> { next.invoke(it); invoke(it); };
  }
}
