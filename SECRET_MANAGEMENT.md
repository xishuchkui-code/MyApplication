# 密钥管理说明

## 概述
本项目使用环境变量来管理敏感信息（API密钥、密钥等），防止将敏感信息上传到 GitHub。

## 文件说明

### .env 文件
- **用途**: 存储本地开发环境的敏感信息
- **位置**: 项目根目录
- **git 忽略**: 已在 .gitignore 中添加，不会被上传
- **创建方法**: 复制 `.env.example` 并改名为 `.env`，然后填入实际的密钥值

### .env.example 文件
- **用途**: 作为 `.env` 文件的模板/示例
- **位置**: 项目根目录
- **git 上传**: 会被上传到 GitHub（作为配置模板）
- **用途**: 团队成员可以看到需要配置哪些变量

## 使用步骤

### 1. 初始化配置
```bash
# 在项目根目录复制模板文件
cp .env.example .env

# 编辑 .env 文件，填入实际的密钥值
# 使用你的文本编辑器打开 .env
```

### 2. 在代码中使用密钥
使用 `ConfigManager` 类来获取配置值：

```java
// 在 MainActivity 中使用
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化配置管理器
        ConfigManager.init(this);
        
        // 获取配置值
        String apiKey = ConfigManager.getApiKey();
        String teacherUrl = ConfigManager.getTeacherUrl();
        boolean isDebug = ConfigManager.isDebugMode();
    }
}
```

## 工作原理

1. **ConfigManager 类**: 
   - 优先从 `.env` 文件（通过 local.properties）读取配置
   - 如果没有找到，则从系统环境变量读取
   - 如果都没有，使用默认值

2. **git 忽略 .env**:
   - `.env` 文件在 `.gitignore` 中
   - 确保本地密钥不会被上传到 GitHub

3. **共享模板**:
   - `.env.example` 被上传到 GitHub
   - 团队成员可以看到需要配置的变量

## 安全性说明

✅ **推荐做法**:
- 在 `.env.example` 中写入变量名和说明
- 在 `.env` 中填入实际的密钥值
- 只上传 `.env.example` 到 GitHub

❌ **不要做的事**:
- 不要在代码中硬编码密钥
- 不要将 `.env` 上传到 GitHub
- 不要在代码注释中写密钥

## 环境变量列表

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `API_KEY` | API 密钥 | default_api_key |
| `SECRET_KEY` | 密钥 | default_secret_key |
| `TEACHER_URL` | 教师示例 URL | https://www.httpbin.org/image/png |
| `TEXT_URL` | 文本 URL | https://www.baidu.com |
| `IMAGE_URL` | 图片 URL | https://www.baidu.com/img/flexible/logo/pc/result.png |
| `DEBUG` | 调试模式 | false |

## 常见问题

**Q: 如果我把密钥不小心上传了怎么办？**
A: 
1. 立即轮换/撤销该密钥
2. 从 Git 历史中删除: `git filter-branch --tree-filter 'rm -f .env' -- --all`
3. 强制推送: `git push --force`

**Q: 如何在不同的开发环境使用不同的配置？**
A: 可以创建多个文件，如 `.env.development`, `.env.production`，然后在 ConfigManager 中根据条件加载不同的文件。

**Q: 如何在 CI/CD 流程中使用密钥？**
A: 在 CI/CD 系统中（如 GitHub Actions）设置环境变量，ConfigManager 会自动从系统环境变量中读取。
