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

package io.artoo.parry.eventbus.impl;

import io.artoo.parry.async.Future;
import io.artoo.parry.eventbus.DeliveryOptions;
import io.artoo.parry.eventbus.Message;
import io.artoo.parry.eventbus.MessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class MessageImpl<U, V> implements Message<V> {
  private static final Logger log = LoggerFactory.getLogger(MessageImpl.class);

  protected MessageCodec<U, V> messageCodec;
  protected final EventBusImpl bus;
  protected String address;
  protected String replyAddress;
  protected Map<String, String> headers;
  protected U sentBody;
  protected V receivedBody;
  protected boolean send;
  protected Object trace;

  public MessageImpl(EventBusImpl bus) {
    this.bus = bus;
  }

  public MessageImpl(String address, Map<String, String> headers, U sentBody,
                     MessageCodec<U, V> messageCodec,
                     boolean send, EventBusImpl bus) {
    this.messageCodec = messageCodec;
    this.address = address;
    this.headers = headers;
    this.sentBody = sentBody;
    this.send = send;
    this.bus = bus;
  }

  protected MessageImpl(MessageImpl<U, V> other) {
    this.bus = other.bus;
    this.address = other.address;
    this.replyAddress = other.replyAddress;
    this.messageCodec = other.messageCodec;
    if (other.headers != null) {
      var entries = other.headers.entrySet();
      this.headers = new HashMap<>();
      for (var entry : entries) {
        this.headers.put(entry.getKey(), entry.getValue());
      }
    }
    if (other.sentBody != null) {
      this.sentBody = other.sentBody;
      this.receivedBody = messageCodec.transform(other.sentBody);
    }
    this.send = other.send;
  }

  public MessageImpl<U, V> copyBeforeReceive() {
    return new MessageImpl<>(this);
  }

  @Override
  public String address() {
    return address;
  }

  @Override
  public Map<String, String> headers() {
    // Lazily decode headers
    if (headers == null) {
      headers = new HashMap<>();
    }
    return headers;
  }

  @Override
  public V body() {
    if (receivedBody == null && sentBody != null) {
      receivedBody = messageCodec.transform(sentBody);
    }
    return receivedBody;
  }

  @Override
  public String replyAddress() {
    return replyAddress;
  }

  @Override
  public void reply(Object message, DeliveryOptions options) {
    if (replyAddress != null) {
      var reply = createReply(message, options);
      bus.sendReply(reply, options, null);
    }
  }

  @Override
  public <R> Future<Message<R>> replyAndRequest(Object message, DeliveryOptions options) {
    if (replyAddress != null) {
      var reply = createReply(message, options);
      ReplyHandler<R> handler = bus.createReplyHandler(reply, false, options);
      bus.sendReply(reply, options, handler);
      return handler.result();
    } else {
      throw new IllegalStateException();
    }
  }

  protected MessageImpl createReply(Object message, DeliveryOptions options) {
    var reply = bus.createMessage(true, replyAddress, options.getHeaders(), message, options.getCodecName());
    reply.trace = trace;
    return reply;
  }

  @Override
  public boolean isSend() {
    return send;
  }

  public void setReplyAddress(String replyAddress) {
    this.replyAddress = replyAddress;
  }

  public MessageCodec<U, V> codec() {
    return messageCodec;
  }

  protected boolean isLocal() {
    return true;
  }
}
