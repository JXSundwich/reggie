package com.sundwich.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sundwich.reggie.entity.Category;

/**
 * @author JX Sun
 * @date 2023.07.20 17:55
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
