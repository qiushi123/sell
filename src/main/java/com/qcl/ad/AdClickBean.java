package com.qcl.ad;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 用户广告点击排名的bean
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AdClickBean {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动生成
    private Long id;

    private String dateTime;
    private String openid;
    private String name;
    private String city;
    private Integer clickNum;


}
