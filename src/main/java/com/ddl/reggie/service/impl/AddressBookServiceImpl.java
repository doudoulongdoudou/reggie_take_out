package com.ddl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddl.reggie.pojo.AddressBook;
import com.ddl.reggie.service.AddressBookService;
import com.ddl.reggie.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2024-04-24 22:02:58
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




