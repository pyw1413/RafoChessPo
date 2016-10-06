gameserver使用标准的Smartfox架构
1. 自定义扩展
2. 注册handler
3. 调用业务处理逻辑

代码目前暂时分为两块，老的逻辑和新的逻辑
1. 老逻辑
此部分代码在包com.kodgames.battle下，目标是将老逻辑无缝切换到smartfox的接口
分层结构为：
    server action service
分层结构说明：
  a. server
  smartfox的extension代码，将负责房间的游戏管理，包括房间的用户信息、打牌逻辑、算分逻辑、聊天
  b. action
  本层为原有的服务端接口，此处转换为smartfox的handler，负责接收客户端请求，调用服务端处理逻辑，封装返回对象
  c. service
  servcie层为原有的主要游戏逻辑, 含有room、battle、record、chat四个子服务，分别处理房间管理、游戏管理、分数管理、聊天管理


2. 新逻辑
此部分代码在包com.rafo.chess下
