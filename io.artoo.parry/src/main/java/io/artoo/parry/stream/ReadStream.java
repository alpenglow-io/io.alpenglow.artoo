package io.artoo.parry.stream;

import io.artoo.parry.WriteStream;
import io.artoo.parry.async.Handler;

public interface ReadStream<T> extends StreamBase {

  /**
   * Set an exception handler on the read stream.
   *
   * @param handler  the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> exceptionHandler(Handler<Throwable> handler);

  /**
   * Set a data handler. As data is read, the handler will be called with the data.
   *
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> handler(Handler<T> handler);

  /**
   * Pause the {@code ReadStream}, it sets the buffer in {@code fetch} mode and clears the actual demand.
   * <p>
   * While it's paused, no data will be sent to the data {@code handler}.
   *
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> pause();

  /**
   * Resume reading, and sets the buffer in {@code flowing} mode.
   * <p/>
   * If the {@code ReadStream} has been paused, reading will recommence on it.
   *
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> resume();

  /**
   * Fetch the specified {@code amount} of elements. If the {@code ReadStream} has been paused, reading will
   * recommence with the specified {@code amount} of items, otherwise the specified {@code amount} will
   * be added to the current stream demand.
   *
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> fetch(long amount);

  /**
   * Set an end handler. Once the stream has ended, and there is no more data to be read, this handler will be called.
   *
   * @return a reference to this, so the API can be used fluently
   */
  ReadStream<T> endHandler(Handler<Void> endHandler);

  /**
   * Pause this stream and return a {@link Pipe} to transfer the elements of this stream to a destination {@link WriteStream}.
   * <p/>
   * The stream will be resumed when the pipe will be wired to a {@code WriteStream}.
   *
   * @return a pipe
   */
  default Pipe<T> pipe() {
    pause();
    return new PipeImpl<>(this);
  }

  /**
   * Same as {@link #pipeTo(WriteStream, Handler)} but returns a {@code Future} of the asynchronous result
   */
  default Future<Void> pipeTo(WriteStream<T> dst) {
    Promise<Void> promise = Promise.promise();
    new PipeImpl<>(this).to(dst, promise);
    return promise.future();
  }

  /**
   * Pipe this {@code ReadStream} to the {@code WriteStream}.
   * <p>
   * Elements emitted by this stream will be written to the write stream until this stream ends or fails.
   * <p>
   * Once this stream has ended or failed, the write stream will be ended and the {@code handler} will be
   * called with the result.
   *
   * @param dst the destination write stream
   */
  default void pipeTo(WriteStream<T> dst, Handler<AsyncResult<Void>> handler) {
    new PipeImpl<>(this).to(dst, handler);
  }
}
