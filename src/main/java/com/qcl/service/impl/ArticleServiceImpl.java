package com.qcl.service.impl;

import com.qcl.dataobject.Article;
import com.qcl.repository.ArticleRepository;
import com.qcl.service.ArticleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 类目
 * Created by qcl on 2017/12/16.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository repository;

    //获取文章列表
    @Override
    public List<Article> findAll() {
        return repository.findAll();
    }

    //获取单篇文章
    @Override
    public Article findOne(long aid) {
        return repository.findById(aid).get();
    }


}
