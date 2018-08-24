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
 * 用户每周广告点击排名的bean
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AdClickWeekBean {
    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动生成
    private Long id;

    private String weekTime;//第几月的第几周
    private String name;
    private String city;
    private Integer clickAdNum;//广告点击量
    private Integer clickVideoNum;//视频广告点击量
    private String salary;//每周每人工资


}
