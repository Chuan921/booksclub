# 读书会活动在线管理系统（BookClub System）

一个基于 **Spring Boot + Thymeleaf + Spring Data JPA + MySQL** 实现的读书会活动在线管理系统。项目围绕“读书会活动发布、报名参与、签到互动、评论交流、收藏点赞、消息通知”这一完整业务链路展开，适合用于课程设计、毕业设计、Java Web 实训项目展示，以及中小型社群活动管理系统的原型开发。

---

## 1. 项目简介

本项目主要用于解决读书会活动管理过程中的几个典型问题：

- 活动信息分散，缺少统一发布与浏览入口
- 用户报名、取消报名、签到流程不够清晰
- 活动参与后的互动功能较弱，缺少评论、点赞、收藏等能力
- 活动开始前缺少有效提醒，影响到场率
- 发起人缺少查看报名名单、管理活动的后台能力

围绕以上问题，系统提供了一个面向用户与活动发起人的 Web 管理平台。用户可以注册登录、浏览活动、报名参加、进行签到、评论交流、收藏和点赞；活动发起人可以发布和维护活动、查看报名人员；系统还会自动发送活动提醒和互动通知。

---

## 2. 项目亮点

- **完整的读书会业务闭环**：从活动发布、报名、签到，到评论互动、通知提醒，流程完整
- **前后端一体化实现**：采用 Thymeleaf 模板引擎渲染页面，适合 Java Web 教学与项目答辩展示
- **轻量级权限控制**：支持普通用户与管理员角色，活动修改/删除有权限校验
- **互动功能丰富**：支持活动点赞、评论点赞、活动收藏、评论发布与删除
- **定时通知提醒**：基于 Spring Scheduling 实现活动前 1 天提醒和活动前 10 分钟签到提醒
- **较清晰的分层结构**：Controller / Service / Repository / Entity 分层明确，便于维护和扩展
- **适合作为课程设计项目继续演进**：后续可以扩展 JWT 登录、文件上传、后台管理、统计报表等能力

---

## 3. 技术栈

### 后端

- Java 8
- Spring Boot 2.7.18
- Spring MVC
- Spring Data JPA
- Hibernate
- Lombok
- MySQL 8.x

### 前端

- Thymeleaf
- HTML5
- CSS3
- 原生 JavaScript（Fetch API）

### 构建与运行

- Maven / Maven Wrapper（`mvnw`）

---

## 4. 项目结构

```text
bookclub
├── src
│   ├── main
│   │   ├── java/com/zxy/bookclub
│   │   │   ├── controller          # 控制层：提供页面路由和 REST API
│   │   │   ├── service             # 业务层：封装活动、报名、签到、评论、通知等逻辑
│   │   │   ├── repository          # 持久层：基于 Spring Data JPA 操作数据库
│   │   │   ├── entity              # 实体类：活动、用户、报名、评论、点赞、收藏、通知等
│   │   │   ├── dto                 # 通用返回结果封装
│   │   │   └── BookclubApplication.java
│   │   └── resources
│   │       ├── application.properties  # 项目配置文件
│   │       ├── templates               # Thymeleaf 页面模板
│   │       └── static                  # 静态资源（CSS / JS）
│   └── test
│       └── java/com/zxy/bookclub       # 基础测试类
├── bookclub.sql                        # 数据库初始化脚本
├── pom.xml                             # Maven 配置
├── mvnw / mvnw.cmd                     # Maven Wrapper
└── README.md
```

---

## 5. 功能模块说明

## 5.1 用户模块

### 已实现功能

- 用户注册
- 用户登录
- 获取用户信息
- 修改个人资料
- 区分普通用户（`USER`）与管理员（`ADMIN`）角色

### 对应接口

- `POST /api/user/register`：用户注册
- `POST /api/user/login`：用户登录
- `GET /api/user/{id}`：获取用户信息
- `PUT /api/user/{id}`：修改个人信息

### 实现说明

当前项目采用的是**轻量级登录方案**：

- 用户登录成功后，前端将用户对象保存到 `localStorage`
- 页面通过 `Auth.check()` 判断是否登录
- 页面导航栏会根据用户状态动态切换

这类实现方式简单直接，适合课程设计和原型系统，但在真实生产环境中建议升级为 **Session / JWT / Spring Security** 方案。

---

## 5.2 活动模块

### 已实现功能

