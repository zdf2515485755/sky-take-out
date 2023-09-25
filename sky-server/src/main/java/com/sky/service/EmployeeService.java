package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {

    Result<EmployeeLoginVO>login(EmployeeLoginDTO employeeLoginDTO);

    Result save(EmployeeDTO employeeDTO);

    Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    Result changeEmployeeStatusById(Integer status, Long id);

    Result<Employee> queryById(Long id);

    Result updateEmployee(EmployeeDTO employeeDTO);
}
