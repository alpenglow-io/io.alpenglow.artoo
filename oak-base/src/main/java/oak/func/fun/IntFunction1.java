package oak.func.fun;

@FunctionalInterface
public interface IntFunction1<R> extends Function1<Integer, R> {
  R applyInt(final int value);

  @Override
  default R apply(final Integer value) {
    return applyInt(value);
  }
}
