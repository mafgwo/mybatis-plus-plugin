package org.py.plugin.mybatisplus

import com.baomidou.plugin.idea.mybatisx.codegenerator.utils.ColUtil
import spock.lang.Specification

class ColUtilsTest extends Specification {

    def "column to java"() {
        expect:
        ColUtil.columnToJava(column) == result

        where:
        column           | result
        "tinyint"        | "Integer"
        "tinyint"        | "Integer"
        "smallint"       | "Integer"
        "mediumint"      | "Integer"
        "int"            | "Integer"
        "integer"        | "Integer"

        "bigint"         | "Long"

        "float"          | "Float"

        "double"         | "Double"

        "decimal"        | "BigDecimal"

        "bit"            | "Boolean"

        "char"           | "String"
        "varchar"        | "String"
        "tinytext"       | "String"
        "text"           | "String"
        "mediumtext"     | "String"
        "longtext"       | "String"

        "date"           | "Timestamp"
        "datetime"       | "Timestamp"
        "timestamp"      | "Timestamp"

        ""               | "unknownType"
        "i am an apple " | "unknownType"
        null             | "unknownType"

    }


}
