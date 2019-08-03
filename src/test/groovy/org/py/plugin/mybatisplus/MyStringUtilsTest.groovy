package org.py.plugin.mybatisplus

import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.MyStringUtils
import spock.lang.Specification

class MyStringUtilsTest extends Specification {
    def "test inString"() {
        def result
        when:
        result = MyStringUtils.inString("123", "123", "xxx")

        then:
        result
    }

    def "toCamelCase test"() {
        expect:
        MyStringUtils.toCamelCase(column) == camelCaseStr

        where:
        column        | camelCaseStr
        "_apple"      | "Apple"
        "apple"       | "apple"
        "apple_color" | "appleColor"
        "_a_b_c"      | "ABC"
        "ABC"         | "abc"
        "AaBbCc"      | "aabbcc"

    }

    def "toCapitalizeCamelCase test"() {
        expect:
        MyStringUtils.toCapitalizeCamelCase(column) == camelCaseStr

        where:
        column        | camelCaseStr
        "_apple"      | "Apple"
        "apple"       | "Apple"
        "apple_color" | "AppleColor"
        "_a_b_c"      | "ABC"
        "ABC"         | "Abc"
        "AaBbCc"      | "Aabbcc"
    }

    def "toUnderScoreCase test"() {
        expect:
        MyStringUtils.toUnderScoreCase(camelCaseStr) == column

        where:
        camelCaseStr | column
        "_apple"     | "_apple"
        "apple"      | "apple"
        "AppleColor" | "apple_color"
        "_a_b_c"     | "_a_b_c"
        "ABC"        | "a_bc"
        "AaBbCc"     | "aa_bb_cc"
    }

}