- 发布活动
- 查看活动列表
- 查看活动详情
- 按关键字搜索活动
- 按活动类型筛选活动
- 查看“我发布的活动”
- 修改活动
- 删除活动
- 管理员查看所有活动

### 活动类型

- `ONLINE`：线上活动
- `OFFLINE`：线下活动
- `HYBRID`：混合活动

### 对应接口

- `POST /api/activity?userId={userId}`：创建活动
- `GET /api/activity/list`：查询活动列表（支持 `keyword`、`type`、`userId`）
- `GET /api/activity/my?userId={userId}`：查看我发布的活动
- `GET /api/activity/admin/all?userId={userId}`：管理员查看全部活动
- `GET /api/activity/{id}?userId={userId}`：查看活动详情
- `PUT /api/activity/{id}?userId={userId}`：修改活动
- `DELETE /api/activity/{id}?userId={userId}`：删除活动

### 实现特点

- 活动列表会动态补充以下信息：
  - 发起人昵称/用户名
  - 当前报名人数
  - 点赞数
  - 当前用户是否已点赞
  - 当前用户是否已收藏
  - 当前用户是否已报名
- 删除活动时，系统会对相关数据进行**手动级联删除**，包括：
  - 评论点赞
  - 评论
  - 活动点赞
  - 收藏
  - 签到
  - 报名记录
  - 相关通知

这使得删除逻辑更完整，避免产生脏数据。

---

## 5.3 报名模块

### 已实现功能

- 用户报名活动
- 用户取消报名
- 查看我的报名记录
- 活动发起人查看报名名单

### 对应接口

- `POST /api/registration?activityId={activityId}&userId={userId}`：报名活动
- `DELETE /api/registration?activityId={activityId}&userId={userId}`：取消报名
- `GET /api/registration/my?userId={userId}`：查看我的报名
- `GET /api/registration/activity/{activityId}?userId={userId}`：活动发起人查看报名名单

### 报名逻辑校验

系统在报名时做了较完整的业务判断：

- 不允许重复报名
- 活动不存在时无法报名
- 活动已结束时无法报名
- 活动状态不是 `ACTIVE` 时无法报名
- 活动人数已满时无法报名

---

## 5.4 签到模块

### 已实现功能

- 活动签到
- 查询签到状态

### 对应接口

- `POST /api/checkin?activityId={activityId}&userId={userId}`：签到
- `GET /api/checkin/status?activityId={activityId}&userId={userId}`：获取签到状态

### 签到规则

- 只有已报名用户才能签到
- 签到时间从**活动开始前 10 分钟**开放
- 活动结束后不能签到
- 不允许重复签到

这部分逻辑能够很好体现系统对“业务规则”的封装能力。

---

## 5.5 评论与互动模块

### 已实现功能

- 对活动发表评论
- 查看某个活动下的评论列表
- 删除评论
- 活动点赞 / 取消点赞
- 评论点赞 / 取消点赞
- 活动收藏 / 取消收藏
- 查看我的收藏
- 分享活动链接
- 分享评论链接（带评论锚点）

### 对应接口

#### 评论相关

- `POST /api/comment`：发表评论
- `GET /api/comment/activity/{activityId}?userId={userId}`：获取活动评论
- `DELETE /api/comment/{id}?userId={userId}`：删除评论

#### 互动相关

- `POST /api/interaction/like/activity?activityId={activityId}&userId={userId}`：活动点赞/取消点赞
- `POST /api/interaction/like/comment?commentId={commentId}&userId={userId}`：评论点赞/取消点赞
- `POST /api/interaction/favorite?activityId={activityId}&userId={userId}`：收藏/取消收藏
- `GET /api/interaction/favorites?userId={userId}`：获取我的收藏

### 权限设计

- 评论删除：仅评论作者或管理员可以删除
- 活动修改/删除：仅活动发起人或管理员可以操作

---

## 5.6 消息通知模块

### 已实现功能

- 获取消息列表
- 获取未读消息数量
- 单条消息设为已读
- 全部消息设为已读
- 评论活动时通知发起人
- 点赞活动时通知发起人
- 点赞评论时通知评论作者
- 活动开始前自动提醒报名用户

### 对应接口

- `GET /api/notification/list?userId={userId}`：获取消息列表
- `GET /api/notification/unread-count?userId={userId}`：获取未读消息数量
- `PUT /api/notification/{id}/read`：单条已读
- `PUT /api/notification/read-all?userId={userId}`：全部已读

