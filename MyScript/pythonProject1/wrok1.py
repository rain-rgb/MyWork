import paho.mqtt.client as mqtt
import pymysql
import json
import uuid
import time
import logging
from datetime import datetime
import collections

# ================= 配置日志 =================
logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler()
    ]
)
logger = logging.getLogger("MQTT_Edge_Service")

# ================= 系统配置 =================
EDGE_DEVICE_ID = "1000000001154323"  # 边设备ID

# ================= 数据库配置 =================
DB_CONFIG = {
    "host": "10.8.0.10",
    "port": 3306,
    "user": "trtm",
    "password": "trtm2019-8888",
    "db": "jeecg-boot",
    "charset": "utf8mb4",
    "cursorclass": pymysql.cursors.DictCursor,
    "init_command": "SET time_zone='+8:00'"
}

# ================= 表结构常量 =================
TABLE_NAME = "pushandreturn"

# ================= MQTT配置 =================
BROKER = "47.97.158.215"
PORT = 8881
TOPICS = [
    ("/v1/+/topo/request", 0),
    ("/v1/+/service/request", 0)
]


# ================= 数据库工具函数 =================
def get_db_connection():
    try:
        return pymysql.connect(**DB_CONFIG)
    except Exception as e:
        logger.error(f"数据库连接失败: {str(e)}")
        raise


