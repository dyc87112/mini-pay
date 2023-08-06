# MiniPay

最近因为要给多个独立开发应用提供一个共用的支付服务，原本打算直接找个开源的（预算不足）。调研了几个热门的开源支付项目，这些项目的功能都非常强大，但这也使得上手成本和部署成本都略高。实际上，好多功能其实我们都用不到，

所以，脑子一热，决定撸个轮子。 既然要撸轮子，还是要好好撸一下，所以这几天好好思考了一下这个轮子的定位。

最后，决定MiniPay的核心定位是：**简单、轻量、开箱即用**！

所以，MiniPay的架构不会特别复杂，让更多开发者都能看懂和参与；项目的构建和部署也要尽量便捷，用户通过**简单配置，直接启动**就能用上的支付服务！

这个开源项目的构建，可以帮助到需要一些简单支付功能的开发者，你可以直接用来作为自己系统里的微服务，也可以基于此打造自己的支付系统。 同时，MiniPay的框架选型将尽可能的采用Spring全家桶的方案，比如：数据访问将采用Spring Data JPA，而不是MyBatis；权限控制将采用Spring Security，而不是Shiro...所以，如果您正在学习Spring全家桶，那么该项目也可以作为一个实战项目来一起锻炼！

## 技术栈

- Java 17
- Spring Boot 3.1.x
- Spring Data JPA
- Spring Security

## 路线图

**管理功能**

- [ ] 简单登录页面

**微信支付对接**

- [x] 扫码支付（Native）
  - [x] 预下单接口
  - [x] 获取二维码接口
  - [x] 支付回调接口
  - [ ] 订单查询接口
  - [ ] 关闭订单接口：超时未支付、订单支付失败
- [ ] 测试页面
- [ ] 订单管理页面

**支付宝对接**

- [ ] 支付宝支付
  - [ ] 支付宝
  - [ ] 支付宝订单管理

## 
