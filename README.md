# VCMY License Client

### 说明
License Client提供如下三个功能：
  * 客户端密钥对的生成
  * 证书请求文件的生成
  * 证书文件的校验

### 依赖
  * C: [openssl](https://www.openssl.org/docs/)
  * Java: [org.bouncycastle](http://www.bouncycastle.org/docs/docs1.5on/index.html)
  * Python: [cryptography](https://cryptography.io/en/latest/)

具体配置步骤，请查看 `<language>/README.md`

### License 类
客户端除了提供接口，还提供了 License 类，用于序列化和反序列化。
  * Java: com.vcmy.license.License
  * Python: vcmy_license.models.License

建议为验证通过的 License 文件创建该类的实例，并序列化到数据库中。
后期从数据库中获取证书，并再次调用验证证书的接口。

注意：
  * License 类提供了有效时间的字段，这些字段只能用于数据获取和展示，不能作为
    License 有效性的判断依据，因为数据库记录可以被修改。License 有效性只能通过调用
    “验证证书” 接口获得。
  * 该如果该类不满足使用需求，可以继承该类或者重写新的 License 类。

### 保护机制
除了校验机制，License 实际有效工作还需要保护机制：
  * 保护证书校验不被绕过，交付的产品中只有二进制的文件，而没有源文件。
    如`.c -> .so`, `.java -> .class`, `.py -> .pyc`
  * 通过周期性的任务检查导入的 License 文件的有效性，
    如每天定时启动后台线程，调用“验证证书” 接口，如果所有的证书都过期，需要有相应的策略。
