package fobo66.exchangecourcesbelarus.util

import org.junit.Assert.assertEquals
import org.junit.Test

class BankNameNormalizerTest {

  private val bankNameNormalizer = BankNameNormalizer()

  @Test
  fun `bank name inside typographic quotes`() {
    val bankName = "ЗАО «БСБ Банк»"
    assertEquals("БСБ Банк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name inside regular quotes`() {
    val bankName = "ЗАО \"БСБ Банк\""
    assertEquals("БСБ Банк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name inside multiple quotes`() {
    val bankName = "ЗАО «Статусбанк» (бывш. \"Евроторгинвестбанк\")"
    assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name inside multiple pairs of typographical quotes`() {
    val bankName = "ЗАО «Статусбанк» (бывш. «Евроторгинвестбанк»)"
    assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name inside multiple pairs of regular quotes`() {
    val bankName = "ЗАО \"Статусбанк\" (бывш. \"Евроторгинвестбанк\")"
    assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name with multiple nested quotes starting with typographical`() {
    val bankName = "ЗАО «Статусбанк \"Евроторгинвестбанк\"»"
    assertEquals("Статусбанк Евроторгинвестбанк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name with multiple nested quotes starting with regular`() {
    val bankName = "ЗАО \"Статусбанк «Евроторгинвестбанк»\""
    assertEquals("Статусбанк Евроторгинвестбанк", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `real bank name with nested imbalanced quotes`() {
    val bankName = "ЗАО «Банк «Решение»"
    assertEquals("Банк Решение", bankNameNormalizer.normalize(bankName))
  }

  @Test
  fun `bank name without quotes`() {
    val bankName = "Статусбанк"
    assertEquals("Статусбанк", bankNameNormalizer.normalize(bankName))
  }
}
