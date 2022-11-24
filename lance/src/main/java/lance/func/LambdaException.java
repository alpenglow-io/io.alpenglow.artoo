package lance.func;

public final class LambdaException extends RuntimeException {
  LambdaException(Throwable throwable) {
    super(throwable);
  }
}