def save_mqtt_record(topic, payload, push_name, return_value):
    conn = None
    try:
        try:
            payload_json = json.loads(payload)
        except json.JSONDecodeError:
            payload_json = {"raw_data": payload.decode('utf-8', errors='replace')}

        try:
            return_json = json.loads(return_value) if isinstance(return_value, str) else return_value
        except:
            return_json = {"raw_response": return_value}

        record_id = str(uuid.uuid4().hex)

        conn = get_db_connection()
        with conn.cursor() as cursor:
            sql = f"""
            INSERT INTO {TABLE_NAME} 
                (pushDataId, pushName, pushJson, ReturnValue, pushDate)
            VALUES 
                (%s, %s, %s, %s, NOW())
            """

            push_data = {
                "topic": topic,
                "payload": payload_json,
                "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            }

            params = (
                record_id,
                push_name,
                json.dumps(push_data, ensure_ascii=False),
                json.dumps(return_json, ensure_ascii=False)
            )

            cursor.execute(sql, params)
            conn.commit()
            logger.info(f"已保存记录: {push_name} | ID: {record_id}")
            return True

    except pymysql.err.IntegrityError:
        logger.warning("唯一键冲突: pushDataId重复")
        return False
    except Exception as e:
        if conn:
            conn.rollback()
        logger.error(f"数据插入失败: {str(e)}")
        return False
    finally:
        if conn:
            conn.close()


# ================= 消息处理函数 =================
def process_topo_message(payload_data):
    try:
        req_type = payload_data.get('type', '').upper()
        if req_type == "CMD_TOPO_ADD":
            return "端设备添加信息响应"
        elif req_type == "CMD_TOPO_DEL":
            return "端设备删除信息响应"
        elif req_type == "CMD_TOPO_UPDATE":
            return "端设备更新信息响应"
        else:
            return "获取数据"
    except (json.JSONDecodeError, KeyError):
        return "获取数据"


def process_service_message(payload_data):
    try:
        if payload_data.get('type') == "CMD_RPC":
            return "数据获取响应"
        else:
            return "数据获取响应"
    except:
        return "数据获取响应"


def build_response_payload(payload_data, topic_type, req_type=None):
    try:
        logger.debug(f"收到请求: type={payload_data.get('type')}, topic={topic_type}")
    except Exception as e:
        logger.error(f"解析请求失败: {str(e)}")
        payload_data = {"raw": payload_data}

    mid = payload_data.get('mid', 0)
    # timestamp = int(time.time() * 1000)
    # 获取当前时间并格式化为 "YYYY-MM-DD HH:MM:SS"
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    if topic_type == "topo":
        # 修改位置：build_response_payload函数中的CMD_TOPO_ADD处理部分
        if req_type == "CMD_TOPO_ADD":
            logger.info("处理端设备添加请求...")

            # 从请求中获取原始deviceId（作为第一层deviceId）
            request_device_id = payload_data.get('deviceId', '')

            # 兼容不同字段名称：nodeInfos 或 deviceInfos
            node_infos = payload_data.get('param', {}).get('nodeInfos', [])
            if not node_infos:
                node_infos = payload_data.get('param', {}).get('deviceInfos', [])

            result_list = []
            for node in node_infos:
                # 自定义生成规则：A05 + nodeId
                generated_device_id = "A05" + node.get('nodeId', '')
                device_name = node.get('name', '')
                result_list.append(collections.OrderedDict([
                    ("statusCode", 200),
                    ("statusDesc", "SUCCESS!"),
                    ("nodeId", node.get('nodeId', '')),
                    ("deviceId", generated_device_id),  # 使用自定义生成的deviceId
                    # ("name", device_name),
                    # ("profile", collections.OrderedDict([
                    #     ("url", "http://xxxxxx"),
                    #     ("name", device_name),
                    #     ("size", 23),
                    #     ("md5", "xxx")
                    # ]))
                ]))

            # 严格按照文档2.1.2示例的顺序
            response = collections.OrderedDict([
                ("mid", mid),
                ("deviceId", request_device_id),  # 第一层使用请求中的deviceId
                ("timestamp", timestamp),
                ("type", "CMD_TOPO_ADD"),
                ("code", 200),
                ("msg", "SUCCESS!"),
                ("param", collections.OrderedDict([
                    ("data", result_list)
                ]))
            ])
            return json.dumps(response, ensure_ascii=False)

        elif req_type == "CMD_TOPO_DEL":
            logger.info("处理端设备删除请求...")
            node_ids = payload_data.get('param', {}).get('nodeIds', [])
            result_list = []
            for node_id in node_ids:
                result_list.append(collections.OrderedDict([
                    ("statusCode", 200),
                    ("statusDesc", "SUCCESS!"),
                    ("deviceId", node_id)
                ]))

            # 严格按照文档2.2.2示例的顺序
            response = collections.OrderedDict([
                ("mid", mid),
                ("deviceId", EDGE_DEVICE_ID),
                ("timestamp", timestamp),
                ("type", "CMD_TOPO_DEL"),
                ("code", 200),
                ("msg", "SUCCESS!"),
                ("param", collections.OrderedDict([
                    ("data", result_list)
                ]))
            ])
            return json.dumps(response, ensure_ascii=False)

        elif req_type == "CMD_TOPO_UPDATE":
            logger.info("处理端设备更新请求...")
            node_statuses = payload_data.get('param', {}).get('nodeStatuses', [])
            result_list = []
            for status_info in node_statuses:
                result_list.append(collections.OrderedDict([
                    ("statusCode", 200),
                    ("statusDesc", "SUCCESS!"),
                    ("deviceId", status_info.get('deviceId', ''))
                ]))

            # 严格按照文档2.3.2示例的顺序
            response = collections.OrderedDict([
                ("mid", mid),
                ("deviceId", EDGE_DEVICE_ID),
                ("timestamp", timestamp),
                ("type", "CMD_TOPO_UPDATE"),
                ("code", 200),
                ("msg", "SUCCESS!"),
                ("param", collections.OrderedDict([
                    ("data", result_list)
                ]))
            ])
            return json.dumps(response, ensure_ascii=False)
        else:
            logger.warning(f"未知的拓扑操作类型: {req_type}")
            return json.dumps({
                "code": 400,
                "message": f"未知的拓扑操作类型: {req_type}",
                "data": payload_data
            }, ensure_ascii=False)

    elif topic_type == "service":
        if req_type == "CMD_RPC":
            logger.info("处理数据获取请求...")
            # 严格按照文档2.6.2示例的顺序
            response = collections.OrderedDict([
                ("msg", "SUCCESS"),
                ("code", 200),
                ("expire", -1),
                ("mid", mid),
                ("type", "CMD_RPC"),
                ("deviceId", payload_data.get('deviceId', 'unknown')),
                ("timestamp", timestamp),
                ("param", payload_data.get('param', {}))
            ])
            return json.dumps(response, ensure_ascii=False)
        else:
            logger.warning(f"未知的服务请求类型: {req_type}")
            return json.dumps({
                "code": 200,
                "message": "处理成功",
                "data": payload_data
            }, ensure_ascii=False)
    else:
        logger.warning(f"未知的主题类型: {topic_type}")
        return json.dumps({
            "code": 200,
            "message": "处理成功",
            "data": payload_data
        }, ensure_ascii=False)


# ================= MQTT回调函数 =================
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        logger.info(f"成功连接到MQTT代理: {BROKER}:{PORT}")
        logger.info("订阅主题: " + ", ".join([f"{t[0]} (QoS {t[1]})" for t in TOPICS]))

        for topic, qos in TOPICS:
            result, mid = client.subscribe(topic, qos)
            if result == mqtt.MQTT_ERR_SUCCESS:
                logger.info(f"订阅成功: {topic} (QoS {qos}), mid={mid}")
            else:
                logger.error(f"订阅失败: {topic}, 错误码: {result}")
    else:
        logger.error(f"连接失败: {mqtt.connack_string(rc)}")


def on_subscribe(client, userdata, mid, granted_qos):
    logger.info(f"订阅确认: mid={mid}, QoS={granted_qos}")


def on_disconnect(client, userdata, rc):
    if rc != 0:
        logger.error(f"意外断开连接: {mqtt.error_string(rc)}")
        try:
            logger.info("尝试重新连接...")
            client.reconnect()
        except Exception as e:
            logger.error(f"重连失败: {str(e)}")


def on_message(client, userdata, msg):
    try:
        logger.info(f"\n{'=' * 50}")
        logger.info(f"收到消息: 主题={msg.topic}, 长度={len(msg.payload)}字节")

        # 核心功能：只处理请求主题，忽略响应主题
        if not msg.topic.endswith("/request"):
            logger.info(f"忽略非请求主题: {msg.topic}")
            return

        try:
            payload_str = msg.payload.decode('utf-8')
            logger.debug(f"原始消息内容:\n{payload_str}")

            # 解析消息内容
            payload_data = json.loads(payload_str)

            # 尝试原始解析逻辑
            if 'payload' in payload_data and isinstance(payload_data['payload'], dict):
                # 如果是封装格式 {topic:..., payload:..., timestamp:...}
                actual_payload = payload_data['payload']
                logger.debug("检测到封装格式消息，提取payload字段")
            else:
                # 直接请求格式
                actual_payload = payload_data

            req_type = actual_payload.get('type')

            # 如果原始逻辑无法解析type，则尝试新的解析逻辑
            if not req_type:
                logger.warning("原始解析逻辑无法获取type，尝试新的解析逻辑")
                # 尝试从根级别获取type
                req_type = payload_data.get('type')
                if req_type:
                    logger.info("新解析逻辑成功获取type")
                    actual_payload = payload_data
                else:
                    # 尝试其他可能的结构...
                    logger.error("新解析逻辑仍无法获取type")

            logger.info(f"解析成功: type={req_type}, mid={actual_payload.get('mid')}")
        except Exception as e:
            logger.error(f"解析消息失败: {str(e)}")
            req_type = None
            actual_payload = {"raw": payload_str}

        topic_parts = msg.topic.split('/')
        if len(topic_parts) < 4:
            logger.error(f"主题格式错误: {msg.topic}")
            return

        app_name = topic_parts[2]
        topic_type = topic_parts[3]

        response_topic = ""
        push_name = "获取数据"

        if topic_type == "topo":
            push_name = process_topo_message(actual_payload)
            response_topic = f"/v1/{app_name}/topo/response"
            logger.info(f"处理拓扑请求: push_name={push_name}, response_topic={response_topic}")

        elif topic_type == "service":
            push_name = process_service_message(actual_payload)
            response_topic = f"/v1/{app_name}/service/response"
            logger.info(f"处理服务请求: push_name={push_name}, response_topic={response_topic}")

        response_json = build_response_payload(actual_payload, topic_type, req_type)
        logger.info(f"构建响应成功: 长度={len(response_json)}字节")

        logger.debug(f"响应内容:\n{response_json}")

        if save_mqtt_record(msg.topic, msg.payload, push_name, response_json):
            logger.info("数据库保存成功")
        else:
            logger.error("数据库保存失败")

        if response_topic:
            result, mid = client.publish(response_topic, response_json, qos=0)
            if result == mqtt.MQTT_ERR_SUCCESS:
                logger.info(f"已发送响应到: {response_topic}, mid={mid}")
            else:
                logger.error(f"发送失败: {response_topic}, 错误码: {result}")
        else:
            logger.warning(f"未识别的主题类型 - {topic_type}")

        logger.info(f"{'=' * 50}")

    except Exception as e:
        logger.error(f"消息处理错误: {str(e)}")
        try:
            error_response = json.dumps({
                "code": 500,
                "message": f"处理错误: {str(e)}",
                "data": None
            })
            save_mqtt_record(msg.topic, msg.payload, "处理错误", error_response)
        except Exception as e2:
            logger.error(f"保存错误记录失败: {str(e2)}")


# ================= MQTT客户端设置 =================
def setup_mqtt_client():
    client_id = f"edge_service_{int(time.time())}"
    client = mqtt.Client(client_id=client_id)

    client.on_connect = on_connect
    client.on_disconnect = on_disconnect
    client.on_subscribe = on_subscribe
    client.on_message = on_message

    client.will_set("/edge/status", payload="offline", qos=0, retain=True)

    return client


# ================= 主程序 =================
if __name__ == '__main__':
    client = setup_mqtt_client()

    logger.info(f"尝试连接MQTT服务器: {BROKER}:{PORT}")
    try:
        client.connect(BROKER, PORT, 60)
        logger.info(f"MQTT客户端已启动，连接至 {BROKER}:{PORT}")
    except Exception as e:
        logger.error(f"连接失败: {str(e)}")
        exit(1)

    try:
        client.loop_forever()
    except KeyboardInterrupt:
        logger.info("程序已终止")
        client.disconnect()
    except Exception as e:
        logger.error(f"主循环错误: {str(e)}")