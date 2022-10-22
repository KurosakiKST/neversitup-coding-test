package com.neversitup.codingtest.testing

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PriceCheckUtilTest {

    @Test
    fun `valid amount returns true`() {
        val result = PriceCheckUtil.validatePriceCheckInput(
            "100.25"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `zero amount returns false`() {
        val result = PriceCheckUtil.validatePriceCheckInput(
            "0.0"
        )
        assertThat(result).isFalse()
    }
}