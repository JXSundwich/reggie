package com.sundwich.reggie.dto;


import com.sundwich.reggie.entity.Setmeal;
import com.sundwich.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
