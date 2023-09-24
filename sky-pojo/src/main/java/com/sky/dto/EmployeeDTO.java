package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author mrzhang
 */
@Data
public class EmployeeDTO implements Serializable {

    @NotBlank(message = "username不能为空")
    private String username;
    @NotBlank(message = "name不能为空")
    private String name;
    @NotBlank(message = "phone不能为空")
    private String phone;

    private Integer sex;
    @NotBlank(message = "idNumber不能为空")
    private String idNumber;

}
