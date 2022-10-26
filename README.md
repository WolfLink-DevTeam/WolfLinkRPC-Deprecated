# WolfLinkRPC
## 这是什么
  
绫狼互联 RPC远程程序调用系统，即分布式数据交互服务系统，支持跨平台进行数据传输、API调用  
关于RPC可以参考这篇文章 https://zhuanlan.zhihu.com/p/187560185  
基于RabbitMQ实现中央数据包转发、客户端权限鉴定  
并且拥有额外的客户鉴权，保证接口不被滥用  

绫狼互联RPC 拥有两套指令模块，Analyse模块、LocalCommand模块  
分别用来解析远程数据包、本地字符串指令  
可以为每一个指令设置不同的权限等级需求，当调用者权限不满足时拒绝访问该指令  
  
## 目前可以做什么
  
同步Minecraft服务器和QQ群的消息  
远程开启、关闭Minecraft服务器  
远程执行windows脚本  
远程调用BukkitAPI并返回值  
发送广播消息到所有在线的客户端  
......

## 如何部署

1. abab
2. abab
3. abab

## 阿巴阿巴，还没写完

还在完善代码中~

开源协议 GNU Affero General Public License v3.0 [https://choosealicense.com/licenses/agpl-3.0/#]
