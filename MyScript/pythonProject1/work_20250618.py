import paho.mqtt.client as mqtt
import pymysql
import json
import uuid
import time
import logging
from datetime import datetime
import collections
import base64

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
APP_NAME = "your_app_name"  # 应用名称

# ================= 数据库配置 =================
DB_CONFIG = {
    "host": "10.8.0.10",
    "port": 3306,
    "user": "trtm",
    "password": "trtm2019-8888",
    "db": "jeecg-boot",  # 使用同一个数据库
    "charset": "utf8mb4",
    "cursorclass": pymysql.cursors.DictCursor,
    "init_command": "SET time_zone='+8:00'"
}

# ================= 表结构常量 =================
TABLE_NAME = "pushandreturn"  # 使用同一个表

# ================= MQTT配置 =================
BROKER = "47.97.158.215"
PORT = 8881
TOPICS = [
    ("/v1/+/topo/request", 0),
    ("/v1/+/service/request", 0),
    ("/v1/+/service/command", 0),
    ("/v1/+/app/command", 0),
    ("/v1/+/service/data", 0)  # 新增数据接收主题
]


# ================= 数据库工具函数 =================
def get_db_connection():
    try:
        return pymysql.connect(**DB_CONFIG)
    except Exception as e:
        logger.error(f"数据库连接失败: {str(e)}")
        raise


def save_mqtt_record(topic, payload, push_name, return_value):
    """
    存储MQTT记录到数据库，push_name字段根据类型设置为"数据上报"
    """
    conn = None
    try:
        # 处理payload为JSON
        try:
            payload_json = json.loads(payload)
        except json.JSONDecodeError:
            payload_json = {"raw_data": payload.decode('utf-8', errors='replace')}

        # 处理return_value为JSON
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
                push_name,  # 使用传入的push_name
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


def process_service_command(payload_data):
    try:
        command = payload_data.get('param', {}).get('cmd', '')
        device_id = payload_data.get('deviceId', '')
        logger.info(f"收到控制命令: {command} | 设备: {device_id}")
        return "业务控制命令"
    except Exception as e:
        logger.error(f"处理控制命令失败: {str(e)}")
        return "业务控制命令"


def process_app_command(payload_data):
    try:
        config_data = payload_data.get('param', {})
        container = config_data.get('container', '')
        app_name = config_data.get('appName', '')
        file_name = config_data.get('name', '')

        # 解码Base64配置内容
        config_base64 = config_data.get('configFile', '')
        if config_base64:
            try:
                config_content = base64.b64decode(config_base64).decode('utf-8')
                logger.info(f"收到配置更新: {app_name} | 文件: {file_name} | 大小: {len(config_content)}字节")
            except Exception as e:
                logger.error(f"配置解码失败: {str(e)}")

        return "业务配置更新"
    except Exception as e:
        logger.error(f"处理配置更新失败: {str(e)}")
        return "业务配置更新"


# ================= 数据上报处理 =================
def process_service_data(payload_data):
    """
    处理数据上报消息，并存储到数据库
    """
    try:
        device_id = payload_data.get('deviceId', 'unknown')
        logger.info(f"收到数据上报: 设备={device_id}")

        # 将完整数据存储到数据库
        save_mqtt_record(
            topic="",  # 实际topic在on_message中设置
            payload=json.dumps(payload_data),
            push_name="数据上报",  # 固定pushName为"数据上报"
            return_value=""
        )

        save_to_substation_table(device_id, payload_data)

        return "数据上报已存储"

        return "数据上报已存储"
    except Exception as e:
        logger.error(f"处理数据上报失败: {str(e)}")
        return "数据处理失败"


