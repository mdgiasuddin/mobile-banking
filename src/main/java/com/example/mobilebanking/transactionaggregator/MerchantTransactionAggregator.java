package com.example.mobilebanking.transactionaggregator;

import com.example.mobilebanking.common.config.JsonSerde;
import com.example.mobilebanking.common.dto.MerchantSettlementEvent;
import com.example.mobilebanking.common.dto.TransactionEvent;
import com.example.mobilebanking.transactionaggregator.dto.MerchantAggregate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Configuration
@EnableKafkaStreams
@RequiredArgsConstructor
public class MerchantTransactionAggregator {

    private final ObjectMapper objectMapper;

    @Bean
    public KStream<String, TransactionEvent> topology(StreamsBuilder builder) {

        JsonSerde<TransactionEvent> transactionSerde = new JsonSerde<>(TransactionEvent.class, objectMapper);
        JsonSerde<MerchantAggregate> aggregateSerde = new JsonSerde<>(MerchantAggregate.class, objectMapper);
        JsonSerde<MerchantSettlementEvent> settlementSerde = new JsonSerde<>(MerchantSettlementEvent.class, objectMapper);

        // 🔹 Source
        KStream<String, TransactionEvent> source =
                builder.stream(
                        "transaction",
                        Consumed.with(Serdes.String(), transactionSerde)
                );

        source
                .filter((k, tx) -> tx != null && tx.isP2M())
                .groupBy(
                        (k, tx) -> String.valueOf(tx.getToAccountId()),
                        Grouped.with(Serdes.String(), transactionSerde)
                )
                .windowedBy(
                        TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1))
                )
                .aggregate(
                        MerchantAggregate::new,
                        (merchantId, tx, agg) -> agg.add(tx),
                        Materialized.<String, MerchantAggregate, WindowStore<Bytes, byte[]>>as(
                                        "merchant-transaction-aggregate-store"
                                )
                                .withKeySerde(Serdes.String())
                                .withValueSerde(aggregateSerde)
                )
                .suppress(
                        Suppressed.untilWindowCloses(
                                Suppressed.BufferConfig.unbounded()
                        )
                )
                .toStream()
                .map((windowedKey, agg) -> {
                    MerchantSettlementEvent merchantSettlement =
                            new MerchantSettlementEvent(
                                    UUID.randomUUID(),
                                    windowedKey.key(),
                                    agg.getTotalAmount(),
                                    agg.getCount(),
                                    windowedKey.window().endTime()
                            );

                    return KeyValue.pair(windowedKey.key(), merchantSettlement);
                })
                .to(
                        "merchant-settlement",
                        Produced.with(Serdes.String(), settlementSerde)
                );

        return source;

    }

}