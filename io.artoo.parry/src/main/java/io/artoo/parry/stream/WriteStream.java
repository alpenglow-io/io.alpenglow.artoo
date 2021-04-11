/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.artoo.parry.stream;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;

/**
 *
 * Represents a stream of data that can be written to.
 * <p>
 * Any class that implements this interface can be used by a {@link Pipe} to pipe data from a {@code ReadStream}
 * to it.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface WriteStream<T> extends StreamBase {

  /**
   * Set an exception handler on the write stream.
   *
   * @param handler  the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  @Override
  WriteStream<T> exceptionHandler(Handler<Throwable> handler);

  /**
   * Write some data to the stream.
   *
   * <p> The data is usually put on an internal write queue, and the write actually happens
   * asynchronously. To avoid running out of memory by putting too much on the write queue,
   * check the {@link #writeQueueFull} method before writing. This is done automatically if
   * using a {@link Pipe}.
   *
   * <p> When the {@code data} is moved from the queue to the actual medium, the returned
   * {@link Future} will be completed with the write result, e.g the future is succeeded
   * when a server HTTP response buffer is written to the socket and failed if the remote
   * client has closed the socket while the data was still pending for write.
   *
   * @param data  the data to write
   * @return a future completed with the write result
   */
  Future<Void> write(T data);

  /**
   * Same as {@link #write(T)} but with an {@code handler} called when the operation completes
   */
  void write(T data, Handler<AsyncResult<Void>> handler);

  /**
   * Ends the stream.
   * <p>
   * Once the stream has ended, it cannot be used any more.
   *
   * @return a future completed with the result
   */
  default Future<Void> end() {
    Promise<Void> promise = Promise.promise();
    end(promise);
    return promise.future();
  }

  /**
   * Same as {@link #end()} but with an {@code handler} called when the operation completes
   */
  void end(Handler<AsyncResult<Void>> handler);

  /**
   * Same as {@link #end()} but writes some data to the stream before ending.
   *
   * @implSpec The default default implementation calls sequentially {@link #write(Object)} then {@link #end()}
   * @apiNote Implementations might want to perform a single operation
   * @param data the data to write
   * @return a future completed with the result
   */
  default Future<Void> end(T data) {
    Promise<Void> provide = Promise.promise();
    end(data, provide);
    return provide.future();
  }

  /**
   * Same as {@link #end(T)} but with an {@code handler} called when the operation completes
   */
  default void end(T data, Handler<AsyncResult<Void>> handler) {
    if (handler != null) {
      write(data, ar -> {
        if (ar.succeeded()) {
          end(handler);
        } else {
          handler.handle(ar);
        }
      });
    } else {
      end(data);
    }
  }

  WriteStream<T> setWriteQueueMaxSize(int maxSize);

  /**
   * This will return {@code true} if there are more bytes in the write queue than the value set using {@link
   * #setWriteQueueMaxSize}
   *
   * @return {@code true} if write queue is full
   */
  boolean writeQueueFull();

  WriteStream<T> drainHandler(Handler<Void> handler);
}
