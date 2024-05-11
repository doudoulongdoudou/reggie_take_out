package com.ddl.reggie.utils;

import com.aliyun.tea.*;

/**
 * ClassName: SMSUtils
 * Package: com.ddl.reggie.utils
 * Description:
 * 阿里云短信服务
 *
 * @Author 豆豆龙
 * @Create 3/5/2024 下午9:27
 */

public class SMSUtils {

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dysmsapi20170525.Client createClient() throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId("LTAI5tLKXm6RkRGpMHohcH6b")
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret("TndBh990cqjZevJL3Qtf4dYPbAkZIN");
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 发送验证码
     * @param signName
     * @param templateCode
     * @param phoneNumbers
     * @param param
     * @throws Exception
     */
    public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(signName,templateCode,phoneNumbers,param);
        com.aliyun.dysmsapi20170525.Client client = SMSUtils.createClient();
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                //.setSignName("阿里云短信测试")
                .setSignName(signName)
                //.setTemplateCode("SMS_154950909")
                .setTemplateCode(templateCode)
                //.setPhoneNumbers("15268269261")
                .setPhoneNumbers(phoneNumbers)
                //.setTemplateParam("{\"code\":\"6666\"}");
                .setTemplateParam("{\"code\":\""+param+"\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            System.out.println(error.getMessage());
            // 诊断地址
            System.out.println(error.getData().get("Recommend"));
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }

}
