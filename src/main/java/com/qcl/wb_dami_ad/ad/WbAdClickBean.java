package com.qcl.wb_dami_ad.ad;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * Created by qcl on 2018/6/26.
 * 外包大米---用户广告点击排名的bean
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class WbAdClickBean {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动生成
    private Long id;

    private String dateTime;
    private String name;
    private String city;
    private Integer clickNum;
}
