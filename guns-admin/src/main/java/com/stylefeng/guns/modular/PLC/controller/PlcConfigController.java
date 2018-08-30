package com.stylefeng.guns.modular.PLC.controller;

import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.modular.system.model.PlcConfig;
import com.stylefeng.guns.modular.PLC.service.IPlcConfigService;

/**
 * PLC配置管理控制器
 *
 * @author fengshuonan
 * @Date 2018-08-22 13:07:57
 */
@Controller
@RequestMapping("/plcConfig")
public class PlcConfigController extends BaseController {

    private String PREFIX = "/PLC/plcConfig/";

    @Autowired
    private IPlcConfigService plcConfigService;

    /**
     * 跳转到PLC配置管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "plcConfig.html";
    }

    /**
     * 跳转到添加PLC配置管理
     */
    @RequestMapping("/plcConfig_add")
    public String plcConfigAdd() {
        return PREFIX + "plcConfig_add.html";
    }

    /**
     * 跳转到修改PLC配置管理
     */
    @RequestMapping("/plcConfig_update/{plcConfigId}")
    public String plcConfigUpdate(@PathVariable Integer plcConfigId, Model model) {
        PlcConfig plcConfig = plcConfigService.selectById(plcConfigId);
        model.addAttribute("item",plcConfig);
        LogObjectHolder.me().set(plcConfig);
        return PREFIX + "plcConfig_edit.html";
    }

    /**
     * 获取PLC配置管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return plcConfigService.selectList(null);
    }

    /**
     * 新增PLC配置管理
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(PlcConfig plcConfig) {
        plcConfigService.insert(plcConfig);
        return SUCCESS_TIP;
    }

    /**
     * 删除PLC配置管理
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer plcConfigId) {
        plcConfigService.deleteById(plcConfigId);
        return SUCCESS_TIP;
    }

    /**
     * 修改PLC配置管理
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(PlcConfig plcConfig) {
        plcConfigService.updateById(plcConfig);
        return SUCCESS_TIP;
    }

    /**
     * PLC配置管理详情
     */
    @RequestMapping(value = "/detail/{plcConfigId}")
    @ResponseBody
    public Object detail(@PathVariable("plcConfigId") Integer plcConfigId) {
        return plcConfigService.selectById(plcConfigId);
    }
}
