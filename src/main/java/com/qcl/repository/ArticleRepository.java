package com.qcl.repository;

import com.qcl.dataobject.Article;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 类目
 * jpa用来操作bean进行数据库操作的dao
 * Created by qcl on 2017/12/16.
 */
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
