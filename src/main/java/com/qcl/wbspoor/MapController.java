package com.qcl.wbspoor;

import com.qcl.api.ResultApi;
import com.qcl.enums.ResultEnum;
import com.qcl.exception.SellException;
import com.qcl.utils.ResultApiUtil;
import com.qcl.utils.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 足迹
 */

@RestController
@RequestMapping("/map")
@Slf4j
public class MapController {

    @Autowired
    Repository repository;

    //获取某一条足迹
    @GetMapping("/spoorid")
    public ResultApi findOne(@RequestParam(name = "spoorid") Long spoorid) {
        Spoor spoor = repository.findBySpoorid(spoorid);
        return ResultApiUtil.success(spoor);
    }

    /**
     * 保存足迹
     */
    @PostMapping("/save")
    public ResultApi save(@Valid SpoorForm spoorForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("[注册用户] 参数不正确，runnerForm={}", spoorForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode()
                    , bindingResult.getFieldError().getDefaultMessage());
        }
        Spoor bean = new Spoor();
        bean.setUserid(spoorForm.getUserid());
        bean.setStartTime(spoorForm.getStartTime());
        bean.setEndTime(spoorForm.getEndTime());
        bean.setDuration(spoorForm.getDuration());
        bean.setLenght(spoorForm.getLenght());
        bean.setCity(spoorForm.getCity());
        bean.setYear(Utils.getSysYear());
        bean.setStartLatitude(spoorForm.getStartLatitude());
        bean.setStartlongitude(spoorForm.getStartlongitude());
        bean.setEndLatitude(spoorForm.getEndLatitude());
        bean.setEndlongitude(spoorForm.getEndlongitude());

        bean.setMessage(spoorForm.getMessage());

        Spoor bean1 = repository.save(bean);
        return ResultApiUtil.success(bean1);
    }

    /*
    * 获取某一用户，某一年的足迹列表
    * */
    @PostMapping("/getList")
    public ResultApi getList(@RequestParam(name = "userid") Long userid,
                             @RequestParam(name = "year") String year) {
        if (userid == 0 || userid == null) {
            throw new SellException(ResultEnum.USER_NO_HAVE);
        }
        //查询条件构造
        Specification<Spoor> spec = (Specification<Spoor>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.equal(root.get("userid"), userid));
            list.add(cb.equal(root.get("year"), year));

            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        return ResultApiUtil.success(repository.findAll(spec));
    }


    /*
    * 用户相关
    * */
    @Autowired
    private RepositoryUser repositoryUser;


    @GetMapping("/getUser")
    public ResultApi getUser(@RequestParam(name = "openid") String openid) {
        if (StringUtils.isEmpty(openid)) {
            throw new SellException(ResultEnum.USER_NO_HAVE);
        }
        return ResultApiUtil.success(repositoryUser.findByOpenid(openid));
    }

    @PostMapping("/saveUser")
    public ResultApi saveUser(@RequestParam(name = "openid") String openid) {
        if (StringUtils.isEmpty(openid)) {
            throw new SellException(ResultEnum.USER_NO_HAVE);
        }
        SpoorUser user = new SpoorUser();
        user.setOpenid(openid);
        return ResultApiUtil.success(repositoryUser.save(user));
    }

}
