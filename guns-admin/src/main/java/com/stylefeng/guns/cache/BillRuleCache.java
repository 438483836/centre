package com.stylefeng.guns.cache;

import com.stylefeng.guns.modular.business.service.IBillCodeDefinitionService;
import com.stylefeng.guns.modular.system.model.BillCodeDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 缓存面单规则
 * Created by Vincent on 2018-08-23.
 */
@Service
public class BillRuleCache implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(BillRuleCache.class);

    private static ApplicationContext applicationContext;

    private static List<BillCodeDefinition> billCodeDefinitions = new ArrayList<BillCodeDefinition>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        startService();
    }

    private void startService() {
        initCache();
    }

    public static void initCache() {
        billCodeDefinitions.clear();
        IBillCodeDefinitionService iBillCodeDefinitionService = applicationContext.getBean(IBillCodeDefinitionService.class);
        List<BillCodeDefinition> portSyncList = iBillCodeDefinitionService.selectList(null);
        billCodeDefinitions.addAll(portSyncList);
        logger.info("Start init success");
    }

    public static Set<String> matchBarCode(Set<String> codeLists){
        Set<String> matchBarcode=new HashSet<>();
        if (CollectionUtils.isEmpty(codeLists)) {
            logger.info("codeList is empty");
            return matchBarcode;
        }
        for(String code : codeLists) {
            for (BillCodeDefinition billCodeDefinition : billCodeDefinitions) {
                if(billCodeDefinition.getTotalLength()==code.length())
                    if(code.indexOf(billCodeDefinition.getStartChars())>=0)
                    {
                        matchBarcode.add(code);
                    }
            }
        }
        return matchBarcode;
    }

}
