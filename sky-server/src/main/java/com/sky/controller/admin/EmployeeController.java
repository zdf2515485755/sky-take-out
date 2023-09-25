package com.sky.controller.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 * @author mrzhang
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody @Validated EmployeeLoginDTO employeeLoginDTO) {

        log.info("员工登录：{}", employeeLoginDTO);
        return employeeService.login(employeeLoginDTO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 添加员工
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody @Validated EmployeeDTO employeeDTO)
    {
        return employeeService.save(employeeDTO);
    }

    /**
     * 员工模糊分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO)
    {
        return employeeService.pageQuery(employeePageQueryDTO);
    }

    /**
     * 根据员工id改变员工账号状态
     * @param status 账号状态
     * @param id 员工id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result changeEmployeeStatusById(@PathVariable("status") Integer status, Long id)
    {
        return employeeService.changeEmployeeStatusById(status, id);
    }

    /**
     * 根据员工id查询员工信息
     * @return
     */
    @GetMapping("/query/{id}")
    public Result<Employee> queryById(@PathVariable ("id") Long id)
    {
        return employeeService.queryById(id);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping("/update")
    public Result updateEmployee(@RequestBody @Validated EmployeeDTO employeeDTO)
    {
        return employeeService.updateEmployee(employeeDTO);
    }
}
