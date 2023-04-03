package re.artoo.lance.func;

@FunctionalInterface
public interface TryIntConsumer1<A> {
  void invoke(int integer, A a) throws Throwable;

  default <SELF> SELF selfInvoke(SELF self, int value, A a) throws Throwable {
    invoke(value, a);
    return self;
  }

  default <SELF> SELF selfAccept(SELF self, int value, A a) {
    accept(value, a);
    return self;
  }

  default void accept(int integer, A a) {
    try {
      invoke(integer, a);
    } catch (Throwable throwable) {
      throw new InvokeException(throwable);
    }
  }
}
