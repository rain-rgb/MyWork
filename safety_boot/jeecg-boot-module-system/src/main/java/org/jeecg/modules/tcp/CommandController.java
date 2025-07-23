package org.jeecg.modules.tcp;


import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: 消息
 * @author: jeecg-boot
 * @date: 2019-04-09
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/systems/ssoredirect")
public class CommandController extends JeecgController<SysMessage, ISysMessageService> {

    private final TcpClient tcpClient;
    private final SocketPool socketPool;
//    @Autowired
//    private  ISysConfigService sysConfigService;

    // 初始化连接池和 TCP 客户端
    public CommandController() {
        // SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.CONTROL_LMD_DEVICE);
        String address = "47.97.158.215";// selectsysconfigone.getCurdate();
        int port = 8030; // selectsysconfigone.getCurid();
        this.socketPool = new SocketPool(address, port); // 替换为目标服务器的 IP 和端口
        this.tcpClient = new TcpClient(socketPool);
    }

    // HTTP POST 接口，接收命令并发送到 TCP 服务器
    @AutoLog(value = "龙门吊- 控制器接口")
    @ApiOperation(value = "龙门吊- 控制器接口", notes = "龙门吊- 控制器接口")
    @PostMapping(value = "/controlByWeb")
    public Result<?> sendCommand(@RequestBody String command) {
        String msg = "";
//        JSONObject jsonObject = new JSONObject(command);

        try {
//            String s = tcpClient.sendCommand((String) jsonObject.get("command"));
            String s = tcpClient.sendCommand(command);
            msg = "命令已发送: " + command + ";返回：" + s;
            return Result.OK(msg);
        } catch (Exception e) {
            msg = "发送命令失败fail: " + e.getMessage();
            return Result.error(msg);
        }

    }

}
