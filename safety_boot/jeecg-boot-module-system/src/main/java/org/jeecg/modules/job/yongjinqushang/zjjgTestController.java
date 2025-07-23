package org.jeecg.modules.job.yongjinqushang;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName zjjgTestController：
 * @Description 线上IP为白名单，测试接口用
 * @Author 55314
 * @Date 2023/3/20 16:08
 * @Version 1.0
 **/
@Api(tags = "zjjg_test")
@RestController
@RequestMapping("/zjjg/zjjgTestController")
@Slf4j
public class zjjgTestController {
    private static final String CLIENT_CODE = "gaoxun";
    @PostMapping(value = "/posttest")
    public Result<?> test(@RequestParam(name = "url", required = true) String url, @RequestBody String args) {
        try {
            // 验证URL参数
            if (StringUtils.isBlank(url)) {
                return Result.error("URL参数不能为空");
            }

            // 设置请求头和请求体
            HttpResponse response = HttpRequest.post(url)
                    .header("client-code", CLIENT_CODE)
                    .body(args)
                    .timeout(25000)
                    .execute();

            // 检查响应状态码
            if (response.isOk()) {
                String result = response.body();
                return Result.OK(result);
            } else {
                log.error("请求失败，状态码：{}", response.getStatus());
                return Result.error("请求失败");
            }
        } catch (Exception e) {
            log.error("请求发生异常", e);
            return Result.error("请求发生异常"+e);
        }
    }
}
