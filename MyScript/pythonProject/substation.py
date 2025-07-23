from random import random

from flask import Flask, request, jsonify
import pymysql
import uuid
import json
import time
from datetime import datetime

app = Flask(__name__)

# ================= 数据库配置 =================
DB_CONFIG = {
    # "host": "10.8.0.1",
    "host":"10.8.0.10",
    "port": 3306,
    "user": "trtm",
    "password": "trtm2019-8888",
    # "db": "boot",
    "db": "jeecg-boot",
    "charset": "utf8mb4",
    "cursorclass": pymysql.cursors.DictCursor,
    "ssl": False,
    "init_command": "SET time_zone='+8:00'"
}

# ================= 表结构常量 =================
TABLE_NAME = "pushandreturn"
FIXED_PUSH_NAME = "变电站数据"   # 对应pushName字段注释

# ================= 工具函数 =================
def get_db_connection():
    """获取数据库连接"""
    try:
        return pymysql.connect(**DB_CONFIG)
    except Exception as e:
        app.logger.error(f"数据库连接失败: {str(e)}")
        raise

def validate_request(data):
    errors = []
    required_root = ["id", "version", "function", "caller", "body"]
    required_body = ["sn", "module", "manufacture", "algId", "checkId"]

    # 字段存在性校验
    for field in required_root:
        if field not in data:
            errors.append(f"缺少根字段: {field}")

    # 类型校验（body必须为字典）
    body = data.get("body", {})
    if not isinstance(body, dict):
        errors.append("body必须为字典类型")
    else:
        for sub_field in required_body:
            if sub_field not in body:
                errors.append(f"body缺少字段: {sub_field}")

    return errors

def is_sn_registered(sn):
    """通过pushJson字段查询SN"""
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            # 使用JSON_EXTRACT精确匹配pushJson中的sn字段
            sql = f"""
            SELECT id 
            FROM {TABLE_NAME} 
            WHERE JSON_VALID(pushJson) = 1 
              AND JSON_UNQUOTE(JSON_EXTRACT(pushJson, '$.body.sn')) = %s
            """
            cursor.execute(sql, (sn,))
            return cursor.fetchone() is not None
    except Exception as e:
        app.logger.error(f"SN查询失败: {str(e)}")
        raise
    finally:
        conn.close()

# def generate_dev_id():
#     """生成符合pushDataId字段要求的ID（varchar255）"""
#     return str(uuid.uuid4().hex)  # 生成32位HEX字符串，满足255长度限制
# def generate_dev_id():
#     """生成符合pushDataId字段要求的16位纯数字ID"""
#     # 获取当前时间戳的毫秒部分（取后6位）
#     timestamp_part = str(int(time.time() * 1000))[-6:]
#
#     # 生成UUID并转换为整数，取低10位数字
#     uuid_part = str(uuid.uuid4().int % (10 ** 10))
#
#     # 组合时间戳部分和UUID部分，确保总长度为16位
#     combined = (timestamp_part + uuid_part).zfill(16)
#
#     # 截取前16位，确保ID长度符合要求
#     return combined[:16]
def generate_dev_id():
    return 1401001000676792
def save_device_record(req_data, dev_id, response_data):
    """插入数据（完全匹配截图字段）"""
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            # 严格对应表结构字段：id自增无需插入
            sql = f"""
            INSERT INTO {TABLE_NAME} 
                (pushDataId, pushName, pushJson, ReturnValue, pushDate)
            VALUES 
                (%s, %s, %s, %s, NOW())
            """
            params = (
                dev_id,  # varchar(255)
                FIXED_PUSH_NAME,  # varchar(255)
                json.dumps(req_data, ensure_ascii=False),  # longtext
                json.dumps(response_data, ensure_ascii=False),  # longtext
            )
            # 打印完整SQL和参数
            formatted_sql = cursor.mogrify(sql, params).encode('utf-8')
            app.logger.debug(f"""
                        [DEBUG] 执行SQL语句:
                        {formatted_sql}
                        [DEBUG] 插入的请求数据:
                        {json.dumps(req_data, indent=2, ensure_ascii=False)}
                        [DEBUG] 插入的响应数据:
                        {json.dumps(response_data, indent=2, ensure_ascii=False)}
                        """)

            cursor.execute(sql, params)
            conn.commit()
            app.logger.info(f"影响行数: {cursor.rowcount}")  # 添加此行
            return True
    except pymysql.err.IntegrityError:
        app.logger.error("唯一键冲突: pushDataId重复")
        return False
    except Exception as e:
        conn.rollback()
        app.logger.error(f"数据插入失败: {str(e)}")
        raise
    finally:
        conn.close()
def get_existing_dev_id(sn):
    """查询已存在的设备ID"""
    try:
        conn = get_db_connection()
        with conn.cursor() as cursor:
            sql = f"""
            SELECT pushDataId, pushJson
            FROM {TABLE_NAME} 
            WHERE JSON_VALID(pushJson) = 1 
              AND JSON_UNQUOTE(JSON_EXTRACT(pushJson, '$.body.sn')) = %s
            ORDER BY pushDate DESC 
            LIMIT 1
            """
            cursor.execute(sql, (sn,))
            result = cursor.fetchone()
            return result['pushDataId'] if result else None
    except Exception as e:
        app.logger.error(f"设备查询失败: {str(e)}")
        raise
    finally:
        conn.close()

# ================= 接口路由 =================
@app.route('/v2/iot/devices/direct/auth', methods=['POST'])
def device_auth():
    # 1. 校验JSON格式
    if not request.is_json:
        return jsonify({"code": 4000, "errMsg": "请求必须为JSON格式"}), 400

    # 2. 解析并校验数据
    try:
        req_data = request.get_json()
    except:
        return jsonify({"code": 4000, "errMsg": "非法JSON格式"}), 400

    if errors := validate_request(req_data):
        return jsonify({"code": 4001, "errMsg": "参数错误", "details": errors}), 400

    # 3. SN重复性检查
    sn = req_data["body"]["sn"]
    try:
        existing_dev_id = get_existing_dev_id(sn)
        if existing_dev_id:
            return jsonify({
                "code": 2000,
                "errMsg": "设备已存在",
                "id": req_data["id"],
                "value": {"devId": existing_dev_id}
            })
    except:
        return jsonify({"code": 5001, "errMsg": "系统繁忙"}), 500

    # 4. 生成设备ID
    dev_id = generate_dev_id()

    # 5. 构造响应
    response_data = {
        "code": 2000,
        "errMsg": "成功！",
        "id": req_data["id"],
        "value": {"devId": dev_id}
    }

    # 6. 保存记录
    if not save_device_record(req_data, dev_id, response_data):
        return jsonify({"code": 5002, "errMsg": "数据存储冲突"}), 500

    return jsonify(response_data)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=7887, debug=True)