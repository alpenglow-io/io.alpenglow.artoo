/*
 * Copyright (c) 2011-2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.artoo.parry.eventbus;

import java.util.Map;
import java.util.Objects;

/**
 * Delivery options are used to configure message delivery.
 * <p>
 * Delivery options allow to configure delivery timeout and message codec name, and to provide any headers
 * that you wish to send with the message.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class DeliveryOptions {

  /**
   * The default send timeout.
   */
  public static final long DEFAULT_TIMEOUT = 30 * 1000;

  /**
   * Whether the message should be delivered to local consumers only by default = false.
   */
  public static final boolean DEFAULT_LOCAL_ONLY = false;

  private long timeout = DEFAULT_TIMEOUT;
  private String codecName;
  private Map<String, String> headers;
  private boolean localOnly = DEFAULT_LOCAL_ONLY;

  /**
   * Default constructor
   */
  public DeliveryOptions() {
  }

  /**
   * Get the send timeout.
   * <p>
   * When sending a message with a response handler a send timeout can be provided. If no response is received
   * within the timeout the handler will be called with a failure.
   *
   * @return  the value of send timeout
   */
  public long getSendTimeout() {
    return timeout;
  }

  /**
   * Set the send timeout.
   *
   * @param timeout  the timeout value, in ms.
   * @return  a reference to this, so the API can be used fluently
   */
  public DeliveryOptions setSendTimeout(long timeout) {
    assert timeout >= 1;
    this.timeout = timeout;
    return this;
  }

  /**
   * Get the codec name.
   * <p>
   * When sending or publishing a message a codec name can be provided. This must correspond with a previously registered
   * message codec. This allows you to send arbitrary objects on the event bus (e.g. POJOs).
   *
   * @return  the codec name
   */
  public String getCodecName() {
    return codecName;
  }

  /**
   * Set the codec name.
   *
   * @param codecName  the codec name
   * @return  a reference to this, so the API can be used fluently
   */
  public DeliveryOptions setCodecName(String codecName) {
    this.codecName = codecName;
    return this;
  }

  public DeliveryOptions addHeader(String key, String value) {
    Objects.requireNonNull(key, "no null key accepted");
    Objects.requireNonNull(value, "no null value accepted");
    headers.put(key, value);
    return this;
  }

  public DeliveryOptions setHeaders(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * @return whether the message should be delivered to local consumers only
   */
  public boolean isLocalOnly() {
    return localOnly;
  }

  /**
   * Whether a message should be delivered to local consumers only. Defaults to {@code false}.
   *
   * <p>
   * <strong>This option is effective in clustered mode only and does not apply to reply messages</strong>.
   *
   * @param localOnly {@code true} to deliver to local consumers only, {@code false} otherwise
   * @return a reference to this, so the API can be used fluently
   */
  public DeliveryOptions setLocalOnly(boolean localOnly) {
    this.localOnly = localOnly;
    return this;
  }
}