### 通知类型

系统中已经定义了以下通知类型：

- `REMINDER_1DAY`：活动开始前 1 天提醒
- `REMINDER_10MIN`：活动开始前 10 分钟签到提醒
- `COMMENT`：有人评论了你的活动
- `LIKE_ACTIVITY`：有人点赞了你的活动
- `LIKE_COMMENT`：有人点赞了你的评论

---

## 5.7 定时任务模块

项目启用了 `@EnableScheduling`，并在 `ScheduleService` 中实现了定时提醒功能。

### 当前定时任务

系统每分钟执行一次检查：

1. **活动开始前 24 小时提醒**
2. **活动开始前 10 分钟签到提醒**

### 设计说明

- 系统会查询即将开始的活动
- 再遍历已报名用户发送站内通知
- 同一类型提醒做了“是否已存在”的判断，避免重复发送

这个模块是项目中非常具有展示价值的功能点之一，体现了系统不只是 CRUD，还包含时间驱动型业务逻辑。

---

## 6. 页面功能说明

项目采用 Thymeleaf 页面模板，当前主要页面包括：

- `/`：首页
- `/login`：登录页
- `/register`：注册页
- `/activities`：活动列表页
- `/activity/{id}`：活动详情页
- `/create-activity`：发布活动页
- `/edit-activity/{id}`：编辑活动页（复用发布页面）
- `/my-activities`：我发布的活动
- `/my-registrations`：我的报名
- `/my-favorites`：我的收藏
- `/notifications`：消息通知页
- `/profile`：个人中心页

前端静态资源主要位于：

- `src/main/resources/static/css/style.css`
- `src/main/resources/static/js/main.js`

其中 `main.js` 统一封装了：

- 登录状态管理
- API 请求方法
- 日期格式化方法
- 活动类型标签渲染
- 签到按钮状态判断
- 分享链接复制与 Toast 提示
- 导航栏更新与未读消息数刷新

---

## 7. 数据库设计

项目提供了 `bookclub.sql` 初始化脚本，核心表如下：

- `user`：用户表
- `activity`：活动表
- `registration`：报名记录表
- `check_in`：签到记录表
- `comment`：评论表
- `activity_like`：活动点赞表
- `comment_like`：评论点赞表
- `favorite`：收藏表
- `notification`：站内消息表

### 数据关系概览

- 一个用户可以创建多个活动
- 一个用户可以报名多个活动，一个活动也可被多个用户报名
- 一个用户可对多个活动点赞/收藏
- 一个活动下可以有多条评论
- 评论也支持点赞
- 通知与用户关联，部分通知同时关联活动

### 初始化数据

SQL 脚本中内置了两类测试数据：

#### 管理员账号

- 用户名：`admin`
- 密码：`admin123`

#### 普通测试账号

- 用户名：`user1`
- 密码：`123456`

#### 示例活动

- 《深度工作》线上读书分享会
- 《人类简史》线下读书会

---

## 8. 核心代码架构说明

## 8.1 分层设计

项目整体采用典型的 Spring Boot 三层架构：

### Controller 层

负责接收前端请求、处理参数、返回统一结果。

主要控制器包括：

- `UserController`
- `ActivityController`
- `RegistrationController`
- `CheckInController`
- `CommentController`
- `InteractionController`
- `NotificationController`
- `PageController`

### Service 层

负责封装业务逻辑，是整个项目的核心。

主要服务包括：

- `UserService`
- `ActivityService`
- `RegistrationService`
- `CheckInService`
- `CommentService`
- `InteractionService`
- `NotificationService`
- `ScheduleService`

### Repository 层

负责数据持久化，基于 Spring Data JPA 自动生成常见 CRUD 方法，并通过自定义查询补充业务场景。

---

## 8.2 实体设计特点

项目中的实体类大量使用了 `@Transient` 字段，用于承载**非数据库字段但页面需要展示的数据**，例如：

- 活动发起人名称 `creatorName`
- 当前报名人数 `currentParticipants`
- 当前用户是否点赞 `liked`
- 当前用户是否收藏 `favorited`
- 当前用户是否报名 `registered`
- 评论用户名 `userName`
- 收藏记录中的活动对象 `activity`

这种方式能在不破坏表结构的前提下，增强接口返回的可读性和页面展示能力。

---

