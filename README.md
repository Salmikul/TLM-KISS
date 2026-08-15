# TLM Kiss Addon（东方小女仆亲吻附属）

Minecraft 1.20.1 **Fabric** 附属模组，为 [Touhou Little Maid](https://github.com/TartaricAcid/TouhouLittleMaid) 的 Fabric 移植版添加**自定义按键亲吻女仆**功能。

## 功能

- 默认按键 **K**（可在 控制 → 按键绑定 → TLM 亲吻附属 中修改）
- 准星指向女仆，或附近 8 格内最近的女仆
- 触发后：
  1. 女仆周围生成大量爱心粒子
  2. 镜头平滑放大（FOV 降低，ease-in-out 曲线）
  3. 短暂保持 + 轻微朝向女仆
  4. 平滑恢复原 FOV

整个动画约 2 秒，纯客户端视觉效果，不需要服务器权限。

## 依赖

- Minecraft 1.20.1
- Fabric Loader ≥ 0.15
- Fabric API
- **touhou_little_maid**（本仓库对应的 Fabric 移植版：`touhoulittlemaid-fabric-0.8.2-forge1.5.3+mc1.20.1`）

## 构建方法

1. 把 `touhoulittlemaid-fabric-0.8.2-forge1.5.3+mc1.20.1.jar` 放到项目根目录的 `libs/` 文件夹。
2. 确保安装了 JDK 17+。
3. 在项目根目录执行：

```bash
./gradlew build
```

（如果没有 gradlew，可先用 IntelliJ 打开项目让它自动生成，或从 Fabric 官方示例模组复制 wrapper。）

构建产物位于：

```
build/libs/tlm-kiss-addon-1.0.0.jar
```

把该 jar 与原版 Touhou Little Maid 一起放入 `mods` 文件夹即可。

## 项目结构

```
src/
├── main/java/.../TlmKissAddon.java          # 主入口
├── client/java/.../client/
│   ├── TlmKissClient.java                   # 按键注册 + 检测女仆 + 粒子
│   └── KissCameraHandler.java               # 平滑 FOV 动画 + 轻微看向
└── client/java/.../mixin/CameraMixin.java   # 修改 GameRenderer.getFov
```

## 自定义

- 修改默认按键：`TlmKissClient.java` 中的 `GLFW.GLFW_KEY_K`
- 调整放大程度：`KissCameraHandler.TARGET_FOV_SCALE`（0.55 ≈ 放大到原 FOV 的 55%）
- 调整动画时长：`ZOOM_IN_TICKS` / `HOLD_TICKS` / `ZOOM_OUT_TICKS`

## 许可

MIT

## 注意

当前沙箱环境因文件系统限制无法完成 Fabric Loom 的完整构建（写入 mapped Minecraft POM 时 I/O 错误）。
请在本地（Windows / macOS / Linux）使用 JDK 17+ 执行 `./gradlew build` 即可得到 jar。

产物路径：`build/libs/tlm-kiss-addon-1.0.0.jar`
