package com.heartiger.taskapigateway.utils;

import com.heartiger.taskapigateway.dto.ResultDTO;
import com.heartiger.taskapigateway.enums.ResultEnum;

public class ResultDTOUtil {

  public static ResultDTO<Object> success() {
    return success(null);
  }

  public static ResultDTO<Object> success(Object object) {
    ResultDTO<Object> resultDTO = new ResultDTO<>(ResultEnum.ACTION_SUCCESS);
    resultDTO.setData(object);
    return resultDTO;
  }

  public static ResultDTO error(Integer code, String message) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setCode(code);
    resultDTO.setMessage(message);
    return resultDTO;
  }

  public static ResultDTO error(ResultEnum resultEnum) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setCode(resultEnum.getCode());
    resultDTO.setMessage(resultEnum.getMessage());
    return resultDTO;
  }
}