## 8.3 统一返回结果封装

项目使用 `Result<T>` 作为统一响应结构，便于前后端协作。

典型返回内容通常包含：

- 状态码
- 消息
- 数据对象

这种设计让前端能统一处理成功、失败和提示信息。

---

## 9. 本地运行指南

## 9.1 环境要求

请先确保本地已安装以下环境：

- JDK 8
- MySQL 8.x
- Maven 3.6+（或者直接使用项目自带 `mvnw`）

---

## 9.2 创建数据库

先执行项目根目录下的 SQL 脚本：

```sql
source bookclub.sql;
```

或手动导入 `bookclub.sql` 文件。

---

## 9.3 修改数据库配置

编辑文件：

```properties
src/main/resources/application.properties
```

当前默认配置为：

```properties
server.port=8083

spring.datasource.url=jdbc:mysql://localhost:3306/bookclub?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect

spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

请根据你的本地 MySQL 用户名和密码修改以下配置：

- `spring.datasource.username`
- `spring.datasource.password`

---

## 9.4 启动项目

### 方式一：使用 Maven

```bash
mvn spring-boot:run
```

### 方式二：使用 Maven Wrapper

Linux / macOS：

```bash
./mvnw spring-boot:run
```

Windows：

```bash
mvnw.cmd spring-boot:run
```

### 方式三：打包后运行

```bash
mvn clean package
java -jar target/bookclub-1.0.0.jar
```

---

## 9.5 访问系统

项目启动后，在浏览器中访问：

```text
http://localhost:8083
```

---

## 10. 默认账号

### 管理员

- 用户名：`admin`
- 密码：`admin123`

### 普通用户

- 用户名：`user1`
- 密码：`123456`

---

## 11. 典型业务流程

## 11.1 普通用户使用流程

1. 注册并登录系统
2. 浏览活动列表，按关键词/类型筛选
3. 查看活动详情
4. 报名参加活动
5. 在活动开始前 10 分钟进行签到
6. 活动结束后发表评论或点赞互动
7. 收藏感兴趣的活动，方便后续查看
8. 在消息中心查看通知和提醒

## 11.2 发起人使用流程

1. 登录系统
2. 发布新的读书会活动
3. 在“活动管理”中查看自己发布的活动
4. 编辑活动信息或删除活动
5. 查看某场活动的报名名单
6. 收到用户评论、点赞等通知

## 11.3 管理员使用流程

1. 使用管理员账号登录
2. 查看所有活动
3. 对活动进行维护与删除
4. 具备更高权限处理内容管理问题

---

## 12. 项目特色与可展示点

- **不仅仅是基础 CRUD**，而是包含报名校验、签到时间窗口、权限控制、提醒通知等真实业务逻辑
- **具备社交互动属性**，评论、点赞、收藏增强了系统完整性
- **具备定时任务能力**，体现了对“事件驱动 / 时间驱动业务”的处理
- **前后端联动清晰**，适合向老师或面试官展示从数据库到页面交互的完整开发过程
- **分层结构规范**，后续容易扩展为前后端分离项目

---

## 13. 当前项目的不足与可优化方向

### 安全性方面

- 当前密码采用明文存储与比对，不适合生产环境
- 登录状态保存在 `localStorage`，缺少真正的鉴权机制
- 建议后续引入：
  - Spring Security
  - JWT
  - BCrypt 密码加密

### 功能方面

可以继续扩展：

- 活动封面上传
- 书籍信息管理
- 评论回复功能
- 活动报名审核
- 活动二维码签到
- 管理后台仪表盘
- 数据统计与可视化
- 邮件 / 短信提醒
- 分页查询与排序
- Redis 缓存热门活动

### 工程化方面

可以继续优化：

- 全局异常处理
- 参数校验（`@Valid`）
- 日志记录规范化
- 接口文档（Swagger / OpenAPI）
- 单元测试与集成测试完善
- Docker 部署与 CI/CD

---

## 14. 后续维护建议

为了便于团队协作或二次开发，建议后续统一以下规范：

- Controller 只负责请求处理，不写复杂业务逻辑
- Service 负责完整业务校验与组合逻辑
- Repository 只处理数据访问
- 统一返回结构与异常处理
- 对角色权限做更标准化设计

---

## 15. 致谢

本项目用于读书会活动在线管理场景的系统实践，适合作为 Spring Boot Web 项目的综合练习案例。
