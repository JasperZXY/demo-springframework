# demo-springframework

demo-springframework共包含两个模块：
- demo-springframework-withoutAnnotation模块为纯基于XML/Java的配置元数据的项目，不使用注解。
- demo-springframework-withAnnotation模块为基于XML/Java的配置元数据的项目，但优先使用注解(<context:annotation-config/>)。
- demo-springframework-temp为临时项目，准备删除。

注意：
1.PropertySourcesPlaceholderConfigurer将PropertySources作为Placeholder，因此同时可以被@Value和Environment访问

TODO:
1.补充在Spring中使用AspectJ；
2.补充@Valid支持分组验证；
3.以下考虑从XmlConfig项目中迁出：
a.@Valid/@Validated注解；
b.@Format注解；
4.解析自定义配置标签