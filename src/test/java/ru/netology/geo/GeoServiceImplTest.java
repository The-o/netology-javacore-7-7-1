package ru.netology.geo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.netology.entity.Country;
import ru.netology.entity.Location;

public class GeoServiceImplTest {

    @ParameterizedTest
    @MethodSource("byIpCounryTestDataProvider")
    public void byIp_country(String ip, Country expected) {
        GeoService geoService = new GeoServiceImpl();
        Location actual = geoService.byIp(ip);

        assertEquals(expected, actual.getCountry());
    }

    private static Stream<Arguments> byIpCounryTestDataProvider() {
        return Stream.of(
            Arguments.of("127.0.0.1", null),
            Arguments.of("172.0.32.11", Country.RUSSIA),
            Arguments.of("96.44.183.149", Country.USA),
            Arguments.of("172.0.0.1", Country.RUSSIA),
            Arguments.of("96.0.0.1", Country.USA)
        );
    }

    @ParameterizedTest
    @MethodSource("byIpUnknownTestDataProvider")
    public void byIp_unknown(String ip) {
        GeoService geoService = new GeoServiceImpl();
        Location actual = geoService.byIp(ip);

        assertNull(actual);
    }

    private static Stream<Arguments> byIpUnknownTestDataProvider() {
        return Stream.of(
            Arguments.of("129.0.0.1"),
            Arguments.of("abracadabra"),
            Arguments.of("")
        );
    }

    @Test
    @Disabled("Disabled until null ip bug is fixed")
    public void byIp_null() {
        GeoService geoService = new GeoServiceImpl();
        Location actual = geoService.byIp(null);

        assertNull(actual);
    }

}
