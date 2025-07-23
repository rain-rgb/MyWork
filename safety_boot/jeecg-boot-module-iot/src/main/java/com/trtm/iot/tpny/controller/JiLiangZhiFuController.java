package com.trtm.iot.tpny.controller;

import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.RSAUtil;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 摊铺碾压数据监测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-19
 * @Version: V1.0
 */

@RestController
@RequestMapping("/jiliangzhifu")
@Slf4j
public class JiLiangZhiFuController extends JeecgController<FrontDeviceRealdata, IFrontDeviceRealdataService> {

	/**
	 * 平安守护跳转
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/findToken")
	public String index1() throws Exception{
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApl9NXKjUy8irc67RKqpS4gDLxphPhV/p92T5lIVo8WbVc1vxtvd3WCf5mfvmV2CMcNdmUOnSHynHcpCq5MhUYpAtEjDl5IYGw2DjMzfgzYT63FFXWYn1vo5zba+fPz94vEhLPOx9BczG3U1AVx76W2CzSgmRKbIYwMYF0HWTKd+73V22f1Fvgi3Wl3Tk4YzvVh/a/BKQbfYdCSczl8WPQsu92aEeSwdqeeDaQU3ob9DlRVD30rDlmKxH8fVMUv3ezcOJgncvNtI502fxcgxSax1reZSuMG99i3gDQ/F7y5yLsMDR8AhQBnnPB5MvIZ4oMatE9Nf9Z2cmOU1rwAsxFwIDAQAB";
		String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCmX01cqNTLyKtzrtEqqlLiAMvGmE+FX+n3ZPmUhWjxZtVzW/G293dYJ/mZ++ZXYIxw12ZQ6dIfKcdykKrkyFRikC0SMOXkhgbDYOMzN+DNhPrcUVdZifW+jnNtr58/P3i8SEs87H0FzMbdTUBXHvpbYLNKCZEpshjAxgXQdZMp37vdXbZ/UW+CLdaXdOThjO9WH9r8EpBt9h0JJzOXxY9Cy73ZoR5LB2p54NpBTehv0OVFUPfSsOWYrEfx9UxS/d7Nw4mCdy820jnTZ/FyDFJrHWt5lK4wb32LeAND8XvLnIuwwNHwCFAGec8Hky8hnigxq0T01/1nZyY5TWvACzEXAgMBAAECggEAXYMLlJ1Anhn4ZFlKL9T0KRuRbdqGN80lUGxs69Hgnm7SYri3L8QATIiLfsVbPpmB1pr7Y1dJR0LQW9B8fYMsCwpGiwb48G10t7Rpf4xUyq+H4PXHRwiIG8ctkeU4ePBQCGQKBgBWdUAYNqMJGu7KgTnlDCQ8d1cnGpMzCWxMfyfeCJ52LV2fGv7GKfeEv53GpNB0nvPT5048TvNFZjUsfOlyFGzKYVUI4nTkRmZfOGcCLUwmvH0MsVZ/75WaPGbeg3z3N6vA0HI5erlWLBz9yX6J0tjGltWiwa/lox67qgogrj59jRpUpZ8mBhzosZ12UKZNAfaUHee8QJg77i+FIQKBgQDQ1gWvZ3EccSG1jH2WkV50HLoqja5LYwiLHL8lJ8SSI/oZDsyPhUD7xLB7HA/7DKKkwJBxFoO1/uD2WubCSlTrrceGQxv5HOanvb6d5nN2Hz3Z8zJrBJvZpvAGzfeWtCmgMNBwyOPxx5VYfGg4bGyTiWVykOCDKEVYAtYOSCccFQKBgQDL8jaYENEWYJhBVwo31HV95xdJTSs7/HxKzj9iWiLUPdQ8bBl6vhtQmugadZcRLFFmUquJtIFoi0nvDmoft4j9D4DYRBbZuWGegQ6JL+T17/7nDXDKjfSLBHQkeGRfgzZ3WnjKQ2oTWxT5ftUhByhaGSHIb8i7ro0RAt4+wvOnewKBgQCWVZueG28XtHE5yjsWSuud1phFzdxstIwP6M/JJYmiUnfV1gt4Pcr78ynAVgFDDmN/CohwgG6scyDq96EquGvNhToe8uyONjntAvDXpm/z1qWl4ianV5gB5OThYSAcwvE8OiezKUq+YJioLFayV4sJ4ly/Tn/wqurQYqpJebN9yQKBgQCoHY6BRYOFpbR8UAp9QSZk1YStiO41sYinhWJcaZ7CRAFamWVbd7A+fqMLhJk1r8mdxlHe3qu6CO+2HrS+VmWW7RuxqlcHvLfKl+nqjk93qBEAV2T9o4zMt6whkBh1X00bj7l4SZ/8Op2MmM+JPUQ3GPZvKi5W/6kLN8kKBVwiVwKBgQDE4aaLzfOEZg1thyrcJKayRbNDHBCI8EizPSRuqvM+9AUDovofujL7lZQgmFpsrF0RJtDNvX/L4q7tvS2Mz4ZcRtQaIM2UbkfWK1IgvuTcqYkcBAgpV+dLT5BQZA61aQOrNr8xgFIt5ovPgvKrzbDyGfOr9LP3a0tx1A/+Z1khYw==";
		Map<String, Object> claim = new HashMap<>();
		claim.put("username", "王志云");
		String token = JwtUtils.generateToken(claim, RSAUtil.getPrivateKey(privateKey), 30);
		System.out.println(token);
		Object username = JwtUtils.getInfoFromToken(token, RSAUtil.getPublicKey(publicKey)).get("username");
		System.out.println(username);

		return token;
	}


}
