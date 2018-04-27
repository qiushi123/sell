package com.qcl.service;

import com.qcl.dataobject.Article;

import java.util.List;

/**
 * Created by qcl on 2018/4/27.
 */
public interface ArticleService {
    //获取所有文章
    List<Article> findAll();

    //获取单篇文章
    Article findOne(long aid);
}
