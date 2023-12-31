# activiti-achieve

[![Java](https://img.shields.io/badge/Java-17-brightgreen.svg?style=flat&logo=java)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![JUnit](https://img.shields.io/badge/JUnit-5.9.2-brightgreen.svg?style=flat&logo=junit5)](https://junit.org/junit5/docs/current/user-guide)
[![Gradle](https://img.shields.io/badge/Gradle-8.4-brightgreen.svg?style=flat&logo=gradle)](https://docs.gradle.org/8.4/userguide/installation.html)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0.2-brightgreen.svg?style=flat&logo=springboot)](https://docs.spring.io/spring-boot/docs/3.0.2/reference/htmlsingle/)
[![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-2022.0.0-brightgreen.svg?style=flat&logo=spring)](https://docs.spring.io/spring-cloud/docs/2020.0.0/reference/htmlsingle/)
[![Spring Cloud Alibaba](https://img.shields.io/badge/Spring_Cloud_Alibaba-2022.0.0.0-brightgreen.svg?style=flat&logo=alibabacloud)](https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/zh-cn/index.html)
[![Release](https://img.shields.io/badge/Release-0.12.2-blue.svg)](https://github.com/aaric/activiti-achieve/releases)

> Activiti 7.4 Learning.

## Gradle

> [Activiti User Guide](https://www.activiti.org/userguide/)  
> [7.4.0 - Activiti & Activiti Cloud Developers Guide](https://activiti.gitbook.io/activiti-7-developers-guide/releases/7.4.0)

```groovy
ext {
    activitiVersion = "7.4.0"
}

dependencyManagement {
    imports {
        mavenBom "org.activiti:activiti-dependencies:$activitiVersion"
    }
}

dependencies {
    implementation "org.activiti:activiti-engine"
    implementation "org.activiti:activiti-spring"
    implementation "org.activiti:activiti-bpmn-model"
    implementation "org.activiti:activiti-bpmn-converter"
    implementation "org.activiti:activiti-json-converter"
    implementation "org.activiti:activiti-bpmn-layout"
}
```

## Init MySQL

```java
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class BpmnTests {

    @Autowired
    private StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration;

    @Test
    public void testInitDb() throws Exception {
        standaloneProcessEngineConfiguration.buildProcessEngine();
        log.info("ok");
    }
}
```

&emsp;&emsp;**Result:**

![at7 tables](docs/img/at7-tables.png)

## SimSun Fonts

```bash
apk add --no-cache fontconfig ttf-dejavu
mkdir /usr/share/fonts/microsoft

docker cp SimSun.ttf zd55_tool_edition:/usr/share/fonts/microsoft
```
