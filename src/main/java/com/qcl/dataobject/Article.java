package com.qcl.dataobject;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/**
 * 类目对应的bean
 * Created by qcl on 2017/12/16.
 */
@Entity
@DynamicUpdate
@Data//避免重复写get和set，tostring
public class Article {
    @Id
    @GeneratedValue
    private Long aid;

    private String title;
    private String descStr;
    private String content;
    private String readNum;//浏览量
    private String createTime;//创建时间
}
