package oak.type;

@FunctionalInterface
public interface AsArray<T> {
  T[] eval();
}
