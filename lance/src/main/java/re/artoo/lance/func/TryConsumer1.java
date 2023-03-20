package re.artoo.lance.func;

import java.util.function.Consumer;

@FunctionalInterface
public interface TryConsumer1<A> extends Consumer<A> {
  void invoke(A a) throws Throwable;

  default <SELF> SELF selfInvoke(SELF self, A a) throws Throwable {
    invoke(a);
    return self;
  }

  default <SELF> SELF selfAccept(SELF self, A a) {
    accept(a);
    return self;
  }

  @Override
  default void accept(A a) {
    try {
      invoke(a);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
