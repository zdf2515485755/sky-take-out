package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.EmployeeLoginVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

public interface EmployeeService {

    Result<EmployeeLoginVO>login(EmployeeLoginDTO employeeLoginDTO);

    Result save(@RequestBody @Validated EmployeeDTO employeeDTO);

    Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
