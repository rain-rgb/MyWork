import paho.mqtt.client as mqtt
import json
import time
import random
import base64
import threading
from colorama import Fore, Style, init

# 初始化颜色
init(autoreset=True)

# MQTT配置
BROKER = "47.97.158.215"
PORT = 8881
APP_NAME = "test_app"
EDGE_DEVICE_ID = "1000000001154323"
END_DEVICE_ID = "3000000005980004"

# 存储接收到的消息
received_messages = []

# 创建MQTT客户端
client = mqtt.Client(client_id=f"test_client_{int(time.time())}")


def on_connect(client, userdata, flags, rc):
    print(Fore.GREEN + f"连接成功: 状态码={rc}")
    # 订阅响应主题
    client.subscribe(f"/v1/{APP_NAME}/topo/response", 0)
    client.subscribe(f"/v1/{APP_NAME}/service/response", 0)
    client.subscribe(f"/v1/{APP_NAME}/service/reply", 0)
    client.subscribe(f"/v1/{APP_NAME}/app/reply", 0)
    client.subscribe(f"/v1/{APP_NAME}/service/data", 0)


def on_message(client, userdata, msg):
    # 存储接收到的消息
    message = {
        "topic": msg.topic,
        "payload": json.loads(msg.payload.decode())
    }
    received_messages.append(message)

    # 打印接收到的消息
    print(Fore.CYAN + f"\n{'=' * 80}")
    print(Fore.YELLOW + f"收到响应: 主题={msg.topic}")
    print(Fore.CYAN + "完整响应内容:")
    print(json.dumps(json.loads(msg.payload.decode()), indent=2))
    print(Fore.CYAN + f"{'=' * 80}")


client.on_connect = on_connect
client.on_message = on_message
client.connect(BROKER, PORT, 60)
client.loop_start()

# 测试数据
test_cases = [
    {
        "name": "端设备添加",
        "topic": f"/v1/{APP_NAME}/topo/request",
        "payload": {
            "type": "CMD_TOPO_ADD",
            "mid": int(time.time() * 1000),
            "timestamp": int(time.time() * 1000),
            "expire": -1,
            "param": {
                "nodeInfos": [
                    {
                        "nodeId": f"testSN{random.randint(100, 999)}",
                        "name": "test_device",
                        "description": "测试设备",
                        "mfgInfo": "TestMFG",
                        "nodeModel": "TestModel"
                    }
                ]
            }
        }
    },
    {
        "name": "端设备删除",
        "topic": f"/v1/{APP_NAME}/topo/request",
        "payload": {
            "type": "CMD_TOPO_DEL",
            "mid": int(time.time() * 1000),
            "timestamp": int(time.time() * 1000),
            "expire": -1,
            "param": {
                "nodeIds": [f"testSN{random.randint(100, 999)}"]
            }
        }
    },
    {
        "name": "端设备更新",
        "topic": f"/v1/{APP_NAME}/topo/request",
        "payload": {
            "type": "CMD_TOPO_UPDATE",
            "mid": int(time.time() * 1000),
            "timestamp": int(time.time() * 1000),
            "expire": -1,
            "param": {
                "nodeStatuses": [
                    {
                        "deviceId": END_DEVICE_ID,
                        "status": random.choice(["ONLINE", "OFFLINE"])
                    }
                ]
            }
        }
    },
    {
        "name": "数据获取请求",
        "topic": f"/v1/{APP_NAME}/service/request",
        "payload": {
            "type": "CMD_RPC",
            "mid": int(time.time() * 1000),
            "timestamp": int(time.time() * 1000),
            "deviceId": EDGE_DEVICE_ID,
            "param": {
                "method": "getDeviceStatus",
                "paras": {"deviceId": END_DEVICE_ID}
            }
        }
    },
    {
        "name": "业务控制命令",
        "topic": f"/v1/{APP_NAME}/service/command",
        "payload": {
            "type": "CMD_SERVICE",
            "mid": int(time.time() * 1000),
            "deviceId": END_DEVICE_ID,
            "timestamp": int(time.time() * 1000),
            "expire": -1,
            "app": APP_NAME,
            "param": {
                "cmd": "control_device",
                "paras": {
                    "command": random.choice(["start", "stop", "reset"])
                }
            }
        }
    },
    {
        "name": "业务配置更新",
        "topic": f"/v1/{APP_NAME}/app/command",
        "payload": {
            "type": "CMD_APP_REMOTE_CONFIG_DOWN",
            "mid": int(time.time() * 1000),
            "deviceId": EDGE_DEVICE_ID,
            "timestamp": int(time.time() * 1000),
            "expire": -1,
            "param": {
                "container": "test_container",
                "appName": APP_NAME,
                "name": "config.json",
                "configFile": base64.b64encode(b'{"setting": "value"}').decode()
            }
        }
    },
    {
        "name": "数据主动上报",
        "topic": f"/v1/{APP_NAME}/service/data",
        "payload": {
            "type": "CMD_REPORTDATA",
            "mid": int(time.time() * 1000),
            "deviceId": END_DEVICE_ID,
            "timestamp": int(time.time() * 1000),
            "param": {
                "cmd": "device_status",
                "deviceId": END_DEVICE_ID,
                "data": {
                    "temperature": random.uniform(20.0, 40.0),
                    "humidity": random.uniform(30.0, 80.0),
                    "status": random.choice(["normal", "warning", "error"])
                }
            }
        }
    }
]


# 打印发送的消息
def print_sent_message(test_case):
    print(Fore.GREEN + f"\n{'=' * 80}")
    print(Fore.YELLOW + f"发送测试数据 ({test_case['name']})")
    print(Fore.CYAN + f"主题: {test_case['topic']}")
    print(Fore.CYAN + "完整发送内容:")
    print(json.dumps(test_case['payload'], indent=2))
    print(Fore.GREEN + f"{'=' * 80}")


# 打印所有接收到的消息
def print_received_messages():
    if received_messages:
        print(Fore.MAGENTA + f"\n{'=' * 80}")
        print(Fore.YELLOW + "所有接收到的消息:")
        for i, msg in enumerate(received_messages, 1):
            print(Fore.CYAN + f"\n消息 #{i}:")
            print(Fore.CYAN + f"主题: {msg['topic']}")
            print(Fore.CYAN + "内容:")
            print(json.dumps(msg['payload'], indent=2))
        print(Fore.MAGENTA + f"{'=' * 80}")


# 每10秒发送一条测试数据
test_index = 0
try:
    while True:
        test_case = test_cases[test_index % len(test_cases)]
        topic = test_case["topic"]
        payload = json.dumps(test_case["payload"])

        # 打印发送的消息
        print_sent_message(test_case)

        # 发送消息
        client.publish(topic, payload)

        test_index += 1

        # 每发送4条消息后打印所有接收到的消息
        if test_index % 4 == 0:
            print_received_messages()

        time.sleep(10)

except KeyboardInterrupt:
    print("\n测试中断，打印所有接收到的消息:")
    print_received_messages()
    client.disconnect()