def build_response_payload(payload_data, topic_type, req_type=None):
    try:
        logger.debug(f"收到请求: type={payload_data.get('type')}, topic={topic_type}")
    except Exception as e:
        logger.error(f"解析请求失败: {str(e)}")
        payload_data = {"raw": payload_data}

    mid = payload_data.get('mid', 0)
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")

    if topic_type == "topo":
        if req_type == "CMD_TOPO_ADD":
            logger.info("处理端设备添加请求...")
            request_device_id = payload_data.get('deviceId', '')
            node_infos = payload_data.get('param', {}).get('nodeInfos', [])
            if not node_infos:
                node_infos = payload_data.get('param', {}).get('deviceInfos', [])

            result_list = []
            for node in node_infos:
                generated_device_id = "A05" + node.get('nodeId', '')
                device_name = node.get('name', '')
                result_list.append(collections.OrderedDict([
                    ("statusCode", 200),
                    ("statusDesc", "SUCCESS!"),
                    ("nodeId", node.get('nodeId', '')),
                    ("deviceId", generated_device_id),
                ]))

            response = collections.OrderedDict([
                ("mid", mid),
                ("deviceId", request_device_id),
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

    # ================= 新增功能: 业务控制命令响应 =================
    elif topic_type == "service_command":
        """
        2.7.2 业务控制命令响应
        主题: /v1/${appName}/service/reply
        """
        logger.info("处理业务控制命令响应...")
        response = collections.OrderedDict([
            ("type", "CMD_SERVICE"),
            ("mid", mid),
            ("timestamp", timestamp),
            ("deviceId", payload_data.get('deviceId', 'unknown')),
            ("code", 200),
            ("msg", "SUCCESS"),
            ("param", {
                "cmd": payload_data.get('param', {}).get('cmd', ''),
                "paras": payload_data.get('param', {}).get('paras', {})
            })
        ])
        return json.dumps(response, ensure_ascii=False)

    # ================= 新增功能: 业务配置更新响应 =================
    elif topic_type == "app_command":
        """
        2.8.2 业务配置更新响应
        主题: /v1/${appName}/app/reply
        """
        logger.info("处理业务配置更新响应...")
        response = collections.OrderedDict([
            ("type", "CMD_CONFIG_REPLY"),
            ("mid", mid),
            ("code", 200),
            ("msg", "SUCCESS"),
            ("timestamp", int(time.time() * 1000))
        ])
        return json.dumps(response, ensure_ascii=False)

    else:
        logger.warning(f"未知的主题类型: {topic_type}")
        return json.dumps({
            "code": 200,
            "message": "处理成功",
            "data": payload_data
        }, ensure_ascii=False)


# ================= 新增功能：数据点表解析和存储 =================

POINT_TABLE_DICT = {
# ================= 完整的点表映射字典（4.1-4.10节） =================
    # ============== 4.1 TTAH(温湿度) ==============
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池电量", "unit": "%"},
    "DevState": {"name": "设备状态", "unit": ""},

    # ============== 4.2 TWTR(水浸) ==============
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池电量", "unit": "%"},
    "WLState": {"name": "水浸状态", "unit": ""},

    # ============== 4.3 TWLV(水位) ==============
    "SWData": {"name": "水位值", "unit": "cm"},
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池电量", "unit": "%"},
    "SWState": {"name": "水位状态", "unit": ""},

    # ============== 4.4 TDEH(除湿机) ==============
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "Power": {"name": "除湿机功率", "unit": "W"},
    "State": {"name": "设备状态", "unit": ""},
    "CtrlCmd": {"name": "开关控制", "unit": ""},

    # ============== 4.5 TACD(空调) ==============
    "PhaseACur": {"name": "A相电流", "unit": "A"},
    "PhaseBCur": {"name": "B相电流", "unit": "A"},
    "PhaseCCur": {"name": "C相电流", "unit": "A"},
    "ModeFeed": {"name": "空调模式", "unit": ""},
    "SpeedFeed": {"name": "空调风速", "unit": ""},
    "SwingFeed": {"name": "空调风摆", "unit": ""},
    "TmpFeed": {"name": "空调温度", "unit": ""},
    "Power": {"name": "空调功率", "unit": "W"},
    "ReactivePower": {"name": "无功功率", "unit": "Var"},
    "Energy": {"name": "电能", "unit": "kWh"},
    "PhaseAVol": {"name": "A相电压", "unit": "V"},
    "PhaseBVol": {"name": "B相电压", "unit": "V"},
    "PhaseCVol": {"name": "C相电压", "unit": "V"},
    "Hz": {"name": "电源频率", "unit": "Hz"},
    "AirState": {"name": "空调状态", "unit": ""},
    "OverCurAct": {"name": "是否处于过流状态", "unit": ""},
    "UF3_HOffAct": {"name": "是否处于按频率切机状态(三段)", "unit": ""},
    "UF2_SOffAct": {"name": "是否处于按频率软停机状态(二段)", "unit": ""},
    "UF1_TmpAct": {"name": "是否处于已调温状态(一段)", "unit": ""},
    "TmpSet": {"name": "空调温度设定", "unit": ""},
    "ModeSet": {"name": "模式设定", "unit": ""},
    "SpeedSet": {"name": "风速设定", "unit": ""},
    "DirectSet": {"name": "风向设定", "unit": ""},
    "AirControl": {"name": "开关控制", "unit": ""},
    "loadFileAct": {"name": "负荷定值读取/激活", "unit": ""},

    # ============== 4.6 TFAN(风机) ==============
    "FanCur": {"name": "风机工作电流", "unit": "A"},
    "FanVol": {"name": "风机工作电压", "unit": "V"},
    "Power": {"name": "风机功率", "unit": "W"},
    "FanState": {"name": "风机状态", "unit": ""},
    "FanControl": {"name": "开关控制", "unit": ""},

    # ============== 4.7 TLIG(灯光) ==============
    "Power": {"name": "风机功率", "unit": "W"},  # 注意：文档中写的是"风机功率"，但实际应为灯光相关
    "LampState": {"name": "灯光状态", "unit": ""},
    "LampCtrl": {"name": "灯光开关", "unit": ""},

    # ============== 4.8 TSF6(六氟化硫) ==============
    "O2": {"name": "氧气浓度", "unit": "%"},  # 文档中写的是"02"，但实际应为O2
    "SF6": {"name": "六氟化硫浓度", "unit": "%"},
    "O3": {"name": "臭氧浓度", "unit": "%"},  # 文档中写的是"03"，但实际应为O3
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池剩余电量", "unit": "%"},
    "RunState": {"name": "运行状态", "unit": ""},

    # ============== 4.9 TACO(防凝露) ==============
    "CondensationState": {"name": "防凝露控制设备状态", "unit": ""},
    "CondensationCtrl": {"name": "防凝露控制设备开关", "unit": ""},

    # ============== 4.10 TTWP(水泵) ==============
    "WpState": {"name": "水泵状态", "unit": ""},
    "Wpctrl": {"name": "水泵开关", "unit": ""},

    # ============== 4.11 TMNS(开关柜监测) ==============
    "UpTmpA": {"name": "闸刀上触头A相温度", "unit": "℃"},
    "UpTmpB": {"name": "闸刀上触头B相温度", "unit": "℃"},
    "UpTmpC": {"name": "闸刀上触头C相温度", "unit": "℃"},
    "DownTmpA": {"name": "闸刀下触头A相温度", "unit": "℃"},
    "DownTmpB": {"name": "闸刀下触头B相温度", "unit": "℃"},
    "DownTmpC": {"name": "闸刀下触头C相温度", "unit": "℃"},
    "MidTmpA": {"name": "母排A相温度", "unit": "℃"},
    "MidTmpB": {"name": "母排B相温度", "unit": "℃"},
    "MidTmpC": {"name": "母排C相温度", "unit": "℃"},
    "OpnA": {"name": "开断电流", "unit": "A"},

    # ============== 4.12 TIDD(红外双鉴) ==============
    "DetectState": {"name": "红外双鉴状态", "unit": ""},

    # ============== 4.13 TIST(红外对射) ==============
    "InfrareState": {"name": "红外对射状态", "unit": ""},

    # ============== 4.14 TELR(电子围栏) ==============
    "ElectronicFenceState": {"name": "电子围栏状态", "unit": ""},

    # ============== 4.15 TSMK(烟感) ==============
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池剩余电量", "unit": "%"},
    "FireState": {"name": "消防状态", "unit": ""},

    # ============== 4.16 TDRM(门禁与电子锁) ==============
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池剩余电量", "unit": "%"},
    "DoorState": {"name": "门磁状态", "unit": ""},
    "LockState": {"name": "锁状态", "unit": ""},
    "DoorEvent": {"name": "门禁事件", "unit": ""},
    "DoorOper": {"name": "门禁开关", "unit": ""},

    # ============== 4.17 TPDS(高频/特高频局放) ==============
    "PDMAXFZ": {"name": "局放采样峰值", "unit": "dbm"},
    "PDPhase": {"name": "峰值相位", "unit": ""},
    "PDMAXCount": {"name": "局放脉冲次数", "unit": ""},

    # ============== 4.18 TMNPDS(超声波局放、暂态地局放) ==============
    "AEMax": {"name": "超声波放电幅值", "unit": "db"},
    "AEAvg": {"name": "超声波放电均值", "unit": "db"},
    "AECount": {"name": "超声波放电次数", "unit": "次"},
    "TEVMax": {"name": "暂态地电波放电幅值", "unit": "db"},
    "TEVAvg": {"name": "暂态地电波放电均值", "unit": "db"},
    "TEVCount": {"name": "暂态地电波放电次数", "unit": "次"},
    "BatteryDumpEnergy": {"name": "电池电量", "unit": "%"},
    "PDTime": {"name": "局放监测时间", "unit": ""},
    "DevState": {"name": "设备状态", "unit": ""},

    # ============== 4.19 TMNPDFS(三合一局放) ==============
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "cycleCount": {"name": "周期数", "unit": ""},
    "phaseCount": {"name": "每周期相位数", "unit": ""},
    "PDTime": {"name": "局放监测时间", "unit": ""},
    "AAMax": {"name": "超声波放电幅值", "unit": "db"},
    "AAAvg": {"name": "超声波放电均值", "unit": "db"},
    "AAPDPercent": {"name": "局放类型置信度", "unit": ""},
    "AAPDType": {"name": "超声波局放类型", "unit": ""},
    "AAPDPhase": {"name": "超声波峰值相位", "unit": ""},
    "AACount": {"name": "超声波放电次数", "unit": "次"},
    "AAAmplLower": {"name": "采样值下限", "unit": ""},
    "AAAmplUpper": {"name": "采样值上限", "unit": ""},
    "TEVMax": {"name": "暂态地放电幅值", "unit": "db"},
    "TEVAvg": {"name": "暂态地放电均值", "unit": "db"},
    "TEVPDPercent": {"name": "局放类型置信度", "unit": ""},
    "TEVPDType": {"name": "暂态地局放类型", "unit": ""},
    "TEVPDPhase": {"name": "暂态地峰值相位", "unit": ""},
    "TEVCount": {"name": "暂态地放电次数", "unit": "次"},
    "TEVAmplLower": {"name": "采样值下限", "unit": ""},
    "TEVAmplUpper": {"name": "采样值上限", "unit": ""},
    "UHFMax": {"name": "特高频放电幅值", "unit": "db"},
    "UHFAvg": {"name": "特高频放电均值", "unit": "db"},
    "UHFPDPercent": {"name": "局放类型置信度", "unit": ""},
    "UHFPDType": {"name": "特高频局放类型", "unit": ""},
    "UHFPDPhase": {"name": "特高频峰值相位", "unit": ""},
    "UHFCount": {"name": "特高频放电次数", "unit": "次"},
    "UHFAmplLower": {"name": "采样值下限", "unit": ""},
    "UHFAmplUpper": {"name": "采样值上限", "unit": ""},
    "AADevState": {"name": "超声波诊断设备状态", "unit": ""},
    "TEVDevState": {"name": "暂态地诊断设备状态", "unit": ""},
    "UHFDevState": {"name": "特高频诊断设备状态", "unit": ""},

    # ============== 4.20 TTWL(电缆头无线测温) ==============
    "PhaseATmp": {"name": "A相温度", "unit": "℃"},
    "PhaseBTmp": {"name": "B相温度", "unit": "℃"},
    "PhaseCTmp": {"name": "C相温度", "unit": "℃"},
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池剩余电量", "unit": "%"},

    # ============== 4.21 TBMO(蓄电池) ==============
    "GroupVolt": {"name": "整组电压", "unit": "V"},
    "GroupCurrent": {"name": "整组电流", "unit": "A"},
    "Singleld": {"name": "单节编号", "unit": ""},
    "SingleVolt": {"name": "单节电压", "unit": "V"},
    "SingleBatTemp": {"name": "单节温度", "unit": "℃"},
    "SingleCurrent": {"name": "单节电流", "unit": "A"},
    "SingleBatRes": {"name": "单节内阻", "unit": "mΩ"},
    "SingleBatCap": {"name": "单节容量", "unit": "AH"},
    "SingleLineRes": {"name": "连接条电阻", "unit": "Ω"},

    # ============== 4.22 TNOI(变压器噪声) ==============
    "Noise": {"name": "噪声", "unit": "db"},
    "BatteryVol": {"name": "电池电压", "unit": "V"},
    "BatteryDumpEnergy": {"name": "电池剩余电量", "unit": "%"},
    "NoiseState": {"name": "噪声状态", "unit": ""},

    # ============== 4.23 TBMOG(蓄电池组) ==============
    "GroupVolt": {"name": "整组电压", "unit": "V"},
    "GroupCurrent": {"name": "整组电流", "unit": "A"},
    "RunState": {"name": "运行状态", "unit": ""},
    "GroupCtrl": {"name": "电池组控制", "unit": ""},

    # ============== 4.24 TBMOS(蓄电池单节) ==============
    "SingleVolt": {"name": "单节电压", "unit": "V"},
    "SingleBatTemp": {"name": "单节温度", "unit": "℃"},
    "SingleBatRes": {"name": "单节内阻", "unit": "mΩ"},

    # ============== 4.25 TMDJDT(密度继电器) ==============
    "RTPse": {"name": "实时压力", "unit": "Bar"},
    "Tmp": {"name": "温度", "unit": "℃"},
    "PTwenty": {"name": "P20", "unit": "Bar"},
    "MWConts": {"name": "微水含量", "unit": "ppm"},
    "DewPoints": {"name": "露点", "unit": "℃"},
    "Density": {"name": "密度", "unit": "g/L"},
    "DevState": {"name": "传感器状态", "unit": ""},
    "LeakAlarm": {"name": "泄漏报警", "unit": ""},
    "LiqAlarm": {"name": "液化报警", "unit": ""},
    "AtrTFauState": {"name": "闭锁2节点接线故障状态", "unit": ""},
    "AtrOFauState": {"name": "闭锁1节点接线故障状态", "unit": ""},
    "AlaNoFauState": {"name": "报警节点接线故障状态", "unit": ""},
    "AtrTActState": {"name": "闭锁2信号节点动作状态", "unit": ""},
    "AtrOActState": {"name": "闭锁1信号节点动作状态", "unit": ""},
    "AlaNoActState": {"name": "报警信号节点动作状态", "unit": ""},
    "OverPreAlarm": {"name": "超压报警", "unit": ""},
    "FreAcq": {"name": "发送间隔", "unit": "S"},

    # ============== 4.26 TACD-SP(边设备-单相空调) ==============
    "PhaseCur": {"name": "电流", "unit": "A"},
    "PhaseVol": {"name": "电压", "unit": "V"},
    "ModeFeed": {"name": "空调模式", "unit": ""},
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "SpeedFeed": {"name": "空调风速", "unit": ""},
    "TmpFeed": {"name": "空调温度", "unit": "℃"},
    "Power": {"name": "空调总有功", "unit": "W"},
    "ReactivePower": {"name": "空调总无功", "unit": "Var"},
    "Energy": {"name": "电能", "unit": "kW·h"},
    "Hz": {"name": "电源频率", "unit": "Hz"},
    "FreAcq": {"name": "发送周期", "unit": "秒"},
    "AirState": {"name": "空调状态", "unit": ""},
    "PowerState": {"name": "电源回路状态", "unit": ""},
    "TmpSet": {"name": "空调温度设定", "unit": ""},
    "ModeSet": {"name": "模式设定", "unit": ""},
    "SpeedSet": {"name": "风速设定", "unit": ""},
    "AirControl": {"name": "开关控制", "unit": ""},
    "FreAcqSet": {"name": "发送周期", "unit": "秒"},
    "PowerControl": {"name": "电源回路状态", "unit": ""},

    # ============== 4.27 TACD-MP(边设备-三相空调) ==============
    "PhaseACur": {"name": "A相电流", "unit": "A"},
    "PhaseAVol": {"name": "A相电压", "unit": "V"},
    "PhaseBCur": {"name": "B相电流", "unit": "A"},
    "PhaseBVol": {"name": "B相电压", "unit": "V"},
    "PhaseCCur": {"name": "C相电流", "unit": "A"},
    "PhaseCVol": {"name": "C相电压", "unit": "V"},
    "ModeFeed": {"name": "空调模式", "unit": ""},
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "SpeedFeed": {"name": "空调风速", "unit": ""},
    "TmpFeed": {"name": "空调温度", "unit": "℃"},
    "Power": {"name": "空调总有功", "unit": "W"},
    "ReactivePower": {"name": "空调总无功", "unit": "Var"},
    "Energy": {"name": "电能", "unit": "kW·h"},
    "Hz": {"name": "电源频率", "unit": "Hz"},
    "FreAcq": {"name": "发送周期", "unit": "秒"},
    "AirState": {"name": "空调状态", "unit": ""},
    "PowerState": {"name": "电源回路状态", "unit": ""},
    "TmpSet": {"name": "空调温度设定", "unit": ""},
    "ModeSet": {"name": "模式设定", "unit": ""},
    "SpeedSet": {"name": "风速设定", "unit": ""},
    "AirControl": {"name": "空调开关控制", "unit": ""},
    "FreAcqSet": {"name": "发送周期", "unit": "秒"},
    "PowerControl": {"name": "电源回路状态", "unit": ""},

    # ============== 4.28 TACD-SP-TER(端设备-单相空调) ==============
    "PhaseCur": {"name": "电流", "unit": "A"},
    "PhaseVol": {"name": "电压", "unit": "V"},
    "ModeFeed": {"name": "空调模式", "unit": ""},
    "SwingFeed": {"name": "空调风摆", "unit": ""},
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "SpeedFeed": {"name": "空调风速", "unit": ""},
    "TmpFeed": {"name": "空调温度", "unit": "℃"},
    "TotPF": {"name": "功率因数", "unit": ""},
    "Power": {"name极": "空调总有功", "unit": "W"},
    "ReactivePower": {"name": "空调总无功", "unit": "Var"},
    "TotVA": {"name": "视在功率", "极unit": "W"},
    "Energy": {"name": "电能", "unit": "kW·h"},
    "Hz": {"name": "电源频率", "unit": "Hz"},
    "FreAcq": {"name": "发送周期", "unit": "秒"},
    "AirState": {"name": "空调状态", "unit": ""},
    "PowerState": {"name": "电源回路状态", "unit": ""},
    "TmpSet": {"name": "空调温度设定", "unit": ""},
    "ModeSet": {"name": "模式设定", "unit": ""},
    "SpeedSet": {"name": "风速设定", "unit": ""},
    "AirControl": {"name": "开关控制", "unit": ""},
    "FreAcqSet": {"name": "发送周期", "unit": "秒"},
    "PowerControl": {"name": "电源回路状态", "unit": ""},

    # ============== 4.29 TACD-MP-TER(端设备-三相空调) ==============
    "PhaseACur": {"name": "A相电流", "unit": "A"},
    "PhaseAVol": {"name": "A相电压", "unit": "V"},
    "PhaseBCur": {"name": "B相电流", "unit": "A"},
    "PhaseBVol": {"name": "B相电压", "unit": "V"},
    "PhaseCCur": {"name": "C相电流", "unit": "A"},
    "PhaseCVol": {"name": "C相电压", "unit": "V"},
    "ModeFeed": {"name": "空调模式", "unit": ""},
    "SwingFeed": {"name": "空调风摆", "unit": ""},
    "Tmp": {"name": "温度", "unit": "℃"},
    "Hum": {"name": "湿度", "unit": "%"},
    "SpeedFeed": {"name": "空调风速", "unit极": ""},
    "TmpFeed": {"name": "空调温度", "unit": "℃"},
    "TotPF": {"name": "功率因数", "unit": ""},
    "Power": {"name": "空调总有功", "unit": "W"},
    "ReactivePower": {"name": "空调总无功", "unit": "Var"},
    "TotVA": {"name": "视在功率", "unit": "W"},
    "Energy": {"name": "电能", "unit": "kW·h"},
    "Hz": {"name": "电源频率", "unit": "Hz"},
    "FreAcq": {"name": "发送周期", "unit": "秒"},
    "AirState": {"name": "空调状态", "unit": ""},
    "PowerState": {"name": "电源回路状态", "unit": ""},
    "TmpSet": {"name": "空调温度设定", "unit": ""},
    "ModeSet": {"name": "模式设定", "unit": ""},
    "SpeedSet": {"name": "风速设定", "unit": ""},
    "AirControl": {"name": "空调开关控制", "unit": ""},
    "FreAcqSet": {"name": "发送周期", "unit": "秒"},
    "PowerControl": {"name": "电源回路状态", "unit": ""},

    # ============== 4.30 TLVFI_RANGING(故障指示器) ==============
    "CUBatteyVol": {"name": "汇集单元电池电压", "unit": "V"},
    "CUMainVol": {"name": "汇集单元主供电源电压", "unit": "V"},
    "CUTotalNumRecData": {"name": "汇集单元故障录波数据总存储设置数量", "unit": ""},
    "CUCurNumRecData": {"name": "汇集单元故障录波数据当前存储数量", "unit": ""},
    "CUWSignalStrength": {"name": "无线信号强度", "unit": ""},
    "CUGPSLongitude": {"name": "GPS经度", "unit": ""},
    "CUGPSLatitude": {"name": "GPS纬度", "unit": ""},
    "CUGroundStatus": {"name": "汇集单元接地状态", "unit": ""},
    "CUAccSensorXaxis": {"name": "加速度传感器X轴", "unit": "mg"},
    "CUAccSensorYaxis": {"name": "加速度传感器Y轴", "unit": "mg"},
    "CUAccSensorZaxis": {"name": "加速度传感器Z轴", "unit": "mg"},
    "CUAphaseRssi": {"name": "汇集侧a相采集单元rssi", "unit": ""},
    "CUBphaseRssi": {"name": "汇集侧b相采集单元rssi", "unit": ""},
    "CUCphaseRssi": {"name": "汇集侧c相采集单元rssi", "unit": ""},
    "PACurrent": {"name": "A相负荷电流", "unit": "A"},
    "PBCurrent": {"name": "B相负荷电流", "unit": "A"},
    "PCCurrent": {"name": "C相负荷电流", "unit": "A"},
    "PAElectricIntensity": {"name": "A相电场强度", "unit": ""},
    "PBElectricIntensity": {"name": "B相电场强度", "unit": ""},
    "PCElectricIntensity": {"name": "C相电场强度", "unit": ""},
    "PATemperature": {"name": "A相温度", "unit": "℃"},
    "PBTemperature": {"name": "B相温度", "unit": "℃"},
    "PCTemperature": {"name": "C相温度", "unit": "℃"},
    "PABatteryVol": {"name": "A相电池电压", "unit": "V"},
    "PBBatteryVol": {"name": "B极相电池电压", "unit": "V"},
    "PCBatteryVol": {"name": "C相电池电压", "unit": "V"},
    "PAMainVol": {"name": "A相主供电源电压", "unit": "V"},
    "PBMainVol": {"name": "B相主供电源电压", "unit": "V"},
    "PCMainVol": {"name": "C相主供电源电压", "unit": "V"},
    "PAGroudFaultCurrent": {"name": "A相接地故障电流", "unit": "A"},
    "PBGroudFaultCurrent": {"name": "B相接地故障电流", "unit": "A"},
    "PCGroudFaultCurrent": {"name": "C相接地故障电流", "unit": "A"},
    "PAShortFaultCurrent": {"name": "A相短路故障电流", "unit": "A"},
    "PBShortFaultCurrent": {"name": "B相短路故障电流", "unit": "A"},
    "PCShortFaultCurrent": {"name": "C相短路故障电流", "unit": "A"},
    "PARssi": {"name": "A相rssi", "unit": ""},
    "PBRssi": {"name": "B相rssi", "unit": ""},
    "PCRssi": {"name": "C相rssi", "unit": ""},
    "CURunState": {"name": "汇集单元运行状态", "unit": ""},
    "CUBatLowerVolAlarm": {"name": "汇集单元电池低电压告警", "unit": ""},
    "CUShortFaultAlarm": {"name": "短路故障总", "unit": ""},
    "CUGroundFaultAlarm": {"name": "接地故障总", "unit": ""},
    "CUPowerFaultAlarm": {"name": "停电信号总", "unit": ""},
    "CUWaveRecFinish": {"name": "录波完成", "unit": ""},
    "CUGPSComState": {"name": "汇集单元GPS通信状态", "unit": ""},
    "CUAccAngleChange": {"name": "汇集单元加速度角度变化", "unit": ""},
    "PAComState": {"name": "A相采集单元通信状态", "unit": ""},
    "PAInstShortFault": {"name": "A相瞬时性短路", "unit": ""},
    "PAPermShortFault": {"name": "A相永久性短路", "unit": ""},
    "PAGroundAlarm": {"name": "A相接地", "unit": ""},
    "PAPwUnderAlarm": {"name": "A相电池欠压告警", "unit": ""},
    "PALinePowerOff": {"name": "A相线路停电", "unit": ""},
    "PBComState": {"name": "B相采集单元通信状态", "unit": ""},
    "PBInstShortFault": {"name": "B相瞬时性短路", "unit": ""},
    "PBPermShortFault": {"name": "B相永久性短路", "unit": ""},
    "PBGroundAlarm": {"name": "B相接地", "unit": ""},
    "PBPwUnderAlarm": {"name": "B相电池欠压告警", "unit": ""},
    "PBLinePowerOff": {"name": "B相线路停电", "unit": ""},
    "PCComState": {"name": "C相采集单元通信状态", "unit": ""},
    "PCInstShortFault": {"name": "C相瞬时性短路", "unit": ""},
    "PCPermShortFault": {"name": "C相永久性短路", "unit": ""},
    "PCGroundAlarm": {"name": "C相接地", "unit": ""},
    "PCPwUnderAlarm": {"name": "C相电池欠压告警", "unit": ""},
    "PCLinePowerOff": {"name": "C相线路停电", "unit": ""},
    "PAFlashAlarm": {"name": "A相闪烁报警", "unit": ""},
    "PABackPwInUse": {"name": "A相后备电源投入使用", "unit": ""},
    "PAReclosing": {"name": "A相重合闸", "unit": ""},
    "PAInstGroundAlarm": {"name": "A相短时性接地", "unit": ""},
    "PAPermGroundAlarm": {"name": "A相永久性接地", "unit": ""},
    "PBFlashAlarm": {"name": "B相闪烁报警", "unit": ""},
    "PBBackPwInUse": {"name": "B相后备电源投入使用", "unit": ""},
    "PBReclosing": {"name": "B相重合闸", "unit": ""},
    "PBInstGroundAlarm": {"name": "B相短时性接地", "unit": ""},
    "PBPermGroundAlarm": {"name": "B相永久性接地", "unit": ""},
    "PCFlashAlarm": {"name": "C相闪烁报警", "unit": ""},
    "PCBackPwInUse": {"name": "C相后备电源投入使用", "unit": ""},
    "PCReclosing": {"name": "C相重合闸", "unit": ""},
    "PCInstGroundAlarm": {"name": "C相短时性接地", "unit": ""},
    "PCPermGroundAlarm": {"name": "C相永久性接地", "unit": ""},
    "CUWaveRecFinishV8": {"name": "录波完成(高频)", "unit": ""},
    "CUBackPwInUse": {"name": "汇集单元后备电源投入使用", "unit": ""},

# ============== 4.30.3 录波点表 ==============
    "CUWaveRecByHand": {"name": "手动触发录波", "unit": ""},
    "CULogFileUpload": {"name": "采集单元Log文件上传触发", "unit": ""},
    "CUFlashLightAction": {"name": "闪灯告警动作/复归（ABC相）", "unit": ""},
    "PAFlashLightAction": {"name": "A相闪灯告警动作/复归", "unit": ""},
    "PBFlashLightAction": {"name": "B相闪灯告警动作/复归", "unit": ""},
    "PCFlashLightAction": {"name": "C相闪灯告警动作/复归", "unit": ""},
}




def save_to_substation_table(device_sn, payload_data):
    """
    解析数据上报消息并存储到substation表
    参数:
        device_sn: 设备SN
        payload_data: 数据上报的JSON内容
    """
    conn = None
    try:
        # 1. 准备存储的数据
        data_points = payload_data.get('param', {}).get('data', {})
        timestamp = payload_data.get('timestamp')

        # 2. 解析数据点 - 直接使用英文点位编码查找中文解释
        parsed_data = {}
        alarm_status = "正常"  # 默认状态

        for point_code, value in data_points.items():  # point_code 是英文点位编码
            point_info = POINT_TABLE_DICT.get(point_code, {})
            name = point_info.get('name', point_code)  # 如果没有映射，使用原始编码
            unit = point_info.get('unit', '')

            # 格式化值（带单位）
            formatted_value = f"{value}{unit}" if unit else str(value)

            # 存储到解析结果 - 使用中文名称作为键
            parsed_data[name] = formatted_value

            # 检测报警状态（根据点表约定，状态点1表示报警）
            if "状态" in name and value == 1:
                alarm_status = "报警"

        # 3. 准备数据库插入数据
        creatime = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        if timestamp:
            try:
                # 尝试转换时间戳（可能是毫秒级）
                ts = float(timestamp)
                if ts > 1e12:  # 毫秒时间戳
                    ts /= 1000
                creatime = datetime.fromtimestamp(ts).strftime("%Y-%m-%d %H:%M:%S")
            except:
                pass

        # 4. 打印解析后的数据到控制台
        print(f"\n{'=' * 50}")
        print(f"设备SN: {device_sn}")
        print(f"报警状态: {alarm_status}")
        print(f"数据时间: {creatime}")
        print("解析后的数据点（存储字段名）:")

        # 格式化打印解析数据
        for name, value in parsed_data.items():
            print(f"  {name}: {value}")

        print(f"{'=' * 50}\n")

        # 将解析数据转换为JSON字符串
        info_json = json.dumps(parsed_data, ensure_ascii=False, indent=2)
        logger.info(f"解析后的JSON数据 (长度: {len(info_json)}):\n{info_json}")

        # 5. 插入数据库
        conn = get_db_connection()
        with conn.cursor() as cursor:
            sql = """
            INSERT INTO substation 
                (device_sn, stustus, creatime, info, alarm_status)
            VALUES 
                (%s, %s, %s, %s, %s)
            """

            # 状态字段（stustus）暂时留空
            params = (
                device_sn,
                "",  # stustus字段
                creatime,
                info_json,
                alarm_status
            )

            cursor.execute(sql, params)
            conn.commit()
            logger.info(f"已存储到substation表: 设备={device_sn}, 报警状态={alarm_status}")
            return True

    except pymysql.err.IntegrityError:
        logger.warning("唯一键冲突: substation表记录重复")
        return False
    except Exception as e:
        if conn:
            conn.rollback()
        logger.error(f"存储到substation表失败: {str(e)}")
        return False
    finally:
        if conn:
            conn.close()


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

        # 核心功能：处理各种主题
        if msg.topic.endswith("/request") or msg.topic.endswith("/command") or msg.topic.endswith("/data"):
            try:
                payload_str = msg.payload.decode('utf-8')
                logger.debug(f"原始消息内容:\n{payload_str}")
                payload_data = json.loads(payload_str)
                req_type = payload_data.get('type')
                logger.info(f"解析成功: type={req_type}")
            except Exception as e:
                logger.error(f"解析消息失败: {str(e)}")
                req_type = None
                payload_data = {"raw": payload_str}

            topic_parts = msg.topic.split('/')
            if len(topic_parts) < 4:
                logger.error(f"主题格式错误: {msg.topic}")
                return

            app_name = topic_parts[2]
            topic_type = topic_parts[3]
            full_topic_type = f"{topic_parts[3]}/{topic_parts[4]}" if len(topic_parts) > 4 else topic_type

            response_topic = ""
            push_name = "获取数据"

            # 处理不同类型的消息
            if full_topic_type == "topo/request":
                push_name = process_topo_message(payload_data)
                response_topic = f"/v1/{app_name}/topo/response"
                logger.info(f"处理拓扑请求: push_name={push_name}, response_topic={response_topic}")
                response_json = build_response_payload(payload_data, "topo", req_type)

            elif full_topic_type == "service/request":
                push_name = process_service_message(payload_data)
                response_topic = f"/v1/{app_name}/service/response"
                logger.info(f"处理服务请求: push_name={push_name}, response_topic={response_topic}")
                response_json = build_response_payload(payload_data, "service", req_type)

            elif full_topic_type == "service/command":
                push_name = process_service_command(payload_data)
                response_topic = f"/v1/{app_name}/service/reply"
                logger.info(f"处理业务控制命令: push_name={push_name}, response_topic={response_topic}")
                response_json = build_response_payload(payload_data, "service_command", req_type)

            elif full_topic_type == "app/command":
                push_name = process_app_command(payload_data)
                response_topic = f"/v1/{app_name}/app/reply"
                logger.info(f"处理业务配置更新: push_name={push_name}, response_topic={response_topic}")
                response_json = build_response_payload(payload_data, "app_command", req_type)

            # ================= 数据上报处理 =================
            elif full_topic_type == "service/data":
                """
                数据上报处理
                """
                # 处理数据上报
                status = process_service_data(payload_data)
                logger.info(f"处理数据上报: {status}")

                # 数据上报不需要回复响应
                response_topic = None

                # 不需要额外存储，因为process_service_data已经处理
                logger.info(f"{'=' * 50}")
                return  # 提前返回

            else:
                logger.warning(f"未识别的主题类型: {full_topic_type}")
                response_json = json.dumps({
                    "code": 400,
                    "message": f"未识别的主题类型: {full_topic_type}"
                }, ensure_ascii=False)

            logger.info(f"构建响应成功: 长度={len(response_json)}字节")
            logger.debug(f"响应内容:\n{response_json}")

            # 保存记录到数据库
            save_mqtt_record(
                topic=msg.topic,
                payload=msg.payload,
                push_name=push_name,
                return_value=response_json
            )

            if response_topic:
                result, mid = client.publish(response_topic, response_json, qos=0)
                if result == mqtt.MQTT_ERR_SUCCESS:
                    logger.info(f"已发送响应到: {response_topic}, mid={mid}")
                else:
                    logger.error(f"发送失败: {response_topic}, 错误码: {result}")

        else:
            logger.info(f"忽略非请求/命令/数据主题: {msg.topic}")

        logger.info(f"{'=' * 50}")

    except Exception as e:
        logger.error(f"消息处理错误: {str(e)}")
        try:
            error_response = json.dumps({
                "code": 500,
                "message": f"处理错误: {str(e)}",
                "data": None
            })
            save_mqtt_record(
                topic=msg.topic,
                payload=msg.payload,
                push_name="处理错误",
                return_value=error_response
            )
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