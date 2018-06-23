//package com.heartiger.task_user.client;
//
//import com.heartiger.task_user.dto.ResultDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
///**
// * Autowire this interface in other class to call this method to use it.
// */
//@FeignClient(name = "task")
//public interface CategoryClient {
//
//    /* Maybe can use @RequestParam int tid, @RequestParam int oid */
//    @DeleteMapping("/api/categories/delete")
//    ResultDTO deleteCategory(@RequestParam("cid") int cid, @RequestParam("oid") int oid);
//}
