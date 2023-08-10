package com.sundwich.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sundwich.reggie.entity.AddressBook;
import com.sundwich.reggie.mapper.AddressBookMapper;
import com.sundwich.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
