package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.result.Result;
import com.sky.vo.EmployeeLoginVO;

public interface EmployeeService {

    Result<EmployeeLoginVO>login(EmployeeLoginDTO employeeLoginDTO);
}
