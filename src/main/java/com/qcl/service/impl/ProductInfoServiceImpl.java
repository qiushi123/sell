package com.qcl.service.impl;


import com.qcl.dataobject.ProductInfo;
import com.qcl.dto.CartDTO;
import com.qcl.enums.ProductStatusEnum;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.repository.ProductInfoRepository;
import com.qcl.service.ProductInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qcl on 2017/12/17.
 */
@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).get();
    }

    /*
    * 查询所有在架商品
    * */
    @Override
    public List<ProductInfo> findAll() {
        return repository.findByProductStatus(ProductStatusEnum.PRODUCT_UP.getCode());
    }

    /*
    * 分页查询
    * */
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /*
    * 添加和更新数据
    * */
    @Override
    public ProductInfo save(ProductInfo info) {
        return repository.save(info);
    }

    //加库存
    @Override
    @Transactional
    public void addcreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            //            ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            repository.save(productInfo);
        }
    }

    //减库存
    @Override
    @Transactional
    public void deletecreaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            //            ProductInfo productInfo = repository.findOne(cartDTO.getProductId());
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_ERROR_0);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);

        }

    }
}
