package org.py.plugin.mybatisplus

import com.baomidou.plugin.idea.mybatisx.codegenerator.db.BaseDb
import com.baomidou.plugin.idea.mybatisx.codegenerator.db.MysqlDb
import com.baomidou.plugin.idea.mybatisx.codegenerator.db.OracleDb
import spock.lang.Specification

class DBTest extends Specification {
    BaseDb db

    def "test Mysql"() {
        when:
        db = new MysqlDb()

        then:
        db.dbName() == "mysql"
        db.getJdbcDriver() == "com.mysql.cj.jdbc.Driver"
    }

    def "test Oracle"() {
        when:
        db = new OracleDb()

        then:
        db.dbName() == "mysql"
        db.getJdbcDriver() == "com.mysql.cj.jdbc.Driver"
    }


}
