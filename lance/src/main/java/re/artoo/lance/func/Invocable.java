package re.artoo.lance.func;

public interface Invocable {
  default <TARGET> TARGET attempt(ProviderTarget<? extends TARGET> provider) {
    try {
      return provider.invoke();
    } catch (Throwable throwable) {
      throw throwable instanceof RuntimeException it ? it : new InvokeException(throwable);
    }
  }

  default void attempt(ProviderVoid provider) {
    try {
      provider.invoke();
    } catch (Throwable throwable) {
      throw throwable instanceof RuntimeException it ? it : new InvokeException(throwable);
    }
  }
}
