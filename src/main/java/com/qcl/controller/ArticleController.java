package com.qcl.controller;

import com.qcl.api.ResultApi;
import com.qcl.dataobject.Article;
import com.qcl.service.ArticleService;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文章
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService service;

    //文章列表
    @GetMapping("/list")
    public ResultApi list() {
        List<Article> lists = service.findAll();
        return ResultApiUtil.success(lists);
    }

    //单篇文章
    @GetMapping("/one")
    public ResultApi findOne(@RequestParam(name = "aid") long aid) {
        Article article = service.findOne(aid);
        return ResultApiUtil.success(article);
    }

}
