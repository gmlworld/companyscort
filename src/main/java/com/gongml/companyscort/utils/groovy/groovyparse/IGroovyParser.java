package com.gongml.companyscort.utils.groovy.groovyparse;

/**
 * @program: companyscort
 * @description:
 * @author: Gongml
 * @create: 2018-12-25 16:54
 **/
public interface IGroovyParser<I,O> {
    O parse(I input);
}
