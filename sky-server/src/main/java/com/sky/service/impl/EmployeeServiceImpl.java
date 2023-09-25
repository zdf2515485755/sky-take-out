package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author mrzhang
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Result<EmployeeLoginVO> login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(queryWrapper);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        String employeePassword = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!employee.getPassword().equals(employeePassword)) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //登录成功后，生成jwt令牌
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                employee.getId());

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        //3、返回实体对象
        return Result.success(employeeLoginVO);
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    public Result save(EmployeeDTO employeeDTO)
    {
        Employee employee = new Employee();

        BeanUtils.copyProperties(employeeDTO, employee);

        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());

        employee.setPassword(password);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);

        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    public Result<PageResult> pageQuery(EmployeePageQueryDTO employeePageQueryDTO)
    {
        int pageNum = employeePageQueryDTO.getPage();
        int pageSize = employeePageQueryDTO.getPageSize();
        String userName = employeePageQueryDTO.getName();
        //设置分页模糊查询
        Page<Employee> page = Page.of(pageNum, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!StringUtils.isEmpty(userName), Employee::getUsername, userName);

        Page<Employee> employeePage = employeeMapper.selectPage(page, lambdaQueryWrapper);
        PageResult pageResult = new PageResult();
        pageResult.setRecords(employeePage.getRecords());
        pageResult.setTotal(employeePage.getTotal());

        return Result.success(pageResult);

    }

    public Result changeEmployeeStatusById(Integer status, Long id)
    {
        Employee employee = Employee.builder()
                .id(id)
                .status(status).build();
        employeeMapper.updateById(employee);

        return Result.success();
    }

    public Result<Employee> queryById(Long id)
    {
        Employee employee = employeeMapper.selectById(id);
        return Result.success(employee);
    }

    public Result updateEmployee(EmployeeDTO employeeDTO)
    {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        String password = employeeDTO.getPassword();
        String passwordByMd5= DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(passwordByMd5);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.updateById(employee);

        return Result.success();
    }
}
