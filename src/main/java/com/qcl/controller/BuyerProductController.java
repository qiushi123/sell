package com.qcl.controller;

import com.qcl.api.ProductApi;
import com.qcl.api.ProductInfoApi;
import com.qcl.api.ResultApi;
import com.qcl.dataobject.ProductCategory;
import com.qcl.dataobject.ProductInfo;
import com.qcl.enums.ResultEnum;
import com.qcl.service.impl.CategoryServiceImpl;
import com.qcl.service.impl.ProductInfoServiceImpl;
import com.qcl.utils.ResultApiUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by qcl on 2017/12/17.
 * 要返回的json如下
 * {
 * "code": 100,
 * "msg": "成功",
 * "data": [
 * {
 * "name": "粥品类",
 * "type": 2,
 * "foods": [
 * {
 * "id": "1",
 * "name": "小米粥",
 * "price": 3.2,
 * "desc": "很好喝",
 * "icon": "http://xxxxx.jpg"
 * },
 * {
 * "id": "2",
 * "name": "小米粥2",
 * "price": 3.2,
 * "desc": "很好喝2",
 * "icon": "http://xxxxx.jpg"
 * }
 * ]
 * }
 * ]
 * }
 */

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoServiceImpl productInfoService;//查询所有商品
    @Autowired
    private CategoryServiceImpl categoryService;//查询所有类目

    @GetMapping("/list")
    public ResultApi list() {
        //1，查询所有上架商品
        List<ProductInfo> productInfoList = productInfoService.findAll();
        //2，查询所有类目（一次性查询）
        //java8的lambda表达式，获取所有类目
        List<Integer> typeList = productInfoList
                .stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());
        //如果上面java8的lambda不熟悉可以用传统遍历方法，但是建议用上面java8
        //        List<Integer> typeList = new ArrayList<>();
        //        for (ProductInfo info : productInfoList) {
        //            typeList.add(info.getCategoryType());
        //        }
        //所有在架商品的类目
        List<ProductCategory> categoryList = categoryService.findByCategoryTypeIn(typeList);
        //3，数据拼接
        List<ProductApi> productApis = new ArrayList<>();
        for (ProductCategory category : categoryList) {//遍历所有类
            //每个类目
            ProductApi productApi = new ProductApi();
            productApi.setCategoryType(category.getCategoryType());
            productApi.setCategoryName(category.getCategoryName());

            //遍历类目下的商品集合
            List<ProductInfoApi> productInfoApis = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {//遍历所有商品
                if (productInfo.getCategoryType() == category.getCategoryType()) {
                    ProductInfoApi productInfoApi = new ProductInfoApi();
                    //复制对象数据
                    BeanUtils.copyProperties(productInfo, productInfoApi);
                    productInfoApis.add(productInfoApi);
                }
            }
            productApi.setCategoryData(productInfoApis);
            productApis.add(productApi);
        }


        return ResultApiUtil.success(ResultEnum.RESULT_OK, productApis);
    }

    @PostMapping("/add")
    public ProductInfo add(@RequestParam(name = "productId") String productId,
                           @RequestParam(name = "productName") String productName) {
        ProductInfo info = new ProductInfo();
        info.setProductId(productId);
        info.setProductName(productName);
        info.setProductPrice(new BigDecimal(3.2));
        info.setProductStock(100);
        info.setProductDescription("很好喝12");
        info.setProductIcon("http://xxxxx.jpg");
        info.setCategoryType(2);
        info.setProductStatus(0);
        return productInfoService.save(info);
    }

    /**
     * 接收的是如下json字符串，如果id存在就更新内容{
     * "productId": "10",
     * "productName": "奶茶100",
     * "productPrice": 6,
     * "productStock": 10,
     * "productDescription": "很好喝10",
     * "productIcon": "http://xxxxx.jpg",
     * "productType": null,
     * "categoryType": 2,
     * "productStatus": 0,
     * "createTime": null
     * }
     */
    @PostMapping("/addbody")
    public ProductInfo add(@RequestBody ProductInfo info) {
        return productInfoService.save(info);
    }
}
