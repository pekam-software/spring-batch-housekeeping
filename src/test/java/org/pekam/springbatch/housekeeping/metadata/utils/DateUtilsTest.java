package org.pekam.springbatch.housekeeping.metadata.utils;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DateUtilsTest {

    @ParameterizedTest
    @MethodSource(value = "expectedDates")
    void addDays(int daysToAdd, Instant expectedDate) {
        // given
        Date initialDate = Date.from(
                LocalDate.of(2024, 3, 1)
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
        );

        // when
        Date dateWithDaysAdded = DateUtils.addDays(initialDate, daysToAdd);

        // then
        assertThat(dateWithDaysAdded).isInSameDayAs(Date.from(expectedDate));
    }

    private static Stream<Arguments> expectedDates() {
        return Stream.of(
                Arguments.of(
                        Named.of(
                                "one day",
                                1
                        ),
                        LocalDate.of(2024, 3, 2)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                ),
                Arguments.of(
                        Named.of(
                                "10 days",
                                10
                        ),
                        LocalDate.of(2024, 3, 11)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                ),
                Arguments.of(
                        Named.of(
                                "one month",
                                30
                        ),
                        LocalDate.of(2024, 3, 31)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                ),
                Arguments.of(
                        Named.of(
                                "100 days",
                                100
                        ),
                        LocalDate.of(2024, 6, 9)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                ),
                Arguments.of(
                        Named.of(
                                "one year",
                                365
                        ),
                        LocalDate.of(2025, 3, 1)
                                .atStartOfDay()
                                .toInstant(ZoneOffset.UTC)
                )
        );
    }

}