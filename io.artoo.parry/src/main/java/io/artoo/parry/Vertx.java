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

package io.artoo.parry;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;
import io.artoo.parry.eventbus.EventBus;
import io.artoo.parry.eventloop.EventLoopGroup;
import io.artoo.parry.stream.ReadStream;
import io.artoo.parry.stream.TimeoutStream;

/**
 * The entry point into the Vert.x Core API.
 * <p>
 * You use an instance of this class for functionality including:
 * <ul>
 *   <li>Creating TCP clients and servers</li>
 *   <li>Creating HTTP clients and servers</li>
 *   <li>Creating DNS clients</li>
 *   <li>Creating Datagram sockets</li>
 *   <li>Setting and cancelling periodic and one-shot timers</li>
 *   <li>Getting a reference to the event bus API</li>
 *   <li>Getting a reference to the file system API</li>
 *   <li>Getting a reference to the shared data API</li>
 *   <li>Deploying and undeploying verticles</li>
 * </ul>
 * <p>
 * Most functionality in Vert.x core is fairly low level.
 * <p>
 * To create an instance of this class you can use the static factory methods: {@link #vertx},
 * {@link #vertx(io.vertx.core.VertxOptions)} and {@link #clusteredVertx(io.vertx.core.VertxOptions, Handler)}.
 * <p>
 * Please see the user manual for more detailed usage information.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public interface Vertx {

  /**
   * Creates a non clustered instance using default options.
   *
   * @return the instance
   */
  static Vertx vertx() {
    return vertx(new VertxOptions());
  }

  /**
   * Creates a non clustered instance using the specified options
   *
   * @param options  the options to use
   * @return the instance
   */
  static Vertx vertx(VertxOptions options) {
    return new VertxBuilder(options).init().vertx();
  }

  /**
   * Gets the current context
   *
   * @return The current context or {@code null} if there is no current context
   */
  static Context currentContext() {
    return ContextInternal.current();
  }

  /**
   * Gets the current context, or creates one if there isn't one
   *
   * @return The current context (created if didn't exist)
   */
  Context getOrCreateContext();

  EventBus eventBus();

  long setTimer(long delay, Handler<Long> handler);

  /**
   * Returns a one-shot timer as a read stream. The timer will be fired after {@code delay} milliseconds after
   * the {@link ReadStream#handler} has been called.
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @return the timer stream
   */
  TimeoutStream timerStream(long delay);

  /**
   * Set a periodic timer to fire every {@code delay} milliseconds, at which point {@code handler} will be called with
   * the id of the timer.
   *
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @param handler  the handler that will be called with the timer ID when the timer fires
   * @return the unique ID of the timer
   */
  long setPeriodic(long delay, Handler<Long> handler);

  /**
   * Returns a periodic timer as a read stream. The timer will be fired every {@code delay} milliseconds after
   * the {@link ReadStream#handler} has been called.
   *
   * @param delay  the delay in milliseconds, after which the timer will fire
   * @return the periodic stream
   */
  TimeoutStream periodicStream(long delay);

  /**
   * Cancels the timer with the specified {@code id}.
   *
   * @param id  The id of the timer to cancel
   * @return true if the timer was successfully cancelled, or false if the timer does not exist.
   */
  boolean cancelTimer(long id);

  /**
   * Puts the handler on the event queue for the current context so it will be run asynchronously ASAP after all
   * preceeding events have been handled.
   *
   * @param action - a handler representing the action to execute
   */
  void runOnContext(Handler<Void> action);

  /**
   * Stop the the Vertx instance and release any resources held by it.
   * <p>
   * The instance cannot be used after it has been closed.
   * <p>
   * The actual close is asynchronous and may not complete until after the call has returned.
   *
   * @return a future completed with the result
   */
  Future<Void> close();

  /**
   * Like {@link #close} but the completionHandler will be called when the close is complete
   *
   * @param completionHandler  The handler will be notified when the close is complete.
   */
  void close(Handler<AsyncResult<Void>> completionHandler);

  /**
   * Safely execute some blocking code.
   * <p>
   * Executes the blocking code in the handler {@code blockingCodeHandler} using a thread from the worker pool.
   * <p>
   * When the code is complete the handler {@code resultHandler} will be called with the result on the original context
   * (e.g. on the original event loop of the caller).
   * <p>
   * A {@code Future} instance is passed into {@code blockingCodeHandler}. When the blocking code successfully completes,
   * the handler should call the {@link Promise#complete} or {@link Promise#complete(Object)} method, or the {@link Promise#fail}
   * method if it failed.
   * <p>
   * In the {@code blockingCodeHandler} the current context remains the original context and therefore any task
   * scheduled in the {@code blockingCodeHandler} will be executed on the this context and not on the worker thread.
   * <p>
   * The blocking code should block for a reasonable amount of time (i.e no more than a few seconds). Long blocking operations
   * or polling operations (i.e a thread that spin in a loop polling events in a blocking fashion) are precluded.
   * <p>
   * When the blocking operation lasts more than the 10 seconds, a message will be printed on the console by the
   * blocked thread checker.
   * <p>
   * Long blocking operations should use a dedicated thread managed by the application, which can interact with
   * verticles using the event-bus or {@link Context#runOnContext(Handler)}
   *
   * @param blockingCodeHandler  handler representing the blocking code to run
   * @param resultHandler  handler that will be called when the blocking code is complete
   * @param ordered  if true then if executeBlocking is called several times on the same context, the executions
   *                 for that context will be executed serially, not in parallel. if false then they will be no ordering
   *                 guarantees
   * @param <T> the type of the result
   */
  <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered, Handler<AsyncResult<T>> resultHandler);

  /**
   * Like {@link #executeBlocking(Handler, boolean, Handler)} called with ordered = true.
   */
  <T> void executeBlocking(Handler<Promise<T>> blockingCodeHandler, Handler<AsyncResult<T>> resultHandler);

  /**
   * Same as {@link #executeBlocking(Handler, boolean, Handler)} but with an {@code handler} called when the operation completes
   */
  <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler, boolean ordered);

  /**
   * Same as {@link #executeBlocking(Handler, Handler)} but with an {@code handler} called when the operation completes
   */
  <T> Future<T> executeBlocking(Handler<Promise<T>> blockingCodeHandler);

  /**
   * Return the Netty EventLoopGroup used by Vert.x
   *
   * @return the EventLoopGroup
   */
  EventLoopGroup nettyEventLoopGroup();

  /**
   * Set a default exception handler for {@link Context}, set on {@link Context#exceptionHandler(Handler)} at creation.
   *
   * @param handler the exception handler
   * @return a reference to this, so the API can be used fluently
   */
  Vertx exceptionHandler(Handler<Throwable> handler);

  /**
   * @return the current default exception handler
   */
  Handler<Throwable> exceptionHandler();
}
