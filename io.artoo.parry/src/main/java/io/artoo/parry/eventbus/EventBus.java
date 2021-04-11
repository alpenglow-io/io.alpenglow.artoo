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

package io.artoo.parry.eventbus;

import io.artoo.parry.async.AsyncResult;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;

/**
 * A Vert.x event-bus is a light-weight distributed messaging system which allows different parts of your application,
 * or different applications and services to communicate with each in a loosely coupled way.
 * <p>
 * An event-bus supports publish-subscribe messaging, point-to-point messaging and request-response messaging.
 * <p>
 * Message delivery is best-effort and messages can be lost if failure of all or part of the event bus occurs.
 * <p>
 * Please refer to the documentation for more information on the event bus.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

public interface EventBus {

  /**
   * Sends a message.
   * <p>
   * The message will be delivered to at most one of the handlers registered to the address.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @return a reference to this, so the API can be used fluently
   */
  EventBus send(String address, Object message);

  /**
   * Like {@link #send(String, Object)} but specifying {@code options} that can be used to configure the delivery.
   *
   * @param address  the address to send it to
   * @param message  the message, may be {@code null}
   * @param options  delivery options
   * @return a reference to this, so the API can be used fluently
   */
  EventBus send(String address, Object message, DeliveryOptions options);

  /**
   * Sends a message and and specify a {@code replyHandler} that will be called if the recipient
   * subsequently replies to the message.
   * <p>
   * The message will be delivered to at most one of the handlers registered to the address.
   *
   * @param address  the address to send it to
   * @param message  the message body, may be {@code null}
   * @param replyHandler  reply handler will be called when any reply from the recipient is received
   * @return a reference to this, so the API can be used fluently
   */
  default <T> EventBus request(String address, Object message, Handler<AsyncResult<Message<T>>> replyHandler) {
    return request(address, message, new DeliveryOptions(), replyHandler);
  }

  /**
   * Like {@link #request(String, Object, Handler)} but returns a {@code Future} of the asynchronous result
   */
  default <T> Future<Message<T>> request(String address, Object message) {
    return request(address, message, new DeliveryOptions());
  }

  /**
   * Like {@link #request(String, Object, Handler)} but specifying {@code options} that can be used to configure the delivery.
   *
   * @param address  the address to send it to
   * @param message  the message body, may be {@code null}
   * @param options  delivery options
   * @param replyHandler  reply handler will be called when any reply from the recipient is received
   * @return a reference to this, so the API can be used fluently
   */
  default <T> EventBus request(String address, Object message, DeliveryOptions options, Handler<AsyncResult<Message<T>>> replyHandler) {
    Future<Message<T>> reply = request(address, message, options);
    reply.onComplete(replyHandler);
    return this;
  }

  /**
   * Like {@link #request(String, Object, DeliveryOptions, Handler)} but returns a {@code Future} of the asynchronous result
   */
  <T> Future<Message<T>> request(String address, Object message, DeliveryOptions options);

  /**
   * Publish a message.<p>
   * The message will be delivered to all handlers registered to the address.
   *
   * @param address  the address to publish it to
   * @param message  the message, may be {@code null}
   * @return a reference to this, so the API can be used fluently
   *
   */
  EventBus publish(String address, Object message);

  /**
   * Like {@link #publish(String, Object)} but specifying {@code options} that can be used to configure the delivery.
   *
   * @param address  the address to publish it to
   * @param message  the message, may be {@code null}
   * @param options  the delivery options
   * @return a reference to this, so the API can be used fluently
   */
  EventBus publish(String address, Object message, DeliveryOptions options);

  <T> MessageConsumer<T> consumer(String address);

  /**
   * Create a consumer and register it against the specified address.
   *
   * @param address  the address that will register it at
   * @param handler  the handler that will process the received messages
   *
   * @return the event bus message consumer
   */
  <T> MessageConsumer<T> consumer(String address, Handler<Message<T>> handler);

  /**
   * Like {@link #consumer(String)} but the address won't be propagated across the cluster.
   *
   * @param address  the address to register it at
   * @return the event bus message consumer
   */
  <T> MessageConsumer<T> localConsumer(String address);

  /**
   * Like {@link #consumer(String, Handler)} but the address won't be propagated across the cluster.
   *
   * @param address  the address that will register it at
   * @param handler  the handler that will process the received messages
   * @return the event bus message consumer
   */
  <T> MessageConsumer<T> localConsumer(String address, Handler<Message<T>> handler);

  <T> MessageProducer<T> sender(String address);

  /**
   * Like {@link #sender(String)} but specifying delivery options that will be used for configuring the delivery of
   * the message.
   *
   * @param address  the address to send it to
   * @param options  the delivery options
   * @return The sender
   */
  <T> MessageProducer<T> sender(String address, DeliveryOptions options);

  <T> MessageProducer<T> publisher(String address);

  /**
   * Like {@link #publisher(String)} but specifying delivery options that will be used for configuring the delivery of
   * the message.
   *
   * @param address  the address to publish it to
   * @param options  the delivery options
   * @return The publisher
   */
  <T> MessageProducer<T> publisher(String address, DeliveryOptions options);

  /**
   * Register a message codec.
   * <p>
   * You can register a message codec if you want to send any non standard message across the event bus.
   * E.g. you might want to send POJOs directly across the event bus.
   * <p>
   * To use a message codec for a send, you should specify it in the delivery options.
   *
   * @param codec  the message codec to register
   * @return a reference to this, so the API can be used fluently
   */
  EventBus registerCodec(MessageCodec codec);

  /**
   * Unregister a message codec.
   * <p>
   * @param name  the name of the codec
   * @return a reference to this, so the API can be used fluently
   */
  EventBus unregisterCodec(String name);

  /**
   * Register a default message codec.
   * <p>
   * You can register a message codec if you want to send any non standard message across the event bus.
   * E.g. you might want to send POJOs directly across the event bus.
   * <p>
   * Default message codecs will be used to serialise any messages of the specified type on the event bus without
   * the codec having to be specified in the delivery options.
   *
   * @param clazz  the class for which to use this codec
   * @param codec  the message codec to register
   * @return a reference to this, so the API can be used fluently
   */
  <T> EventBus registerDefaultCodec(Class<T> clazz, MessageCodec<T, ?> codec);

  /**
   * Unregister a default message codec.
   * <p>
   * @param clazz  the class for which the codec was registered
   * @return a reference to this, so the API can be used fluently
   */
  EventBus unregisterDefaultCodec(Class clazz);

  /**
   * Add an interceptor that will be called whenever a message is sent from Vert.x
   *
   * @param interceptor  the interceptor
   * @return a reference to this, so the API can be used fluently
   */
  <T> EventBus addOutboundInterceptor(Handler<DeliveryContext<T>> interceptor);

  /**
   * Remove an interceptor that was added by {@link #addOutboundInterceptor(Handler)}
   *
   * @param interceptor  the interceptor
   * @return a reference to this, so the API can be used fluently
   */
  <T> EventBus removeOutboundInterceptor(Handler<DeliveryContext<T>> interceptor);

  /**
   * Add an interceptor that will be called whenever a message is received by Vert.x
   *
   * @param interceptor  the interceptor
   * @return a reference to this, so the API can be used fluently
   */
  <T> EventBus addInboundInterceptor(Handler<DeliveryContext<T>> interceptor);

  /**
   * Remove an interceptor that was added by {@link #addInboundInterceptor(Handler)}
   *
   * @param interceptor  the interceptor
   * @return a reference to this, so the API can be used fluently
   */
  <T> EventBus removeInboundInterceptor(Handler<DeliveryContext<T>> interceptor);
}

