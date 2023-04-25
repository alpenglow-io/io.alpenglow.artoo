package re.artoo.lance.func;

@FunctionalInterface
public interface ProviderTarget<TARGET> extends Invocable {
  TARGET invoke() throws Throwable;
}
