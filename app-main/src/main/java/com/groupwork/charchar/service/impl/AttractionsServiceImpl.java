package com.groupwork.charchar.service.impl;

import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.groupwork.charchar.dao.AttractionsDao;
import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;


@Service("attractionsService")
public class AttractionsServiceImpl extends ServiceImpl<AttractionsDao, AttractionsEntity> implements AttractionsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttractionsEntity> page = this.page(
                new Query<AttractionsEntity>().getPage(params),
                new QueryWrapper<AttractionsEntity>()
        );

        return new PageUtils(page);
    }

}