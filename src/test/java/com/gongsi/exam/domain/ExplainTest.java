package com.gongsi.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gongsi.exam.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExplainTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Explain.class);
        Explain explain1 = new Explain();
        explain1.setId(1L);
        Explain explain2 = new Explain();
        explain2.setId(explain1.getId());
        assertThat(explain1).isEqualTo(explain2);
        explain2.setId(2L);
        assertThat(explain1).isNotEqualTo(explain2);
        explain1.setId(null);
        assertThat(explain1).isNotEqualTo(explain2);
    }
}
