package com.groupwork.charchar.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.group.charchar.utils.PageUtils;
import com.group.charchar.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.groupwork.charchar.entity.AttractionsEntity;
import com.groupwork.charchar.service.AttractionsService;


/**
 * @author wangyilong
 * @email 571379772@qq.com
 * @date 2023-03-24 15:33:03
 */
@RestController
@RequestMapping("product/attractions")
public class AttractionsController {
    @Autowired
    private AttractionsService attractionsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attractionsService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/near/location/{latitude}/{longitude}/{radius}")
    public @ResponseBody List<AttractionsEntity> getNearByLocation(@PathVariable("latitude") double latitude,
                                                     @PathVariable("longitude") double longitude,
                                                     @PathVariable("radius") double radius) throws IOException {
        List<AttractionsEntity> res = attractionsService.getNearByLocation(latitude, longitude, radius);
        return res;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attractionId}")
    public R info(@PathVariable("attractionId") Integer attractionId) {
        AttractionsEntity attractions = attractionsService.getById(attractionId);

        return R.ok().put("attractions", attractions);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttractionsEntity attractions) {
        attractionsService.save(attractions);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttractionsEntity attractions) {
        attractionsService.updateById(attractions);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] attractionIds) {
        attractionsService.removeByIds(Arrays.asList(attractionIds));

        return R.ok();
    }

}
