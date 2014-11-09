package net.freelogue.recommender;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

public class CommonUtilTest {
    @Test
    public void testStringTrimLast(){
        assertThat(CommonUtil.stringTrimLast("World"),
                   equalTo("Worl"));
        assertThat(CommonUtil.stringTrimLast("World",2),
                equalTo("Wor"));
        assertThat(CommonUtil.stringTrimLast("World",3),
                equalTo("Wo"));
        assertThat(CommonUtil.stringTrimLast("World",5),
                equalTo(""));
        assertThat(CommonUtil.stringTrimLast("World",6),
                equalTo(""));
        assertThat(CommonUtil.stringTrimLast("World",60),
                equalTo(""));
        assertThat(CommonUtil.stringTrimLast(""),
                equalTo(""));
        assertThat(CommonUtil.stringTrimLast("",5),
                equalTo(""));
        assertThat(CommonUtil.stringTrimLast(null),
                equalTo(null));
    }
}