/**
 * 全局变量声明
 */
var boundary, // 所有点的边界信息（左上右下的gps坐标值）
  allInfoData = {}, // 所有机械信息
  allWorkData = {}, // 所有机械运动轨迹点转换为的屏幕坐标点
  allTrackData = {}, // 机械运动的单轨迹点转换为双轨迹点（屏幕坐标点）
  allAngData = {}, // 所有机械运动轨迹点的方向

  road_boundary = [],
  pile_num_list = [],

  roadData = [], // 道路边界设计数据
  pileNumData = [], // 桩号数据(道路中线)

  stationPrefix = 'K', // 桩号前缀
  start_station = '', // 开始桩号
  end_station = '', // 结束桩号

  // 所有机械信息
  machine_info = {
    // "652": {"size": 2.130, "name": "南拒马河定兴段四标羊脚碾06", "sn": "1061797", "id": 549, "type": 10},
    // "620": {"size": 2.130, "name": "南拒马河定兴段四标羊脚碾05", "sn": "1037652", "id": 547, "type": 10},
    // "656": {"size": 1.000, "name": "南拒马河定兴段四标小压路机07", "sn": "1062055", "id": 553, "type": 0},
    // "657": {"size": 1.000, "name": "南拒马河定兴段四标小型压路机08", "sn": "1016908", "id": 554, "type": 0},
    // "591": {"size": 2.130, "name": "南拒马河定兴段四标振动碾01", "sn": "1062062", "id": 521, "type": 0},
    // "592": {"size": 2.130, "name": "南拒马河定兴段四标羊角碾02", "sn": "1062044", "id": 522, "type": 10}
  },
  showList = [],  // 要显示的机械id数组
  focus_machine = '', // 需要居中的机械id
  machineImg = new Image(),
  lastMachinePos = {},  // 上一次绘制时，机器的位置角度信息

  timesColor = {
    minTimes: 5,
    maxTimes: 10,
    color: [[0, 232, 255], [0, 255, 255], [100, 255, 255], [0, 255, 0], [255, 255, 150], [255, 255, 100], [255, 255, 50]]
  },

  project_name,
  section_name,
  project_id,
  section_id, // 标段id
  start_date, // 开始时间
  end_date, // 结束时间

  // 请求数据的时间段
  curStartTime,
  curEndTime,

  // web worker线程，计算转换轨迹点
  calTrackWorker = null,

  timerId = null, // 定时器id
  playSpeed = 1, // 播放速度，默认正常速度
  curIndex = 0,  // 当前时间机械的点索引
  indexStep = 1, // curIndex每次递增的步数
  indexTime = null, // curIndex变化时的时间，用于对机械运动做smooth处理
  isPlay = true, // 是否播放

  sliderIns = null,

  scaleFactor = 0.3; // 鼠标滚轮缩放因子

machineImg.src = './image/double.png';



// 获取道路边界和道路中线
function initRoadData(road_result, pile_result) {
  boundary = getBoundary(road_result);

  let width = canvasOpt.width;
  let height = canvasOpt.height;
  let bW = boundary.right - boundary.left;
  let bH = boundary.top - boundary.bottom;
  let s1 = bW / width, s2 = bH / height;
  canvasOpt.factor = Math.max(s1, s2);

  roadData = nePt2canvasPt(road_result, boundary, canvasOpt.factor);
  pileNumData = nePt2canvasPt(pile_result, boundary, canvasOpt.factor);
}

// 机械工程坐标转换
function initCompactData(allInfoData) {
  for (let key in allInfoData) {
    showList.push(key);
    let machine = machine_info[key];
    let curRs = allInfoData[key];
    if (curRs.length === 0 || !machine) continue;
    let pts = nePt2canvasPt(curRs, boundary, canvasOpt.factor);
    allWorkData[key] = pts;
    calTrackPtsByWorker(pts, key);
    allAngData[key] = getPtsAngle(pts, canvasOpt.factor);
  }
}

/**
 * 历史回放设置播放速度
 * @param speed
 */
function setPlaySpeed(speed) {
  if (timerId != null) {
    clearInterval(timerId);
  }
  timerId = setInterval(function() {
    curIndex += indexStep;
    indexTime = new Date().getTime();
  }, 1000/speed);
}

/**
 * 计算实际的点索引
 * 这个方法有bug  如果点的时间不连续，机械随时间的运动不准确
 * @param beginWorkTime
 * @returns {number}
 */
function calcWorkIndex(srcPts) {
  return calcWorkIndexByTime(srcPts);
  let len = srcPts.length;
  let beginWorkTime = srcPts[0].gpsTime;
  let start = Date.parse(start_date);
  let nowork = (beginWorkTime - start) / 1000;
  if (curIndex < nowork) {
    return 0;
  }

  let res =  curIndex - nowork;
  if (res > len - 1) {
    res = len - 1;
  }

  return res;
}

/**
 * 根据时间计算点索引，保证运动时间准确
 * @param pts
 * @returns {number|number}
 */
function calcWorkIndexByTime(pts) {
  let curTime = getCurTime().getTime();
  return lower_index(pts, curTime);
}


