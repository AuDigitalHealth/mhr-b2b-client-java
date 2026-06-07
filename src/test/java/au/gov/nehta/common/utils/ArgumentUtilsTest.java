package au.gov.nehta.common.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArgumentUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void checkNotNull_rejectsNull() {
        ArgumentUtils.checkNotNull((Object) null, "value");
    }

    @Test
    public void checkNotNull_acceptsValue() {
        ArgumentUtils.checkNotNull("ok", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNotNullNorBlank_rejectsBlank() {
        ArgumentUtils.checkNotNullNorBlank("   ", "field");
    }

    @Test
    public void checkNotNullNorBlank_acceptsTrimmedValue() {
        ArgumentUtils.checkNotNullNorBlank(" value ", "field");
    }

    @Test
    public void isNullOrBlank_handlesNullBlankAndValue() {
        assertTrue(ArgumentUtils.isNullOrBlank(null));
        assertTrue(ArgumentUtils.isNullOrBlank("  "));
        assertFalse(ArgumentUtils.isNullOrBlank("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMaxLength_rejectsLongValue() {
        ArgumentUtils.checkMaxLength("abcd", 3, "field");
    }

    @Test
    public void checkNotNullNorEmpty_acceptsCollection() {
        ArgumentUtils.checkNotNullNorEmpty(Arrays.asList("x"), "collection required");
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkNotNullNorEmpty_rejectsEmptyCollection() {
        ArgumentUtils.checkNotNullNorEmpty(Collections.emptyList(), "collection required");
    }
}
