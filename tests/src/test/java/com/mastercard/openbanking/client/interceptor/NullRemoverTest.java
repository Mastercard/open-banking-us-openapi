package com.mastercard.openbanking.client.interceptor;

import org.junit.jupiter.api.Test;

import static client.interceptor.NullRemoverResponseInterceptor.NullRemover;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NullRemoverTest {

    @Test
    void removeNullFields() {
        assertEquals("", NullRemover.removeNullFields(""));
        assertEquals("{}", NullRemover.removeNullFields("{}"));
        assertEquals("{}", NullRemover.removeNullFields("{\"field\":null}"));
        assertEquals("{\"field2\":\"dummy\"}", NullRemover.removeNullFields("{\"field1\":null, \"field2\":\"dummy\", \"field3\":null}"));
        assertEquals("[]", NullRemover.removeNullFields("[]"));
        assertEquals("[{},{\"field\":\"dummy\"},{}]", NullRemover.removeNullFields("[{\"field\":null}, {\"field\":\"dummy\"}, {\"field\":null}]"));
        assertEquals("{\"field\":[{}]}", NullRemover.removeNullFields("{\"field\": [{\"field\": null}]}"));
    }
}
