# Stock

启动参数 init:设置该参数会执行command line runner中的代码

启动时候设置生产环境prod或者开发环境dev 例如：java -jar xxx.jar --spring.profiles.active=dev


不适用docker启动: 
mvn package && java -jar target/stock-0.0.1.jar

使用项目中POM.xml配置的docker插件生成Docker镜像：
mvn install dockerfile:build