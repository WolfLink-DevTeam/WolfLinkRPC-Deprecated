<a name="d3n6K"></a>
### [WolfLink-RPC 开发者指南](https://github.com/MikkoAyaka/WolfLinkRPC/wiki/%E5%BC%80%E5%8F%91%E8%80%85%E6%8C%87%E5%8D%97)
<a name="OuzWl"></a>
### 系统介绍
<a name="Hw2Yl"></a>
#### 核心架构
数据包传输功能基于 RabbitMQ-Server 高效消息代理软件，通过在不同平台的客户端实现AMQP ( Advanced Message Queuing Protocol ) 高级消息队列协议与 RabbitMQ-Server 建立跨平台、多客户端、中央集成式连接，由RabbitMQ-Server对接收到的数据包进行中转，从而实现的RPC ( Remote Procedure Call ) 远程程序调用系统。
<a name="FFftW"></a>
#### 使用环境
用于架设微服务架构，为功能模块较多的系统实现服务解耦，使各个模块之间的耦合度降到最低，互相保持独立，通过给定的指令进行访问(常规实现方式是通过webAPI进行交互，其鉴权过程较为复杂、繁琐)，可以大幅提高项目的可维护性。<br />用于对接业务相关度较高，但其实现层保持独立的多个系统。例如 Mirai与Bukkit 互通，许多国内的Minecraft服务器都会有一个QQ群，可以视QQ群与Minecraft服务器为满足以上条件的两个独立的系统，通过WolfLinkRPC进行对接，可以实现QQ群与Minecraft服务器的聊天互通，在QQ群调用服务器内指令，查询玩家数据，调用Bukkit API实现一些复杂的业务功能等。当然，由于WolfLinkRPC内部实现了两套指令系统：一套用于分析远程指令，另外一套用于分析本地指令。因此，每一个对接了该RPC系统的客户端都同时充当生产者与消费者两个角色。
<a name="UZW37"></a>
#### 支持的运行环境
[Mirai-Console [QQ机器人 插件 单个群聊环境]](https://github.com/mamoe/mirai)<br />[Bukkit [Minecraft 服务器 插件]](https://github.com/Bukkit/Bukkit)<br />Windows [Windows 系统，命令行软件](目前只在 windows10 上进行了测试)

_如有其它需要可参考上方开发者指南，基于Common模块自行开发_

<a name="Ci1A3"></a>
### 功能模块
<a name="L8In8"></a>
#### 本地指令
本地指令部分考虑到使用情景较少，因此设计较为简洁，将一条完整的指令根据结构拆分为先后两部分：指令主体、指令参数。<br />以 **>** 符号为根指令，调用任何指令都需要加上该根指令作为前缀，例如 可以输入 **> 帮助 >** 以查询 **>** 指令下级指令列表(后续考虑优化该查询方式)。每一级指令之间以空格作为分隔符，从左往右按顺序在指令树中进行查找，将能匹配到的最长长度指令作为本次调用的**指令主体**，剩下的没有在指令树中被匹配到的部分将被视为**指令参数**。
<a name="CVPsK"></a>
#### 远程指令
一般需要调用某个客户端的本地指令发起远程指令数据包，同时会为该数据包添加一个回调事件，以便在收到回调后执行相应的操作。如果长时间没有收到回调数据包，则视为请求超时，会删除对应的回调事件，并在消息接收窗口输出相应的提示信息。<br />以 Mirai平台 调用 Windows平台 的指令为例：<br />在Mirai监听的群聊内发送指令
> \> 接口调用 windows_default 帮助

即可发起一个远程指令，目标客户端名称为 windows_default，远程指令内容为 帮助<br />会返回以下信息
>  -<br />[ 绫狼互联-RPC ] 远程指令帮助 - WINDOWS<br />-<br />设置权限 {用户uniqueID} {权限名} - 权限等级必须低于发起者<br />添加服务器数据 {服务器名称}||{服务器标题}||{服务端文件夹}||{启动脚本名(带后缀)}<br />管理MC服务器 查询所有<br />管理MC服务器 启动 {服务器名称}<br />管理MC服务器 关闭 {服务器名称}<br />读取文件 {文件路径} {文件类型} - 文件路径用 |符号 作为文件夹分隔符，例如 C:|Program Files|Logs|latest log<br />运行文件 {文件路径} - 文件路径用 |符号 作为文件夹分隔符，例如 C:|Program Files|Java|java.exe<br />-  

再尝试发送一个复杂一点的指令，例如
> **> 接口调用 windows_default** 运行文件 C:|Users|MikkoAyaka|Desktop|test TXT

以上指令在 Mirai端 发起，加粗部分为 Mirai端 提供的本地指令部分，最终到达 Windows端 的远程指令为
> 运行文件 C:|Users|MikkoAyaka|Desktop|test TXT

再由 Windows端 的远程指令分析模块对指令内容进行处理，会以TXT格式读取文件路径为C:\Users\MikkoAyaka\Desktop\test.txt 然后将内容以字符串的形式传回 Mirai端 (如果内容太多，Mirai端会报错，提示只允许发送最长不超过5000个字符的消息，后续考虑转为发送文件)
<a name="DnJyE"></a>
#### 权限鉴定
<a name="YZZOw"></a>
##### 基础接入鉴权 - 保障平台内数据包信息可靠
基础鉴权部分主要交给 RabbitMQ-Server 完成，在服务端部署完毕后，登入 RabbitMQ-Server 的管理页面，为各个即将接入的客户端分配账号。之后客户端只需要凭借自己的账号即可接入中央服务器，获得数据包的基础收发权限。
<a name="JCTrf"></a>
##### 本地鉴权 - 主要为单端多用户的使用情景设计
考虑到某些情况下，RPC系统中的单个客户端可能会被多个具有不同身份的用户使用，因此对本地指令也设计了一套权限鉴定机制，当用户调用本地指令时，根据用户的唯一特征码(unique ID)从该客户端本地的数据库中获取用户权限信息，与指令需求的权限信息进行比对，权限需求满足指令才会被成功执行，否则会被拒绝执行。
<a name="qX1qI"></a>
##### 远程鉴权 - 防止接口被滥用，为某些敏感功能的接口设置权限门槛
由于采用了中央集成式的网络架构模式，并且中央服务器仅提供消息转发的功能，导致RPC系统中每一个客户端对外提供的接口都可以被系统中任何客户端访问，因此对于接口的权限鉴定则显得更为重要。与本地鉴权的方式大致相同，只是为每一个对外访问的接口提供一个设置访问权限的功能，当接收到远程数据包，尝试调用客户端内某一个远程指令，则会分别获取数据包发送者在本地的权限等级 以及 该接口最低的访问权限要求，当数据包发送者权限满足需求，指令才会被执行，否则会被拒绝执行。
<a name="JpqFx"></a>
##### 注意
要确保以上鉴权机制能**正确且安全**的运作，需要为每一个客户端的用户分配一个在该RPC系统内独一无二的特征标识码，用于区别用户。如果只使用用户名作为特征码，则会存在改名篡权之类的风险漏洞。<br />而在仓库中给出的三个RPC客户端实现中都没有对用户进行统一的特征码生成与绑定，那在这个由Windows端、Mirai端、Bukkit端组成的RPC系统中是如何区分用户的？<br />其实原理没变，依然是使用特征码进行区别。只要能够保证特征码之间互不冲突，不会存在一个特征码对应两个不同的用户的情况，鉴权机制就能正确且安全的运行。目前，Mirai端只支持绑定一个QQ群，对其提供业务接入，每一个QQ群内的群员都是该RPC系统的一个用户，而QQ自身就提供了一套简单的特征码，即QQ号，由5~12位纯数字组成。在Bukkit端，也提供了UUID作为用户特征码，为64字符长度只包含英文数字和-连接符的字符串。而Windows端则是由于使用情景简单，因为Windows端的使用者必然拥有该Windows端所属服务器的操作权限，所以我们认为每一个Windows端都只有一个用户，为该用户设置特征码为windows_{自定义名称}。在windows端的特征码中出现了下划线，也就确保了windows端的特征码不会与Mirai端、Bukkit端的特征码冲突。那么这样就确保了整个系统万无一失了吗？并没有。存在以下两种风险情况：<br />①RPC系统存在多个Bukkit服务器，并且其中同时存在正版服务器和离线服务器。由于Bukkit特性，这两类服务器对玩家UUID的生成机制不同，可能会存在正版服务器的玩家与离线服务器的玩家UUID一致的情况，导致二者权限受关联，可能存在篡权风险。<br />②RPC系统存在多个Windows客户端，由于windows客户端的标识名可以自定义，则有很大可能出现多个标识名相同的windows客户端，由于windows客户端的实现机制特殊，将客户端的特征码作为队列名进行监听，因此多个重名用户会争夺同一个队列中的数据包资源，并且同样也存在篡权风险。<br />第一种情况发生的概率极低，但的确有可能发生。第二种情况发生概率相对更大，但由于目前项目还处于快速迭代阶段，也没有投入正式的生产环境，因此暂时还没有进行处理。接下来的更新中会考虑其他较为简单的方式来解决上述问题。<br />规范的特征码处理方式应该为：在RPC系统中部署一个专门用于生成、分发、存储用户特征码的客户端，其他客户端获取用户权限只需要调用该专用客户端提供的接口。如果访问量较大可以考虑进行集群部署，在RPC系统中完成集群部署也非常简单，这里不做过多的说明。
<a name="fPsyE"></a>
### 如何部署
<a name="JCOMK"></a>
#### RabbitMQ-Server 中央服务端
如果你希望搭建一个私有的RPC系统，则需要自行搭建 RabbitMQ 消息中转服务器，步骤如下：

1. 前往 [Erlang 官网](https://github.com/erlang/otp/releases/download/OTP-25.1.2/otp_win64_25.1.2.exe)，下载最新版本的 Erlang 运行库 ( 一般为 Windows 64位 系统环境，选择对应版本即可)
2. 安装 Erlang 到本地目录
3. 配置好 Erlang 的环境变量，添加环境变量 ERLANG_HOME，变量值为 Erlang 根文件夹的目录路径，例如 C:\Program Files\Erlang。然后编辑 Path 变量，新建一项值为 %ERLANG_HOME%\bin 然后保存
4. 下载RabbitMQ-Server服务端并安装
5. 安装完成后前往其安装目录，进入其目录内 rabbitmq_server-3.7.3\sbin 文件夹内，在此目录中打开 cmd，输入 rabbitmq-plugins enable rabbitmq_management 启用rabbitmq自带的可视化管理页面功能
6. 打开浏览器，输入 http://localhost:15672 账号密码默认都为guest

至此部署完毕，如需添加用户，请前往RabbitMQ服务端后台页面进行添加<br />想要修改服务端对外开放的TCP连接端口(默认为5672)则需要修改配置文件，前往%Appdata%\Roaming\RabbitMQ目录，创建一个新文件 rabbitmq.conf 在其中写入以下内容的配置项：
> listeners.tcp.default = 12345

保存，然后重启RabbitMQ服务(在cmd中输入net stop rabbitmq停止服务，net start rabbitmq启用服务)<br />TCP端口即被成功修改为12345
<a name="qxyfH"></a>
#### Bukkit 插件端

1. 下载好最新版本的 Bukkit-WolfLinkRPC 插件
2. 将其放置在 服务端目录\plugins 文件夹内
3. 启动服务器，让插件生成默认配置文件，然后关闭服务器
4. 前往 plugins 目录下找到插件对应的配置文件夹，对相应配置进行修改
```yaml
# RabbitMQ-Server 远程服务器IP & TCP端口
Host: 127.0.0.1
Port: 23333
# RabbitMQ-Server 账户&密码
Username: myaccount
Password: mypassword
Debug: false
# 权限列表，如需自己添加权限，请使用该RPC客户端提供的指令进行调整
PermissionList: []
# 由于是bukkit平台下的RPC客户端
# 则实际队列名(客户端名)为 bukkit_paper_saw
ClientTag: paper_saw

```

5. 再次启动服务器，连接成功，配置完毕；在该Bukkit服务器内输入 > 帮助 > 即可查询本地指令。
<a name="WYGqk"></a>
#### Mirai Console 插件端

1. 下载好最新版本的Mirai-WolfLinkRPC 插件
2. 将其放置在 Mirai-Console目录\plugins 文件夹内
3. 启动服务器，登录QQ，让插件生成默认配置文件，然后关闭服务器
4. 前往 Mirai-Console目录\data\org.wolflink.mirai.wolflinkrpc\ 修改配置文件
```yaml
# 启用的QQ群群号，目前只支持单个群聊
enabledGroups: 
  - 123456789
# 权限列表，可以自行添加权限
# 例如 3401286177: ONLY_MANAGER 将QQ号为3401286177的用户设置为唯一管理
permissionMap: 
  测试用户的uniqueID: DEFAULT
# 是否开启调试模式，会输出额外的日志信息
debug: false
# RabbitMQ-Server 远程服务器IP & TCP端口
host: 127.0.0.1
port: 23333
# RabbitMQ-Server 账户&密码
username: myaccount2
password: mypassword2
# 由于是mirai平台下的RPC客户端
# 实际队列名(客户端名)为 mirai_bot1
clientTag: bot1
```

5. 再次启动 Mirai机器人，完成QQ登录后即配置完毕。在相应群聊内发送 > 帮助 > 即可查询本地指令帮助。
<a name="PO946"></a>
#### Windows 端

1. 下载好最新版本的 Windows-WolfLinkRPC.jar
2. 编写启动脚本运行该jar文件，无额外参数要求，可参考以下内容
```bash
Java -Xms1G -Xmx1G -jar Windows-WolfLinkRPC.jar
pause
```

3. 运行，等待配置文件生成后关闭
4. 修改配置文件，内容与Mirai/Bukkit端几乎一致
5. 再次运行，连接成功即视为配置完毕，在控制台输入 > 帮助 > 查询本地指令帮助。

### 开源协议
开源协议 GNU Affero General Public License v3.0 [https://choosealicense.com/licenses/agpl-3.0/#]

### 最后的最后
喜欢的话，点个star吧！如果在使用过程中遇到了什么问题可以加我QQ 3401286177反馈哦，一定会尽快回复的！  
本人刚接触开发一年左右时间，代码水平和架构水平都比较低..希望大佬们轻点喷，也希望能有人帮忙贡献一点代码啦
最后，非常感谢你能看到这里！
