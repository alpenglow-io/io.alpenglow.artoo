package re.artoo.lance.task;

import re.artoo.lance.func.TryConsumer1;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

public sealed interface MessageBus {
  static MessageBus messageBus() {
    return new MessageLoop(new ConcurrentHashMap<>(), new ConcurrentLinkedQueue<>());
  }

  <R extends Record> MessageBus on(Class<R> record, TryConsumer1<R> consumer);
}

final class MessageLoop implements MessageBus, Runnable {
  private final Map<Class<? extends Record>, Queue<TryConsumer1<? extends Record>>> consumers;
  private final Queue<? extends Record> records;

  MessageLoop(Map<Class<? extends Record>, Queue<TryConsumer1<? extends Record>>> consumers, Queue<? extends Record> records) {
    this.consumers = consumers;
    this.records = records;
  }

  @Override
  public <R extends Record> MessageBus on(Class<R> record, TryConsumer1<R> consumer) {
    if (consumers.containsKey(record)) {
      consumers.get(record).offer(consumer);
    } else {
      consumers.put(record, new ConcurrentLinkedQueue<>(Set.of(consumer)));
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  public <R extends Record> MessageBus fire(R record) {
    if (consumers.containsKey(record.getClass())) {
      consumers.get(record.getClass())
        .stream()
        .map(it -> (TryConsumer1<R>) it)
        .forEach(it -> it.accept(record));
    }
    return this;
  }

  @Override
  public void run() {
    while (true) {
      if (!records.isEmpty() && !consumers.isEmpty()) {
        try (var service = Executors.newVirtualThreadPerTaskExecutor()) {
          for (final var record : records) {
            var consumer = consumers.get(record.getClass());
            for (final var cons : consumer) {
              service.execute(() -> {
                //cons.accept(record);
              });
            }
          }
        }
      }
    }
  }
}
