package com.example

import com.example.data.MigraCatalogData
import com.example.data.fx.FxRepository
import com.example.model.AppLang
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun fxCrossRateAndConversion_computesAccurately() {
        val rates = MigraCatalogData.defaultRatesPerRub()
        val cross = FxRepository.getCrossRate("RUB", "INR", rates)
        assertEquals(0.935, cross, 0.0001)
        val formatted = FxRepository.formatFxMoney(10000.0 * cross, "INR")
        assertEquals("9,350.00", formatted)
    }

    @Test
    fun qrMatrix_is21x21WithFinderPatterns() {
        val m = MigraCatalogData.generateQrMatrix("TQn9Y2khEsLJW1ChVWFMSMeRDow5KcbLSE:5.39:TRC-20 (Tron)")
        assertEquals(21, m.size)
        assertEquals(21, m[0].size)
        // Top-left finder corner
        assertTrue(m[0][0])
        assertTrue(m[3][3])
        // Top-right finder corner
        assertTrue(m[0][20])
        // Bottom-left finder corner
        assertTrue(m[20][0])
    }

    @Test
    fun bilingualLocalization_translatesProfessionsAndCountries() {
        assertEquals("Сварщик", MigraCatalogData.profName("Welder", AppLang.RU))
        assertEquals("Welder", MigraCatalogData.profName("Welder", AppLang.EN))
        assertEquals("Узбекистан", MigraCatalogData.countryName("UZ", AppLang.RU))
    }
}
