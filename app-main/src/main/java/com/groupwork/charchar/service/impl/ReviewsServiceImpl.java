package com.groupwork.charchar.service.impl;

import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.groupwork.charchar.dao.ReviewsDao;
import com.groupwork.charchar.entity.ReviewsEntity;
import com.groupwork.charchar.service.ReviewsService;


@Service("reviewsService")
public class ReviewsServiceImpl extends ServiceImpl<ReviewsDao, ReviewsEntity> implements ReviewsService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ReviewsEntity> page = this.page(
                new Query<ReviewsEntity>().getPage(params),
                new QueryWrapper<ReviewsEntity>()
        );

        return new PageUtils(page);
    }

}