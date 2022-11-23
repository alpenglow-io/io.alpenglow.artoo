package io.artoo.lance.tuple;

final class NominalTupleException extends RuntimeException {
  public NominalTupleException(final String message, final ReflectiveOperationException exception) {
    super(message, exception);
  }
}
