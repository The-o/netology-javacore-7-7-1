package ru.netology.sender;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

public class MessageSenderImplTest {

    @Test
    public void send_empty_ip() {
        String expected = "OK";

        GeoService geoService = mock(GeoService.class);

        LocalizationService localizationService = mock(LocalizationService.class);
        when(localizationService.locale(Country.USA)).thenReturn(expected);

        MessageSender sender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>(){{
            put("x-real-ip", "");
        }};
        String actual = sender.send(headers);

        assertEquals(expected, actual);
    }

    @Test
    @Disabled("Disabled until unknown location bug is fixed")
    public void send_unknown_country() {
        String expected = "OK";
        String fakeIp = "127.0.0.1";

        GeoService geoService = mock(GeoService.class);
        when(geoService.byIp(fakeIp)).thenReturn(null);

        LocalizationService localizationService = mock(LocalizationService.class);
        when(localizationService.locale(Country.USA)).thenReturn(expected);

        MessageSender sender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>(){{
            put("x-real-ip", fakeIp);
        }};
        String actual = sender.send(headers);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("senderWithIpTestDataProvider")
    public void send_with_ip(Location location) {
        String expected = "OK";
        String fakeIp = "127.0.0.1";

        GeoService geoService = mock(GeoService.class);
        when(geoService.byIp(fakeIp)).thenReturn(location);

        LocalizationService localizationService = mock(LocalizationService.class);
        when(localizationService.locale(location.getCountry())).thenReturn(expected);

        MessageSender sender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>(){{
            put("x-real-ip", fakeIp);
        }};
        String actual = sender.send(headers);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> senderWithIpTestDataProvider() {
        return Stream.of(
            Arguments.of(new Location(null, Country.BRAZIL, null, 0)),
            Arguments.of(new Location(null, Country.USA, null, 0)),
            Arguments.of(new Location(null, Country.RUSSIA, null, 0)),
            Arguments.of(new Location(null, Country.GERMANY, null, 0))
        );
    }
}
