package com.ddl.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ddl.reggie.pojo.Employee;
import com.ddl.reggie.service.EmployeeService;
import com.ddl.reggie.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 10795
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2024-04-24 22:02:58
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




