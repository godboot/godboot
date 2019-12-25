package com.godboot.app.controller;

import com.godboot.app.model.dto.AccountDTO;
import com.godboot.app.model.search.AccountSearchDTO;
import com.godboot.app.service.IAccountService;
import com.godboot.framework.entity.PageResult;
import com.godboot.framework.entity.ServiceResult;
import com.godboot.framework.entity.SessionUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("account")
public class AccountController {
    @Resource
    private IAccountService accountService;

    @ResponseBody
    @RequestMapping("list")
    public ServiceResult<PageResult<List<AccountDTO>>> list() throws InterruptedException {
        return accountService.getAccountList(new AccountSearchDTO(), new SessionUser());
    }

    @ResponseBody
    @RequestMapping("test")
    public ServiceResult<Object> test() {
        return accountService.test(new AccountSearchDTO(), new SessionUser());
    }
}
