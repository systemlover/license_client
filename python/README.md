# VCMY License Client for Python

### 处理依赖
~~~~
pip install cryptography
~~~~

### 安装

从 http://192.168.33.10:8002/yaoxinwei/license_client/releases 下载 whl 文件，
用如下命令安装：
~~~~
# Python2
pip install --no-index vcmy_license-1.0.0-py2-none-any.whl

# Python3
pip3 install --no-index vcmy_license-1.0.0-py3-none-any.whl

vi <project_name>/settings.py
INSTALLED_APPS = [
    ...
    'vcmy_license.apps.VcmyLicenseConfig',
]
~~~~

### 卸载
```
pip uninstall vcmy-license
# or
pip3 uninstall vcmy-license
```

### 接口
  * 生成证书请求文件，返回 `byte` 字符串
    * `private_key_path` 为客户端的私钥路径

  ```
  def get_request_bytes(private_key_path)
  ```

  * 加载证书，返回 `cryptography.x509.Certificate` 对象
  ```
  def load_certificate(license_content)
  ```

  * 验证证书
    * `ca_cert` 为 License Server 的证书（含公钥），可以从 http://<license_server>/ca_certificate/ 获得
    * `client_cert` 为 License Server 发给客户的证书
    * `check_pubkey` 用于设置是否要检查 client_cert 的公钥是否和客户端的私钥匹配。
      一般设置为True，启用公钥检查；当用于批量授权模式时，需要设为False，不启用公钥检查
    * `client_key_path` 为客户端的私钥路径

  ```
  def verify_certificate(ca_cert, client_cert,
            check_pubkey=True, client_key_path=None)
  ```
