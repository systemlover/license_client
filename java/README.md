# VCMY License Client for Java

### 处理依赖
从 http://192.168.33.10:8002/yaoxinwei/license_client/releases
下载 vcmy-license-1.0.0.jar

将这个 jar 包加入本地的 Maven 仓库：
```
mvn install:install-file -Dfile=vcmy-license-1.0.0.jar -DgroupId=com.vcmy \
    -DartifactId=license -Dversion=1.0.0 -Dpackaging=jar
```

修改 Maven 配置文件 pom.xml，添加依赖，`vi pom.xml`：
~~~~
<dependencies>
  ...

  <dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.59</version>
  </dependency>

  <dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk15on</artifactId>
    <version>1.59</version>
  </dependency>

  <dependency>
    <groupId>com.vcmy</groupId>
    <artifactId>license</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
~~~~

### 接口
  * 创建 LicenseManager 对象
    * `basedir` 为密钥存放的路径

  ```
  public LicenseManager(String basedir)
  ```

  * 生成证书请求文件
  ```
  public ByteArrayOutputStream getRequest()
  ```

  * 加载证书
  ```
  public static X509Certificate loadPemX509Certificate(byte[] license_content)
  ```

  * 验证证书，重载方法，接收三种不同形式的证书
    * `licenseContent` 为证书的内容，接收字符或字节数组
    * `clientCert` 为 X509Certificate 对象，其他两个方法实际调用该方法
    * `checkPubkey` 用于设置是否要检查 clientCert 的公钥是否和客户端的私钥匹配。
      一般设置为true，启用公钥检查；当用于批量授权模式时，需要设为false，不启用公钥检查

  ```
  public boolean verify(X509Certificate clientCert, boolean checkPubkey)
  public boolean verify(byte[] licenseContent, boolean checkPubkey)
  public boolean verify(String licenseContent, boolean checkPubkey)
  ```
