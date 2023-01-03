package re.artoo.lance.func;

public final class LambdaCallException extends RuntimeException {
  LambdaCallException(Throwable throwable) {
    super(throwable);
  }
}
