# VCMY License Client for C

### 处理依赖
~~~~
# On Ubuntu or Debian
apt apt -y install gcc make libssl-dev

# On CentOS or RHEL
yum -y install gcc make openssl-devel
~~~~

### 安装
http://192.168.33.10:8002/yaoxinwei/license_client/releases
提供了在 ubuntu 18.04 预编译好的动态链接库，可以下载后放到 /usr/lib/：
~~~~
cp libvcmylicense.so.1.0.0 /usr/lib/
ln -s /usr/lib/libvcmylicense.so.1.0.0 /usr/lib/libvcmylicense.so
~~~~

如果要重新编译，命令如下：
~~~~
make
make install
~~~~

### 卸载
```
rm -f /usr/lib/libvcmylicense.so*
```

### 接口

  * 检查文件是否存在，返回1表示存在，0表示不存在
  ```
  int vcmy_exists(const char *szPath);
  ```

  * 生成客户端私钥，返回私钥的指针，如果为NULL表示无法生成
  ```
  EVP_PKEY *vcmy_generate_key(const char *szPath);
  ```

  * 加载客户端私钥
  ```
  EVP_PKEY *vcmy_load_key(const char *szPath);
  ```

  * 生成证书请求文件
  ```
  int vcmy_save_x509_req(EVP_PKEY *pkey, const char *szPath);
  ```

  * 加载证书
  ```
  X509 *vcmy_load_x509_cert(const char *szPath);
  ```

  * 验证证书
    * `ca_cert` 为 License Server 的证书（含公钥），可以从 http://<license_server>/ca_certificate/ 获得
    * `client_cert` 为 License Server 发给客户的证书
    * `check_pubkey` 用于设置是否要检查 client_cert 的公钥是否和客户端的私钥匹配。
      一般设置为1，启用公钥检查；当用于批量授权模式时，需要设为0，不启用公钥检查
    * `pkey` 为客户端的私钥

  ```
  int vcmy_verify_x509_cert(X509 *ca_cert, X509 *client_cert,
    const int check_pubkey, const EVP_PKEY *pkey);
  ```
