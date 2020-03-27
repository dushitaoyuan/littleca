# littleca-shell

## 项目介绍
littleca-shell-core:  
 基于openssl 和keytool的证书生成工具包
littleca-shel-ui :  
    配套界面  
1. 目前只支持rsa
2. 修改脚本可支持更多算法
3. shell无交互设计,参数由上层应用封装

### 生成脚本和配置

1. 示例配置
docs/openssl  
    - windows  
    - linux  
2. 脚本   
docs/shell  
    - windows  
    - linux 
cert_shell.xxx 交互型脚本(按照提示输入参数)
cert_shell_auto.xxx 自动化脚本(需入参)
   


## 
1.  认证接口（/auth）
2. token刷新接口（/auth/refresh）  
3. 用户信息获取接口  
参见com.taoyuanx.ca.auth.controller
## 认证设计
1. 接入账户提前申请，由管理端分配 apiAccount 和 apiSecret 或 apiAccount，并交换公钥）
2. 认证请求参数(AuthRequestDTO)：json(random,apiAccount,timestamp,sign)  
    基于 apiAccount+时间戳+签名（公私钥）验证请求方身份

## 请求参数构造
参见：com.taoyuanx.test.LittleAuthRequestTest  