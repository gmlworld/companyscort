package com.gongml.companyscort.utils.groovy;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Component;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-25 17:33
 **/
@Component
public class CompilerConfiguration2 extends CompilerConfiguration {

    public CompilerConfiguration2(){
        super();
        super.setScriptBaseClass(GrovvyFunction.class.getName());
    }
}
