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

import io.artoo.parry.ContextInternal;
import io.artoo.parry.async.Future;
import io.artoo.parry.async.Handler;
import io.artoo.parry.async.Promise;
import io.artoo.parry.eventbus.Message;
import io.artoo.parry.eventbus.ReplyException;
import io.artoo.parry.eventbus.ReplyFailure;

class ReplyHandler<T> extends HandlerRegistration<T> implements Handler<Long> {

  private final EventBusImpl eventBus;
  private final ContextInternal context;
  private final Promise<Message<T>> result;
  //private final long timeoutID;
  private final long timeout;
  private final boolean src;
  private final String repliedAddress;
  Object trace;

  ReplyHandler(EventBusImpl eventBus, ContextInternal context, String address, String repliedAddress, boolean src, long timeout) {
    super(context, eventBus, address, src);
    this.eventBus = eventBus;
    this.context = context;
    this.result = context.promise();
    this.src = src;
    this.repliedAddress = repliedAddress;
    //this.timeoutID = eventBus.vertx.setTimer(timeout, this);
    this.timeout = timeout;
  }

  Future<Message<T>> result() {
    return result.future();
  }

  void fail(ReplyException failure) {
    if (eventBus.vertx.cancelTimer(timeoutID)) {
      unregister();
      doFail(failure);
    }
  }

  private void doFail(ReplyException failure) {
    result.fail(failure);
  }

  @Override
  public void handle(Long id) {
    unregister();
    doFail(new ReplyException(ReplyFailure.TIMEOUT, "Timed out after waiting " + timeout + "(ms) for a reply. address: " + address + ", repliedAddress: " + repliedAddress));
  }

  @Override
  protected boolean doReceive(Message<T> reply) {
    dispatch(null, reply, context);
    return true;
  }

  void register() {
    register(repliedAddress, true, null);
  }

  @Override
  protected void dispatch(Message<T> reply, ContextInternal context, Handler<Message<T>> handler /* null */) {
    if (eventBus.vertx.cancelTimer(timeoutID)) {
      unregister();
      if (reply.body() instanceof ReplyException) {
        doFail((ReplyException) reply.body());
      } else {
        trace(reply, null);
        result.complete(reply);
      }
    }
  }
}
