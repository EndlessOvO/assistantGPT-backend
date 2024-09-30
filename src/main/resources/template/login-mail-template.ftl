<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="email code">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  </head>
  <body>
    <div style="height: 100%; width: 100%;display: flex; align-items: center;justify-content: center;justify-items: center;">
      <div style="width: 675px; height: 300px;background-color: aliceblue;text-align: center;border-radius: 20px;box-shadow: 0px 0px 5px 2px #ccc;background-image:linear-gradient(135deg,#FFD26F 15%,#3677FF 100%)">
        <div style="font-size: 14px;">
          <div style="font-size: 38px;font-weight: bold;margin-top: 8%;">[Assistant GPT] 登录链接</div>
          <div style="margin-top: 4%">
            <div>
              这是您登录
              <a href="#" target="_blank" style="text-decoration: none;font-size: 18px;font-weight: bolder;"> ASSISTANT GPT </a>
              的链接：<a href="{0}" target="_blank">点击链接登录</a>。
            </div>
            <div>请注意：此链接将在 5 分钟后过期。</div>
            <div style="margin-top: 1.5%;">如果您未请求此链接，您可以直接忽略此电子邮件。</div>
          </div>
          <div style="margin-top: 3%;font-size: 18px;font-weight: bold;">系统邮件，请勿回复</div>
        </div>
      </div>
    </div>
  </body>
</html>