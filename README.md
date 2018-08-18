# demo-springframework

demo-springframework项目共包含两个模块：
- demo-springframework-withoutAnnotation模块为纯基于XML/Java的配置元数据的项目，不使用注解。
- demo-springframework-withAnnotation模块为基于XML/Java的配置元数据的项目，但优先使用注解(<context:annotation-config/>)。


TODO:
1. 补充在Spring中使用AspectJ；
2. 解析自定义配置xml标签
3. demo-springframework-temp为临时项目，准备删除。
4. 补充测试用例
5. 补充@Valid支持分组验证；
6. 以下考虑从XmlConfig项目中迁出：
- a.@Valid/@Validated注解；
- b.@Format注解；
