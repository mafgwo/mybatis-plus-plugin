package com.baomidou.plugin.idea.mybatisx.codegenerator.domain;

import com.baomidou.mybatisplus.annotation.IdType;

public class IdTypeObj {
    public IdTypeObj(IdType idType, String remark) {
        this.idType = idType;
        this.remark = remark;
    }

    private IdType idType;
    private String remark;

    public IdType getIdType() {
        return idType;
    }

    public void setIdType(IdType idType) {
        this.idType = idType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
