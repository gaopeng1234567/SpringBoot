# 分布式会话

场景:
实现方式:

* session
* jwt

session实现

* 引入jar

> session.jar

* 配置redis
* 开启注解
* 原理

> servlet创建的request、response对象，springSession通过SessionRepositoryFilter过滤器,提供了redis、JDBC、mongodb三种方式实现
> 用HttpSessionWrapper重写了HttpSession类，将创建的session放入本地hashMap和redis中，使用UUID作为SessionId。
> 获取session先从本地中，然后从redis。然后更新最后一个获取时间等。
> 他在dofilter之后的finally中进行更新最后时间，而不是每次getSession中更新，减少远端redis访问次数，当然也可以开启立即更新

jwt实现

* 组成部分
    * Header
      ```
      描述JWT元数据
      {alg:"HS256",
      typ: "JWT"}
      1、alg属性表示签名的算法，默认算法为HS256，可以自行别的算法。
      2、typ属性表示这个令牌的类型，JWT令牌就为JWT。
      Header = Base64(上方json数据)
      ```
    * Payload
      ```
      使用方式如下，userid,created(token创建时间),exp(最近更新时间)
      data = {
      "userid":"yangguo",
      "created":1489079981393,"exp":1489684781
      }
      Payload = Base64(data) //可以被反编码，所以不要放入敏感信息
      ```
    * Signature
      > secret为加密的密钥，密钥存在服务端

> ex: Header.Payload.Signature
> 3yJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNzE3NjE0OTQ3OCwiZXhwIjoxNTc3NzgwOTQ5fQ.qSlhJNpom2XeeqMyXST2AdHvAjztWqR4zvQQEc‐K8qMsJ3XQpwpQ

* 加密

* 验证

* JWT工作方式
    > 通常我们把token设置在request‐Header头中，每次请求前都在请求头加上下方配置
    > Authorization: Bearer <token>

* JWT身份认证流程
  * 1、用户提供用户名和密码登录
  * 2、服务器校验用户是否正确，如正确，就返回token给客户端，此token可以包含用户信息
  * 3、客户端存储token，可以保存在cookie或者local storage
  * 4、客户端以后请求时，都要带上这个token，一般放在请求头中
  * 5、服务器判断是否存在token，并且解码后就可以知道是哪个用户
  * 6、服务器这样就可以返回该用户的相关信息了

* 对比
JWT用户信息存在客户端，session存放在服务端
  