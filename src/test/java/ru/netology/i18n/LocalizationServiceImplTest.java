package ru.netology.i18n;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import ru.netology.entity.Country;

public class LocalizationServiceImplTest {

    @ParameterizedTest
    @MethodSource("lcaleTestDataProvider")
    public void locale_test(Country country, String expected) {
        LocalizationService localizationService = new LocalizationServiceImpl();

        String actual = localizationService.locale(country);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> lcaleTestDataProvider() {
        String msgEn = "Welcome";
        String msgRu = "Добро пожаловать";

        return Stream.of(
            Arguments.of(Country.RUSSIA, msgRu),
            Arguments.of(Country.USA, msgEn),
            Arguments.of(Country.BRAZIL, msgEn),
            Arguments.of(Country.GERMANY, msgEn)
        );
    }
}
