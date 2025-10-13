[![Platform][1]][2] [![GitHub license][3]][4]  [![GitHub license][5]][6] 

[1]:https://img.shields.io/badge/platform-Android-blue.svg  
[2]:https://github.com/hegaojian/JetpackMvvm
[3]:https://img.shields.io/github/release/hegaojian/JetpackMvvm.svg
[4]:https://github.com/hegaojian/JetpackMvvm/releases/latest
[5]:https://img.shields.io/badge/license-Apache%202-blue.svg
[6]:https://github.com/hegaojian/JetpackMvvm/blob/master/LICENSE

# 🏗 JetpackMvvm

**JetpackMvvm** 是一个基于 Jetpack 架构组件构建的 Android MVVM 快速开发框架，旨在帮助开发者快速搭建高质量、可维护、可扩展的应用。

---

## ✨ 框架特性

- 🧠 **标准化 MVVM 架构设计**  
  基于 ViewModel、LiveData、Repository 的分层体系，提供清晰、可维护的应用结构。

- ⚡ **协程驱动的响应式数据流**  
  全面采用 Kotlin Coroutines 进行异步管理，天然支持挂起函数与结构化并发。

- 🔄 **灵活的加载与状态管理机制**  
  内置统一的页面状态切换（加载中 / 空 / 错误 / 成功），支持全局配置与自定义样式。

- 🧩 **高度模块化的基类封装**  
  提供 BaseActivity、BaseFragment、BaseViewModel 等基础实现，快速构建页面逻辑。

- 🪶 **无侵入式视图绑定支持**  
  兼容 ViewBinding 与 DataBinding，减少模板代码，让开发更轻量高效。

- 🌐 **网络层可插拔设计**  
  简洁而灵活的封装，可与 Retrofit、OkHttp 或任意网络框架无缝配合使用。

- 💎 **丰富的便捷工具集**  
  内置常用封装：本地自动缓存、Glide 图片加载、Gson 数据解析、日志打印等，开箱即用。
  
- 🌈 **完整示例工程，助你快速上手**   
  基于**玩Android API** 开发了一个示例,注释非常详细，App展示框架在真实项目中的使用方式与开发流程。
---

## 📦 玩Android APK 下载体验

### 🔗 下载渠道

- [🌍 GitHub 下载](https://github.com/hegaojian/JetpackMvvm/releases/download/2.0.0/app-release.apk)
- [🚀 第三方下载（推荐）](https://www.pgyer.com/jjbeautiful)

### 📱 扫码下载（推荐）

<p align="left">
  <img src="https://github.com/user-attachments/assets/288a73f1-2e10-404a-8fc5-2acb9b1799ed" width="150" height="150" alt="JetpackMvvm Demo 二维码"/>
</p>


## 📖 目录导航（补全中）

| 模块 | 说明 |
|------|------|
| [快速开始](https://github.com/hegaojian/JetpackMvvm/wiki/Getting‐Started) | 一步步构建第一个 基于JetpackMvvm的应用 |
| [架构设计](./Architecture) | 框架架构图与核心思路 |
| [核心模块说明](./Core-Modules) | Base、Core、Ext、NetWork 等模块介绍 |
| [扩展功能](./Advanced) | 协程封装、状态切换、全局配置等 |
| [常见问题](./FAQ) | 常见使用问题与解决方案 |
| [贡献指南](./Contributing) | 如何参与贡献与提交 PR |
| [版本变更记录](./Changelog) | 更新历史与版本说明 |

---

## 💬 联系与支持

- 💡 欢迎通过 [Issues](https://github.com/hegaojian/JetpackMvvm/issues) 提交问题或建议  
- ❤️ 如果这个框架帮助到你，请帮忙点一个 ⭐ 支持一下  
- 📫 QQ交流群：419581249

---
## License
``` license
 Copyright 2019, hegaojian(何高建)       
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

