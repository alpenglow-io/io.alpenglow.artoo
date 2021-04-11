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

package io.artoo.parry.stream.impl;

import io.artoo.parry.async.Handler;
import io.artoo.parry.stream.Pump;
import io.artoo.parry.stream.ReadStream;
import io.artoo.parry.stream.WriteStream;

import java.util.Objects;

public class PumpImpl<T> implements Pump {

  private final ReadStream<T> readStream;
  private final WriteStream<T> writeStream;
  private final Handler<T> dataHandler;
  private final Handler<Void> drainHandler;
  private int pumped;

  /**
   * Create a new {@code Pump} with the given {@code ReadStream} and {@code WriteStream}. Set the write queue max size
   * of the write stream to {@code maxWriteQueueSize}
   */
  public PumpImpl(ReadStream<T> rs, WriteStream<T> ws, int maxWriteQueueSize) {
    this(rs, ws);
    this.writeStream.setWriteQueueMaxSize(maxWriteQueueSize);
  }

  public PumpImpl(ReadStream<T> rs, WriteStream<T> ws) {
    Objects.requireNonNull(rs);
    Objects.requireNonNull(ws);
    this.readStream = rs;
    this.writeStream = ws;
    drainHandler = v-> readStream.resume();
    dataHandler = data -> {
      writeStream.write(data);
      incPumped();
      if (writeStream.writeQueueFull()) {
        readStream.pause();
        writeStream.drainHandler(drainHandler);
      }
    };
  }

  /**
   * Set the write queue max size to {@code maxSize}
   */
  @Override
  public PumpImpl setWriteQueueMaxSize(int maxSize) {
    writeStream.setWriteQueueMaxSize(maxSize);
    return this;
  }

  /**
   * Start the Pump. The Pump can be started and stopped multiple times.
   */
  @Override
  public PumpImpl start() {
    readStream.handler(dataHandler);
    return this;
  }

  /**
   * Stop the Pump. The Pump can be started and stopped multiple times.
   */
  @Override
  public PumpImpl stop() {
    writeStream.drainHandler(null);
    readStream.handler(null);
    return this;
  }

  /**
   * Return the total number of elements pumped by this pump.
   */
  @Override
  public synchronized int numberPumped() {
    return pumped;
  }

  // Note we synchronize as numberPumped can be called from a different thread however incPumped will always
  // be called from the same thread so we benefit from bias locked optimisation which should give a very low
  // overhead
  private synchronized void incPumped() {
    pumped++;
  }
}
